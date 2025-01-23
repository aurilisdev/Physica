package electrodynamics.common.recipe.categories.fluid2gas.specificmachines;

import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.fluid2gas.Fluid2GasRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ElectrolyticSeparatorRecipe extends Fluid2GasRecipe {

	public static final String RECIPE_GROUP = "electrolytic_separator_recipe";
	public static final ResourceLocation RECIPE_ID = Electrodynamics.rl(RECIPE_GROUP);

	public ElectrolyticSeparatorRecipe(String recipeGroup, List<FluidIngredient> inputFluidIngredients, GasStack outputGasStack, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
		super(recipeGroup, inputFluidIngredients, outputGasStack, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);

	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ElectrodynamicsRecipeInit.ELECTROLYTIC_SEPARATOR_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ElectrodynamicsRecipeInit.ELECTROLYTIC_SEPERATOR_TYPE.get();
	}

}
