package electrodynamics.common.tile.machines.quarry;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerCoolantResavoir;
import electrodynamics.common.network.utils.FluidUtilities;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericMaterialTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class TileCoolantResavoir extends GenericMaterialTile {

	public TileCoolantResavoir(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_COOLANTRESAVOIR.get(), pos, state);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentFluidHandlerSimple(Constants.QUARRY_WATERUSAGE_PER_BLOCK * 1000, this, "tank").setInputDirections(BlockEntityUtils.MachineDirection.FRONT, BlockEntityUtils.MachineDirection.BACK, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT).setValidFluids(Fluids.WATER));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().bucketInputs(1)).valid(machineValidator()));
		addComponent(new ComponentContainerProvider(SubtypeMachine.coolantresavoir, this).createMenu((id, player) -> new ContainerCoolantResavoir(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	private void tickServer(ComponentTickable tick) {
		FluidUtilities.drainItem(this, this.<ComponentFluidHandlerSimple>getComponent(IComponentType.FluidHandler).toArray());
	}

	public boolean hasEnoughFluid(int fluidAmnt) {
		ComponentFluidHandlerSimple simple = getComponent(IComponentType.FluidHandler);
		return !simple.isEmpty() && simple.getFluidAmount() >= fluidAmnt;
	}

	public void drainFluid(int fluidAmnt) {
		ComponentFluidHandlerSimple simple = getComponent(IComponentType.FluidHandler);
		simple.drain(fluidAmnt, FluidAction.EXECUTE);
	}

	@Override
	public int getComparatorSignal() {
		ComponentFluidHandlerSimple handler = (ComponentFluidHandlerSimple) getComponent(IComponentType.FluidHandler);
		return (int) (((double) handler.getFluidAmount() / (double) Math.max(1, handler.getCapacity())) * 15.0);
	}

}
