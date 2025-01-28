package electrodynamics.datagen.server.recipe.types.custom.item2item;

import electrodynamics.api.References;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.recipe.categories.item2item.specificmachines.OxidationFurnaceRecipe;
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

public class ElectrodynamicsOxidationFurnaceRecipes extends AbstractRecipeGenerator {

    public static double OXIDATIONFURNACE_USAGE_PER_TICK = 350.0;
    public static int OXIDATIONFURNACE_REQUIRED_TICKS = 200;

    public final String modID;

    public ElectrodynamicsOxidationFurnaceRecipes(String modID) {
        this.modID = modID;
    }

    public ElectrodynamicsOxidationFurnaceRecipes() {
        this(References.ID);
    }

    @Override
    public void addRecipes(RecipeOutput output) {

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.aluminum), 3), 0.4F, 200, 350.0, "ingot_aluminum", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.ORE_ALUMINUM, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SALTPETER, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.5))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.calciumcarbonate), 2), 0.1F, 200, 350.0, "calcium_carbonate", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_SODIUMCARBONATE, 1)
                //
                .addItemStackInput(new ItemStack(Items.BONE_MEAL))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.chromiumdisilicide)), 0.3F, 200, 350.0, "chromium_disilicide", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_CHROMIUM, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SILICA, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.chromium)), 0.3F, 200, 350.0, "ingot_chromium", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_CHROMIUM, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_CALCIUMCARBONATE, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.75))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.disulfur), 1), 0.1F, 200, 350.0, "sulfur_dioxide", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SULFUR, 1)
                //
                .addItemTagInput(ItemTags.COALS, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.silica), 3), 0.1F, 200, 350.0, "dust_silica", modID)
                //
                .addItemTagInput(Tags.Items.SANDS, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.COAL_COKE, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.sodiumcarbonate), 1), 0.1F, 200, 350.0, "sodium_carbonate", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SALT, 1)
                //
                .addItemTagInput(ItemTags.COALS, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.sulfurdichloride), 1), 0.1F, 200, 350.0, "sulfur_dichloride", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SALT, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SULFUR, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.thionylchloride), 1), 0.1F, 200, 350.0, "thionyl_chloride", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_SULFURDICHLORIDE, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_TRISULFUR, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.titanium), 1), 0.2F, 200, 350.0, "ingot_titanium", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_DITITANIUM, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SALT, 1)
                //
                .addItemBiproduct(new ProbableItem(new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()), 0.75))
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.trisulfur), 1), 0.1F, 200, 350.0, "sulfur_trioxide", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_DISULFUR, 1)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_VANADIUM, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.vanadium), 1), 0.1F, 200, 350.0, "vanadium_oxide", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_VANADIUM, 1)
                //
                .addItemTagInput(ItemTags.COALS, 1)
                //
                .save(output);

        newRecipe(new ItemStack(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get(), 1), 0.1F, 200, 350.0, "fiberglass_sheet", modID)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.PLASTIC, 1)
                //
                .addItemTagInput(Tags.Items.GLASS_BLOCKS, 1)
                //
                .save(output);

    }

    public Item2ItemBuilder<OxidationFurnaceRecipe> newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
        return new Item2ItemBuilder<>(OxidationFurnaceRecipe::new, stack, RecipeCategory.ITEM_2_ITEM, modID, "oxidation_furnace/" + name, group, xp, ticks, usagePerTick);
    }

}
