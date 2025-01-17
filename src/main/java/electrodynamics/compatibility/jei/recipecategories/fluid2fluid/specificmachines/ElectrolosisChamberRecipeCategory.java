package electrodynamics.compatibility.jei.recipecategories.fluid2fluid.specificmachines;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.recipe.categories.fluid2fluid.specificmachines.ElectrolosisChamberRecipe;
import electrodynamics.common.settings.Constants;
import electrodynamics.compatibility.jei.recipecategories.fluid2fluid.Fluid2FluidRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.types.ArrowAnimatedObject;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.compatibility.jei.utils.gui.types.ItemSlotObject;
import electrodynamics.compatibility.jei.utils.gui.types.fluidgauge.FluidGaugeObject;
import electrodynamics.compatibility.jei.utils.label.types.PowerLabelWrapperConstant;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;

public class ElectrolosisChamberRecipeCategory extends Fluid2FluidRecipeCategory<ElectrolosisChamberRecipe> {

    public static final BackgroundObject BACK_WRAP = new BackgroundObject(132, 64);
    public static final ItemSlotObject INPUT_BUCKET_SLOT = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, ScreenComponentSlot.IconType.FLUID_DARK, 26, 36, RecipeIngredientRole.INPUT);
    public static final ItemSlotObject OUTPUT_BUCKET_SLOT = new ItemSlotObject(ScreenComponentSlot.SlotType.NORMAL, ScreenComponentSlot.IconType.FLUID_DARK, 88, 36, RecipeIngredientRole.OUTPUT);

    public static final ArrowAnimatedObject ANIM_RIGHT_ARROW = new ArrowAnimatedObject(ScreenComponentProgress.ProgressBars.PROGRESS_ARROW_RIGHT_BIG, 34, 17, IDrawableAnimated.StartDirection.LEFT);

    public static final FluidGaugeObject IN_GAUGE = new FluidGaugeObject(10, 5);
    public static final FluidGaugeObject OUT_GAUGE = new FluidGaugeObject(108, 5);

    public static final PowerLabelWrapperConstant POWER_LABEL = new PowerLabelWrapperConstant(2, 55, Constants.ELECTROLOSIS_CHAMBER_TARGET_JOULES, 1920);

    public static final int ANIM_TIME = 50;

    public static ItemStack INPUT_MACHINE = new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolosischamber));

    public static final RecipeType<ElectrolosisChamberRecipe> RECIPE_TYPE = RecipeType.create(References.ID, ElectrolosisChamberRecipe.RECIPE_GROUP, ElectrolosisChamberRecipe.class);

    public ElectrolosisChamberRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, ElectroTextUtils.jeiTranslated(ElectrolosisChamberRecipe.RECIPE_GROUP), INPUT_MACHINE, BACK_WRAP, RECIPE_TYPE, ANIM_TIME);
        setInputSlots(guiHelper, INPUT_BUCKET_SLOT);
        setOutputSlots(guiHelper, OUTPUT_BUCKET_SLOT);
        setFluidInputs(guiHelper, IN_GAUGE);
        setFluidOutputs(guiHelper, OUT_GAUGE);
        setAnimatedArrows(guiHelper, ANIM_RIGHT_ARROW);
        setLabels(POWER_LABEL);
    }
}
