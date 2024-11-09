package electrodynamics.compatibility.jei.recipecategories.fluid2gas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.categories.fluid2gas.Fluid2GasRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsItems;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public abstract class Fluid2GasRecipeCategory<T extends Fluid2GasRecipe> extends AbstractRecipeCategory<T> {

    public Fluid2GasRecipeCategory(IGuiHelper guiHelper, Component title, ItemStack inputMachine, BackgroundObject wrapper, RecipeType<T> recipeType, int animationTime) {
        super(guiHelper, title, inputMachine, wrapper, recipeType, animationTime);
    }

    @Override
    public List<List<FluidStack>> getFluidInputs(Fluid2GasRecipe recipe) {
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
    public List<List<ItemStack>> getItemInputs(Fluid2GasRecipe recipe) {
        List<List<ItemStack>> ingredients = new ArrayList<>();

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
    public List<ItemStack> getItemOutputs(Fluid2GasRecipe recipe) {
        List<ItemStack> outputItems = new ArrayList<>();

        ItemStack output = new ItemStack(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());

        IGasHandlerItem outputHandler = output.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        outputHandler.fill(recipe.getGasRecipeOutput(), GasAction.EXECUTE);

        output = outputHandler.getContainer();

        if (output.getItem() == Items.AIR) {
            output = new ItemStack(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());
        }

        outputItems.add(output);

        if (recipe.hasGasBiproducts()) {
            for (ProbableGas gas : recipe.getGasBiproducts()) {
                ItemStack temp = new ItemStack(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());

                IGasHandlerItem handler = temp.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

                if (handler != null) {

                    handler.fill(gas.getFullStack(), GasAction.EXECUTE);

                    temp = handler.getContainer();

                }
                if (temp.getItem() == Items.AIR) {
                    temp = new ItemStack(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());
                }
                outputItems.add(temp);
            }
        }

        return outputItems;
    }

    @Override
    public List<GasStack> getGasOutputs(Fluid2GasRecipe recipe) {
        List<GasStack> outputs = new ArrayList<>();
        outputs.add(recipe.getGasRecipeOutput());
        if (recipe.hasGasBiproducts()) {
            outputs.addAll(Arrays.asList(recipe.getFullGasBiStacks()));
        }
        return outputs;
    }

}
