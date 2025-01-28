package electrodynamics.common.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public abstract class ElectrodynamicsRecipe implements Recipe<ElectrodynamicsRecipe>, RecipeInput {

    private final String group;

    private final double xp;
    private final int ticks;
    private final double usagePerTick;

    private final List<ProbableItem> itemBiproducts;
    private final List<ProbableFluid> fluidBiproducts;

    private final List<ProbableGas> gasBiproducts;

    private final HashMap<Integer, List<Integer>> itemArrangements = new HashMap<>();
    @Nullable
    private List<Integer> fluidArrangement;
    @Nullable
    private List<Integer> gasArrangement;

    public ElectrodynamicsRecipe(String recipeGroup, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        group = recipeGroup;
        xp = experience;
        this.ticks = ticks;
        this.usagePerTick = usagePerTick;
        this.itemBiproducts = itemBiproducts;
        this.fluidBiproducts = fluidBiproducts;
        this.gasBiproducts = gasBiproducts;
    }

    /**
     * NEVER USE THIS METHOD!
     */
    @Override
    public boolean matches(ElectrodynamicsRecipe recipe, Level world) {
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public boolean hasItemBiproducts() {
        return itemBiproducts.size() != 0;
    }

    public boolean hasFluidBiproducts() {
        return fluidBiproducts.size() != 0;
    }

    public boolean hasGasBiproducts() {
        return gasBiproducts.size() != 0;
    }

    @Nullable
    public List<ProbableItem> getItemBiproducts() {
        return itemBiproducts;
    }

    @Nullable
    public List<ProbableFluid> getFluidBiproducts() {
        return fluidBiproducts;
    }

    @Nullable
    public List<ProbableGas> getGasBiproducts() {
        return gasBiproducts;
    }

    public ItemStack[] getFullItemBiStacks() {
        ItemStack[] items = new ItemStack[getItemBiproductCount()];
        for (int i = 0; i < getItemBiproductCount(); i++) {
            items[i] = itemBiproducts.get(i).getFullStack();
        }
        return items;
    }

    public FluidStack[] getFullFluidBiStacks() {
        FluidStack[] fluids = new FluidStack[getFluidBiproductCount()];
        for (int i = 0; i < getFluidBiproductCount(); i++) {
            fluids[i] = fluidBiproducts.get(i).getFullStack();
        }
        return fluids;
    }

    public GasStack[] getFullGasBiStacks() {
        GasStack[] gases = new GasStack[getGasBiproductCount()];
        for (int i = 0; i < getGasBiproductCount(); i++) {
            gases[i] = gasBiproducts.get(i).getFullStack();
        }
        return gases;
    }

    public int getItemBiproductCount() {
        return itemBiproducts.size();
    }

    public int getFluidBiproductCount() {
        return fluidBiproducts.size();
    }

    public int getGasBiproductCount() {
        return gasBiproducts.size();
    }

    public double getXp() {
        return xp;
    }

    public int getTicks() {
        return ticks;
    }

    public double getUsagePerTick() {
        return usagePerTick;
    }

    public void setItemArrangement(Integer procNumber, List<Integer> arrangement) {
        itemArrangements.put(procNumber, arrangement);
    }

    public List<Integer> getItemArrangment(Integer procNumber) {
        return itemArrangements.get(procNumber);
    }

    public void setFluidArrangement(List<Integer> arrangement) {
        fluidArrangement = arrangement;
    }

    public List<Integer> getFluidArrangement() {
        return fluidArrangement;
    }

    public void setGasArrangement(List<Integer> arrangement) {
        gasArrangement = arrangement;
    }

    public List<Integer> getGasArrangement() {
        return gasArrangement;
    }

    public static List<RecipeHolder<ElectrodynamicsRecipe>> findRecipesbyType(RecipeType<? extends ElectrodynamicsRecipe> typeIn, Level world) {
        return world != null ? world.getRecipeManager().getAllRecipesFor((RecipeType<ElectrodynamicsRecipe>) typeIn) : Collections.emptyList();
    }

    @Nullable
    public static ElectrodynamicsRecipe getRecipe(ComponentProcessor pr, List<RecipeHolder<ElectrodynamicsRecipe>> cachedRecipes) {
        for (RecipeHolder<ElectrodynamicsRecipe> recipe : cachedRecipes) {
            if (recipe.value().matchesRecipe(pr)) {
                return recipe.value();
            }
        }
        return null;
    }

    public static Pair<List<Integer>, Boolean> areItemsValid(List<CountableIngredient> ingredients, List<ItemStack> stacks) {
        Boolean valid = true;
        List<Integer> slotOreintation = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            CountableIngredient ing = ingredients.get(i);
            int slotNum = -1;
            for (int j = 0; j < stacks.size(); j++) {
                if (ing.test(stacks.get(j))) {
                    slotNum = j;
                    break;
                }
            }
            if (slotNum > -1 && !slotOreintation.contains(slotNum)) {
                slotOreintation.add(slotNum);
            }
        }
        if (slotOreintation.size() < ingredients.size()) {
            valid = false;
        }
        return Pair.of(slotOreintation, valid);
    }

    public static Pair<List<Integer>, Boolean> areFluidsValid(List<FluidIngredient> ingredients, FluidTank[] fluidTanks) {
        Boolean valid = true;
        List<Integer> tankOrientation = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            FluidIngredient ing = ingredients.get(i);
            int tankNum = -1;
            for (int j = 0; j < fluidTanks.length; j++) {
                if (ing.test(fluidTanks[j].getFluid())) {
                    tankNum = j;
                    break;
                }
            }
            if (tankNum > -1 && !tankOrientation.contains(tankNum)) {
                tankOrientation.add(tankNum);
            }
        }
        if (tankOrientation.size() < ingredients.size()) {
            valid = false;
        }
        return Pair.of(tankOrientation, valid);
    }

    public static Pair<List<Integer>, Boolean> areGasesValid(List<GasIngredient> ingredients, GasTank[] gasTanks) {
        Boolean valid = true;
        List<Integer> tankOrientation = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            GasIngredient ing = ingredients.get(i);
            int tankNum = -1;
            for (int j = 0; j < gasTanks.length; j++) {
                if (ing.testGas(gasTanks[j].getGas(), true, true)) {
                    tankNum = j;
                    break;
                }
            }
            if (tankNum > -1 && !tankOrientation.contains(tankNum)) {
                tankOrientation.add(tankNum);
            }
        }
        if (tankOrientation.size() < ingredients.size()) {
            valid = false;
        }
        return Pair.of(tankOrientation, valid);
    }

    public abstract boolean matchesRecipe(ComponentProcessor pr);

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int p_346128_) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
}
