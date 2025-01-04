package electrodynamics.common.recipe.categories.fluiditem2gas;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.recipeutils.AbstractMaterialRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;

public abstract class FluidItem2GasRecipe extends AbstractMaterialRecipe {

    private List<FluidIngredient> fluidIngredients;
    private List<CountableIngredient> ingredients;
    private GasStack outputStack;

    public FluidItem2GasRecipe(String group, List<CountableIngredient> inputItems, List<FluidIngredient> inputFluids, GasStack outputGas, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(group, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        ingredients = inputItems;
        fluidIngredients = inputFluids;
        outputStack = outputGas;
    }

    @Override
    public boolean matchesRecipe(ComponentProcessor pr) {
        Pair<List<Integer>, Boolean> itemPair = areItemsValid(getCountedIngredients(), ((ComponentInventory) pr.getHolder().getComponent(IComponentType.Inventory)).getInputsForProcessor(pr.getProcessorNumber()));
        if (itemPair.getSecond()) {
            Pair<List<Integer>, Boolean> fluidPair = areFluidsValid(getFluidIngredients(), pr.getHolder().<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks());
            if (fluidPair.getSecond()) {
                setItemArrangement(pr.getProcessorNumber(), itemPair.getFirst());
                setFluidArrangement(fluidPair.getFirst());
                return true;
            }
        }
        return false;
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public GasStack getGasRecipeOutput() {
        return outputStack;
    }

    public List<CountableIngredient> getCountedIngredients() {
        return ingredients;
    }

    public interface Factory<T extends FluidItem2GasRecipe> {

        T create(String group, List<CountableIngredient> inputItems, List<FluidIngredient> inputFluids, GasStack outputGas, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts);

    }

}
