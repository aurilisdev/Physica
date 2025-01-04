package electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid;

import electrodynamics.api.References;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.recipe.categories.fluiditem2fluid.specificmachines.ChemicalMixerRecipe;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.FluidItem2FluidBuilder;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectrodynamicsChemicalMixerRecipes extends AbstractRecipeGenerator {

	public static double CHEMICALMIXER_USAGE_PER_TICK = 400.0;
	public static int CHEMICALMIXER_REQUIRED_TICKS = 200;

	public final String modID;

	public ElectrodynamicsChemicalMixerRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsChemicalMixerRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_HYDRAULIC, 1000), 0, 200, 400.0, "hydraulic_fluid", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.ETHANOL, 500)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.DUST_SILICA, 1)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_HYDROFLUORICACID, 1000), 0, 200, 400.0, "hydrofluoric_acid", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.SULFURIC_ACID, 1000)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.RAW_ORE_FLUORITE, 1)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_CLAY, 1000), 0, 200, 400.0, "liquid_clay", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemTagInput(ItemTags.DIRT, 1)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUIDS_SULFATE.getValue(SubtypeSulfateFluid.molybdenum), 250), 0, 200, 400.0, "molybdenum_from_carrots", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.SULFURIC_ACID, 100)
				//
				.addItemTagInput(Tags.Items.CROPS_CARROT, 6)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUIDS_SULFATE.getValue(SubtypeSulfateFluid.molybdenum), 250), 0, 200, 400.0, "molybdenum_from_potatos", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.SULFURIC_ACID, 100)
				//
				.addItemTagInput(Tags.Items.CROPS_POTATO, 4)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUIDS_SULFATE.getValue(SubtypeSulfateFluid.molybdenum), 250), 0, 200, 400.0, "molybdenum_from_wheat", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.SULFURIC_ACID, 100)
				//
				.addItemTagInput(Tags.Items.CROPS_WHEAT, 5)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_POLYETHYLENE, 1000), 0, 200, 400.0, "polyethylene", modID)
				//
				.addFluidTagInput(ElectrodynamicsTags.Fluids.ETHANOL, 1000)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.OXIDE_CHROMIUMDISILICIDE, 1)
				//
				.save(output);

		newRecipe(new FluidStack(ElectrodynamicsFluids.FLUID_SULFURICACID, 2500), 0, 200, 400.0, "sulfuric_acid", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1000)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.OXIDE_TRISULFUR, 1)
				//
				.save(output);

	}

	public FluidItem2FluidBuilder<ChemicalMixerRecipe> newRecipe(FluidStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new FluidItem2FluidBuilder<>(ChemicalMixerRecipe::new, stack, RecipeCategory.FLUID_ITEM_2_FLUID, modID, "chemical_mixer/" + name, group, xp, ticks, usagePerTick);
	}

}
