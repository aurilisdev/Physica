package electrodynamics.datagen.server.recipe.types.custom;

import electrodynamics.api.References;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ChemicalReactorBuilder;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;

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

    }

    public ChemicalReactorBuilder newRecipe(float xp, int ticks, double usagePerTick, String name, String group) {
        return new ChemicalReactorBuilder(ElectrodynamicsRecipeBuilder.RecipeCategory.CHEMICAL_REACTOR, modID, name, group, xp, ticks, usagePerTick);
    }
}
