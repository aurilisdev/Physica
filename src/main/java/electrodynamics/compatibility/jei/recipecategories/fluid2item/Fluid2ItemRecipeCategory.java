package electrodynamics.compatibility.jei.recipecategories.fluid2item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import electrodynamics.common.recipe.categories.fluid2item.Fluid2ItemRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public abstract class Fluid2ItemRecipeCategory<T extends Fluid2ItemRecipe> extends AbstractRecipeCategory<T> {

    /*
     * DOCUMENTATION NOTES:
     * 
     * > Items supercede bucket slots in position > All biproducts will be included with the outputSlots field > All fluid
     * bucket output slots will be incled with the outputSlots field
     */

    public Fluid2ItemRecipeCategory(IGuiHelper guiHelper, Component title, ItemStack inputMachine, BackgroundObject bWrap, RecipeType<T> recipeType, int animTime) {

        super(guiHelper, title, inputMachine, bWrap, recipeType, animTime);
    }

    @Override
    public List<List<FluidStack>> getFluidInputs(Fluid2ItemRecipe recipe) {
        List<List<FluidStack>> ingredients = new ArrayList<>();
        for (FluidIngredient ing : recipe.getFluidIngredients()) {
            List<FluidStack> fluids = new ArrayList<>();
            for (FluidStack stack : ing.getMatchingFluids()) {
                if (!BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString().toLowerCase(Locale.ROOT).contains("flow")) {
                    fluids.add(stack);
                }
            }
            ingredients.add(fluids);
        }
        return ingredients;
    }

    @Override
    public List<List<ItemStack>> getItemInputs(Fluid2ItemRecipe recipe) {
        List<FluidIngredient> ings = recipe.getFluidIngredients();
        List<List<ItemStack>> totalBuckets = new ArrayList<>();
        for (FluidIngredient ing : ings) {
            List<ItemStack> buckets = new ArrayList<>();
            for (FluidStack stack : ing.getMatchingFluids()) {
                ItemStack bucket = new ItemStack(stack.getFluid().getBucket(), 1);
                IFluidHandlerItem handler = bucket.getCapability(Capabilities.FluidHandler.ITEM);

                if (handler != null) {

                    handler.fill(stack, FluidAction.EXECUTE);

                    bucket = handler.getContainer();

                }
                buckets.add(bucket);
            }
            totalBuckets.add(buckets);
        }
        return totalBuckets;
    }

    @Override
    public List<ItemStack> getItemOutputs(Fluid2ItemRecipe recipe) {
        List<ItemStack> outputItems = new ArrayList<>();

        outputItems.add(recipe.getItemRecipeOutput());

        if (recipe.hasItemBiproducts()) {
            outputItems.addAll(Arrays.asList(recipe.getFullItemBiStacks()));
        }

        if (recipe.hasFluidBiproducts()) {
            for (ProbableFluid stack : recipe.getFluidBiproducts()) {
                ItemStack temp = new ItemStack(stack.getFullStack().getFluid().getBucket(), 1);
                IFluidHandlerItem handler = temp.getCapability(Capabilities.FluidHandler.ITEM);

                if (handler != null) {

                    handler.fill(stack.getFullStack(), FluidAction.EXECUTE);

                    temp = handler.getContainer();

                }
                outputItems.add(temp);
            }
        }
        return outputItems;
    }

}
