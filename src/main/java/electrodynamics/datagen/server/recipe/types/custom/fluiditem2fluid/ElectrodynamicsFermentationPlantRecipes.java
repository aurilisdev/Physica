package electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid;

import electrodynamics.api.References;
import electrodynamics.common.recipe.categories.fluiditem2fluid.specificmachines.FermentationPlantRecipe;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.FluidItem2FluidBuilder;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectrodynamicsFermentationPlantRecipes extends AbstractRecipeGenerator {

	public static double FERMENTATIONPLANT_USAGE_PER_TICK = 20.0;
	public static int FERMENTATIONPLANT_REQUIRED_TICKS = 2000;

	public final String modID;

	public ElectrodynamicsFermentationPlantRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsFermentationPlantRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_carrots", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemTagInput(Tags.Items.CROPS_CARROT, 12)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_melon_slices", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemStackInput(new ItemStack(Items.MELON_SLICE, 3))
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_mushrooms", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemTagInput(Tags.Items.MUSHROOMS, 11)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_potatos", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemTagInput(Tags.Items.CROPS_POTATO, 13)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_pumpkins", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemStackInput(new ItemStack(Items.PUMPKIN, 12))
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_seeds", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemTagInput(Tags.Items.SEEDS, 9)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_sugar_cane", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemStackInput(new ItemStack(Items.SUGAR_CANE, 9))
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_ETHANOL, 100), 0, 2000, 20.0, "ethanol_from_wheat", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemTagInput(Tags.Items.CROPS_WHEAT, 9)
				//
				.save(output);

	}

	public FluidItem2FluidBuilder<FermentationPlantRecipe> newRecipe(FluidStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new FluidItem2FluidBuilder<>(FermentationPlantRecipe::new, stack, RecipeCategory.FLUID_ITEM_2_FLUID, modID, "fermentation_plant/" + name, group, xp, ticks, usagePerTick);
	}

}
