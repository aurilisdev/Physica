package electrodynamics.datagen.server.recipe.types.custom.item2item;

import electrodynamics.api.References;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.recipe.categories.item2item.specificmachines.MineralGrinderRecipe;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.Item2ItemBuilder;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public class ElectrodynamicsMineralGrinderRecipes extends AbstractRecipeGenerator {

	public static double MINERALGRINDER_USAGE_PER_TICK = 350.0;
	public static int MINERALGRINDER_REQUIRED_TICKS = 200;

	public final String modID;

	public ElectrodynamicsMineralGrinderRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsMineralGrinderRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			if (ingot.grindedDust != null) {
				newRecipe(new ItemStack(ingot.grindedDust.get()), 0, 200, 350.0, "dust_" + ingot.name() + "_from_ingot", modID)
						//
						.addItemTagInput(ingot.tag, 1)
						//
						.save(output);
			}

		}

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.iron)), 0, 200, 350.0, "dust_iron_from_ingot", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS_IRON, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.gold)), 0, 200, 350.0, "dust_gold_from_ingot", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS_GOLD, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.copper)), 0, 200, 350.0, "dust_copper_from_ingot", modID)
				//
				.addItemTagInput(Tags.Items.INGOTS_COPPER, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.netherite)), 0, 200, 350.0, "dust_netherite_from_scrap", modID)
				//
				.addItemStackInput(new ItemStack(Items.NETHERITE_SCRAP))
				//
				.save(output);

		for (SubtypeImpureDust dust : SubtypeImpureDust.values()) {
			newRecipe(new ItemStack(dust.grindedDust.get()), 0.1F, 200, 350.0, "dust_" + dust.name() + "_from_imp_dust", modID)
					//
					.addItemTagInput(dust.tag, 1)
					//
					.save(output);
		}

		for (SubtypeRawOre raw : SubtypeRawOre.values()) {
			if (raw.grindedItem != null) {
				newRecipe(new ItemStack(raw.grindedItem.get(), 2), 0.1F, 200, 350.0, "dust_" + raw.name() + "_from_raw_ore", modID)
						//
						.addItemTagInput(raw.tag, 1)
						//
						.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
						//
						.save(output);
			}
		}

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.copper), 2), 0.1F, 200, 350.0, "dust_copper_from_raw_ore", modID)
				//
				.addItemTagInput(Tags.Items.RAW_MATERIALS_COPPER, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.iron), 2), 0.1F, 200, 350.0, "dust_iron_from_raw_ore", modID)
				//
				.addItemTagInput(Tags.Items.RAW_MATERIALS_IRON, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.gold), 2), 0.1F, 200, 350.0, "dust_gold_from_raw_ore", modID)
				//
				.addItemTagInput(Tags.Items.RAW_MATERIALS_GOLD, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.copper), 2), 0.1F, 200, 350.0, "dust_copper_from_ore", modID)
				//
				.addItemTagInput(ItemTags.COPPER_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.gold), 2), 0.5F, 200, 350.0, "dust_gold_from_ore", modID)
				//
				.addItemTagInput(ItemTags.GOLD_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.iron), 2), 0.3F, 200, 350.0, "dust_iron_from_ore", modID)
				//
				.addItemTagInput(ItemTags.IRON_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.lead), 2), 0.3F, 200, 350.0, "dust_lead_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_LEAD, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.molybdenum), 2), 0.3F, 200, 350.0, "dust_molybdenum_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_MOLYBDENUM, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.netherite), 2), 1F, 200, 350.0, "dust_netherite_from_ore", modID)
				//
				.addItemTagInput(Tags.Items.ORES_NETHERITE_SCRAP, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.silver), 2), 0.5F, 200, 350.0, "dust_silver_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SILVER, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.tin), 2), 0.1F, 200, 350.0, "dust_tin_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_TIN, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.vanadium), 2), 0.1F, 200, 350.0, "dust_vanadium_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_VANADIUM, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(Items.COAL, 3), 0.3F, 200, 350.0, "gem_coal_from_ore", modID)
				//
				.addItemTagInput(ItemTags.COAL_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.05))
				//
				.save(output);

		newRecipe(new ItemStack(Items.DIAMOND, 3), 1.0F, 200, 350.0, "gem_diamond_from_ore", modID)
				//
				.addItemTagInput(ItemTags.DIAMOND_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COAL), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(Items.EMERALD, 3), 1F, 200, 350.0, "gem_emerald_from_ore", modID)
				//
				.addItemTagInput(ItemTags.EMERALD_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(Items.LAPIS_LAZULI, 9), 0.4F, 200, 350.0, "gem_lapis_from_ore", modID)
				//
				.addItemTagInput(ItemTags.LAPIS_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(Items.QUARTZ, 7), 0.7F, 200, 350.0, "gem_quartz_from_ore", modID)
				//
				.addItemTagInput(Tags.Items.ORES_QUARTZ, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COAL), 0.1))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.endereye), 2), 1F, 200, 350.0, "dust_ender_eye", modID)
				//
				.addItemTagInput(Tags.Items.ENDER_PEARLS, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.niter), 3), 0.1F, 200, 350.0, "dust_niter_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SALTPETER, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.potassiumchloride), 3), 0.3F, 200, 350.0, "pot_chloride_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_POTASSIUMCHLORIDE, 1)
				//
				.save(output);

		newRecipe(new ItemStack(Items.REDSTONE, 6), 0.4F, 200, 350.0, "dust_redstone_from_ore", modID)
				//
				.addItemTagInput(ItemTags.REDSTONE_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.05))
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.sulfur), 3), 0.2F, 200, 350.0, "dust_sulfur_from_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SULFUR, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COAL), 0.05))
				//
				.save(output);

		newRecipe(new ItemStack(Items.COBBLESTONE, 1), 0.01F, 200, 350.0, "cobblestone_from_stone", modID)
				//
				.addItemTagInput(Tags.Items.STONES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.GRAVEL), 0.4))
				//
				.save(output);

		newRecipe(new ItemStack(Items.GRAVEL, 1), 0.01F, 200, 350.0, "gravel_from_cobblestone", modID)
				//
				.addItemTagInput(Tags.Items.COBBLESTONES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.SAND), 0.4))
				//
				.save(output);

		newRecipe(new ItemStack(Items.SAND, 1), 0.01F, 200, 350.0, "sand_from_gravel", modID)
				//
				.addItemTagInput(Tags.Items.GRAVELS, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.halite), 3), 0.1F, 200, 350.0, "halite_cystal_from_halite_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SALT, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.uranium)), 0.1F, 200, 350.0, "raw_uranium_from_uranium_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_URANIUM, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.thorium)), 0.1F, 200, 350.0, "raw_thorium_from_thorium_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_THORIUM, 1)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.fluorite)), 0.1F, 200, 350.0, "fluorite_crystal_from_fluorite_ore", modID)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_FLUORITE, 1)
				//
				.save(output);

	}

	public Item2ItemBuilder<MineralGrinderRecipe> newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new Item2ItemBuilder<>(MineralGrinderRecipe::new, stack, RecipeCategory.ITEM_2_ITEM, modID, "mineral_grinder/" + name, group, xp, ticks, usagePerTick);
	}

}
