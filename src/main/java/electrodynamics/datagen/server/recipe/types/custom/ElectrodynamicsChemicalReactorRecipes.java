package electrodynamics.datagen.server.recipe.types.custom;

import electrodynamics.api.References;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ChemicalReactorBuilder;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder;
import electrodynamics.registers.ElectrodynamicsFluids;
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

    }

    public ChemicalReactorBuilder newRecipe(float xp, int ticks, double usagePerTick, String name, String group) {
        return new ChemicalReactorBuilder(ElectrodynamicsRecipeBuilder.RecipeCategory.CHEMICAL_REACTOR, modID, name, group, xp, ticks, usagePerTick);
    }
}
