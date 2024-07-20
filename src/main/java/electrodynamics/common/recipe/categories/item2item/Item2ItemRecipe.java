package electrodynamics.common.recipe.categories.item2item;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

public abstract class Item2ItemRecipe extends ElectrodynamicsRecipe {

    private List<CountableIngredient> inputItems;
    private ItemStack outputItem;

    public Item2ItemRecipe(String group, List<CountableIngredient> inputs, ItemStack output, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(group, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        inputItems = inputs;
        outputItem = output;
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
    public ItemStack assemble(ElectrodynamicsRecipe p_345149_, HolderLookup.Provider p_346030_) {
        return getItemRecipeOutput();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return getItemRecipeOutput();
    }

    public ItemStack getItemRecipeOutput() {
        return outputItem;
    }

    public List<CountableIngredient> getCountedIngredients() {
        return inputItems;
    }

    public interface Factory<T extends Item2ItemRecipe> {

        T create(String group, List<CountableIngredient> inputs, ItemStack output, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts);

    }

}
