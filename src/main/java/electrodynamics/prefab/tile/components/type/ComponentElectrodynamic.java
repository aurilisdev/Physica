package electrodynamics.prefab.tile.components.type;

import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ComponentElectrodynamic implements IComponent, ICapabilityElectrodynamic {

    protected GenericTile holder;

    protected BiFunction<TransferPack, Boolean, TransferPack> functionReceivePower = ICapabilityElectrodynamic.super::receivePower;
    protected BiFunction<TransferPack, Boolean, TransferPack> functionExtractPower = ICapabilityElectrodynamic.super::extractPower;
    protected BiFunction<LoadProfile, Direction, TransferPack> connectedLoadFunction = (profile, dir) -> TransferPack.joulesVoltage(getMaxJoulesStored() - getJoulesStored(), getVoltage());

    protected Supplier<Double> ampacityFunction = ICapabilityElectrodynamic.super::getAmpacity;

    protected Supplier<Double> minimumVoltageFunction = ICapabilityElectrodynamic.super::getMinimumVoltage;
    protected Supplier<Double> maximumVoltageFunction = ICapabilityElectrodynamic.super::getMaximumVoltage;

    protected Consumer<Double> setJoules = null;
    protected HashSet<Direction> relativeOutputDirections = new HashSet<>(); // Leave empty for universal input
    protected HashSet<Direction> relativeInputDirections = new HashSet<>(); // Leave empty for universal output
    protected Property<Double> voltage;
    protected Property<Double> maxJoules;
    protected Property<Double> joules;
    protected DoubleSupplier getJoules = () -> joules.get();
    protected BooleanSupplier hasCapability = () -> true;

    private boolean producesEnergy = false;
    private boolean acceptsEnergy = true;

    private boolean isSided = false;

    private ICapabilityElectrodynamic[] sidedOptionals = new ICapabilityElectrodynamic[6]; // Down Up North South West East

    @Nullable
    private ICapabilityElectrodynamic inputOptional = null;
    @Nullable
    private ICapabilityElectrodynamic outputOptional = null;

    public ComponentElectrodynamic(GenericTile source, boolean isProducer, boolean isReceiver) {

        producesEnergy = isProducer;
        acceptsEnergy = isReceiver;

        holder(source);
        voltage = source.property(new Property<>(PropertyTypes.DOUBLE, "voltage", ElectrodynamicsCapabilities.DEFAULT_VOLTAGE));
        maxJoules = source.property(new Property<>(PropertyTypes.DOUBLE, "maxJoules", 0.0));
        joules = source.property(new Property<>(PropertyTypes.DOUBLE, "joules", 0.0));
    }

    @Override
    public void holder(GenericTile holder) {
        this.holder = holder;
    }

    @Override
    public GenericTile getHolder() {
        return holder;
    }

    @Override
    public double getVoltage() {
        return voltage.get();
    }

    @Override
    public double getMinimumVoltage() {
        return minimumVoltageFunction.get();
    }

    @Override
    public double getMaximumVoltage() {
        return maximumVoltageFunction.get();
    }

    @Override
    public boolean isEnergyProducer() {
        return producesEnergy;
    }

    @Override
    public boolean isEnergyReceiver() {
        return acceptsEnergy;
    }

    @Override
    @Deprecated(forRemoval = false, since = "This is only if you need to force the internal joules count and is overriden in classes where you can do this.")
    public void setJoulesStored(double joules) {
        joules(joules);
    }

    public ICapabilityElectrodynamic getCapability(Direction side, CapabilityInputType type) {
        if (!isSided) {
            return this;
        }
        if (side == null) {
            return null;
        }

        return sidedOptionals[side.ordinal()];
    }

    @Override
    public void refreshIfUpdate(BlockState oldState, BlockState newState) {
        if (isSided && oldState.hasProperty(ElectrodynamicsBlockStates.FACING) && newState.hasProperty(ElectrodynamicsBlockStates.FACING) && oldState.getValue(ElectrodynamicsBlockStates.FACING) != newState.getValue(ElectrodynamicsBlockStates.FACING)) {
            defineOptionals(newState.getValue(ElectrodynamicsBlockStates.FACING));
        }
    }

    @Override
    public void refresh() {

        defineOptionals(holder.getFacing());

    }

    private void defineOptionals(Direction facing) {

        holder.getLevel().invalidateCapabilities(holder.getBlockPos());

        sidedOptionals = new ICapabilityElectrodynamic[6];
        
        inputOptional = null;
        
        outputOptional = null;

        if (isSided) {

            // Input

            if (!relativeInputDirections.isEmpty()) {
                inputOptional = new InputCapabilityDispatcher(this);

                for (Direction dir : relativeInputDirections) {
                    sidedOptionals[BlockEntityUtils.getRelativeSide(facing, dir).ordinal()] = inputOptional;
                }
            }

            if (!relativeOutputDirections.isEmpty()) {
                outputOptional = new OutputCapabilityDispatcher(this);

                for (Direction dir : relativeOutputDirections) {
                    sidedOptionals[BlockEntityUtils.getRelativeSide(facing, dir).ordinal()] = outputOptional;
                }
            }

        }
    }

    @Override
    public TransferPack extractPower(TransferPack transfer, boolean debug) {
        if (isEnergyProducer()) {
            return functionExtractPower.apply(transfer, debug);
        }
        return TransferPack.EMPTY;
    }

    @Override
    public TransferPack receivePower(TransferPack transfer, boolean debug) {
        if (isEnergyReceiver()) {
            return functionReceivePower.apply(transfer, debug);
        }
        return TransferPack.EMPTY;
    }

    @Override
    public TransferPack getConnectedLoad(LoadProfile loadProfile, Direction dir) {
        if (isEnergyReceiver()) {
            return connectedLoadFunction.apply(loadProfile, dir);
        }
        return TransferPack.EMPTY;
    }

    public ComponentElectrodynamic joules(double joules) {
        if (setJoules != null) {
            setJoules.accept(joules);
        } else {
            this.joules.set(Math.max(0, Math.min(maxJoules.get(), joules)));
        }
        if (joules != 0) {
            onChange();
        }
        return this;
    }

    public ComponentElectrodynamic maxJoules(double maxJoules) {
        this.maxJoules.set(Math.max(maxJoules, 0));
        if (joules.get() > maxJoules) {
            joules.set(maxJoules);
        }
        return this;
    }

    public ComponentElectrodynamic setInputDirections(BlockEntityUtils.MachineDirection... dirs) {
        isSided = true;
        for (BlockEntityUtils.MachineDirection dir : dirs) {
            relativeInputDirections.add(dir.mappedDir);
        }
        return this;
    }

    public ComponentElectrodynamic setOutputDirections(BlockEntityUtils.MachineDirection... dirs) {
        isSided = true;
        for (BlockEntityUtils.MachineDirection dir : dirs) {
            relativeOutputDirections.add(dir.mappedDir);
        }
        return this;
    }

    public ComponentElectrodynamic receivePower(BiFunction<TransferPack, Boolean, TransferPack> receivePower) {
        functionReceivePower = receivePower;
        return this;
    }

    public ComponentElectrodynamic extractPower(BiFunction<TransferPack, Boolean, TransferPack> extractPower) {
        functionExtractPower = extractPower;
        return this;
    }

    public ComponentElectrodynamic getConnectedLoad(BiFunction<LoadProfile, Direction, TransferPack> supplier) {
        this.connectedLoadFunction = supplier;
        return this;
    }

    public ComponentElectrodynamic getAmpacity(Supplier<Double> supplier) {
        ampacityFunction = supplier;
        return this;
    }

    public ComponentElectrodynamic getMinimumVoltage(Supplier<Double> supplier) {
        minimumVoltageFunction = supplier;
        return this;
    }

    public ComponentElectrodynamic getMaximumVoltage(Supplier<Double> supplier) {
        maximumVoltageFunction = supplier;
        return this;
    }

    public ComponentElectrodynamic setJoules(Consumer<Double> setJoules) {
        this.setJoules = setJoules;
        return this;
    }

    public ComponentElectrodynamic getJoules(DoubleSupplier getJoules) {
        this.getJoules = getJoules;
        return this;
    }

    public ComponentElectrodynamic voltage(double voltage) {
        this.voltage.set(voltage);
        return this;
    }

    public ComponentElectrodynamic drainElectricItem(int slot) {
        if (holder.hasComponent(IComponentType.Inventory)) {
            ComponentInventory inventory = holder.getComponent(IComponentType.Inventory);
            ItemStack stack = inventory.getItem(slot);
            if (stack.getItem() instanceof IItemElectric electric) {
                TransferPack pack = functionReceivePower.apply(electric.extractPower(stack, maxJoules.get() - joules.get(), false), false);
                if (pack != TransferPack.EMPTY) {
                    onChange();
                }
            }
        }
        return this;
    }

    public ComponentElectrodynamic fillElectricItem(int slot) {
        if (holder.hasComponent(IComponentType.Inventory)) {
            ComponentInventory inventory = holder.getComponent(IComponentType.Inventory);
            ItemStack stack = inventory.getItem(slot);
            if (stack.getItem() instanceof IItemElectric electric) {
                functionExtractPower.apply(electric.receivePower(stack, TransferPack.joulesVoltage(joules.get(), voltage.get()), false), false);
            }
        }
        return this;
    }

    @Override
    public double getJoulesStored() {
        return getJoules.getAsDouble();
    }

    @Override
    public double getMaxJoulesStored() {
        return maxJoules.get();
    }

    @Override
    public double getAmpacity() {
        return ampacityFunction.get();
    }

    @Override
    public void overVoltage(TransferPack transfer) {
        Level world = holder.getLevel();
        BlockPos pos = holder.getBlockPos();
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), (float) Math.log10(10 + transfer.getVoltage() / getVoltage()), ExplosionInteraction.BLOCK);
    }

    @Override
    public IComponentType getType() {
        return IComponentType.Electrodynamic;
    }

    public ComponentElectrodynamic setCapabilityTest(BooleanSupplier test) {
        hasCapability = test;
        return this;
    }

    @Override
    public void onChange() {
        if (holder != null) {
            holder.onEnergyChange(this);
        }
    }

    private static class InputCapabilityDispatcher implements ICapabilityElectrodynamic {

        private ComponentElectrodynamic parent;

        public InputCapabilityDispatcher(ComponentElectrodynamic parent) {
            this.parent = parent;
        }

        @Override
        public double getJoulesStored() {
            return parent.getJoulesStored();
        }

        @Override
        public double getMaxJoulesStored() {
            return parent.getMaxJoulesStored();
        }

        @Override
        public void setJoulesStored(double joules) {
            parent.setJoulesStored(joules);
        }

        @Override
        public double getVoltage() {
            return parent.getVoltage();
        }

        @Override
        public double getMinimumVoltage() {
            return parent.getMinimumVoltage();
        }

        @Override
        public double getMaximumVoltage() {
            return parent.getMaximumVoltage();
        }

        @Override
        public double getAmpacity() {
            return parent.getAmpacity();
        }

        @Override
        public boolean isEnergyReceiver() {
            return true;
        }

        @Override
        public boolean isEnergyProducer() {
            return false;
        }

        @Override
        public TransferPack extractPower(TransferPack transfer, boolean debug) {
            return TransferPack.EMPTY;
        }

        @Override
        public TransferPack receivePower(TransferPack transfer, boolean debug) {
            return parent.receivePower(transfer, debug);
        }

        @Override
        public void overVoltage(TransferPack transfer) {
            parent.overVoltage(transfer);
        }

        @Override
        public void onChange() {
            parent.onChange();
        }

        @Override
        public TransferPack getConnectedLoad(LoadProfile loadProfile, Direction dir) {
            return parent.getConnectedLoad(loadProfile, dir);
        }

    }

    private static class OutputCapabilityDispatcher implements ICapabilityElectrodynamic {

        private ComponentElectrodynamic parent;

        public OutputCapabilityDispatcher(ComponentElectrodynamic parent) {
            this.parent = parent;
        }

        @Override
        public double getJoulesStored() {
            return parent.getJoulesStored();
        }

        @Override
        public double getMaxJoulesStored() {
            return parent.getMaxJoulesStored();
        }

        @Override
        public void setJoulesStored(double joules) {
            parent.setJoulesStored(joules);
        }

        @Override
        public double getVoltage() {
            return parent.getVoltage();
        }

        @Override
        public double getMinimumVoltage() {
            return parent.getMinimumVoltage();
        }

        @Override
        public double getMaximumVoltage() {
            return parent.getMaximumVoltage();
        }

        @Override
        public double getAmpacity() {
            return parent.getAmpacity();
        }

        @Override
        public boolean isEnergyReceiver() {
            return false;
        }

        @Override
        public boolean isEnergyProducer() {
            return true;
        }

        @Override
        public TransferPack extractPower(TransferPack transfer, boolean debug) {
            return parent.extractPower(transfer, debug);
        }

        @Override
        public TransferPack receivePower(TransferPack transfer, boolean debug) {
            return TransferPack.EMPTY;
        }

        @Override
        public void overVoltage(TransferPack transfer) {
            parent.overVoltage(transfer);
        }

        @Override
        public void onChange() {
            parent.onChange();
        }

        @Override
        public TransferPack getConnectedLoad(LoadProfile loadProfile, Direction dir) {
            return TransferPack.EMPTY;
        }

    }

}
