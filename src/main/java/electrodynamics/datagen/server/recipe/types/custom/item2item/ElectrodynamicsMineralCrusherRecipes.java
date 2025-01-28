package electrodynamics.datagen.server.recipe.types.custom.item2item;

import electrodynamics.api.References;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.item.subtype.SubtypePlate;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.recipe.categories.item2item.specificmachines.MineralCrusherRecipe;
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
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

public class ElectrodynamicsMineralCrusherRecipes extends AbstractRecipeGenerator {

    public static double MINERALCRUSHER_USAGE_PER_TICK = 450.0;
    public static int MINERALCRUSHER_REQUIRED_TICKS = 200;

    public final String modID;

    public ElectrodynamicsMineralCrusherRecipes(String modID) {
        this.modID = modID;
    }

    public ElectrodynamicsMineralCrusherRecipes() {
        this(References.ID);
    }

    @Override
    public void addRecipes(RecipeOutput output) {

        for (SubtypePlate plate : SubtypePlate.values()) {
            newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_PLATE.getValue(plate)), 0.1F, 200, 450.0, "plate_" + plate.name() + "_from_ingot", modID)
                    //
                    .addItemTagInput(plate.sourceIngot, 1)
                    //
                    .save(output);
        }

        for (SubtypeCrystal crystal : SubtypeCrystal.values()) {
            if (crystal.crushedItem != null && crystal != SubtypeCrystal.halite) {
                newRecipe(new ItemStack(crystal.crushedItem.get()), 0.0F, 200, 450.0, "imp_dust_" + crystal.name() + "_from_crystal", modID)
                        //
                        .addItemStackInput(new ItemStack(ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(crystal)))
                        //
                        .save(output);
            }

        }

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.salt), 1), 0.1F, 200, 450.0, "salt_from_halite_crystal", modID)
                //
                .addItemStackInput(new ItemStack(ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.halite)))
                //
                .save(output);

        for (SubtypeRawOre raw : SubtypeRawOre.values()) {
            if (raw.crushedItem != null) {
                if (raw == SubtypeRawOre.titanium || raw == SubtypeRawOre.chromium) {
                    newRecipe(new ItemStack(raw.crushedItem.get(), 3), 0.5F, 200, 450.0, "oxide_" + raw.name() + "_from_raw_ore", modID)
                            //
                            .addItemTagInput(raw.tag, 1)
                            //
                            .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.iron)), 0.3))
                            //
                            .save(output);
                } else {
                    newRecipe(new ItemStack(raw.crushedItem.get(), 3), 0.3F, 200, 450.0, "imp_dust_" + raw.name() + "_from_raw_ore", modID)
                            //
                            .addItemTagInput(raw.tag, 1)
                            //
                            .save(output);
                }

            }
        }

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.iron), 3), 0.3F, 200, 450.0, "imp_dust_iron_from_raw_ore", modID)
                //
                .addItemTagInput(Tags.Items.RAW_MATERIALS_IRON, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.gold), 3), 0.3F, 200, 450.0, "imp_dust_gold_from_raw_ore", modID)
                //
                .addItemTagInput(Tags.Items.RAW_MATERIALS_GOLD, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.copper), 3), 0.3F, 200, 450.0, "imp_dust_copper_from_raw_ore", modID)
                //
                .addItemTagInput(Tags.Items.RAW_MATERIALS_COPPER, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.chromite), 3), 0.3F, 200, 450.0, "oxide_chromite_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_CHROMIUM, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.iron)), 0.4))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.copper), 3), 0.3F, 200, 450.0, "imp_dust_copper_from_ore", modID)
                //
                .addItemTagInput(ItemTags.COPPER_ORES, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.gold)), 0.1))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.gold), 3), 0.3F, 200, 450.0, "imp_dust_gold_from_ore", modID)
                //
                .addItemTagInput(ItemTags.GOLD_ORES, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.silver)), 0.2))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.iron), 3), 0.3F, 200, 450.0, "imp_dust_iron_from_ore", modID)
                //
                .addItemTagInput(ItemTags.IRON_ORES, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.lead), 3), 0.3F, 200, 450.0, "imp_dust_lead_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_LEAD, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.silver)), 0.4))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.molybdenum), 3), 0.3F, 200, 450.0, "imp_dust_molybdenum_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_MOLYBDENUM, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.sulfur)), 0.3))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.netherite), 3), 0.3F, 200, 450.0, "imp_dust_netherite_from_ore", modID)
                //
                .addItemTagInput(Tags.Items.ORES_NETHERITE_SCRAP, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(Items.COAL), 0.3))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.silver), 3), 0.2F, 200, 450.0, "imp_dust_silver_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_SILVER, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.gold)), 0.1))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.tin), 3), 0.3F, 200, 450.0, "imp_dust_tin_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_TIN, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(Items.QUARTZ), 0.3))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.vanadium), 3), 0.3F, 200, 450.0, "imp_dust_vanadium_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_VANADIUM, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.lead)), 0.2))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.niter), 4), 0.1F, 200, 450.0, "niter_dust_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_SALTPETER, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.sulfur), 4), 0.1F, 200, 450.0, "sulfur_dust_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_SULFUR, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.dititanium), 3), 0.5F, 200, 450.0, "oxide_titanium_from_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_TITANIUM, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.iron)), 0.3))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITEPLATING.get(), 1), 1F, 200, 450.0, "composite_plate", modID)
                //
                .addItemStackInput(new ItemStack(ElectrodynamicsItems.ITEM_RAWCOMPOSITEPLATING.get()))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.obsidian), 2), 0.1F, 200, 450.0, "dust_obsidian_from_obsidian", modID)
                //
                .addItemTagInput(Tags.Items.OBSIDIANS, 1)
                //
                .save(output);

        newRecipe(new ItemStack(Items.FLINT, 1), 0.1F, 200, 450.0, "flint_from_gravel", modID)
                //
                .addItemTagInput(Tags.Items.GRAVELS, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(Items.SAND), 0.2))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.salt), 5), 0.1F, 200, 450.0, "salt_from_halite_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_SALT, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.fluorite), 2), 0.1F, 200, 450.0, "fluorite_crystal_from_fluorite_ore", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_FLUORITE, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEM_SHEETPLASTIC.get(), 2), 0.1F, 200, 450.0, "plastic_sheet_from_plastic_fibers", modID)
                //
                .addItemStackInput(new ItemStack(ElectrodynamicsItems.ITEM_PLASTIC_FIBERS.get()))
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.chromiumdisilicide)), 1.0))
                //
                .save(output);

        newRecipe(new ItemStack(Items.GLOWSTONE_DUST, 4), 0, 200, 450.0, "glowstone_dust_from_glowstone_block", modID)
                //
                .addItemStackInput(new ItemStack(Blocks.GLOWSTONE))
                //
                .save(output);

        newRecipe(new ItemStack(Items.AMETHYST_SHARD, 4), 0, 200, 300, "amethyst_shard_from_amethyst_block", modID)
                //
                .addItemStackInput(new ItemStack(Items.AMETHYST_BLOCK))
                //
                .save(output);

        newRecipe(new ItemStack(Items.QUARTZ, 4), 0, 200, 450, "nether_quartz_from_quartz_block", modID)
                //
                .addItemStackInput(new ItemStack(Blocks.QUARTZ_BLOCK))
                //
                .save(output);
    }

    public Item2ItemBuilder<MineralCrusherRecipe> newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
        return new Item2ItemBuilder<>(MineralCrusherRecipe::new, stack, RecipeCategory.ITEM_2_ITEM, modID, "mineral_crusher/" + name, group, xp, ticks, usagePerTick);
    }

}
