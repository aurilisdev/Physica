package electrodynamics.compatibility.jei.recipecategories.fluiditem2gas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.categories.fluiditem2gas.FluidItem2GasRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class FluidItem2GasRecipeCategory<T extends FluidItem2GasRecipe> extends AbstractRecipeCategory<T> {

    public FluidItem2GasRecipeCategory(IGuiHelper guiHelper, Component title, ItemStack inputMachine, BackgroundObject wrapper, RecipeType<T> recipeType, int animationTime) {
        super(guiHelper, title, inputMachine, wrapper, recipeType, animationTime);
    }

    @Override
    public List<List<ItemStack>> getItemInputs(T recipe) {
        List<List<ItemStack>> ingredients = new ArrayList<>();

        for (CountableIngredient ing : recipe.getCountedIngredients()) {
            ingredients.add(Arrays.asList(ing.getItemsArray()));
        }

        for (FluidIngredient ing : recipe.getFluidIngredients()) {
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
            ingredients.add(buckets);
        }

        return ingredients;
    }

    @Override
    public List<ItemStack> getItemOutputs(T recipe) {
        List<ItemStack> outputItems = new ArrayList<>();

        if (recipe.hasItemBiproducts()) {
            outputItems.addAll(Arrays.asList(recipe.getFullItemBiStacks()));
        }

        ItemStack bucket = new ItemStack(recipe.getGasRecipeOutput().getGas().getContainer(), 1);

        IGasHandlerItem outputHandler = bucket.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (outputHandler != null) {

            outputHandler.fill(recipe.getGasRecipeOutput(), GasAction.EXECUTE);

            bucket = outputHandler.getContainer();

            outputItems.add(bucket);
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

    @Override
    public List<List<FluidStack>> getFluidInputs(T recipe) {
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
    public List<GasStack> getGasOutputs(T recipe) {
        List<GasStack> outputFluids = new ArrayList<>();
        outputFluids.add(recipe.getGasRecipeOutput());
        if (recipe.hasGasBiproducts()) {
            outputFluids.addAll(Arrays.asList(recipe.getFullGasBiStacks()));
        }
        return outputFluids;
    }

}
