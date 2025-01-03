package electrodynamics.datagen.server.recipe.types.custom.fluid2fluid;

import electrodynamics.api.References;
import electrodynamics.common.fluid.subtype.SubtypeImpureMineralFluid;
import electrodynamics.common.recipe.categories.fluid2fluid.specificmachines.ElectrolosisChamberRecipe;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder;
import electrodynamics.datagen.utils.recipe.builders.Fluid2FluidBuilder;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectrodynamicsElectrolosisChamberRecipes extends AbstractRecipeGenerator {

    public final String modID;

    public ElectrodynamicsElectrolosisChamberRecipes(String modID) {
        this.modID = modID;
    }

    public ElectrodynamicsElectrolosisChamberRecipes() {
        this(References.ID);
    }

    @Override
    public void addRecipes(RecipeOutput output) {

        for (SubtypeImpureMineralFluid impure : SubtypeImpureMineralFluid.values()) {
            newRecipe(new FluidStack(impure.result.get(), 1), 0, 0, 0, "impure_" + impure.name() + "fluid_to_pure_" + impure.name() + "_fluid", modID)
                    //
                    .addFluidStackInput(new FluidStack(ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(impure), 1))
                    //
                    .save(output);

        }

    }

    public Fluid2FluidBuilder<ElectrolosisChamberRecipe> newRecipe(FluidStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
        return new Fluid2FluidBuilder<>(ElectrolosisChamberRecipe::new, stack, ElectrodynamicsRecipeBuilder.RecipeCategory.FLUID_2_FLUID, modID, "electrolosis_chamber/" + name, group, xp, ticks, usagePerTick);
    }
}
