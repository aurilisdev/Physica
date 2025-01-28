package electrodynamics.common.recipe.categories.chemicalreactor;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.Electrodynamics;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.recipeutils.AbstractMaterialRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChemicalReactorRecipe extends AbstractMaterialRecipe {

    public static final String RECIPE_GROUP = "chemical_reactor_recipe";
    public static final ResourceLocation RECIPE_ID = Electrodynamics.rl(RECIPE_GROUP);

    private List<CountableIngredient> itemIngredients;
    private List<FluidIngredient> fluidIngredients;
    private List<GasIngredient> gasIngredients;
    private ItemStack itemOutput;
    private FluidStack fluidOutput;
    private GasStack gasOutput;

    public ChemicalReactorRecipe(String recipeGroup, List<CountableIngredient> inputItems, List<FluidIngredient> inputFluids, List<GasIngredient> inputGases, ItemStack itemOutput, FluidStack fluidOutput, GasStack gasOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(recipeGroup, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        if (inputItems.size() == 0 && inputGases.size() == 0 && inputFluids.size() == 0) {
            throw new RuntimeException("Yoou have created a chemical reactor recipe with no inputs");
        }
        if (itemOutput.isEmpty() && fluidOutput.isEmpty() && gasOutput.isEmpty()) {
            throw new RuntimeException("You have created a chemical reactor recipe with no outputs");
        }
        this.itemIngredients = inputItems;
        this.fluidIngredients = inputFluids;
        this.gasIngredients = inputGases;
        this.itemOutput = itemOutput;
        this.fluidOutput = fluidOutput;
        this.gasOutput = gasOutput;

    }

    @Override
    public boolean matchesRecipe(ComponentProcessor pr) {

        int valid = 0b000;

        if (hasItemInputs()) {
            Pair<List<Integer>, Boolean> itemPair = areItemsValid(getCountedIngredients(), ((ComponentInventory) pr.getHolder().getComponent(IComponentType.Inventory)).getInputsForProcessor(pr.getProcessorNumber()));
            if (itemPair.getSecond()) {
                setItemArrangement(pr.getProcessorNumber(), itemPair.getFirst());
                valid = valid | 1 << 2;
            } else {
                return false;
            }
        }

        if (hasFluidInputs()) {
            Pair<List<Integer>, Boolean> fluidPair = areFluidsValid(getFluidIngredients(), pr.getHolder().<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks());
            if (fluidPair.getSecond()) {
                setFluidArrangement(fluidPair.getFirst());
                valid = valid | 1 << 1;
            } else {
                return false;
            }
        }

        if (hasGasInputs()) {
            Pair<List<Integer>, Boolean> gasPair = areGasesValid(getGasIngredients(), pr.getHolder().<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getInputTanks());
            if (gasPair.getSecond()) {
                setGasArrangement(gasPair.getFirst());
                valid = valid | 1 << 0;
            } else {
                return false;
            }
        }

        return valid > 0;
    }

    public boolean hasItemInputs() {
        return getCountedIngredients().size() > 0;
    }

    public boolean hasFluidInputs() {
        return getFluidIngredients().size() > 0;
    }

    public boolean hasGasInputs() {
        return getGasIngredients().size() > 0;
    }

    public List<CountableIngredient> getCountedIngredients() {
        return itemIngredients;
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public List<GasIngredient> getGasIngredients() {
        return gasIngredients;
    }

    public boolean hasItemOutput() {
        return !getItemRecipeOutput().isEmpty();
    }

    public boolean hasFluidOutput() {
        return !getFluidRecipeOutput().isEmpty();
    }

    public boolean hasGasOutput() {
        return !getGasRecipeOutput().isEmpty();
    }

    @Override
    public ItemStack getItemRecipeOutput() {
        return itemOutput;
    }

    @Override
    public FluidStack getFluidRecipeOutput() {
        return fluidOutput;
    }

    @Override
    public GasStack getGasRecipeOutput() {
        return gasOutput;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_TYPE.get();
    }
}
