package electrodynamics.datagen.server.recipe.types.custom.item2item;

import electrodynamics.api.References;
import electrodynamics.common.item.subtype.SubtypeNugget;
import electrodynamics.common.item.subtype.SubtypeRod;
import electrodynamics.common.recipe.categories.item2item.specificmachines.LatheRecipe;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.Item2ItemBuilder;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;

public class ElectrodynamicsLatheRecipes extends AbstractRecipeGenerator {

	public static int LATHE_REQUIRED_TICKS = 200;
	public static double LATHE_USAGE_PER_TICK = 350.0;

	public final String modID;

	public ElectrodynamicsLatheRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsLatheRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.hslasteel), 1), 0.1F, 200, 350.0, "hsla_steel_rod", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_HSLASTEEL, 2)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.hslasteel), 2), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.stainlesssteel), 1), 0.1F, 200, 350.0, "stainless_steel_rod", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STAINLESSSTEEL, 2)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.stainlesssteel), 2), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.steel), 1), 0.1F, 200, 350.0, "steel_rod", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STEEL, 2)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.steel), 2), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.titaniumcarbide), 1), 0.1F, 200, 350.0, "titanium_carbide_rod", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_TITANIUMCARBIDE, 2)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.titaniumcarbide), 2), 1))
				//
				.save(output);

	}

	public Item2ItemBuilder<LatheRecipe> newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new Item2ItemBuilder<>(LatheRecipe::new, stack, RecipeCategory.ITEM_2_ITEM, modID, "lathe/" + name, group, xp, ticks, usagePerTick);
	}

}
