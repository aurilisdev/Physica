package electrodynamics.compatibility.jei.recipecategories.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import electrodynamics.api.References;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.categories.chemicalreactor.ChemicalReactorRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.types.ArrowAnimatedObject;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.compatibility.jei.utils.gui.types.ItemSlotObject;
import electrodynamics.compatibility.jei.utils.gui.types.fluidgauge.AbstractFluidGaugeObject;
import electrodynamics.compatibility.jei.utils.gui.types.fluidgauge.FluidGaugeObject;
import electrodynamics.compatibility.jei.utils.gui.types.gasgauge.AbstractGasGaugeObject;
import electrodynamics.compatibility.jei.utils.gui.types.gasgauge.GasGaugeObject;
import electrodynamics.compatibility.jei.utils.ingredients.ElectrodynamicsJeiTypes;
import electrodynamics.compatibility.jei.utils.ingredients.IngredientRendererGasStack;
import electrodynamics.compatibility.jei.utils.label.types.BiproductPercentWrapperElectroRecipe;
import electrodynamics.compatibility.jei.utils.label.types.PowerLabelWrapperElectroRecipe;
import electrodynamics.compatibility.jei.utils.label.types.TimeLabelWrapperElectroRecipe;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChemicalReactorRecipeCategory extends AbstractRecipeCategory<ChemicalReactorRecipe> {

    public static final BackgroundObject BACK_WRAP = new BackgroundObject(132, 132);

    public static final ItemSlotObject INPUT_SLOT1 = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, 5, 74, RecipeIngredientRole.INPUT);
    public static final ItemSlotObject INPUT_SLOT2 = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, 5, 94, RecipeIngredientRole.INPUT);
    public static final ItemSlotObject OUTPUT_SLOT = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, 63, 85, RecipeIngredientRole.OUTPUT);
    public static final ItemSlotObject BIPRODUCT_SLOT1 = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, 83, 67, RecipeIngredientRole.OUTPUT);
    public static final ItemSlotObject BIPRODUCT_SLOT2 = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, 83, 85, RecipeIngredientRole.OUTPUT);
    public static final ItemSlotObject BIPRODUCT_SLOT3 = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, 83, 103, RecipeIngredientRole.OUTPUT);
    public static final FluidGaugeObject IN_FLUIDGAUGE1 = new FluidGaugeObject(3, 3);
    public static final FluidGaugeObject IN_FLUIDGAUGE2 = new FluidGaugeObject(17, 3);
    public static final FluidGaugeObject OUT_FLUIDGAUGE1 = new FluidGaugeObject(73, 3);
    public static final FluidGaugeObject OUT_FLUIDGAUGE2 = new FluidGaugeObject(87, 3);
    public static final GasGaugeObject IN_GASGAUGE1 = new GasGaugeObject(31, 3);
    public static final GasGaugeObject IN_GASGAUGE2 = new GasGaugeObject(45, 3);
    public static final GasGaugeObject OUT_GASGAUGE1 = new GasGaugeObject(101, 3);
    public static final GasGaugeObject OUT_GASGAUGE2 = new GasGaugeObject(115, 3);


    public static final ArrowAnimatedObject ANIM_ARROW = new ArrowAnimatedObject(ScreenComponentProgress.ProgressBars.PROGRESS_ARROW_RIGHT, 32, 85, IDrawableAnimated.StartDirection.LEFT);

    public static final PowerLabelWrapperElectroRecipe POWER_LABEL = new PowerLabelWrapperElectroRecipe(2, 123, 480);
    public static final BiproductPercentWrapperElectroRecipe ITEM_LABEL1 = new BiproductPercentWrapperElectroRecipe(101, 72, BiproductPercentWrapperElectroRecipe.BiproductType.ITEM, 0);
    public static final BiproductPercentWrapperElectroRecipe ITEM_LABEL2 = new BiproductPercentWrapperElectroRecipe(101, 90, BiproductPercentWrapperElectroRecipe.BiproductType.ITEM, 1);
    public static final BiproductPercentWrapperElectroRecipe ITEM_LABEL3 = new BiproductPercentWrapperElectroRecipe(101, 108, BiproductPercentWrapperElectroRecipe.BiproductType.ITEM, 2);
    public static final BiproductPercentWrapperElectroRecipe FLUID_LABEL = new BiproductPercentWrapperElectroRecipe(87, 53, BiproductPercentWrapperElectroRecipe.BiproductType.FLUID, 0);
    public static final BiproductPercentWrapperElectroRecipe GAS_LABEL = new BiproductPercentWrapperElectroRecipe(115, 53, BiproductPercentWrapperElectroRecipe.BiproductType.GAS, 0);

    public static final TimeLabelWrapperElectroRecipe TIME_LABEL = new TimeLabelWrapperElectroRecipe(130, 123);

    public static final int ANIM_TIME = 50;

    // public static final String RECIPE_GROUP = SubtypeMachine.energizedalloyer.tag();

    public static ItemStack INPUT_MACHINE = new ItemStack(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR.get());

    public static final RecipeType<ChemicalReactorRecipe> RECIPE_TYPE = RecipeType.create(References.ID, ChemicalReactorRecipe.RECIPE_GROUP, ChemicalReactorRecipe.class);
    public ChemicalReactorRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, ElectroTextUtils.jeiTranslated(ChemicalReactorRecipe.RECIPE_GROUP), INPUT_MACHINE, BACK_WRAP, RECIPE_TYPE, ANIM_TIME);
        setInputSlots(guiHelper, INPUT_SLOT1, INPUT_SLOT2);
        setOutputSlots(guiHelper, OUTPUT_SLOT, BIPRODUCT_SLOT1, BIPRODUCT_SLOT2, BIPRODUCT_SLOT3);
        setFluidInputs(guiHelper, IN_FLUIDGAUGE1, IN_FLUIDGAUGE2);
        setFluidOutputs(guiHelper, OUT_FLUIDGAUGE1, OUT_FLUIDGAUGE2);
        setGasInputs(guiHelper, IN_GASGAUGE1, IN_GASGAUGE2);
        setGasOutputs(guiHelper, OUT_GASGAUGE1, OUT_GASGAUGE2);
        setAnimatedArrows(guiHelper, ANIM_ARROW);
        setLabels(ITEM_LABEL1, ITEM_LABEL2, ITEM_LABEL3, FLUID_LABEL, GAS_LABEL, POWER_LABEL, TIME_LABEL);
    }

    @Override
    public List<List<ItemStack>> getItemInputs(ChemicalReactorRecipe recipe) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        if(recipe.hasItemInputs()){
            recipe.getCountedIngredients().forEach(h -> inputs.add(Arrays.asList(h.getItemsArray())));
        } else {
            inputs.add(Collections.emptyList());
            inputs.add(Collections.emptyList());
        }
        if(inputs.size() < 2) {
            inputs.add(Collections.emptyList());
        }
        return inputs;
    }

    @Override
    public List<ItemStack> getItemOutputs(ChemicalReactorRecipe recipe) {
        List<ItemStack> outputs = new ArrayList<>();
        if(recipe.hasItemOutput()) {
            outputs.add(recipe.getItemRecipeOutput());
        } else {
            outputs.add(ItemStack.EMPTY);
        }
        if (recipe.hasItemBiproducts()) {
            outputs.addAll(Arrays.asList(recipe.getFullItemBiStacks()));

        }
        if(outputs.size() < 4) {
            for(int i = outputs.size() - 1; i < 4; i++){
                outputs.add(ItemStack.EMPTY);
            }
        }
        return outputs;
    }

    @Override
    public List<List<FluidStack>> getFluidInputs(ChemicalReactorRecipe recipe) {
        List<List<FluidStack>> inputs = new ArrayList<>();
        if(recipe.hasFluidInputs()) {
            for (FluidIngredient ing : recipe.getFluidIngredients()) {
                List<FluidStack> fluids = new ArrayList<>();
                for (FluidStack stack : ing.getMatchingFluids()) {
                    if (!BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString().toLowerCase(Locale.ROOT).contains("flow")) {
                        fluids.add(stack);
                    }
                }
                inputs.add(fluids);
            }
        } else {
            inputs.add(Collections.emptyList());
            inputs.add(Collections.emptyList());
        }
        if(inputs.size() < 2) {
            inputs.add(Collections.emptyList());
        }
        return inputs;
    }

    @Override
    public List<FluidStack> getFluidOutputs(ChemicalReactorRecipe recipe) {
        List<FluidStack> outputs = new ArrayList<>();
        if(recipe.hasFluidOutput()) {
            outputs.add(recipe.getFluidRecipeOutput());
        } else {
            outputs.add(FluidStack.EMPTY);
        }
        if (recipe.hasFluidBiproducts()) {
            outputs.addAll(Arrays.asList(recipe.getFullFluidBiStacks()));
        }
        if(outputs.size() < 2) {
            for(int i = outputs.size() - 1; i < 2; i++){
                outputs.add(FluidStack.EMPTY);
            }
        }
        return outputs;
    }

    @Override
    public List<List<GasStack>> getGasInputs(ChemicalReactorRecipe recipe) {
        List<List<GasStack>> inputs = new ArrayList<>();
        if(recipe.hasGasInputs()) {
            for (GasIngredient ing : recipe.getGasIngredients()) {
                inputs.add(ing.getMatchingGases());
            }
        } else {
            inputs.add(Collections.emptyList());
            inputs.add(Collections.emptyList());
        }
        if(inputs.size() < 2) {
            inputs.add(Collections.emptyList());
        }
        return inputs;
    }

    @Override
    public List<GasStack> getGasOutputs(ChemicalReactorRecipe recipe) {
        List<GasStack> outputs = new ArrayList<>();
        if(recipe.hasGasOutput()) {
            outputs.add(recipe.getGasRecipeOutput());
        } else {
            outputs.add(GasStack.EMPTY);
        }
        if (recipe.hasGasBiproducts()) {
            outputs.addAll(Arrays.asList(recipe.getFullGasBiStacks()));
        }
        if(outputs.size() < 2) {
            for(int i = outputs.size() - 1; i < 2; i++){
                outputs.add(GasStack.EMPTY);
            }
        }
        return outputs;
    }

    @Override
    public void setItemInputs(List<List<ItemStack>> inputs, IRecipeLayoutBuilder builder) {
        SlotDataWrapper wrapper;
        for (int i = 0; i < inputSlotWrappers.length; i++) {
            wrapper = inputSlotWrappers[i];
            if(inputs.get(i).isEmpty()){
                continue;
            }
            builder.addSlot(wrapper.role(), wrapper.x(), wrapper.y()).addItemStacks(inputs.get(i));

        }
    }

    @Override
    public void setItemOutputs(List<ItemStack> outputs, IRecipeLayoutBuilder builder) {
        SlotDataWrapper wrapper;
        for (int i = 0; i < outputSlotWrappers.length; i++) {
            wrapper = outputSlotWrappers[i];
            if (i < outputs.size()) {
                if(outputs.get(i).isEmpty()){
                    continue;
                }
                builder.addSlot(wrapper.role(), wrapper.x(), wrapper.y()).addItemStack(outputs.get(i));

            }
        }
    }

    @Override
    public void setFluidInputs(List<List<FluidStack>> inputs, IRecipeLayoutBuilder builder) {
        AbstractFluidGaugeObject wrapper;
        RecipeIngredientRole role = RecipeIngredientRole.INPUT;
        FluidStack stack;

        int maxGaugeCap = 0;

        for (List<FluidStack> stacks : inputs) {

            if(stacks.isEmpty()) {
                continue;
            }

            stack = stacks.get(0);
            int gaugeCap = (int) Math.pow(10, MathUtils.nearestPowerOf10(stack.getAmount(), true));
            if (gaugeCap > maxGaugeCap) {
                maxGaugeCap = gaugeCap;
            }
        }
        
        for (int i = 0; i < fluidInputWrappers.length; i++) {
            wrapper = fluidInputWrappers[i];

            if(inputs.get(i).isEmpty()){
                continue;
            }
            stack = inputs.get(i).get(0);

            if(stack.isEmpty()){
                continue;
            }

            int amt = stack.getAmount();

            //int gaugeCap = (int) Math.pow(10, MathUtils.nearestPowerOf10(amt, true));

            int height = (int) Math.ceil((float) amt / (float) maxGaugeCap * wrapper.getFluidTextHeight());

            builder.addSlot(role, wrapper.getFluidXPos(), wrapper.getFluidYPos() - height).setFluidRenderer(stack.getAmount(), false, wrapper.getFluidTextWidth(), height).addIngredients(NeoForgeTypes.FLUID_STACK, inputs.get(i));
        }
    }

    @Override
    public void setFluidOutputs(List<FluidStack> outputs, IRecipeLayoutBuilder builder) {
        AbstractFluidGaugeObject wrapper;
        RecipeIngredientRole role = RecipeIngredientRole.OUTPUT;
        FluidStack stack;

        int maxGaugeCap = 0;

        for (FluidStack s : outputs) {
            int gaugeCap = (int) Math.pow(10, MathUtils.nearestPowerOf10(s.getAmount(), true));
            if (gaugeCap > maxGaugeCap) {
                maxGaugeCap = gaugeCap;
            }
        }

        for (int i = 0; i < fluidOutputWrappers.length; i++) {
            wrapper = fluidOutputWrappers[i];
            stack = outputs.get(i);

            if(stack.isEmpty()){
                continue;
            }

            int amt = stack.getAmount();

            //int gaugeCap = (int) Math.pow(10, MathUtils.nearestPowerOf10(amt, true));

            int height = (int) Math.ceil((float) amt / (float) maxGaugeCap * wrapper.getFluidTextHeight());
            builder.addSlot(role, wrapper.getFluidXPos(), wrapper.getFluidYPos() - height).setFluidRenderer(stack.getAmount(), false, wrapper.getFluidTextWidth(), height).addIngredient(NeoForgeTypes.FLUID_STACK, stack);
        }
    }

    @Override
    public void setGasInputs(List<List<GasStack>> inputs, IRecipeLayoutBuilder builder) {

        AbstractGasGaugeObject wrapper;
        RecipeIngredientRole role = RecipeIngredientRole.INPUT;
        List<GasStack> stacks;

        int maxGaugeCap = 0;

        for (List<GasStack> stackz : inputs) {

            if(stackz.isEmpty()) {
                continue;
            }

            GasStack stack = stackz.get(0);
            int gaugeCap = (int) Math.pow(10, MathUtils.nearestPowerOf10(stack.getAmount(), true));
            if (gaugeCap > maxGaugeCap) {
                maxGaugeCap = gaugeCap;
            }
        }

        for (int i = 0; i < gasInputWrappers.length; i++) {

            wrapper = gasInputWrappers[i];
            stacks = inputs.get(i);

            if(stacks.isEmpty()){
                continue;
            }

            double amt = stacks.get(0).getAmount();

            //double gaugeCap = Math.pow(10, MathUtils.nearestPowerOf10(amt, true));

            int height = (int) (Math.ceil(amt / maxGaugeCap * (wrapper.getHeight() - 2)));

            int oneMinusHeight = wrapper.getHeight() - height;

            builder.addSlot(role, wrapper.getX() + 1, wrapper.getY() + wrapper.getHeight() - height).addIngredients(ElectrodynamicsJeiTypes.GAS_STACK, stacks).setCustomRenderer(ElectrodynamicsJeiTypes.GAS_STACK, new IngredientRendererGasStack(maxGaugeCap, -oneMinusHeight + 1, height, wrapper.getBarsTexture()));
        }

    }

    @Override
    public void setGasOutputs(List<GasStack> outputs, IRecipeLayoutBuilder builder) {

        AbstractGasGaugeObject wrapper;
        RecipeIngredientRole role = RecipeIngredientRole.OUTPUT;
        GasStack stack;

        int maxGaugeCap = 0;

        for (GasStack s : outputs) {
            int gaugeCap = (int) Math.pow(10, MathUtils.nearestPowerOf10(s.getAmount(), true));
            if (gaugeCap > maxGaugeCap) {
                maxGaugeCap = gaugeCap;
            }
        }

        for (int i = 0; i < gasOutputWrappers.length; i++) {

            wrapper = gasOutputWrappers[i];
            stack = outputs.get(i);

            if(stack.isEmpty()){
                continue;
            }

            double amt = stack.getAmount();

            //double gaugeCap = Math.pow(10, MathUtils.nearestPowerOf10(amt, true));

            int height = (int) (Math.ceil(amt / maxGaugeCap * (wrapper.getHeight() - 2)));

            int oneMinusHeight = wrapper.getHeight() - height;

            builder.addSlot(role, wrapper.getX() + 1, wrapper.getY() + wrapper.getHeight() - height).addIngredient(ElectrodynamicsJeiTypes.GAS_STACK, stack).setCustomRenderer(ElectrodynamicsJeiTypes.GAS_STACK, new IngredientRendererGasStack(maxGaugeCap, -oneMinusHeight + 1, height, wrapper.getBarsTexture()));
        }
    }
}
