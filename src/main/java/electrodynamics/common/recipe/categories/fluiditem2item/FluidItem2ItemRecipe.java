package electrodynamics.common.recipe.categories.fluiditem2item;

import java.util.List;

import com.mojang.datafixers.util.Pair;

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
import net.minecraft.world.item.ItemStack;

public abstract class FluidItem2ItemRecipe extends AbstractMaterialRecipe {

    private List<CountableIngredient> ingredients;
    private List<FluidIngredient> fluidIngredients;
    private ItemStack outputItemStack;

    public FluidItem2ItemRecipe(String group, List<CountableIngredient> itemInputs, List<FluidIngredient> fluidInputs, ItemStack itemOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(group, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        ingredients = itemInputs;
        fluidIngredients = fluidInputs;
        outputItemStack = itemOutput;
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
    public ItemStack getItemRecipeOutput() {
        return outputItemStack;
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    public List<CountableIngredient> getCountedIngredients() {
        return ingredients;
    }

    public interface Factory<T extends FluidItem2ItemRecipe> {

        T create(String group, List<CountableIngredient> itemInputs, List<FluidIngredient> fluidInputs, ItemStack itemOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts);

    }

}
