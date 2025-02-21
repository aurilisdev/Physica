package electrodynamics.common.recipe.categories.item2item.specificmachines;

import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.item2item.Item2ItemRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class OxidationFurnaceRecipe extends Item2ItemRecipe {

	public static final String RECIPE_GROUP = "oxidation_furnace_recipe";
	public static final ResourceLocation RECIPE_ID = Electrodynamics.rl(RECIPE_GROUP);

	public OxidationFurnaceRecipe(String group, List<CountableIngredient> inputs, ItemStack output, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
		super(group, inputs, output, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ElectrodynamicsRecipeInit.OXIDATION_FURNACE_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ElectrodynamicsRecipeInit.OXIDATION_FURNACE_TYPE.get();
	}

}
