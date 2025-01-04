package electrodynamics.common.recipe.categories.fluid2gas;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.recipeutils.AbstractMaterialRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;

public abstract class Fluid2GasRecipe extends AbstractMaterialRecipe {

    private List<FluidIngredient> inputFluidIngredients;
    private GasStack outputGasStack;

    public Fluid2GasRecipe(String recipeGroup, List<FluidIngredient> inputFluidIngredients, GasStack outputGasStack, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(recipeGroup, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        this.inputFluidIngredients = inputFluidIngredients;
        this.outputGasStack = outputGasStack;
    }

    @Override
    public boolean matchesRecipe(ComponentProcessor pr) {
        Pair<List<Integer>, Boolean> pair = areFluidsValid(getFluidIngredients(), pr.getHolder().<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks());
        if (pair.getSecond()) {
            setFluidArrangement(pair.getFirst());
            return true;
        }
        return false;
    }

    @Override
    public GasStack getGasRecipeOutput() {
        return outputGasStack;
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return inputFluidIngredients;
    }

    public interface Factory<T extends Fluid2GasRecipe> {

        T create(String recipeGroup, List<FluidIngredient> inputFluidIngredients, GasStack outputGasStack, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts);

    }

}
