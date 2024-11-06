package electrodynamics.common.tile.pipelines.gas.gastransformer.compressor;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.inventory.container.tile.ContainerCompressor;
import electrodynamics.common.tile.pipelines.gas.gastransformer.GenericTileGasTransformer;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerSideBlock;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.*;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GenericTileCompressor extends GenericTileGasTransformer {
    public GenericTileCompressor(BlockEntityType<?> type, BlockPos worldPos, BlockState blockState) {
        super(type, worldPos, blockState);
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(BASE_INPUT_CAPACITY * 10));
    }

    @Override
    public void tickClient(ComponentTickable tickable) {

        if (!isSoundPlaying && shouldPlaySound()) {
            isSoundPlaying = true;
            SoundBarrierMethods.playTileSound(getSound(), this, true);
        }

    }

    @Override
    public boolean canProcess(ComponentProcessor processor) {

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);

        processor.consumeGasCylinder();
        processor.dispenseGasCylinder();

        Direction facing = getFacing();

        Direction direction = BlockEntityUtils.getRelativeSide(facing, BlockEntityUtils.MachineDirection.LEFT.mappedDir);// opposite of west is east
        BlockPos face = getBlockPos().relative(direction, 2);
        BlockEntity faceTile = getLevel().getBlockEntity(face);
        if (faceTile != null) {

            IGasHandler handler = faceTile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction.getOpposite());

            if(handler != null) {
                GasTank gasTank = gasHandler.getOutputTanks()[0];
                for (int i = 0; i < handler.getTanks(); i++) {
                    GasStack tankGas = gasTank.getGas();
                    double amtAccepted = handler.fillTank(i, tankGas, GasAction.EXECUTE);
                    GasStack taken = new GasStack(tankGas.getGas(), amtAccepted, tankGas.getTemperature(), tankGas.getPressure());
                    gasTank.drain(taken, GasAction.EXECUTE);
                }
            }
        }

        boolean canProcess = checkConditions(processor);
        if (BlockEntityUtils.isLit(this) ^ canProcess) {
            BlockEntityUtils.updateLit(this, canProcess);
            BlockEntity left = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.EAST)));
            BlockEntity right = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.WEST)));
            if (left != null && left instanceof TileGasTransformerSideBlock leftTile && right != null && right instanceof TileGasTransformerSideBlock rightTile) {
                BlockEntityUtils.updateLit(leftTile, canProcess);
                BlockEntityUtils.updateLit(rightTile, canProcess);
            }
        }
        return canProcess;
    }

    private boolean checkConditions(ComponentProcessor processor) {

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);
        GasTank inputTank = gasHandler.getInputTanks()[0];
        GasTank outputTank = gasHandler.getOutputTanks()[0];
        if (inputTank.isEmpty()) {
            return false;
        }

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (electro.getJoulesStored() < USAGE_PER_TICK * processor.operatingSpeed.get()) {
            return false;
        }

        if (outputTank.getGasAmount() >= outputTank.getCapacity()) {
            return false;
        }

        if (!outputTank.isEmpty() && !outputTank.getGas().isSameGas(inputTank.getGas())) {
            return false;
        }

        if (getPressureMultiplier() < 1.0 && inputTank.getGas().getPressure() <= GasStack.VACUUM) {
            return false;
        }

        return true;
    }

    @Override
    public void process(ComponentProcessor processor) {

        double conversionRate = BASE_CONVERSION_RATE * processor.operatingSpeed.get();

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);
        GasTank inputTank = gasHandler.getInputTanks()[0];
        GasTank outputTank = gasHandler.getOutputTanks()[0];

        int currPressure = inputTank.getGas().getPressure();

        int newPressure = (int) (currPressure * getPressureMultiplier());

        GasStack toTake = new GasStack(inputTank.getGas().getGas(), Math.min(conversionRate, inputTank.getGasAmount()), inputTank.getGas().getTemperature(), inputTank.getGas().getPressure());

        toTake.bringPressureTo(newPressure);

        double taken = outputTank.fill(toTake.copy(), GasAction.EXECUTE);

        if (taken == 0) {
            return;
        }

        toTake.setAmount(taken);

        toTake.bringPressureTo(currPressure);

        inputTank.drain(toTake.getAmount(), GasAction.EXECUTE);

    }

    @Override
    public ComponentInventory getInventory() {
        return new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().gasInputs(1).gasOutputs(1).upgrades(3)).valid(machineValidator()).validUpgrades(ContainerCompressor.VALID_UPGRADES);
    }

    public abstract double getPressureMultiplier();

    public abstract void outputToPipe(ComponentProcessor processor, ComponentGasHandlerMulti multi, Direction facing);

    public abstract void updateLit(boolean isHeating, Direction facing);

    public abstract SoundEvent getSound();

}
