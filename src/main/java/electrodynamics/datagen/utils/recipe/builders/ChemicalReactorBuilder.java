package electrodynamics.datagen.utils.recipe.builders;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.categories.chemicalreactor.ChemicalReactorRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChemicalReactorBuilder extends ElectrodynamicsRecipeBuilder<ChemicalReactorRecipe, ChemicalReactorBuilder> {

    private ItemStack itemOutput = ItemStack.EMPTY;
    private FluidStack fluidOutput = FluidStack.EMPTY;
    private GasStack gasOutput = GasStack.EMPTY;

    private List<ItemStack> itemIngredients = new ArrayList<>();
    private List<Pair<TagKey<Item>, Integer>> tagItemIngredients = new ArrayList<>();
    private List<FluidStack> fluidIngredients = new ArrayList<>();
    private List<Pair<TagKey<Fluid>, Integer>> tagFluidIngredients = new ArrayList<>();
    private List<GasStack> gasIngredients = new ArrayList<>();
    private List<Pair<TagKey<Gas>, GasIngWrapper>> tagGasIngredients = new ArrayList<>();

    public ChemicalReactorBuilder(RecipeCategory category, String parent, String name, String group, double experience, int processTime, double usagePerTick) {
        super(category, parent, name, group, experience, processTime, usagePerTick);
    }

    public ChemicalReactorBuilder setItemOutput(ItemStack stack) {
        this.itemOutput = stack;
        return this;
    }

    public ChemicalReactorBuilder setFluidOutput(FluidStack stack) {
        this.fluidOutput = stack;
        return this;
    }

    public ChemicalReactorBuilder setGasOutput(GasStack stack) {
        this.gasOutput = stack;
        return this;
    }

    public ChemicalReactorBuilder addItemStackInput(ItemStack stack) {
        itemIngredients.add(stack);
        return this;
    }

    public ChemicalReactorBuilder addItemTagInput(TagKey<Item> tag, int count) {
        tagItemIngredients.add(Pair.of(tag, count));
        return this;
    }

    public ChemicalReactorBuilder addFluidStackInput(FluidStack stack) {
        fluidIngredients.add(stack);
        return this;
    }

    public ChemicalReactorBuilder addFluidTagInput(TagKey<Fluid> tag, int count) {
        tagFluidIngredients.add(Pair.of(tag, count));
        return this;
    }

    public ChemicalReactorBuilder addGasStackInput(GasStack stack) {
        gasIngredients.add(stack);
        return this;
    }

    public ChemicalReactorBuilder addGasTagInput(TagKey<Gas> tag, GasIngWrapper count) {
        tagGasIngredients.add(Pair.of(tag, count));
        return this;
    }

    @Override
    public ChemicalReactorRecipe makeRecipe() {
        List<CountableIngredient> itemIngs = new ArrayList<>();
        for (ItemStack item : itemIngredients) {
            itemIngs.add(new CountableIngredient(item));
        }
        for (Pair<TagKey<Item>, Integer> pair : tagItemIngredients) {
            itemIngs.add(new CountableIngredient(Ingredient.of(pair.getFirst()), pair.getSecond()));
        }
        List<FluidIngredient> fluidIngs = new ArrayList<>();
        for (FluidStack fluid : fluidIngredients) {
            fluidIngs.add(new FluidIngredient(fluid));
        }
        for (Pair<TagKey<Fluid>, Integer> pair : tagFluidIngredients) {
            fluidIngs.add(new FluidIngredient(pair.getFirst(), pair.getSecond()));
        }
        List<GasIngredient> gasIngs = new ArrayList<>();
        for (GasStack gas : gasIngredients) {
            gasIngs.add(new GasIngredient(gas));
        }
        for (Pair<TagKey<Gas>, GasIngWrapper> pair : tagGasIngredients) {
            gasIngs.add(new GasIngredient(pair.getFirst(), pair.getSecond().amt(), pair.getSecond().temp(), pair.getSecond().pressure()));
        }
        return new ChemicalReactorRecipe(group, itemIngs, fluidIngs, gasIngs, itemOutput, fluidOutput, gasOutput, experience, processTime, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
    }

    @Override
    public Item getResult() {
        return itemOutput.getItem();
    }
}
