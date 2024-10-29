package electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator;

import java.util.function.BiConsumer;

import electrodynamics.Electrodynamics;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates.ManipulatorHeatingStatus;
import electrodynamics.common.inventory.container.tile.ContainerThermoelectricManipulator;
import electrodynamics.common.tile.pipelines.gas.gastransformer.GenericTileGasTransformer;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerAddonTank;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerSideBlock;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti.ComponentFluidHandlerMultiBiDirec;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class TileThermoelectricManipulator extends GenericTileGasTransformer {

    /**
     * NOTE this value can NEVER be more than 90. This is because above 90, it becomes possible to create an infinite energy
     * loop with the steam turbine from Nuclear Science.
     */
    private static final double HEAT_TRANSFER = 10.0; // degrees kelvin

    public final Property<Double> targetTemperature = property(new Property<>(PropertyTypes.DOUBLE, "targettemperature", Gas.ROOM_TEMPERATURE));

    private boolean isFluid = false;
    private boolean changeState = false;

    private Gas evaporatedGas;

    public TileThermoelectricManipulator(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_THERMOELECTRIC_MANIPULATOR.get(), worldPos, blockState);
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(Direction.DOWN).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(BASE_INPUT_CAPACITY * 10));
        addComponent(new ComponentFluidHandlerMultiBiDirec(this).setInputDirections(Direction.DOWN).setInputTanks(1, arr((int) BASE_INPUT_CAPACITY)).setOutputDirections(Direction.DOWN).setOutputTanks(1, arr((int) BASE_OUTPUT_CAPACITY)));
    }

    @Override
    public boolean canProcess(ComponentProcessor processor) {

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);

        processor.consumeGasCylinder();
        processor.dispenseGasCylinder();

        processor.consumeBucket();
        processor.dispenseBucket();

        Direction facing = getFacing();

        Direction direction = BlockEntityUtils.getRelativeSide(facing, Direction.EAST);// opposite of west is east
        BlockPos face = getBlockPos().relative(direction.getOpposite(), 2);
        BlockEntity faceTile = getLevel().getBlockEntity(face);
        if (faceTile != null) {

            IGasHandler handler = faceTile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction);

            if (handler != null) {

                GasTank gasTank = gasHandler.getOutputTanks()[0];
                for (int i = 0; i < handler.getTanks(); i++) {
                    GasStack tankGas = gasTank.getGas();
                    double amtAccepted = handler.fillTank(i, tankGas, GasAction.EXECUTE);
                    GasStack taken = new GasStack(tankGas.getGas(), amtAccepted, tankGas.getTemperature(), tankGas.getPressure());
                    gasTank.drain(taken, GasAction.EXECUTE);
                }

            }

        }

        ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);

        face = getBlockPos().relative(direction.getOpposite()).relative(Direction.DOWN);
        faceTile = getLevel().getBlockEntity(face);
        if (faceTile != null) {

            IFluidHandler handler = faceTile.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction);

            if (handler != null) {

                FluidTank fluidTank = fluidHandler.getOutputTanks()[0];
                FluidStack tankFluid = fluidTank.getFluid();
                int amtAccepted = handler.fill(tankFluid, FluidAction.EXECUTE);
                FluidStack taken = new FluidStack(tankFluid.getFluid(), amtAccepted);
                fluidTank.drain(taken, FluidAction.EXECUTE);

            }

        }

        ManipulatorStatusCheckWrapper result = checkGasConditions(processor);

        if (result.canProcess()) {

            isFluid = false;
            changeState = result.changeState();

        } else {

            result = checkFluidConditions(processor);
            isFluid = result.canProcess();
            changeState = result.changeState();
        }

        boolean isHeating = result.status() == ManipulatorHeatingStatus.HEAT && result.canProcess();

        if (BlockEntityUtils.isLit(this) ^ isHeating) {
            BlockEntityUtils.updateLit(this, isHeating);
            BlockEntity left = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.EAST)));
            BlockEntity right = getLevel().getBlockEntity(getBlockPos().relative(BlockEntityUtils.getRelativeSide(facing, Direction.WEST)));
            if (left != null && left instanceof TileGasTransformerSideBlock leftTile && right != null && right instanceof TileGasTransformerSideBlock rightTile) {
                BlockEntityUtils.updateLit(leftTile, isHeating);
                BlockEntityUtils.updateLit(rightTile, isHeating);
            }
        }

        ManipulatorHeatingStatus currStatus = getBlockState().getValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS);

        if (currStatus != result.status()) {

            getLevel().setBlockAndUpdate(worldPosition, getBlockState().setValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, result.status()));

        }

        return result.canProcess();
    }

    private ManipulatorStatusCheckWrapper checkGasConditions(ComponentProcessor processor) {

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);
        GasTank inputTank = gasHandler.getInputTanks()[0];
        if (inputTank.isEmpty()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (electro.getJoulesStored() < USAGE_PER_TICK * processor.operatingSpeed.get()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        if (inputTank.getGas().getGas().getCondensationTemp() >= targetTemperature.get()) {

            // gas is condensed
            ComponentFluidHandlerMultiBiDirec fluidHandler = getComponent(IComponentType.FluidHandler);

            FluidTank outputTank = fluidHandler.getOutputTanks()[0];

            if (outputTank.getFluidAmount() >= outputTank.getCapacity()) {
                return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
            }

            Fluid condensedFluid = inputTank.getGas().getGas().getCondensedFluid();

            if (condensedFluid == null) {
                return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
            }

            if (!outputTank.isEmpty() && !outputTank.getFluid().getFluid().isSame(condensedFluid)) {
                return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
            }

            ManipulatorHeatingStatus status;

            if (inputTank.getGas().getTemperature() < targetTemperature.get()) {
                status = ManipulatorHeatingStatus.HEAT;
            } else if (inputTank.getGas().getTemperature() > targetTemperature.get()) {
                status = ManipulatorHeatingStatus.COOL;
            } else {
                status = ManipulatorHeatingStatus.OFF;
            }

            return new ManipulatorStatusCheckWrapper(true, status, true);

        }
        GasTank outputTank = gasHandler.getOutputTanks()[0];
        if (outputTank.getGasAmount() >= outputTank.getCapacity()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        if (!outputTank.isEmpty() && !outputTank.getGas().isSameGas(inputTank.getGas())) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        if (inputTank.getGas().getTemperature() <= GasStack.ABSOLUTE_ZERO) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        ManipulatorHeatingStatus status;

        if (inputTank.getGas().getTemperature() < targetTemperature.get()) {
            status = ManipulatorHeatingStatus.HEAT;
        } else if (inputTank.getGas().getTemperature() > targetTemperature.get()) {
            status = ManipulatorHeatingStatus.COOL;
        } else {
            status = ManipulatorHeatingStatus.OFF;
        }

        return new ManipulatorStatusCheckWrapper(true, status, false);

    }

    private ManipulatorStatusCheckWrapper checkFluidConditions(ComponentProcessor processor) {

        ComponentFluidHandlerMultiBiDirec fluidHandler = getComponent(IComponentType.FluidHandler);

        FluidTank inputTank = fluidHandler.getInputTanks()[0];

        if (inputTank.isEmpty()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (electro.getJoulesStored() < USAGE_PER_TICK * processor.operatingSpeed.get()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);

        GasTank outputTank = gasHandler.getOutputTanks()[0];

        if (outputTank.getGasAmount() >= outputTank.getCapacity()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        evaporatedGas = ElectrodynamicsGases.MAPPED_GASSES.getOrDefault(inputTank.getFluid().getFluid(), ElectrodynamicsGases.EMPTY.value());

        if (evaporatedGas.isEmpty()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        if (targetTemperature.get() <= evaporatedGas.getCondensationTemp()) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        if (!outputTank.isEmpty() && !outputTank.getGas().getGas().equals(evaporatedGas)) {
            return new ManipulatorStatusCheckWrapper(false, ManipulatorHeatingStatus.OFF, false);
        }

        return new ManipulatorStatusCheckWrapper(true, ManipulatorHeatingStatus.HEAT, true);
    }

    @Override
    public void process(ComponentProcessor processor) {
        ComponentFluidHandlerMultiBiDirec fluidHandler = getComponent(IComponentType.FluidHandler);
        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);

        double conversionRate = BASE_CONVERSION_RATE * processor.operatingSpeed.get();

        // fluid to gas
        if (isFluid && changeState) {

            FluidTank inputTank = fluidHandler.getInputTanks()[0];
            GasTank outputTank = gasHandler.getOutputTanks()[0];

            double deltaT = targetTemperature.get() - evaporatedGas.getCondensationTemp();

            conversionRate = conversionRate * getAdjustedHeatingFactor(deltaT);

            if (conversionRate < 1) {
                conversionRate = 1;
            }

            double maxTake = inputTank.getFluidAmount() > conversionRate ? conversionRate : inputTank.getFluidAmount();

            GasStack evaporatedPotential = new GasStack(evaporatedGas, maxTake, evaporatedGas.getCondensationTemp(), Gas.PRESSURE_AT_SEA_LEVEL);

            evaporatedPotential.heat(deltaT);

            double taken = outputTank.fill(evaporatedPotential, GasAction.EXECUTE);

            if (taken == 0) {
                return;
            }

            evaporatedPotential.setAmount(taken);

            evaporatedPotential.heat(-deltaT);

            inputTank.drain((int) Math.ceil(evaporatedPotential.getAmount()), FluidAction.EXECUTE);

            // gas to fluid
        } else if (changeState) {

            GasTank inputTank = gasHandler.getInputTanks()[0];
            FluidTank outputTank = fluidHandler.getOutputTanks()[0];

            double targetTemp = targetTemperature.get() < inputTank.getGas().getGas().getCondensationTemp() ? inputTank.getGas().getGas().getCondensationTemp() : targetTemperature.get();

            double deltaT = targetTemp - inputTank.getGas().getTemperature();

            conversionRate = conversionRate * getAdjustedHeatingFactor(deltaT);

            if (conversionRate < 1) {
                conversionRate = 1;
            }

            GasStack condensedPotential = new GasStack(inputTank.getGas().getGas(), inputTank.getGasAmount(), inputTank.getGas().getTemperature(), inputTank.getGas().getPressure());

            condensedPotential.bringPressureTo(Gas.PRESSURE_AT_SEA_LEVEL);

            condensedPotential.heat(deltaT);

            int fluidAmount = (int) Math.floor(Math.min(conversionRate, condensedPotential.getAmount()));

            if (fluidAmount == 0) {
                return;
            }

            FluidStack condensedFluid = new FluidStack(condensedPotential.getGas().getCondensedFluid(), fluidAmount);

            int taken = outputTank.fill(condensedFluid, FluidAction.EXECUTE);

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

            double deltaT = targetTemperature.get() - inputTank.getGas().getTemperature();

            conversionRate = conversionRate * getAdjustedHeatingFactor(deltaT);

            double maxTake = inputTank.getGasAmount() > conversionRate ? conversionRate : inputTank.getGasAmount();

            GasStack condensedPotential = new GasStack(inputTank.getGas().getGas(), maxTake, inputTank.getGas().getTemperature(), inputTank.getGas().getPressure());

            condensedPotential.heat(deltaT);

            double taken = outputTank.fill(condensedPotential, GasAction.EXECUTE);

            if (taken == 0) {
                return;
            }

            condensedPotential.setAmount(taken);

            condensedPotential.heat(-deltaT);

            inputTank.drain(condensedPotential, GasAction.EXECUTE);
        }

    }

    @Override
    public void tickClient(ComponentTickable tickable) {
        ManipulatorHeatingStatus status = getBlockState().getValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS);
        if(status == ManipulatorHeatingStatus.OFF) {
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

            if(status == ManipulatorHeatingStatus.HEAT){
                particle = ParticleTypes.SMOKE;
            } else {
                particle = ParticleTypes.SNOWFLAKE;
            }

            level.addParticle(particle, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);
            //level.addParticle(ParticleTypes.FLAME, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);






        }
    }

    @Override
    public void updateAddonTanks(int count, boolean isLeft) {
        ComponentGasHandlerMulti handler = getComponent(IComponentType.GasHandler);
        ComponentFluidHandlerMulti multi = getComponent(IComponentType.FluidHandler);
        if (isLeft) {
            multi.getInputTanks()[0].setCapacity((int) (BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * count));
            handler.getInputTanks()[0].setCapacity(BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * count);
        } else {
            multi.getOutputTanks()[0].setCapacity((int) (BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * count));
            handler.getOutputTanks()[0].setCapacity(BASE_INPUT_CAPACITY + TileGasTransformerAddonTank.ADDITIONAL_CAPACITY * count);
        }
    }

    @Override
    public ComponentContainerProvider getContainerProvider() {
        return new ComponentContainerProvider("container.thermoelectricmanipulator", this).createMenu((id, inv) -> new ContainerThermoelectricManipulator(id, inv, getComponent(IComponentType.Inventory), getCoordsArray()));
    }

    @Override
    public ComponentInventory getInventory() {
        return new ComponentInventory(this, InventoryBuilder.newInv().bucketInputs(1).gasInputs(1).bucketOutputs(1).gasOutputs(1).upgrades(3)).valid(machineValidator()).validUpgrades(ContainerThermoelectricManipulator.VALID_UPGRADES);
    }

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

            FluidStack fluidStack = new FluidStack(fluid, (int) tankGas.getAmount());

            outputTank.fill(fluidStack, FluidAction.EXECUTE);

        };
    }

    private static double getAdjustedHeatingFactor(double deltaT) {

        if (deltaT == 0) {
            return 1;
        }

        return Math.abs(HEAT_TRANSFER / deltaT);

    }

    private static record ManipulatorStatusCheckWrapper(boolean canProcess, ManipulatorHeatingStatus status, boolean changeState) {

    }

}
