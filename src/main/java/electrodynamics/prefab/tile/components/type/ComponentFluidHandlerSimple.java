package electrodynamics.prefab.tile.components.type;

import java.util.HashSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import electrodynamics.api.fluid.PropertyFluidTank;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Extension of PropertyFluidTank implementing directional I/O and the Component system
 * 
 * This is a separate class because ComponentFluidHandlerMulti is does not have segregated input and output tanks.
 * Instead it has a single tank that is used for both functions.
 * 
 * This class also has no concept of a RecipeType tied to it since recipes have segregated inputs and outputs.
 * 
 * @author skip999
 *
 */
public class ComponentFluidHandlerSimple extends PropertyFluidTank implements IComponentFluidHandler {

    @Nullable
    public Direction[] inputDirections;
    @Nullable
    public Direction[] outputDirections;
    @Nullable
    private TagKey<Fluid>[] validFluidTags;
    @Nullable
    private Fluid[] validFluids;

    private HashSet<Fluid> validatorFluids = new HashSet<>();

    private IFluidHandler[] sidedOptionals = new IFluidHandler[6]; // Down Up North South West East

    @Nullable
    private IFluidHandler inputOptional = null;
    @Nullable
    private IFluidHandler outputOptional = null;

    private boolean isSided = false;

    public ComponentFluidHandlerSimple(int capacity, Predicate<FluidStack> validator, GenericTile holder, String key) {
        super(capacity, validator, holder, key);
    }

    public ComponentFluidHandlerSimple(int capacity, GenericTile holder, String key) {
        super(capacity, holder, key);
    }

    protected ComponentFluidHandlerSimple(ComponentFluidHandlerSimple other) {
        super(other);
    }

    public ComponentFluidHandlerSimple setInputDirections(BlockEntityUtils.MachineDirection... directions) {
        inputDirections = BlockEntityUtils.MachineDirection.toDirectionArray(directions);
        isSided = true;
        return this;
    }

    public ComponentFluidHandlerSimple setOutputDirections(BlockEntityUtils.MachineDirection... directions) {
        outputDirections = BlockEntityUtils.MachineDirection.toDirectionArray(directions);
        isSided = true;
        return this;
    }

    @Override
    public ComponentFluidHandlerSimple setCapacity(int capacity) {
        return (ComponentFluidHandlerSimple) super.setCapacity(capacity);
    }

    @Override
    public ComponentFluidHandlerSimple setValidator(Predicate<FluidStack> validator) {
        return (ComponentFluidHandlerSimple) super.setValidator(validator);
    }

    public ComponentFluidHandlerSimple setValidFluids(Fluid... fluids) {
        validFluids = fluids;
        return this;
    }

    public ComponentFluidHandlerSimple setValidFluidTags(TagKey<Fluid>... fluids) {
        validFluidTags = fluids;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComponentFluidHandlerSimple tank) {
            return tank.getFluid().equals(getFluid()) && tank.getCapacity() == getCapacity();
        }
        return false;
    }

    @Override
    public IComponentType getType() {
        return IComponentType.FluidHandler;
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
    public void refreshIfUpdate(BlockState oldState, BlockState newState) {
        if (isSided && oldState.hasProperty(ElectrodynamicsBlockStates.FACING) && newState.hasProperty(ElectrodynamicsBlockStates.FACING) && oldState.getValue(ElectrodynamicsBlockStates.FACING) != newState.getValue(ElectrodynamicsBlockStates.FACING)) {
            defineOptionals(newState.getValue(ElectrodynamicsBlockStates.FACING));
        }
    }

    @Override
    public @org.jetbrains.annotations.Nullable IFluidHandler getCapability(@org.jetbrains.annotations.Nullable Direction side, CapabilityInputType type) {
        if (!isSided) {
            return this;
        }
        if (side == null) {
            return null;
        }

        return sidedOptionals[side.ordinal()];
    }

    @Override
    public void refresh() {

        defineOptionals(holder.getFacing());

    }

    private void defineOptionals(Direction facing) {

        holder.getLevel().invalidateCapabilities(holder.getBlockPos());

        sidedOptionals = new IFluidHandler[6];

        inputOptional = null;

        outputOptional = null;

        if (isSided) {

            // Input

            if (inputDirections != null) {
                inputOptional = new InputTank(this);

                for (Direction dir : inputDirections) {
                    sidedOptionals[BlockEntityUtils.getRelativeSide(facing, dir).ordinal()] = inputOptional;
                }
            }

            if (outputDirections != null) {
                outputOptional = new OutputTank(this);

                for (Direction dir : outputDirections) {
                    sidedOptionals[BlockEntityUtils.getRelativeSide(facing, dir).ordinal()] = outputOptional;
                }
            }

        }

    }

    @Override
    public void onLoad() {
        IComponentFluidHandler.super.onLoad();
        if (validFluids != null) {
            for (Fluid fluid : validFluids) {
                validatorFluids.add(fluid);
            }
        }
        if (validFluidTags != null) {
            for (TagKey<Fluid> tag : validFluidTags) {
                BuiltInRegistries.FLUID.getTag(tag).get().stream().forEach(holder -> {
                    validatorFluids.add(holder.value());
                });
            }
        }
        if (!validatorFluids.isEmpty()) {
            validator = fluidStack -> validatorFluids.contains(fluidStack.getFluid());
        }
    }

    @Override
    public PropertyFluidTank[] getInputTanks() {
        return toArray();
    }

    @Override
    public PropertyFluidTank[] getOutputTanks() {
        return toArray();
    }

    public PropertyFluidTank[] toArray() {
        return new PropertyFluidTank[] { this };
    }

    private class InputTank extends ComponentFluidHandlerSimple {

        public InputTank(ComponentFluidHandlerSimple property) {
            super(property);
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }

    }

    private class OutputTank extends ComponentFluidHandlerSimple {

        public OutputTank(ComponentFluidHandlerSimple property) {
            super(property);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

    }

}
