package electrodynamics.prefab.tile.components.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.api.gas.PropertyGasTank;
import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.recipeutils.AbstractMaterialRecipe;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.utils.IComponentGasHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class ComponentGasHandlerMulti implements IComponentGasHandler {

    private GenericTile holder;

    @Nullable
    public Direction[] inputDirections;
    @Nullable
    public Direction[] outputDirections;

    private boolean isSided = false;

    private PropertyGasTank[] inputTanks = new PropertyGasTank[0];
    private PropertyGasTank[] outputTanks = new PropertyGasTank[0];

    @Nullable
    private RecipeType<? extends AbstractMaterialRecipe> recipeType;

    @Nullable
    private TagKey<Gas>[] validInputGasTags;
    @Nullable
    private Gas[] validInputGases;
    private HashSet<Gas> inputValidatorGases = new HashSet<>();

    @Nullable
    private TagKey<Gas>[] validOutputGasTags;
    @Nullable
    private Gas[] validOutputGases;
    private HashSet<Gas> outputValidatorGases = new HashSet<>();

    private IGasHandler[] sidedOptionals = new IGasHandler[6]; // Down Up North South West East

    @Nullable
    private IGasHandler inputOptional = null;
    @Nullable
    private IGasHandler outputOptional = null;

    public ComponentGasHandlerMulti(GenericTile holder) {
        this.holder = holder;

        if (!holder.getBlockState().hasProperty(GenericEntityBlock.FACING)) {
            throw new UnsupportedOperationException("The tile " + holder + " must have the FACING direction property!");
        }
    }

    public ComponentGasHandlerMulti setInputTanks(int count, double[] capacity, double[] maxTemperature, int[] maxPressure) {
        inputTanks = new PropertyGasTank[count];
        if (capacity.length < count) {
            throw new UnsupportedOperationException("The number of capacities does not match the number of input tanks");
        }
        if (maxPressure.length < count) {
            throw new UnsupportedOperationException("The number of max temperatures does not match the number of input tanks");
        }
        if (maxTemperature.length < count) {
            throw new UnsupportedOperationException("The number of max pressures does not match the number of input tanks");
        }
        for (int i = 0; i < count; i++) {
            inputTanks[i] = new PropertyGasTank(holder, "input" + i, capacity[i], maxTemperature[i], maxPressure[i]);
        }
        return this;
    }

    public ComponentGasHandlerMulti setOutputTanks(int count, double[] capacity, double[] maxTemperature, int[] maxPressure) {
        outputTanks = new PropertyGasTank[count];
        if (capacity.length < count) {
            throw new UnsupportedOperationException("The number of capacities does not match the number of output tanks");
        }
        if (maxPressure.length < count) {
            throw new UnsupportedOperationException("The number of max temperatures does not match the number of output tanks");
        }
        if (maxTemperature.length < count) {
            throw new UnsupportedOperationException("The number of max pressures does not match the number of output tanks");
        }
        for (int i = 0; i < count; i++) {
            outputTanks[i] = new PropertyGasTank(holder, "output" + i, capacity[i], maxTemperature[i], maxPressure[i]);
        }
        return this;
    }

    public ComponentGasHandlerMulti setTanks(int inputCount, double[] inputCapacity, double[] inputMaxTemperature, int[] inputMaxPressure, int outputCount, double[] outputCapacity, double[] outputMaxTemperature, int[] outputMaxPressure) {
        return setInputTanks(inputCount, inputCapacity, inputMaxTemperature, inputMaxPressure).setOutputTanks(outputCount, outputCapacity, outputMaxTemperature, outputMaxPressure);
    }

    public ComponentGasHandlerMulti setInputGases(Gas... gases) {
        validInputGases = gases;
        return this;
    }

    public ComponentGasHandlerMulti setInputGasTags(TagKey<Gas>... gases) {
        validInputGasTags = gases;
        return this;
    }

    public ComponentGasHandlerMulti setOutputGases(Gas... gases) {
        validOutputGases = gases;
        return this;
    }

    public ComponentGasHandlerMulti setOutputGasTags(TagKey<Gas>... gases) {
        validOutputGasTags = gases;
        return this;
    }

    public ComponentGasHandlerMulti setInputDirections(BlockEntityUtils.MachineDirection... directions) {
        isSided = true;
        inputDirections = BlockEntityUtils.MachineDirection.toDirectionArray(directions);
        return this;
    }

    public ComponentGasHandlerMulti setOutputDirections(BlockEntityUtils.MachineDirection... directions) {
        isSided = true;
        outputDirections = BlockEntityUtils.MachineDirection.toDirectionArray(directions);
        return this;
    }

    public ComponentGasHandlerMulti setRecipeType(RecipeType<? extends AbstractMaterialRecipe> recipeType) {
        this.recipeType = recipeType;
        return this;
    }

    // It is assumed you have defined tanks when calling this method
    public ComponentGasHandlerMulti setCondensedHandler(BiConsumer<GasTank, GenericTile> consumer) {
        for (PropertyGasTank tank : inputTanks) {
            tank.setOnGasCondensed(consumer);
        }
        for (PropertyGasTank tank : outputTanks) {
            tank.setOnGasCondensed(consumer);
        }
        return this;
    }

    public int tankCount(boolean input) {
        if (input) {
            return inputTanks == null ? 0 : inputTanks.length;
        }
        return outputTanks == null ? 0 : outputTanks.length;
    }

    public GasStack getGasInTank(int tank, boolean input) {
        if (input) {
            return inputTanks[tank].getGas();
        }
        return outputTanks[tank].getGas();
    }

    @Nullable
    public PropertyGasTank getTankFromGas(Gas gas, boolean isInput) {
        if (isInput) {
            for (PropertyGasTank tank : inputTanks) {
                if (tank.getGas().getGas().equals(gas)) {
                    return tank;
                }
            }
            for (PropertyGasTank tank : inputTanks) {
                if (tank.isEmpty()) {
                    return tank;
                }
            }
        }
        for (PropertyGasTank tank : outputTanks) {
            if ((tank.getGas().getGas().equals(gas))) {
                return tank;
            }
        }
        for (PropertyGasTank tank : outputTanks) {
            if (tank.isEmpty()) {
                return tank;
            }
        }

        return null;
    }

    public double getTankCapacity(int tank, boolean input) {
        if (input) {
            return inputTanks[tank].getCapacity();
        }
        return outputTanks[tank].getCapacity();
    }

    public boolean isGasValid(int tank, @NotNull GasStack stack, boolean input) {
        if (input) {
            return inputTanks[tank].isGasValid(stack);
        }
        return outputTanks[tank].isGasValid(stack);
    }

    public double fill(int tank, GasStack resource, GasAction action, boolean input) {
        if (input) {
            return inputTanks[tank].fill(resource, action);
        }
        return outputTanks[tank].fill(resource, action);
    }

    public @NotNull GasStack drain(int tank, GasStack resource, GasAction action, boolean input) {
        if (input) {
            return inputTanks[tank].drain(resource, action);
        }
        return outputTanks[tank].drain(resource, action);
    }

    public @NotNull GasStack drain(int tank, double maxDrain, GasAction action, boolean input) {
        if (input) {
            return inputTanks[tank].drain(maxDrain, action);
        }
        return outputTanks[tank].drain(maxDrain, action);
    }

    @Override
    public @org.jetbrains.annotations.Nullable IGasHandler getCapability(@org.jetbrains.annotations.Nullable Direction direction, CapabilityInputType mode) {
        if (direction == null || !isSided) {
            return null;
        }
        return sidedOptionals[direction.ordinal()];
    }

    @Override
    public void refreshIfUpdate(BlockState oldState, BlockState newState) {
        if (isSided && oldState.hasProperty(GenericEntityBlock.FACING) && newState.hasProperty(GenericEntityBlock.FACING) && oldState.getValue(GenericEntityBlock.FACING) != newState.getValue(GenericEntityBlock.FACING)) {
            defineOptionals(newState.getValue(GenericEntityBlock.FACING));
        }
    }

    @Override
    public void refresh() {

        defineOptionals(holder.getFacing());

    }

    private void defineOptionals(Direction facing) {

        holder.getLevel().invalidateCapabilities(holder.getBlockPos());

        sidedOptionals = new IGasHandler[6];

        inputOptional = null;

        outputOptional = null;

        // Input

        if (inputDirections != null) {
            inputOptional = new InputTankDispatcher(inputTanks);

            for (Direction dir : inputDirections) {
                sidedOptionals[BlockEntityUtils.getRelativeSide(facing, dir).ordinal()] = inputOptional;
            }
        }

        if (outputDirections != null) {
            outputOptional = new OutputTankDispatcher(outputTanks);

            for (Direction dir : outputDirections) {
                sidedOptionals[BlockEntityUtils.getRelativeSide(facing, dir).ordinal()] = outputOptional;
            }
        }

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
    public void onLoad() {
        IComponentGasHandler.super.onLoad();
        if (recipeType != null) {
            List<RecipeHolder<ElectrodynamicsRecipe>> recipes = ElectrodynamicsRecipe.findRecipesbyType(recipeType, holder.getLevel());

            List<Gas> inputGasHolder = new ArrayList<>();
            List<Gas> outputGasHolder = new ArrayList<>();

            double maxGasInputAmount = 0;
            double maxGasInputTemperature = 0;
            int maxGasInputPressure = 0;

            double maxGasOutputAmount = 0;
            double maxGasOutputTemperature = 0;
            int maxGasOutputPressure = 0;

            double maxGasBiproductAmount = 0;
            double maxGasBiproductTemperature = 0;
            int maxGasBiproudctPressure = 0;

            for (RecipeHolder<ElectrodynamicsRecipe> iRecipe : recipes) {
                AbstractMaterialRecipe recipe = (AbstractMaterialRecipe) iRecipe.value();
                if (inputTanks != null) {
                    for (GasIngredient ing : recipe.getGasIngredients()) {
                        ing.getMatchingGases().forEach(h -> inputGasHolder.add(h.getGas()));
                        GasStack gas = ing.getGasStack();
                        if (gas.getAmount() > maxGasInputAmount) {
                            maxGasInputAmount = gas.getAmount();
                        }
                        if (gas.getTemperature() > maxGasInputTemperature) {
                            maxGasInputTemperature = gas.getTemperature();
                        }
                        if (gas.getPressure() > maxGasInputPressure) {
                            maxGasInputPressure = gas.getPressure();
                        }
                    }
                }

                if (outputTanks != null) {
                    GasStack output = recipe.getGasRecipeOutput();
                    outputGasHolder.add(output.getGas());
                    if (output.getAmount() > maxGasOutputAmount) {
                        maxGasOutputAmount = output.getAmount();
                    }
                    if (output.getTemperature() > maxGasOutputTemperature) {
                        maxGasOutputTemperature = output.getTemperature();
                    }
                    if (output.getPressure() > maxGasOutputPressure) {
                        maxGasOutputPressure = output.getPressure();
                    }

                    if (recipe.hasGasBiproducts()) {

                        for (GasStack stack : recipe.getFullGasBiStacks()) {

                            outputGasHolder.add(stack.getGas());

                            if (stack.getAmount() > maxGasBiproductAmount) {
                                maxGasBiproductAmount = stack.getAmount();
                            }
                            if (stack.getTemperature() > maxGasBiproductTemperature) {
                                maxGasBiproductTemperature = stack.getTemperature();
                            }
                            if (stack.getPressure() > maxGasBiproudctPressure) {
                                maxGasBiproudctPressure = stack.getPressure();
                            }

                        }

                    }

                }

            }
            inputValidatorGases.addAll(inputGasHolder);
            outputValidatorGases.addAll(outputGasHolder);

            if (maxGasInputAmount > 0) {

                maxGasInputAmount = (maxGasInputAmount / TANK_MULTIPLIER) * TANK_MULTIPLIER + TANK_MULTIPLIER;
                // if you are dumb enough to pass in 2^32 for the pressure the crash in on you pal
                int logged = MathUtils.logBase2(maxGasInputPressure);
                logged++;
                maxGasInputPressure = (int) Math.pow(2, logged);

                for (PropertyGasTank tank : inputTanks) {

                    if (tank.getCapacity() < maxGasInputAmount) {
                        tank.setCapacity(maxGasInputAmount);
                    }

                    if (tank.getMaxTemperature() < maxGasInputTemperature) {

                        tank.setMaxTemperature(maxGasInputTemperature + 10.0);

                    }

                    if (tank.getMaxPressure() < maxGasInputPressure) {

                        tank.setMaxPressure(maxGasInputPressure);

                    }

                }

            }

            int offset = 0;

            if (maxGasOutputAmount > 0) {

                maxGasOutputAmount = (maxGasOutputAmount / TANK_MULTIPLIER) * TANK_MULTIPLIER + TANK_MULTIPLIER;

                PropertyGasTank tank = outputTanks[0];

                int logged = MathUtils.logBase2(maxGasOutputPressure);
                logged++;
                maxGasOutputPressure = (int) Math.pow(2, logged);

                if (tank.getCapacity() < maxGasOutputAmount) {
                    tank.setCapacity(maxGasOutputAmount);
                }

                if (tank.getMaxTemperature() < maxGasOutputTemperature) {

                    tank.setMaxTemperature(maxGasOutputTemperature + 10.0);

                }

                if (tank.getMaxPressure() < maxGasOutputPressure) {

                    tank.setMaxPressure(maxGasOutputPressure);

                }

                offset = 1;

            }

            if (maxGasBiproductAmount > 0) {

                maxGasBiproductAmount = (maxGasBiproductAmount / TANK_MULTIPLIER) * TANK_MULTIPLIER + TANK_MULTIPLIER;

                int logged = MathUtils.logBase2(maxGasBiproudctPressure);
                logged++;
                maxGasBiproudctPressure = (int) Math.pow(2, logged);

                for (int i = 0; i < outputTanks.length - offset; i++) {

                    PropertyGasTank tank = outputTanks[i + offset];

                    if (tank.getCapacity() < maxGasBiproductAmount) {
                        tank.setCapacity(maxGasBiproductAmount);
                    }

                    if (tank.getMaxTemperature() < maxGasBiproductTemperature) {
                        tank.setMaxTemperature(maxGasBiproductTemperature + 10.0);
                    }

                    if (tank.getMaxPressure() < maxGasBiproudctPressure) {
                        tank.setMaxPressure(maxGasBiproudctPressure);
                    }
                }

            }

        } else {
            if (validInputGases != null) {
                for (Gas gas : validInputGases) {
                    inputValidatorGases.add(gas);
                }
            }
            if (validInputGasTags != null) {
                for (TagKey<Gas> tag : validInputGasTags) {
                    ElectrodynamicsGases.GAS_REGISTRY.getTag(tag).get().stream().forEach(holder -> {
                        inputValidatorGases.add(holder.value());
                    });
                }
            }
            if (validOutputGases != null) {
                for (Gas fluid : validOutputGases) {
                    outputValidatorGases.add(fluid);
                }
            }
            if (validOutputGasTags != null) {
                for (TagKey<Gas> tag : validOutputGasTags) {
                    ElectrodynamicsGases.GAS_REGISTRY.getTag(tag).get().stream().forEach(holder -> {
                        outputValidatorGases.add(holder.value());
                    });
                }
            }
        }
        if (!inputValidatorGases.isEmpty()) {
            for (PropertyGasTank tank : inputTanks) {
                tank.setValidator(gasStack -> inputValidatorGases.contains(gasStack.getGas()));
            }
        }
        if (!outputValidatorGases.isEmpty()) {
            for (PropertyGasTank tank : outputTanks) {
                tank.setValidator(gasStack -> outputValidatorGases.contains(gasStack.getGas()));
            }
        }
    }

    @Override
    public IComponentType getType() {
        return IComponentType.GasHandler;
    }

    @Override
    public PropertyGasTank[] getInputTanks() {
        return inputTanks;
    }

    @Override
    public PropertyGasTank[] getOutputTanks() {
        return outputTanks;
    }

    private class InputTankDispatcher implements IGasHandler {

        private PropertyGasTank[] tanks;

        public InputTankDispatcher(PropertyGasTank[] tanks) {
            this.tanks = tanks;
        }

        @Override
        public int getTanks() {
            return tanks.length;
        }

        @Override
        public GasStack getGasInTank(int tank) {
            return tanks[tank].getGas();
        }

        @Override
        public double getTankCapacity(int tank) {
            return tanks[tank].getCapacity();
        }

        @Override
        public double getTankMaxTemperature(int tank) {
            return tanks[tank].getMaxTemperature();
        }

        @Override
        public int getTankMaxPressure(int tank) {
            return tanks[tank].getMaxPressure();
        }

        @Override
        public boolean isGasValid(int tank, GasStack gas) {
            return tanks[tank].isGasValid(gas);
        }

        @Override
        public double fillTank(int tank, GasStack gas, GasAction action) {
            return tanks[tank].fill(gas, action);
        }

        @Override
        public GasStack drainTank(int tank, GasStack gas, GasAction action) {
            return GasStack.EMPTY;
        }

        @Override
        public GasStack drainTank(int tank, double maxFill, GasAction action) {
            return GasStack.EMPTY;
        }

        @Override
        public double heat(int tank, double deltaTemperature, GasAction action) {
            return tanks[tank].heat(deltaTemperature, action);
        }

        @Override
        public double bringPressureTo(int tank, int atm, GasAction action) {
            return tanks[tank].bringPressureTo(atm, action);
        }

    }

    private class OutputTankDispatcher implements IGasHandler {

        private PropertyGasTank[] tanks;

        public OutputTankDispatcher(PropertyGasTank[] tanks) {
            this.tanks = tanks;
        }

        @Override
        public int getTanks() {
            return tanks.length;
        }

        @Override
        public GasStack getGasInTank(int tank) {
            return tanks[tank].getGas();
        }

        @Override
        public double getTankCapacity(int tank) {
            return tanks[tank].getCapacity();
        }

        @Override
        public double getTankMaxTemperature(int tank) {
            return tanks[tank].getMaxTemperature();
        }

        @Override
        public int getTankMaxPressure(int tank) {
            return tanks[tank].getMaxPressure();
        }

        @Override
        public boolean isGasValid(int tank, GasStack gas) {
            return false;
        }

        @Override
        public double fillTank(int tank, GasStack gas, GasAction action) {
            return 0;
        }

        @Override
        public GasStack drainTank(int tank, GasStack gas, GasAction action) {
            return tanks[tank].drain(tank, action);
        }

        @Override
        public GasStack drainTank(int tank, double maxFill, GasAction action) {
            return tanks[tank].drain(maxFill, action);
        }

        @Override
        public double heat(int tank, double deltaTemperature, GasAction action) {
            return tanks[tank].heat(deltaTemperature, action);
        }

        @Override
        public double bringPressureTo(int tank, int atm, GasAction action) {
            return tanks[tank].bringPressureTo(atm, action);
        }

    }

}
