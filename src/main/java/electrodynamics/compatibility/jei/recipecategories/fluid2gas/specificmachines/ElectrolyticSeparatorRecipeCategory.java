package electrodynamics.compatibility.jei.recipecategories.fluid2gas.specificmachines;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.recipe.categories.fluid2gas.specificmachines.ElectrolyticSeparatorRecipe;
import electrodynamics.compatibility.jei.recipecategories.fluid2gas.Fluid2GasRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.types.ArrowAnimatedObject;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.compatibility.jei.utils.gui.types.ItemSlotObject;
import electrodynamics.compatibility.jei.utils.gui.types.fluidgauge.FluidGaugeObject;
import electrodynamics.compatibility.jei.utils.gui.types.gasgauge.GasGaugeObject;
import electrodynamics.compatibility.jei.utils.label.types.PowerLabelWrapperElectroRecipe;
import electrodynamics.compatibility.jei.utils.label.types.TimeLabelWrapperElectroRecipe;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressBars;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;

public class ElectrolyticSeparatorRecipeCategory extends Fluid2GasRecipeCategory<ElectrolyticSeparatorRecipe> {

    public static final BackgroundObject BACK_WRAP = new BackgroundObject(132, 64);

    public static final ItemSlotObject IN_BUCKET_SLOT = new ItemSlotObject(SlotType.NORMAL, IconType.FLUID_DARK, 26, 36, RecipeIngredientRole.INPUT);
    public static final ItemSlotObject OUTPUT_BUCKET_SLOT = new ItemSlotObject(SlotType.NORMAL, IconType.FLUID_DARK, 66, 36, RecipeIngredientRole.OUTPUT);
    public static final ItemSlotObject BIPRODUCT_BUCKET_SLOT = new ItemSlotObject(SlotType.NORMAL, IconType.FLUID_DARK, 106, 36, RecipeIngredientRole.OUTPUT);

    public static final ArrowAnimatedObject ANIM_RIGHT_ARROW_1 = new ArrowAnimatedObject(ProgressBars.PROGRESS_ARROW_RIGHT, 24, 17, StartDirection.LEFT);
    public static final ArrowAnimatedObject ANIM_RIGHT_ARROW_2 = new ArrowAnimatedObject(ProgressBars.PROGRESS_ARROW_RIGHT, 64, 17, StartDirection.LEFT);

    public static final FluidGaugeObject IN_GAUGE = new FluidGaugeObject(8, 5);
    public static final GasGaugeObject OUT_GAUGE = new GasGaugeObject(48, 5);
    public static final GasGaugeObject BIPRODUCT_GAUGE = new GasGaugeObject(88, 5);

    public static final PowerLabelWrapperElectroRecipe POWER_LABEL = new PowerLabelWrapperElectroRecipe(2, 55, 240);
    public static final TimeLabelWrapperElectroRecipe TIME_LABEL = new TimeLabelWrapperElectroRecipe(130, 55);

    public static final int ANIM_TIME = 50;

    public static ItemStack INPUT_MACHINE = new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator));

    public static final RecipeType<ElectrolyticSeparatorRecipe> RECIPE_TYPE = RecipeType.create(References.ID, ElectrolyticSeparatorRecipe.RECIPE_GROUP, ElectrolyticSeparatorRecipe.class);

    public ElectrolyticSeparatorRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, ElectroTextUtils.jeiTranslated(ElectrolyticSeparatorRecipe.RECIPE_GROUP), INPUT_MACHINE, BACK_WRAP, RECIPE_TYPE, ANIM_TIME);
        setInputSlots(guiHelper, IN_BUCKET_SLOT);
        setOutputSlots(guiHelper, OUTPUT_BUCKET_SLOT, BIPRODUCT_BUCKET_SLOT);
        setFluidInputs(guiHelper, IN_GAUGE);
        setGasOutputs(guiHelper, OUT_GAUGE, BIPRODUCT_GAUGE);
        setAnimatedArrows(guiHelper, ANIM_RIGHT_ARROW_1, ANIM_RIGHT_ARROW_2);
        setLabels(POWER_LABEL, TIME_LABEL);
    }

}
