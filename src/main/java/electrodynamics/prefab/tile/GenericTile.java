package electrodynamics.prefab.tile;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import electrodynamics.api.IWrenchItem;
import electrodynamics.api.References;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyManager;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentName;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.tile.components.utils.IComponentGasHandler;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.TriPredicate;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;

public abstract class GenericTile extends BlockEntity implements Nameable, IPropertyHolderTile {
    private final IComponent[] components = new IComponent[IComponentType.values().length];
    private final ComponentProcessor[] processors = new ComponentProcessor[5];
    private final PropertyManager propertyManager = new PropertyManager(this);

    // use this for manually setting the change flag
    public boolean isChanged = false;

    public GenericTile(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
        super(tileEntityTypeIn, worldPos, blockState);
    }

    public <T> Property<T> property(Property<T> prop) {
        for (Property<?> existing : propertyManager.getProperties()) {
            if (existing.getName().equals(prop.getName())) {
                throw new RuntimeException(prop.getName() + " is already being used by another property!");
            }
        }

        return propertyManager.addProperty(prop);
    }

    public boolean hasComponent(IComponentType type) {
        return components[type.ordinal()] != null;
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public <T extends IComponent> T getComponent(IComponentType type) {
        return !hasComponent(type) ? null : (T) components[type.ordinal()];
    }

    public ComponentProcessor getProcessor(int id) {
        return processors[id];
    }

    public ComponentProcessor[] getProcessors() {
        return processors;
    }

    public GenericTile addProcessor(ComponentProcessor processor) {
        for (int i = 0; i < processors.length; i++) {
            if (processors[i] == null) {
                processors[i] = processor;
                processor.holder(this);
                break;
            }
        }
        return this;
    }

    public GenericTile addComponent(IComponent component) {
        component.holder(this);
        if (hasComponent(component.getType())) {
            throw new ExceptionInInitializerError("Component of type: " + component.getType().name() + " already registered!");
        }
        components[component.getType().ordinal()] = component;
        return this;
    }

    @Deprecated(since = "Try not using this method.")
    public GenericTile forceComponent(IComponent component) {
        component.holder(this);
        components[component.getType().ordinal()] = component;
        return this;
    }


    // called when tile is created/loaded from memory
    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        if(propertyManager != null && compound.contains(PropertyManager.NBT_KEY)) {
            CompoundTag propertyData = compound.getCompound(PropertyManager.NBT_KEY);
            propertyManager.loadFromTag(propertyData, registries);
            compound.remove(PropertyManager.NBT_KEY);
        }
        for (IComponent component : components) {
            if (component != null) {
                component.holder(this);
                component.loadFromNBT(compound);
            }
        }
        for (ComponentProcessor pr : processors) {
            if (pr != null) {
                pr.holder(this);
                pr.loadFromNBT(compound);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        if(propertyManager != null) {
            CompoundTag propertyData = new CompoundTag();
            propertyManager.saveToTag(propertyData, registries);
            compound.put(PropertyManager.NBT_KEY, propertyData);
        }
        for (IComponent component : components) {
            if (component != null) {
                component.holder(this);
                component.saveToNBT(compound);
            }
        }
        for (ComponentProcessor pr : processors) {
            if (pr != null) {
                pr.holder(this);
                pr.saveToNBT(compound);
            }
        }
        super.saveAdditional(compound, registries);
    }

    //called either from initial client sync
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if(propertyManager != null) {
            CompoundTag propertyData = new CompoundTag();
            propertyManager.saveAllPropsForClientSync(propertyData, registries);
            tag.put(PropertyManager.NBT_KEY, propertyData);
            propertyManager.clean();
        }

        return tag;
    }

    //Called when Level#sendBlockUpdated is called
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (tile, registries) -> {
            CompoundTag tag = new CompoundTag();
            CompoundTag data = new CompoundTag();
            propertyManager.saveDirtyPropsToTag(data, registries);
            tag.put(PropertyManager.NBT_KEY, data);
            return tag;
        });
    }

    //Only fires on server side
    @Override
    public void onLoad() {
        super.onLoad();

        for (IComponent component : components) {
            if (component != null) {
                component.holder(this);
                component.onLoad();
            }
        }

        if(propertyManager != null) {
            propertyManager.onTileLoaded();
        }
    }

    @Override
    public net.minecraft.network.chat.@NotNull Component getName() {
        return hasComponent(IComponentType.Name) ? this.<ComponentName>getComponent(IComponentType.Name).getName() : net.minecraft.network.chat.Component.literal(References.ID + ".default.tile.name");
    }

    /* Since you have to register it anyway, might as well make it somewhat faster */

    @Nullable
    public ICapabilityElectrodynamic getElectrodynamicCapability(@Nullable Direction side) {
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        return electro == null ? null : electro.getCapability(side, CapabilityInputType.NONE);

    }

    @Nullable
    public IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
        IComponentFluidHandler fluid = getComponent(IComponentType.FluidHandler);
        return fluid == null ? null : fluid.getCapability(side, CapabilityInputType.NONE);
    }

    @Nullable
    public IGasHandler getGasHandlerCapability(@Nullable Direction side) {
        IComponentGasHandler gas = getComponent(IComponentType.GasHandler);
        return gas == null ? null : gas.getCapability(side, CapabilityInputType.NONE);
    }

    @Nullable
    public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        return inv == null ? null : inv.getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (IComponent component : components) {
            if (component != null) {
                component.holder(this);
                component.remove();
            }
        }
        for (ComponentProcessor pr : processors) {
            if (pr != null) {
                pr.holder(this);
                pr.remove();
            }
        }
    }

    public SimpleContainerData getCoordsArray() {
        SimpleContainerData array = new SimpleContainerData(3);
        array.set(0, worldPosition.getX());
        array.set(1, worldPosition.getY());
        array.set(2, worldPosition.getZ());
        return array;
    }

    public boolean isPoweredByRedstone() {
        return level.getDirectSignalTo(worldPosition) > 0;
    }

    public boolean isProcessorActive() {
        for (ComponentProcessor pr : processors) {
            if (pr != null && pr.isActive()) {
                return true;
            }
        }
        return false;
    }

    public int getNumActiveProcessors() {
        int count = 0;
        for (ComponentProcessor pr : processors) {
            if (pr != null && pr.isActive()) {
                count++;
            }
        }
        return count;
    }

    public int getNumProcessors() {
        int count = 0;
        for (ComponentProcessor pr : processors) {
            if (pr != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * NORTH is defined as the default direction
     *
     * @return
     */
    public Direction getFacing() {
        return getBlockState().hasProperty(ElectrodynamicsBlockStates.FACING) ? getBlockState().getValue(ElectrodynamicsBlockStates.FACING) : Direction.NORTH;
    }

    public void onEnergyChange(ComponentElectrodynamic cap) {
        // hook method for now
    }

    // no more polling for upgrade effects :D
    public void onInventoryChange(ComponentInventory inv, int slot) {
        // this can be moved to a seperate tile class in the future
        if (hasComponent(IComponentType.Processor)) {
            this.<ComponentProcessor>getComponent(IComponentType.Processor).onInventoryChange(inv, slot);
        }
        for (ComponentProcessor processor : processors) {
            if (processor != null) {
                processor.onInventoryChange(inv, slot);
            }
        }
    }

    public void onFluidTankChange(FluidTank tank) {
        // hook method for now
    }

    public void onGasTankChange(GasTank tank) {

    }

    public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
        if (hasComponent(IComponentType.ContainerProvider)) {

            if (!level.isClientSide) {

                player.openMenu(getComponent(IComponentType.ContainerProvider));

                player.awardStat(Stats.INTERACT_WITH_FURNACE);

            }

            return InteractionResult.CONSUME;

        }
        return InteractionResult.PASS;
    }

    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
        if (used.getItem() instanceof ItemUpgrade upgrade && hasComponent(IComponentType.Inventory)) {

            ComponentInventory inv = getComponent(IComponentType.Inventory);
            // null check for safety
            if (inv != null && inv.upgrades() > 0) {
                int upgradeIndex = inv.getUpgradeSlotStartIndex();
                for (int i = 0; i < inv.upgrades(); i++) {
                    if (inv.canPlaceItem(upgradeIndex + i, used)) {
                        ItemStack upgradeStack = inv.getItem(upgradeIndex + i);
                        if (upgradeStack.isEmpty()) {
                            if (!level.isClientSide()) {
                                inv.setItem(upgradeIndex + i, used.copy());
                                used.shrink(used.getCount());
                            }
                            return ItemInteractionResult.CONSUME;
                        }
                        if (ItemUtils.testItems(upgrade, upgradeStack.getItem())) {
                            int room = upgradeStack.getMaxStackSize() - upgradeStack.getCount();
                            if (room > 0) {
                                if (!level.isClientSide()) {
                                    int accepted = room > used.getCount() ? used.getCount() : room;
                                    upgradeStack.grow(accepted);
                                    used.shrink(accepted);
                                }
                                return ItemInteractionResult.CONSUME;
                            }
                        }
                    }
                }
            }

        } else if (!(used.getItem() instanceof IWrenchItem)) {

        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void onBlockDestroyed() {

    }

    public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {

    }

    public void onPlace(BlockState oldState, boolean isMoving) {

        for (IComponent component : components) {
            if (component != null) {
                component.holder(this);
                component.onLoad();
            }
        }

    }

    public int getComparatorSignal() {
        return 0;
    }

    public int getDirectSignal(Direction dir) {
        return 0;
    }

    public int getSignal(Direction dir) {
        return 0;
    }

    public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

    }

    public void updateCarriedItemInContainer(ItemStack stack, UUID playerId) {
        Player player = getLevel().getPlayerByUUID(playerId);
        if (player.hasContainerOpen()) {
            player.containerMenu.setCarried(stack);
        }
    }

    protected static TriPredicate<Integer, ItemStack, ComponentInventory> machineValidator() {
        return (x, y, i) -> x < i.getOutputStartIndex() || x >= i.getInputBucketStartIndex() && x < i.getInputGasStartIndex() && y.getCapability(Capabilities.FluidHandler.ITEM) != null || x >= i.getInputGasStartIndex() && x < i.getUpgradeSlotStartIndex() && y.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM) != null
                || x >= i.getUpgradeSlotStartIndex() && y.getItem() instanceof ItemUpgrade upgrade && i.isUpgradeValid(upgrade.subtype);
    }

    public static int[] arr(int... values) {
        return values;
    }

    /**
     * This method will never have air as the newState unless something has gone horribly horribly wrong!
     *
     * @param oldState
     * @param newState
     */
    public void onBlockStateUpdate(BlockState oldState, BlockState newState) {
        for (IComponent component : components) {
            if (component != null) {
                component.refreshIfUpdate(oldState, newState);
            }
        }

        for (IComponent component : processors) {
            if (component != null) {
                component.refreshIfUpdate(oldState, newState);
            }
        }
    }

    public void setPlacedBy(LivingEntity player, ItemStack stack) {

    }

}
