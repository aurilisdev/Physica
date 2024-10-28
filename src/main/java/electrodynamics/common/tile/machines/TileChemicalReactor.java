package electrodynamics.common.tile.machines;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.multiblock.Subnode;
import electrodynamics.api.multiblock.parent.IMultiblockParentTile;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerChemicalReactor;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.*;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.tile.types.GenericGasTile;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class TileChemicalReactor extends GenericGasTile implements IMultiblockParentTile {

    public static final int MAX_FLUID_TANK_CAPACITY = 5000;
    public static final double MAX_GAS_TANK_CAPACITY = 5000.0;

    public TileChemicalReactor(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_CHEMICALREACTOR.get(), worldPos, blockState);
        addComponent(new ComponentTickable(this));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentContainerProvider(SubtypeMachine.chemicalreactor, this).createMenu((id, player) -> new ContainerChemicalReactor(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(Direction.UP, Direction.DOWN).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 4));
        addComponent(new ComponentFluidHandlerMulti(this).setTanks(2, 2, new int[] {MAX_FLUID_TANK_CAPACITY, MAX_FLUID_TANK_CAPACITY}, new int[] {MAX_FLUID_TANK_CAPACITY, MAX_FLUID_TANK_CAPACITY}).setInputDirections(Direction.WEST, Direction.NORTH).setOutputDirections(Direction.EAST, Direction.SOUTH));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().processors(1, 2, 2, 3).bucketInputs(2).bucketOutputs(2).upgrades(3)).setDirectionsBySlot(0, Direction.SOUTH).setDirectionsBySlot(1, Direction.EAST).setSlotsByDirection(Direction.WEST, 2, 3).setSlotsByDirection(Direction.NORTH, 4, 5, 6).validUpgrades(ContainerChemicalReactor.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentGasHandlerMulti(this).setInputDirections(Direction.WEST, Direction.NORTH).setOutputDirections(Direction.EAST, Direction.SOUTH).setInputTanks(2, arr(MAX_GAS_TANK_CAPACITY, MAX_GAS_TANK_CAPACITY), arr(1000.0, 1000.0), arr(1024, 1024)).setOutputTanks(2, arr(MAX_GAS_TANK_CAPACITY, MAX_GAS_TANK_CAPACITY), arr(1000.0, 1000.0), arr(1024, 1024)).setCondensedHandler(getCondensedHandler()));
        //addComponent(new ComponentProcessor(this).canProcess(component -> component.outputToFluidPipe().consumeBucket().dispenseBucket().consumeGasCylinder().dispenseGasCylinder().canProcessFluidItem2FluidRecipe(component, ElectrodynamicsRecipeInit.CHEMICAL_MIXER_TYPE.get())).process(component -> component.processFluidItem2FluidRecipe(component)));
    }

    @Override
    public Subnode[] getSubNodes() {
        return BlockMachine.SUBNODES_CHEMICALREACTOR;
    }

    @Override
    public void onSubnodeDestroyed(TileMultiSubnode subnode) {
        level.destroyBlock(worldPosition, true);
    }

    @Override
    public InteractionResult onSubnodeUseWithoutItem(Player player, BlockHitResult hit, TileMultiSubnode subnode) {
        return useWithoutItem(player, hit);
    }

    @Override
    public ItemInteractionResult onSubnodeUseWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit, TileMultiSubnode subnode) {
        return useWithItem(used, player, hand, hit);
    }

    @Override
    public int getSubdnodeComparatorSignal(TileMultiSubnode subnode) {
        return getComparatorSignal();
    }

    @Override
    public Direction getFacingDirection() {
        return getFacing();
    }

    @Override
    public @Nullable ICapabilityElectrodynamic getSubnodeElectrodynamicCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public @Nullable IFluidHandler getSubnodeFluidHandlerCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return this.<IComponentFluidHandler>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public @Nullable IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
        return null;
    }

    @Override
    public @Nullable IItemHandler getSubnodeItemHandlerCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return this.<ComponentInventory>getComponent(IComponentType.Inventory).getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return null;
    }


}
