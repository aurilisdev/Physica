package electrodynamics.common.recipe.categories.item2fluid;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.common.recipe.recipeutils.AbstractMaterialRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class Item2FluidRecipe extends AbstractMaterialRecipe {

    private List<CountableIngredient> inputItems;
    private FluidStack outputFluid;

    public Item2FluidRecipe(String group, List<CountableIngredient> itemInputs, FluidStack fluidOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(group, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        inputItems = itemInputs;
        outputFluid = fluidOutput;
    }

    @Override
    public boolean matchesRecipe(ComponentProcessor pr) {
        Pair<List<Integer>, Boolean> pair = areItemsValid(getCountedIngredients(), ((ComponentInventory) pr.getHolder().getComponent(IComponentType.Inventory)).getInputsForProcessor(pr.getProcessorNumber()));
        if (pair.getSecond()) {
            setItemArrangement(pr.getProcessorNumber(), pair.getFirst());
            return true;
        }
        return false;
    }

    @Override
    public FluidStack getFluidRecipeOutput() {
        return outputFluid;
    }

    public List<CountableIngredient> getCountedIngredients() {
        List<CountableIngredient> list = new ArrayList<>();
        for (CountableIngredient ing : inputItems) {
            list.add(ing);
        }
        return list;
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return new ArrayList<>();
    }

    public interface Factory<T extends Item2FluidRecipe> {

        T create(String group, List<CountableIngredient> itemInputs, FluidStack fluidOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts);

    }

}
