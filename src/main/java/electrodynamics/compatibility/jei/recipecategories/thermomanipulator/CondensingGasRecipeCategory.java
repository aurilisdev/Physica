package electrodynamics.compatibility.jei.recipecategories.thermomanipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.screen.ITexture.Textures;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import electrodynamics.common.settings.Constants;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes.types.PsuedoGas2FluidRecipe;
import electrodynamics.compatibility.jei.utils.gui.JeiTextures;
import electrodynamics.compatibility.jei.utils.gui.ScreenObject;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.compatibility.jei.utils.gui.types.fluidgauge.FluidGaugeObject;
import electrodynamics.compatibility.jei.utils.gui.types.gasgauge.GasGaugeObject;
import electrodynamics.compatibility.jei.utils.label.AbstractLabelWrapper;
import electrodynamics.compatibility.jei.utils.label.types.PowerLabelWrapperConstant;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class CondensingGasRecipeCategory extends AbstractRecipeCategory<PsuedoGas2FluidRecipe> {

	private static final BackgroundObject BACK_WRAP = new BackgroundObject(132, 70);

	private static final ScreenObject CONDENSER_COLUMN = new ScreenObject(Textures.CONDENSER_COLUMN, 50, 6);

	private static final ScreenObject IN_FLUID_GAUGE = new ScreenObject(JeiTextures.FLUID_GAUGE_DEFAULT, 10, 5);
	private static final FluidGaugeObject OUT_FLUID_GAUGE = new FluidGaugeObject(88, 5);

	private static final GasGaugeObject IN_GAS_GAUGE = new GasGaugeObject(30, 5);
	private static final ScreenObject OUT_GAS_GAUGE = new ScreenObject(JeiTextures.FAKE_GAS_GAUGE, 108, 5);

	private static final PowerLabelWrapperConstant POWER_LABEL = new PowerLabelWrapperConstant(2, 60, Constants.THERMOELECTRIC_MANIPULATOR_USAGE_PER_TICK, 120);
	private static final AbstractLabelWrapper TEMPERATURE_LABEL = new AbstractLabelWrapper(Color.JEI_TEXT_GRAY, 60, 130, true) {

		@Override
		public Component getComponent(AbstractRecipeCategory<?> category, Object recipe) {
			PsuedoGas2FluidRecipe psuedo = (PsuedoGas2FluidRecipe) recipe;
			return Component.literal("<= ").append(ChatFormatter.getChatDisplayShort(psuedo.inputs.get(0).getGasStack().getTemperature(), DisplayUnit.TEMPERATURE_KELVIN));
		}
	};

	private static final int ANIM_TIME = 50;

	public static final String RECIPE_GROUP = "gas_condensing";

	public static ItemStack INPUT_MACHINE = new ItemStack(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get());

	public static final RecipeType<PsuedoGas2FluidRecipe> RECIPE_TYPE = RecipeType.create(References.ID, RECIPE_GROUP, PsuedoGas2FluidRecipe.class);

	public CondensingGasRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, ElectroTextUtils.jeiTranslated(RECIPE_GROUP), INPUT_MACHINE, BACK_WRAP, RECIPE_TYPE, ANIM_TIME);

		setGasInputs(guiHelper, IN_GAS_GAUGE);
		setFluidOutputs(guiHelper, OUT_FLUID_GAUGE);

		setScreenObjects(guiHelper, IN_FLUID_GAUGE, OUT_GAS_GAUGE, CONDENSER_COLUMN);

		setLabels(POWER_LABEL, TEMPERATURE_LABEL);
	}

	@Override
	public List<List<GasStack>> getGasInputs(PsuedoGas2FluidRecipe recipe) {
		List<GasStack> gases = new ArrayList<>();
		for (GasIngredient ing : recipe.inputs) {
			gases.addAll(ing.getMatchingGases());
		}
		return Arrays.asList(gases);
	}

	@Override
	public List<FluidStack> getFluidOutputs(PsuedoGas2FluidRecipe recipe) {
		return Arrays.asList(recipe.output);
	}

	@Override
	public List<List<ItemStack>> getItemInputs(PsuedoGas2FluidRecipe recipe) {
		if (recipe.inputCylinder.isEmpty()) {
			return Collections.emptyList();
		}
		return Arrays.asList(Arrays.asList(recipe.inputCylinder));
	}

	@Override
	public List<ItemStack> getItemOutputs(PsuedoGas2FluidRecipe recipe) {
		if (recipe.outputBucket.isEmpty()) {
			return Collections.emptyList();
		}
		return Arrays.asList(recipe.outputBucket);
	}

}
