package electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator;

import java.util.function.BiConsumer;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.inventory.container.tile.ContainerThermoelectricManipulator;
import electrodynamics.common.tile.pipelines.gas.gastransformer.GenericTileGasTransformer;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public abstract class GenericTileThermoelectricManipulator extends GenericTileGasTransformer {

    /**
     * NOTE heat transfer can NEVER be more than 90. This is because above 90, it becomes possible to create an infinite energy
     * loop with the steam turbine from Nuclear Science.
     */

    public final Property<Integer> targetTemperature = property(new Property<>(PropertyTypes.INTEGER, "targettemperature", Gas.ROOM_TEMPERATURE));

    private boolean isFluid = false;
    private boolean changeState = false;

    private Gas evaporatedGas;

    public GenericTileThermoelectricManipulator(BlockEntityType<?> type, BlockPos worldPos, BlockState blockState) {
        super(type, worldPos, blockState);
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(getUsagePerTick() * 10));
        addComponent(getFluidHandler());
    }

    @Override
    public boolean canProcess(ComponentProcessor processor) {

        Direction facing = getFacing();

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);

        processor.consumeGasCylinder();
        processor.dispenseGasCylinder();

        processor.consumeBucket();
        processor.dispenseBucket();

        outputToPipe(processor, gasHandler, facing);

        ManipulatorStatusCheckWrapper result = checkGasConditions(processor);

        if (result.canProcess()) {

            isFluid = false;

        } else {

            result = checkFluidConditions(processor);
            isFluid = result.canProcess();
        }
        changeState = result.changeState();

        boolean isHeating = result.status() == ElectrodynamicsBlockStates.ManipulatorHeatingStatus.HEAT && result.canProcess();

        updateLit(isHeating, facing);

        ElectrodynamicsBlockStates.ManipulatorHeatingStatus currStatus = getBlockState().getValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS);

        if (currStatus != result.status()) {

            getLevel().setBlockAndUpdate(worldPosition, getBlockState().setValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, result.status()));

        }

        return result.canProcess();
    }

    public abstract void outputToPipe(ComponentProcessor processor, ComponentGasHandlerMulti multi, Direction facing);

    public abstract void updateLit(boolean isHeating, Direction facing);

    private ManipulatorStatusCheckWrapper checkGasConditions(ComponentProcessor processor) {

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);
        GasTank inputTank = gasHandler.getInputTanks()[0];
        if (inputTank.isEmpty()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (electro.getJoulesStored() < getUsagePerTick() * processor.operatingSpeed.get()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        if (inputTank.getGas().getGas().getCondensationTemp() >= targetTemperature.get()) {

            // gas is condensed
            ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);

            FluidTank outputTank = fluidHandler.getOutputTanks()[0];

            if (outputTank.getFluidAmount() >= outputTank.getCapacity()) {
                return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
            }

            if (inputTank.getGas().getGas().noCondensedFluid()) {
                return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
            }

            Fluid condensedFluid = inputTank.getGas().getGas().getCondensedFluid();

            if (!outputTank.isEmpty() && !outputTank.getFluid().getFluid().isSame(condensedFluid)) {
                return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
            }

            ElectrodynamicsBlockStates.ManipulatorHeatingStatus status;

            if (inputTank.getGas().getTemperature() < targetTemperature.get()) {
                status = ElectrodynamicsBlockStates.ManipulatorHeatingStatus.HEAT;
            } else if (inputTank.getGas().getTemperature() > targetTemperature.get()) {
                status = ElectrodynamicsBlockStates.ManipulatorHeatingStatus.COOL;
            } else {
                status = ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF;
            }

            return new ManipulatorStatusCheckWrapper(true, status, true);

        }
        GasTank outputTank = gasHandler.getOutputTanks()[0];
        if (outputTank.getGasAmount() >= outputTank.getCapacity()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        if (!outputTank.isEmpty() && !outputTank.getGas().isSameGas(inputTank.getGas())) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        if (inputTank.getGas().getTemperature() <= GasStack.ABSOLUTE_ZERO) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        ElectrodynamicsBlockStates.ManipulatorHeatingStatus status;

        if (inputTank.getGas().getTemperature() < targetTemperature.get()) {
            status = ElectrodynamicsBlockStates.ManipulatorHeatingStatus.HEAT;
        } else if (inputTank.getGas().getTemperature() > targetTemperature.get()) {
            status = ElectrodynamicsBlockStates.ManipulatorHeatingStatus.COOL;
        } else {
            status = ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF;
        }

        return new ManipulatorStatusCheckWrapper(true, status, false);

    }

    private ManipulatorStatusCheckWrapper checkFluidConditions(ComponentProcessor processor) {

        ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);

        FluidTank inputTank = fluidHandler.getInputTanks()[0];

        if (inputTank.isEmpty()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (electro.getJoulesStored() < getUsagePerTick() * processor.operatingSpeed.get()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);

        GasTank outputTank = gasHandler.getOutputTanks()[0];

        if (outputTank.getGasAmount() >= outputTank.getCapacity()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        evaporatedGas = ElectrodynamicsGases.MAPPED_GASSES.getOrDefault(BuiltInRegistries.FLUID.wrapAsHolder(inputTank.getFluid().getFluid()), ElectrodynamicsGases.EMPTY.value());

        if (evaporatedGas.isEmpty()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        if (targetTemperature.get() <= evaporatedGas.getCondensationTemp()) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        if (!outputTank.isEmpty() && !outputTank.getGas().getGas().equals(evaporatedGas)) {
            return new ManipulatorStatusCheckWrapper(false, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.OFF, false);
        }

        return new ManipulatorStatusCheckWrapper(true, ElectrodynamicsBlockStates.ManipulatorHeatingStatus.HEAT, true);
    }

    @Override
    public void process(ComponentProcessor processor) {
        ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);
        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);

        int conversionRate = (int) (getConversionRate() * processor.operatingSpeed.get());

        // fluid to gas
        if (isFluid && changeState) {

            FluidTank inputTank = fluidHandler.getInputTanks()[0];
            GasTank outputTank = gasHandler.getOutputTanks()[0];

            int deltaT = targetTemperature.get() - evaporatedGas.getCondensationTemp();

            conversionRate = conversionRate * getAdjustedHeatingFactor(deltaT);

            if (conversionRate < 1) {
                conversionRate = 1;
            }

            int maxTake = Math.min(inputTank.getFluidAmount(), conversionRate);

            GasStack evaporatedPotential = new GasStack(evaporatedGas, maxTake, evaporatedGas.getCondensationTemp(), Gas.PRESSURE_AT_SEA_LEVEL);

            evaporatedPotential.heat(deltaT);

            int taken = outputTank.fill(evaporatedPotential, GasAction.EXECUTE);

            if (taken == 0) {
                return;
            }

            evaporatedPotential.setAmount(taken);

            evaporatedPotential.heat(-deltaT);

            inputTank.drain(evaporatedPotential.getAmount(), IFluidHandler.FluidAction.EXECUTE);

            // gas to fluid
        } else if (changeState) {

            GasTank inputTank = gasHandler.getInputTanks()[0];
            FluidTank outputTank = fluidHandler.getOutputTanks()[0];

            int targetTemp = targetTemperature.get() < inputTank.getGas().getGas().getCondensationTemp() ? inputTank.getGas().getGas().getCondensationTemp() : targetTemperature.get();

            int deltaT = targetTemp - inputTank.getGas().getTemperature();

            conversionRate = conversionRate * getAdjustedHeatingFactor(deltaT);

            if (conversionRate < 1) {
                conversionRate = 1;
            }

            GasStack condensedPotential = new GasStack(inputTank.getGas().getGas(), inputTank.getGasAmount(), inputTank.getGas().getTemperature(), inputTank.getGas().getPressure());

            condensedPotential.bringPressureTo(Gas.PRESSURE_AT_SEA_LEVEL);

            condensedPotential.heat(deltaT);

            int fluidAmount = Math.min(conversionRate, condensedPotential.getAmount());

            if (fluidAmount == 0) {
                return;
            }

            FluidStack condensedFluid = new FluidStack(condensedPotential.getGas().getCondensedFluid(), fluidAmount);

            int taken = outputTank.fill(condensedFluid, IFluidHandler.FluidAction.EXECUTE);

            if (taken == 0) {
                return;
            }

            condensedPotential.setAmount(taken);

            condensedPotential.bringPressureTo(inputTank.getGas().getPressure());

            condensedPotential.heat(-deltaT);

            inputTank.drain(condensedPotential, GasAction.EXECUTE);

            // gas to gas
        } else {

            GasTank inputTank = gasHandler.getInputTanks()[0];
            GasTank outputTank = gasHandler.getOutputTanks()[0];

            int deltaT = targetTemperature.get() - inputTank.getGas().getTemperature();

            conversionRate = conversionRate * getAdjustedHeatingFactor(deltaT);

            int maxTake = inputTank.getGasAmount() > conversionRate ? conversionRate : inputTank.getGasAmount();

            GasStack condensedPotential = new GasStack(inputTank.getGas().getGas(), maxTake, inputTank.getGas().getTemperature(), inputTank.getGas().getPressure());

            condensedPotential.heat(deltaT);

            int taken = outputTank.fill(condensedPotential, GasAction.EXECUTE);

            if (taken == 0) {
                return;
            }

            condensedPotential.setAmount(taken);

            condensedPotential.heat(-deltaT);

            inputTank.drain(condensedPotential, GasAction.EXECUTE);
        }

    }

    @Override
    public ComponentInventory getInventory() {
        return new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().bucketInputs(1).gasInputs(1).bucketOutputs(1).gasOutputs(1).upgrades(3)).valid(machineValidator()).validUpgrades(ContainerThermoelectricManipulator.VALID_UPGRADES);
    }

    public abstract IComponentFluidHandler getFluidHandler();

    @Override
    public BiConsumer<GasTank, GenericTile> getCondensedHandler() {

        return (tank, tile) -> {

            FluidTank outputTank = tile.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getOutputTanks()[0];

            GasStack tankGas = tank.getGas().copy();

            tank.setGas(GasStack.EMPTY);

            if (tankGas.isEmpty() || !outputTank.isEmpty()) {
                return;
            }

            Fluid fluid = tankGas.getGas().getCondensedFluid();

            if (fluid.isSame(Fluids.EMPTY)) {
                return;
            }

            tankGas.bringPressureTo(Gas.PRESSURE_AT_SEA_LEVEL);

            FluidStack fluidStack = new FluidStack(fluid, tankGas.getAmount());

            outputTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

        };
    }

    private int getAdjustedHeatingFactor(int deltaT) {

        if (deltaT == 0) {
            return 1;
        }

        return (int) Math.max(1, Math.abs((double) getHeatTransfer() / (double) deltaT));

    }

    public abstract int getHeatTransfer();

    private static record ManipulatorStatusCheckWrapper(boolean canProcess, ElectrodynamicsBlockStates.ManipulatorHeatingStatus status, boolean changeState) {

    }

}
