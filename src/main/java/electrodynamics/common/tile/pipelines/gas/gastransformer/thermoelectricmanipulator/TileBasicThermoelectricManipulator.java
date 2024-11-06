package electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.inventory.container.tile.ContainerThermoelectricManipulator;
import electrodynamics.common.tile.pipelines.gas.gastransformer.IAddonTankManager;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerAddonTank;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.*;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileBasicThermoelectricManipulator extends GenericTileThermoelectricManipulator implements IAddonTankManager {
    public TileBasicThermoelectricManipulator(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_THERMOELECTRIC_MANIPULATOR.get(), worldPos, blockState);
    }

    @Override
    public void outputToPipe(ComponentProcessor processor, ComponentGasHandlerMulti multi, Direction facing) {
        processor.outputToFluidPipe();
        processor.outputToGasPipe();
    }

    @Override
    public void updateLit(boolean isHeating, Direction facing) {
        if (BlockEntityUtils.isLit(this) ^ isHeating) {
            BlockEntityUtils.updateLit(this, isHeating);
        }
    }

    @Override
    public IComponentFluidHandler getFluidHandler() {
        return new ComponentFluidHandlerMulti(this).setInputTanks(1, (int) BASE_INPUT_CAPACITY).setInputDirections(BlockEntityUtils.MachineDirection.BACK).setOutputTanks(1, (int) BASE_OUTPUT_CAPACITY).setOutputDirections(BlockEntityUtils.MachineDirection.FRONT);
    }

    @Override
    public ComponentContainerProvider getContainerProvider() {
        return new ComponentContainerProvider("container.thermoelectricmanipulator", this).createMenu((id, inv) -> new ContainerThermoelectricManipulator(id, inv, getComponent(IComponentType.Inventory), getCoordsArray()));
    }

    @Override
    public void tickClient(ComponentTickable tickable) {
        ElectrodynamicsBlockStates.ManipulatorHeatingStatus status = getBlockState().getValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS);
        if (status == ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF) {
            return;
        }
        if (level.random.nextDouble() < 0.5) {

            //TODO particles

        }
    }

    @Override
    public void updateTankCount() {
        BlockPos abovePos = getBlockPos().above();
        BlockState aboveState = getLevel().getBlockState(abovePos);
        BlockEntity aboveTile;
        int tankCount = 0;
        for (int i = 0; i < TileGasTransformerAddonTank.MAX_ADDON_TANKS; i++) {
            if (!aboveState.is(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK)) {
                break;
            }
            aboveTile = getLevel().getBlockEntity(abovePos);
            if ((aboveTile == null) || !(aboveTile instanceof TileGasTransformerAddonTank tank)) {
                break;
            }
            abovePos = abovePos.above();
            aboveState = getLevel().getBlockState(abovePos);
            tank.setOwnerPos(getBlockPos());
            tankCount++;
        }
        ComponentGasHandlerMulti handler = getComponent(IComponentType.GasHandler);
        ComponentFluidHandlerMulti multi = getComponent(IComponentType.FluidHandler);
        multi.getInputTanks()[0].setCapacity((int) (BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * tankCount));
        handler.getInputTanks()[0].setCapacity(BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * tankCount);
        multi.getOutputTanks()[0].setCapacity((int) (BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * tankCount));
        handler.getOutputTanks()[0].setCapacity(BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * tankCount);

    }
}
