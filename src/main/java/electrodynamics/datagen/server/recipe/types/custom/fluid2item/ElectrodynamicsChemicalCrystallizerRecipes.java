package electrodynamics.datagen.server.recipe.types.custom.fluid2item;

import electrodynamics.api.References;
import electrodynamics.common.fluid.subtype.SubtypePureMineralFluid;
import electrodynamics.common.recipe.categories.fluid2item.specificmachines.ChemicalCrystalizerRecipe;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.Fluid2ItemBuilder;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElectrodynamicsChemicalCrystallizerRecipes extends AbstractRecipeGenerator {

	public static double CHEMICALCRYSTALLIZER_USAGE_PER_TICK = 800.0;
	public static int CHEMICALCRYSTALLIZER_REQUIRED_TICKS = 200;

	public final String modID;

	public ElectrodynamicsChemicalCrystallizerRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsChemicalCrystallizerRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new ItemStack(Items.CLAY_BALL), 0, 200, 800.0, "clay_ball", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.CLAY, 200)
				//
				.save(output);

		newRecipe(new ItemStack(Items.OBSIDIAN), 0, 200, 800.0, "obsidian_from_lava", modID)
				//
				.addFluidTagInput(FluidTags.LAVA, 1000)
				//
				.save(output);

		newRecipe(new ItemStack(ElectrodynamicsItems.ITEM_PLASTIC_FIBERS.get()), 0, 200, 800.0, "plastic_fibers", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.POLYETHLYENE, 1000)
				//
				.save(output);

		for (SubtypePureMineralFluid fluid : SubtypePureMineralFluid.values()) {
			if (fluid.result != null) {
				newRecipe(new ItemStack(fluid.result.get()), 0, 200, 800.0, "crystal_" + fluid.name() + "_from_pure_fluid", modID)
						//
						.addFluidTagInput(fluid.tag, 200)
						//
						.save(output);
			}
		}

	}

	public Fluid2ItemBuilder<ChemicalCrystalizerRecipe> newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new Fluid2ItemBuilder<>(ChemicalCrystalizerRecipe::new, stack, RecipeCategory.FLUID_2_ITEM, modID, "chemical_crystallizer/" + name, group, xp, ticks, usagePerTick);
	}

}
