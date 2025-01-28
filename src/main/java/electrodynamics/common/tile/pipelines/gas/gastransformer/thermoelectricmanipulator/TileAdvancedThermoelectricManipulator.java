package electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator;

import electrodynamics.Electrodynamics;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.inventory.container.tile.ContainerThermoelectricManipulator;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.pipelines.gas.gastransformer.IMultiblockGasTransformer;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerSideBlock;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class TileAdvancedThermoelectricManipulator extends GenericTileThermoelectricManipulator implements IMultiblockGasTransformer {

    public boolean hasBeenDestroyed = false;
    public TileAdvancedThermoelectricManipulator(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_ADVANCED_THERMOELECTRIC_MANIPULATOR.get(), worldPos, blockState);
    }

    @Override
    public void tickClient(ComponentTickable tickable) {
        ElectrodynamicsBlockStates.ManipulatorHeatingStatus status = getBlockState().getValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS);
        if(status == ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF) {
            return;
        }
        if(level.random.nextDouble() < 0.5) {

            Direction direction = getFacing();

            double axisShift = Electrodynamics.RANDOM.nextDouble(0.25) + 0.1;
            double otherShift = Electrodynamics.RANDOM.nextDouble(0.63) + 0.1;
            double yShift = Electrodynamics.RANDOM.nextDouble();

            double xShift = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() < 0 ? -1 + otherShift : otherShift) : direction.getStepZ() < 0 ? 1 - axisShift : axisShift;
            double zShift = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() < 0 ? -1 + otherShift : otherShift) : direction.getStepX() < 0 ? axisShift : 1 - axisShift;

            ParticleOptions particle;

            if(status == ElectrodynamicsBlockStates.ManipulatorHeatingStatus.HEAT){
                particle = ParticleTypes.SMOKE;
            } else {
                particle = ParticleTypes.SNOWFLAKE;
            }

            level.addParticle(particle, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);
            //level.addParticle(ParticleTypes.FLAME, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);

        }
    }

    @Override
    public ComponentContainerProvider getContainerProvider() {
        return new ComponentContainerProvider("container.advancedthermoelectricmanipulator", this).createMenu((id, inv) -> new ContainerThermoelectricManipulator(id, inv, getComponent(IComponentType.Inventory), getCoordsArray()));
    }

    @Override
    public double getUsagePerTick() {
        return Constants.ADVANCED_THERMOELECTRIC_MANIPULATOR_USAGE_PER_TICK;
    }

    @Override
    public int getConversionRate() {
        return Constants.ADVANCED_THERMOELECTRIC_MANIPULATOR_CONVERSION_RATE;
    }

    @Override
    public void updateAddonTanks(int count, boolean isLeft) {
        ComponentGasHandlerMulti handler = getComponent(IComponentType.GasHandler);
        ComponentFluidHandlerMulti multi = getComponent(IComponentType.FluidHandler);
        if (isLeft) {
            multi.getInputTanks()[0].setCapacity(Constants.GAS_TRANSFORMER_BASE_INPUT_CAPCITY + Constants.GAS_TRANSFORMER_ADDON_TANK_CAPCITY * count);
            handler.getInputTanks()[0].setCapacity(Constants.GAS_TRANSFORMER_BASE_INPUT_CAPCITY + Constants.GAS_TRANSFORMER_ADDON_TANK_CAPCITY * count);
        } else {
            multi.getOutputTanks()[0].setCapacity(Constants.GAS_TRANSFORMER_BASE_OUTPUT_CAPCITY + Constants.GAS_TRANSFORMER_ADDON_TANK_CAPCITY * count);
            handler.getOutputTanks()[0].setCapacity(Constants.GAS_TRANSFORMER_BASE_OUTPUT_CAPCITY + Constants.GAS_TRANSFORMER_ADDON_TANK_CAPCITY * count);
        }
    }

    @Override
    public boolean hasBeenDestroyed() {
        return hasBeenDestroyed;
    }

    @Override
    public void onPlace(BlockState oldState, boolean isMoving) {
        super.onPlace(oldState, isMoving);
        if (level.isClientSide) {
            return;
        }
        Direction facing = getFacing();

        BlockEntity left = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.EAST)));
        BlockEntity right = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.WEST)));

        if (left != null && right != null && left instanceof TileGasTransformerSideBlock leftTile && right instanceof TileGasTransformerSideBlock rightTile) {
            leftTile.setOwnerPos(getBlockPos());
            leftTile.setIsLeft();
            leftTile.setChanged();
            rightTile.setOwnerPos(getBlockPos());
            rightTile.setChanged();
        }
    }

    @Override
    public void onBlockDestroyed() {
        if (level.isClientSide || hasBeenDestroyed) {
            return;
        }
        hasBeenDestroyed = true;
        Direction facing = getFacing();
        getLevel().destroyBlock(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.WEST)), false);
        getLevel().destroyBlock(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.EAST)), false);
    }

    @Override
    public void outputToPipe(ComponentProcessor processor, ComponentGasHandlerMulti gasHandler, Direction facing) {

        Direction direction = BlockEntityUtils.getRelativeSide(facing, BlockEntityUtils.MachineDirection.LEFT.mappedDir);// opposite of west is east
        BlockPos face = getBlockPos().relative(direction, 2);
        BlockEntity faceTile = getLevel().getBlockEntity(face);
        if (faceTile != null) {

            IGasHandler handler = faceTile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction.getOpposite());

            if (handler != null) {

                GasTank gasTank = gasHandler.getOutputTanks()[0];
                for (int i = 0; i < handler.getTanks(); i++) {
                    GasStack tankGas = gasTank.getGas();
                    int amtAccepted = handler.fill(tankGas, GasAction.EXECUTE);
                    GasStack taken = new GasStack(tankGas.getGas(), amtAccepted, tankGas.getTemperature(), tankGas.getPressure());
                    gasTank.drain(taken, GasAction.EXECUTE);
                }

            }

        }

        ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);

        face = getBlockPos().relative(direction).relative(Direction.DOWN);
        faceTile = getLevel().getBlockEntity(face);
        if (faceTile != null) {

            IFluidHandler handler = faceTile.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, Direction.UP);

            if (handler != null) {

                FluidTank fluidTank = fluidHandler.getOutputTanks()[0];
                FluidStack tankFluid = fluidTank.getFluid();
                int amtAccepted = handler.fill(tankFluid, IFluidHandler.FluidAction.EXECUTE);
                FluidStack taken = new FluidStack(tankFluid.getFluid(), amtAccepted);
                fluidTank.drain(taken, IFluidHandler.FluidAction.EXECUTE);

            }

        }
    }

    @Override
    public void updateLit(boolean isHeating, Direction facing) {
        if (BlockEntityUtils.isLit(this) ^ isHeating) {
            BlockEntityUtils.updateLit(this, isHeating);
            BlockEntity left = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.EAST)));
            BlockEntity right = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.WEST)));
            if (left != null && left instanceof TileGasTransformerSideBlock leftTile && right != null && right instanceof TileGasTransformerSideBlock rightTile) {
                BlockEntityUtils.updateLit(leftTile, isHeating);
                BlockEntityUtils.updateLit(rightTile, isHeating);
            }
        }
    }

    @Override
    public IComponentFluidHandler getFluidHandler() {
        return new ComponentFluidHandlerMulti.ComponentFluidHandlerMultiBiDirec(this).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).setInputTanks(1, arr(Constants.GAS_TRANSFORMER_BASE_INPUT_CAPCITY)).setOutputDirections(BlockEntityUtils.MachineDirection.BOTTOM).setOutputTanks(1, arr(Constants.GAS_TRANSFORMER_BASE_OUTPUT_CAPCITY));
    }

    @Override
    public int getHeatTransfer() {
        return Constants.ADVANCED_THERMOELECTRIC_MANIPULATOR_HEAT_TRANSFER;
    }
}
