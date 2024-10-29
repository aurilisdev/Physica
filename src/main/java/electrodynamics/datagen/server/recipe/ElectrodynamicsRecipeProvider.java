package electrodynamics.datagen.server.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import electrodynamics.datagen.server.recipe.types.custom.ElectrodynamicsChemicalReactorRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluid2gas.ElectrodynamicsElectrolyticSeparatorRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluid2item.ElectrodynamicsChemicalCrystallizerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid.ElectrodynamicsChemicalMixerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid.ElectrodynamicsFermentationPlantRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid.ElectrodynamicsMineralWasherRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsEnergizedAlloyerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsLatheRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsMineralCrusherRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsMineralGrinderRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsOxidationFurnaceRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsReinforcedAlloyerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsWireMillRecipes;
import electrodynamics.datagen.server.recipe.types.vanilla.ElectrodynamicsCraftingTableRecipes;
import electrodynamics.datagen.server.recipe.types.vanilla.ElectrodynamicsSmeltingRecipes;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class ElectrodynamicsRecipeProvider extends RecipeProvider {

	public final List<AbstractRecipeGenerator> GENERATORS = new ArrayList<>();
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;


	public ElectrodynamicsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
		this.lookupProvider = lookupProvider;
		addRecipes();
	}

	public void addRecipes() {
		GENERATORS.add(new ElectrodynamicsCraftingTableRecipes());
		GENERATORS.add(new ElectrodynamicsSmeltingRecipes());
		GENERATORS.add(new ElectrodynamicsElectrolyticSeparatorRecipes());
		GENERATORS.add(new ElectrodynamicsChemicalCrystallizerRecipes());
		GENERATORS.add(new ElectrodynamicsMineralWasherRecipes());
		GENERATORS.add(new ElectrodynamicsFermentationPlantRecipes());
		GENERATORS.add(new ElectrodynamicsChemicalMixerRecipes());
		GENERATORS.add(new ElectrodynamicsEnergizedAlloyerRecipes());
		GENERATORS.add(new ElectrodynamicsLatheRecipes());
		GENERATORS.add(new ElectrodynamicsMineralCrusherRecipes());
		GENERATORS.add(new ElectrodynamicsMineralGrinderRecipes());
		GENERATORS.add(new ElectrodynamicsOxidationFurnaceRecipes());
		GENERATORS.add(new ElectrodynamicsReinforcedAlloyerRecipes());
		GENERATORS.add(new ElectrodynamicsWireMillRecipes());
		GENERATORS.add(new ElectrodynamicsChemicalReactorRecipes());
	}

    @Override
    protected void buildRecipes(RecipeOutput output) {

        for (AbstractRecipeGenerator generator : GENERATORS) {
            generator.addRecipes(output);
        }
    }

}
