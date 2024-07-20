package electrodynamics.common.recipe.categories.fluid2item;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.common.recipe.recipeutils.AbstractMaterialRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.world.item.ItemStack;

public abstract class Fluid2ItemRecipe extends AbstractMaterialRecipe {

    private List<FluidIngredient> inputFluids;
    private ItemStack outputItem;

    public Fluid2ItemRecipe(String group, List<FluidIngredient> fluidInputs, ItemStack itemOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(group, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        inputFluids = fluidInputs;
        outputItem = itemOutput;
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
    public ItemStack getItemRecipeOutput() {
        return outputItem;
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        List<FluidIngredient> list = new ArrayList<>();
        for (FluidIngredient ing : inputFluids) {
            list.add(ing);
        }
        return list;
    }

    public interface Factory<T extends Fluid2ItemRecipe> {

        T create(String group, List<FluidIngredient> fluidInputs, ItemStack itemOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts);

    }

}
