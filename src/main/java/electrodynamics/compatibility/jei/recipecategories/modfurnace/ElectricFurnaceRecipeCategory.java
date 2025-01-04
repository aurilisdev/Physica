package electrodynamics.compatibility.jei.recipecategories.modfurnace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.settings.Constants;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.ScreenObject;
import electrodynamics.compatibility.jei.utils.gui.types.ArrowAnimatedObject;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.compatibility.jei.utils.gui.types.ItemSlotObject;
import electrodynamics.compatibility.jei.utils.label.types.PowerLabelWrapperConstant;
import electrodynamics.compatibility.jei.utils.label.types.TimeLabelWrapperConstant;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressBars;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressTextures;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class ElectricFurnaceRecipeCategory extends AbstractRecipeCategory<SmeltingRecipe> {

	public static final BackgroundObject BACK_WRAP = new BackgroundObject(132, 58);

	public static final ItemSlotObject INPUT_SLOT = new ItemSlotObject(SlotType.NORMAL, 22, 20, RecipeIngredientRole.INPUT);
	public static final ItemSlotObject OUTPUT_SLOT = new ItemSlotObject(SlotType.BIG, 83, 16, RecipeIngredientRole.OUTPUT);

	public static final ArrowAnimatedObject ANIM_ARROW = new ArrowAnimatedObject(ProgressBars.PROGRESS_ARROW_RIGHT, 50, 23, StartDirection.LEFT);
	public static final ScreenObject FLAME = new ScreenObject(ProgressTextures.FLAME_ON, 5, 23);

	public static final PowerLabelWrapperConstant POWER_LABEL = new PowerLabelWrapperConstant(2, 48, Constants.ELECTRICFURNACE_USAGE_PER_TICK, 120);
	public static final TimeLabelWrapperConstant TIME_LABEL = new TimeLabelWrapperConstant(130, 48, Constants.ELECTRICFURNACE_REQUIRED_TICKS);

	public static final int ANIM_TIME = 50;

	public static final String RECIPE_GROUP = "smelting";

	public static ItemStack INPUT_MACHINE = new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnace));

	public static final RecipeType<SmeltingRecipe> RECIPE_TYPE = RecipeType.create(References.ID, RECIPE_GROUP, SmeltingRecipe.class);

	public ElectricFurnaceRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, ElectroTextUtils.jeiTranslated(RECIPE_GROUP), INPUT_MACHINE, BACK_WRAP, RECIPE_TYPE, ANIM_TIME);
		setInputSlots(guiHelper, INPUT_SLOT);
		setOutputSlots(guiHelper, OUTPUT_SLOT);
		setScreenObjects(guiHelper, FLAME);
		setAnimatedArrows(guiHelper, ANIM_ARROW);
		setLabels(POWER_LABEL, TIME_LABEL);
	}

	@Override
	public List<List<ItemStack>> getItemInputs(SmeltingRecipe recipe) {
		List<List<ItemStack>> inputs = new ArrayList<>();
		for (Ingredient ing : recipe.getIngredients()) {
			inputs.add(Arrays.asList(ing.getItems()));
		}
		return inputs;
	}

	@Override
	public List<ItemStack> getItemOutputs(SmeltingRecipe recipe) {
		List<ItemStack> outputs = new ArrayList<>();
		outputs.add(recipe.getResultItem(null));
		return outputs;
	}

}
