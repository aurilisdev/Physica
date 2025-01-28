package electrodynamics.common.recipe.recipeutils;

import java.util.Collections;
import java.util.List;

import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class AbstractMaterialRecipe extends ElectrodynamicsRecipe {

	public AbstractMaterialRecipe(String recipeGroup, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
		super(recipeGroup, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
	}

	@Override
	public ItemStack assemble(ElectrodynamicsRecipe p_345149_, HolderLookup.Provider p_346030_) {
		return getItemRecipeOutput();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
		return getItemRecipeOutput();
	}

	public FluidStack getFluidRecipeOutput() {
		return FluidStack.EMPTY;
	}

	public GasStack getGasRecipeOutput() {
		return GasStack.EMPTY;
	}

	public List<FluidIngredient> getFluidIngredients() {
		return Collections.emptyList();
	}

	public List<GasIngredient> getGasIngredients() {
		return Collections.emptyList();
	}

	public ItemStack getItemRecipeOutput() {
		return ItemStack.EMPTY;
	}

}
