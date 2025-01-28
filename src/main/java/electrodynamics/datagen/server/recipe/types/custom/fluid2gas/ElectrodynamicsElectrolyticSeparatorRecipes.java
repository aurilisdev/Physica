package electrodynamics.datagen.server.recipe.types.custom.fluid2gas;

import electrodynamics.api.References;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.categories.fluid2gas.specificmachines.ElectrolyticSeparatorRecipe;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.Fluid2GasBuilder;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.FluidTags;

public class ElectrodynamicsElectrolyticSeparatorRecipes extends AbstractRecipeGenerator {

	public static int ELECTROLYTICSEPARATOR_REQUIRED_TICKS = 200;
	public static double ELECTROLYTICSEPARATOR_USAGE_PER_TICK = 250.0;

	public final String modID;

	public ElectrodynamicsElectrolyticSeparatorRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsElectrolyticSeparatorRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new GasStack(ElectrodynamicsGases.OXYGEN.value(), 1, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL), 0, 1, 250.0, "water_to_hydrogen_and_oxygen", modID)
				//
				.addFluidTagInput(FluidTags.WATER, 1)
				//
				.addGasBiproduct(new ProbableGas(new GasStack(ElectrodynamicsGases.HYDROGEN.value(), 2, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL), 1))
				//
				.save(output);

	}

	public Fluid2GasBuilder<ElectrolyticSeparatorRecipe> newRecipe(GasStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new Fluid2GasBuilder<>(ElectrolyticSeparatorRecipe::new, stack, RecipeCategory.FLUID_2_GAS, modID, "electrolytic_separator/" + name, group, xp, ticks, usagePerTick);
	}

}
