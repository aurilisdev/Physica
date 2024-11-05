package electrodynamics.datagen.server.recipe.types.custom;

import electrodynamics.api.References;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ChemicalReactorBuilder;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectrodynamicsChemicalReactorRecipes extends AbstractRecipeGenerator {

    private final String modID;

    public ElectrodynamicsChemicalReactorRecipes(String modID) {
        this.modID = modID;
    }

    public ElectrodynamicsChemicalReactorRecipes() {
        this(References.ID);
    }

    @Override
    public void addRecipes(RecipeOutput output) {

        for (SubtypeSulfateFluid fluid : SubtypeSulfateFluid.values()) {
            newRecipe(0, 200, 800.0, "pure_" + fluid.name() + "_from_" + fluid.name() + "_sulfate", modID)
                    //
                    .setFluidOutput(new FluidStack(fluid.result.get(), 200))
                    //
                    .addFluidTagInput(fluid.tag, 200)
                    //
                    .addFluidTagInput(FluidTags.WATER, 1000)
                    //
                    .addFluidBiproduct(new ProbableFluid(new FluidStack(ElectrodynamicsFluids.FLUID_SULFURICACID.get(), 150), 1))
                    //
                    .save(output);
        }

        newRecipe(0, 200, 700, "hydrochloric_acid", modID)
                //
                .setFluidOutput(new FluidStack(ElectrodynamicsFluids.FLUID_HYDROCHLORICACID.get(), 500))
                //
                .addFluidTagInput(FluidTags.WATER, 1000)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SALT, 5)
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.HYDROGEN, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 500, 5))
                //
                .save(output);

        newRecipe(0, 200, 1000, "ammonia", modID)
                //
                .setGasOutput(new GasStack(ElectrodynamicsGases.AMMONIA.value(), 1000, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL))
                //
                .addFluidTagInput(FluidTags.WATER, 2000)
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.NITROGEN, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 500, 5))
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.HYDROGEN, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 500, 5))
                //
                .save(output);

        newRecipe(0, 200, 1000, "nitric_acid", modID)
                //
                .setFluidOutput(new FluidStack(ElectrodynamicsFluids.FLUID_NITRICACID.get(), 500))
                //
                .addFluidTagInput(FluidTags.WATER, 3000)
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.AMMONIA, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 700, 3))
                //
                .save(output);

    }

    public ChemicalReactorBuilder newRecipe(float xp, int ticks, double usagePerTick, String name, String group) {
        return new ChemicalReactorBuilder(ElectrodynamicsRecipeBuilder.RecipeCategory.CHEMICAL_REACTOR, modID, name, group, xp, ticks, usagePerTick);
    }
}
