package electrodynamics.datagen.server.recipe.types.custom.item2item;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.item.subtype.SubtypeNugget;
import electrodynamics.common.recipe.categories.item2item.specificmachines.WireMillRecipe;
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

public class ElectrodynamicsWireMillRecipes extends AbstractRecipeGenerator {

	public static double WIREMILL_USAGE_PER_TICK = 125.0;
	public static int WIREMILL_REQUIRED_TICKS = 200;

	public final String modID;

	public ElectrodynamicsWireMillRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsWireMillRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.copper)), 0.1F, 200, 125.0, "copper_wire_from_ingot", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS_COPPER, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.copper)), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.gold)), 0.2F, 200, 125.0, "gold_wire_from_ingot", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS_GOLD, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.GOLD_NUGGET), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.iron)), 0.1F, 200, 125.0, "iron_wire_from_ingot", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS_IRON, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.IRON_NUGGET), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.silver)), 0.1F, 200, 125.0, "silver_wire_from_ingot", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_SILVER, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.silver)), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.superconductive)), 0.1F, 200, 125.0, "superconductive_wire_from_ingot", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_SUPERCONDUCTIVE, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.superconductive)), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.tin)), 0.1F, 200, 125.0, "tin_wire_from_ingot", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_TIN, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_NUGGET.getValue(SubtypeNugget.tin)), 1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEM_TITANIUM_COIL.get()), 0.1F, 200, 125.0, "titanium_coil", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_TITANIUM, 9)
				//
				.save(output);

	}

	public Item2ItemBuilder<WireMillRecipe> newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new Item2ItemBuilder<>(WireMillRecipe::new, stack, RecipeCategory.ITEM_2_ITEM, modID, "wire_mill/" + name, group, xp, ticks, usagePerTick);
	}

}
