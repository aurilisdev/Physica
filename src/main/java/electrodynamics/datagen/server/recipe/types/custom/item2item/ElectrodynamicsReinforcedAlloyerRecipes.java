package electrodynamics.datagen.server.recipe.types.custom.item2item;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.recipe.categories.item2item.specificmachines.ReinforcedAlloyerRecipe;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.Item2ItemBuilder;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public class ElectrodynamicsReinforcedAlloyerRecipes extends AbstractRecipeGenerator {

	public static int REINFORCEDALLOYER_REQUIRED_TICKS = 50;
	public static double REINFORCEDALLOYER_USAGE_PER_TICK = 50.0;

	public final String modID;

	public ElectrodynamicsReinforcedAlloyerRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsReinforcedAlloyerRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum), 5), 0.5F, 50, 50.0, "aluminum_glass", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_ALUMINUM, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.DUST_SALTPETER, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.5))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.bronze), 7), 0.1F, 50, 50.0, "ingot_bronze", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_TIN, 1)
				//
				.addItemTagInput(Tags.Items.INGOTS_COPPER, 4)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.5))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_CUSTOMGLASS.getValue(SubtypeGlass.clear), 16), 0.1F, 50, 50.0, "clear_glass", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.COAL_COKE, 1)
				//
				.addItemTagInput(Tags.Items.SANDS, 16)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.hslasteel), 2), 0.3F, 50, 50.0, "ingot_hsla_steel", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STAINLESSSTEEL, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_MOLYBDENUM, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 1))
				//
				.save(output);

		newRecipe(new ItemStack(Items.NETHERITE_INGOT, 2), 1.0F, 50, 50.0, "ingot_netherite", modID)
				//
				.addItemStackInput(new ItemStack(Items.NETHERITE_SCRAP))
				//
				.addItemTagInput(Tags.Items.INGOTS_GOLD, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.75))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.0F, 50, 50.0, "slag", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS, 1)
				//
				.addItemTagInput(Tags.Items.SANDS, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.stainlesssteel), 34), 0.4F, 50, 50.0, "ingot_stainless_steel", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_CHROMIUM, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STEEL, 34)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.75))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.steel), 4), 0.3F, 50, 50.0, "ingot_steel", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS_IRON, 2)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.COAL_COKE, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.5))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.superconductive), 4), 0.3F, 50, 50.0, "ingot_superconductive_endereye", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_SILVER, 5)
				//
				.addItemStackInput(new ItemStack(Items.ENDER_EYE, 4))
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.9))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.superconductive), 4), 0.3F, 50, 50.0, "ingot_superconductive_netherite", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_SILVER, 5)
				//
				.addItemTagInput(Tags.Items.INGOTS_NETHERITE, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.9))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.titaniumcarbide), 4), 0.3F, 50, 50.0, "ingot_titanium_carbide", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_TITANIUM, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.COAL_COKE, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.vanadiumsteel), 8), 0.3F, 50, 50.0, "vanadium_steel_ingot", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STEEL, 8)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_VANADIUM, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.75))
				//
				.save(output);

	}

	public Item2ItemBuilder<ReinforcedAlloyerRecipe> newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new Item2ItemBuilder<>(ReinforcedAlloyerRecipe::new, stack, RecipeCategory.ITEM_2_ITEM, modID, "reinforced_alloyer/" + name, group, xp, ticks, usagePerTick);
	}

}
