package electrodynamics.prefab.tile;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import electrodynamics.api.IWrenchItem;
import electrodynamics.api.References;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.properties.IPropertyType.TagReader;
import electrodynamics.prefab.properties.IPropertyType.TagWriter;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyManager;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentName;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.prefab.utilities.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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

	@Override
	public void load(@NotNull CompoundTag compound) {
		super.load(compound);
		for (Property<?> prop : propertyManager.getProperties()) {
			if (prop.shouldSave()) {
				Object previousValue = prop.get();
				prop.load(prop.getType().readFromTag(new TagReader(prop, compound)));
				compound.remove(prop.getName());
			}
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
	public void saveAdditional(@NotNull CompoundTag compound) {
		for (Property<?> prop : propertyManager.getProperties()) {
			if (prop.shouldSave()) {
				prop.getType().writeToTag(new TagWriter(prop, compound));
			}
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
		super.saveAdditional(compound);
	}

	@Override
	public void onLoad() {
		super.onLoad();

		for (IComponent component : components) {
			if (component != null) {
				component.holder(this);
				component.onLoad();
			}
		}
	}

	@Override
	public net.minecraft.network.chat.@NotNull Component getName() {
		return hasComponent(IComponentType.Name) ? this.<ComponentName>getComponent(IComponentType.Name).getName() : net.minecraft.network.chat.Component.literal(References.ID + ".default.tile.name");
	}

	@Override
	@NotNull
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
		if (cap == ElectrodynamicsCapabilities.ELECTRODYNAMIC && components[IComponentType.Electrodynamic.ordinal()] != null) {
			return components[IComponentType.Electrodynamic.ordinal()].getCapability(cap, side, null);
		}
		if (cap == ForgeCapabilities.FLUID_HANDLER && components[IComponentType.FluidHandler.ordinal()] != null) {
			return components[IComponentType.FluidHandler.ordinal()].getCapability(cap, side, null);
		}
		if (cap == ForgeCapabilities.ITEM_HANDLER && components[IComponentType.Inventory.ordinal()] != null) {
			return components[IComponentType.Inventory.ordinal()].getCapability(cap, side, null);
		}
		if (cap == ElectrodynamicsCapabilities.GAS_HANDLER && components[IComponentType.GasHandler.ordinal()] != null) {
			return components[IComponentType.GasHandler.ordinal()].getCapability(cap, side, null);
		}
		return LazyOptional.empty();
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

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (!level.isClientSide) {
			if (hasComponent(IComponentType.PacketHandler)) {
				this.<ComponentPacketHandler>getComponent(IComponentType.PacketHandler).sendProperties();
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
		return getBlockState().hasProperty(GenericEntityBlock.FACING) ? getBlockState().getValue(GenericEntityBlock.FACING) : Direction.NORTH;
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

	// This is ceded to the tile to allow for greater control with the use function
	public InteractionResult use(Player player, InteractionHand handIn, BlockHitResult hit) {

		ItemStack stack = player.getItemInHand(handIn);
		if (stack.getItem() instanceof ItemUpgrade upgrade && hasComponent(IComponentType.Inventory)) {

			ComponentInventory inv = getComponent(IComponentType.Inventory);
			// null check for safety
			if (inv != null && inv.upgrades() > 0) {
				int upgradeIndex = inv.getUpgradeSlotStartIndex();
				for (int i = 0; i < inv.upgrades(); i++) {
					if (inv.canPlaceItem(upgradeIndex + i, stack)) {
						ItemStack upgradeStack = inv.getItem(upgradeIndex + i);
						if (upgradeStack.isEmpty()) {
							if (!level.isClientSide()) {
								inv.setItem(upgradeIndex + i, stack.copy());
								stack.shrink(stack.getCount());
							}
							return InteractionResult.CONSUME;
						}
						if (ItemUtils.testItems(upgrade, upgradeStack.getItem())) {
							int room = upgradeStack.getMaxStackSize() - upgradeStack.getCount();
							if (room > 0) {
								if (!level.isClientSide()) {
									int accepted = room > stack.getCount() ? stack.getCount() : room;
									upgradeStack.grow(accepted);
									stack.shrink(accepted);
								}
								return InteractionResult.CONSUME;
							}
						}
					}
				}
			}

		} else if (!(stack.getItem() instanceof IWrenchItem)) {
			if (hasComponent(IComponentType.ContainerProvider)) {

				if (!level.isClientSide) {

					player.openMenu(getComponent(IComponentType.ContainerProvider));

					player.awardStat(Stats.INTERACT_WITH_FURNACE);

				}

				return InteractionResult.CONSUME;

			}
		}
		return InteractionResult.PASS;
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
	
	public void setPlacedBy(LivingEntity placer, ItemStack stack) {
		
	}

	public void updateCarriedItemInContainer(ItemStack stack, UUID playerId) {
		Player player = getLevel().getPlayerByUUID(playerId);
		if (player.hasContainerOpen()) {
			player.containerMenu.setCarried(stack);
		}
	}

	protected static TriPredicate<Integer, ItemStack, ComponentInventory> machineValidator() {
		return (x, y, i) -> x < i.getOutputStartIndex() || x >= i.getInputBucketStartIndex() && x < i.getInputGasStartIndex() && CapabilityUtils.hasFluidItemCap(y) || x >= i.getInputGasStartIndex() && x < i.getUpgradeSlotStartIndex() && CapabilityUtils.hasGasItemCap(y) || x >= i.getUpgradeSlotStartIndex() && y.getItem() instanceof ItemUpgrade upgrade && i.isUpgradeValid(upgrade.subtype);
	}

	public static double[] arr(double... values) {
		return values;
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

}
