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
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.settings.Constants;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes.types.PsuedoFluid2GasRecipe;
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

public class EvaporatingFluidRecipeCategory extends AbstractRecipeCategory<PsuedoFluid2GasRecipe> {

	private static final BackgroundObject BACK_WRAP = new BackgroundObject(132, 70);

	private static final ScreenObject CONDENSER_COLUMN = new ScreenObject(Textures.CONDENSER_COLUMN, 50, 6);

	private static final FluidGaugeObject IN_FLUID_GAUGE = new FluidGaugeObject(10, 5);
	private static final ScreenObject OUT_FLUID_GAUGE = new ScreenObject(JeiTextures.FLUID_GAUGE_DEFAULT, 88, 5);

	private static final ScreenObject IN_GAS_GAUGE = new ScreenObject(JeiTextures.FAKE_GAS_GAUGE, 30, 5);
	private static final GasGaugeObject OUT_GAS_GAUGE = new GasGaugeObject(108, 5);

	private static final PowerLabelWrapperConstant POWER_LABEL = new PowerLabelWrapperConstant(2, 60, Constants.THERMOELECTRIC_MANIPULATOR_USAGE_PER_TICK, 120);
	private static final AbstractLabelWrapper TEMPERATURE_LABEL = new AbstractLabelWrapper(Color.JEI_TEXT_GRAY, 60, 130, true) {

		@Override
		public Component getComponent(AbstractRecipeCategory<?> category, Object recipe) {
			PsuedoFluid2GasRecipe psuedo = (PsuedoFluid2GasRecipe) recipe;
			return Component.literal("> ").append(ChatFormatter.getChatDisplayShort(psuedo.output.getTemperature(), DisplayUnit.TEMPERATURE_KELVIN));
		}
	};

	private static final int ANIM_TIME = 50;

	public static final String RECIPE_GROUP = "gas_evaporating";

	public static ItemStack INPUT_MACHINE = new ItemStack(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get());

	public static final RecipeType<PsuedoFluid2GasRecipe> RECIPE_TYPE = RecipeType.create(References.ID, RECIPE_GROUP, PsuedoFluid2GasRecipe.class);

	public EvaporatingFluidRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, ElectroTextUtils.jeiTranslated(RECIPE_GROUP), INPUT_MACHINE, BACK_WRAP, RECIPE_TYPE, ANIM_TIME);

		setFluidInputs(guiHelper, IN_FLUID_GAUGE);
		setGasOutputs(guiHelper, OUT_GAS_GAUGE);

		setScreenObjects(guiHelper, IN_GAS_GAUGE, OUT_FLUID_GAUGE, CONDENSER_COLUMN);

		setLabels(POWER_LABEL, TEMPERATURE_LABEL);
	}

	@Override
	public List<List<FluidStack>> getFluidInputs(PsuedoFluid2GasRecipe recipe) {
		List<FluidStack> gases = new ArrayList<>();
		for (FluidIngredient ing : recipe.inputs) {
			gases.addAll(ing.getMatchingFluids());
		}
		return Arrays.asList(gases);
	}

	@Override
	public List<GasStack> getGasOutputs(PsuedoFluid2GasRecipe recipe) {
		return Arrays.asList(recipe.output);
	}

	@Override
	public List<List<ItemStack>> getItemInputs(PsuedoFluid2GasRecipe recipe) {
		if (recipe.inputBucket.isEmpty()) {
			return Collections.emptyList();
		}
		return Arrays.asList(Arrays.asList(recipe.inputBucket));
	}

	@Override
	public List<ItemStack> getItemOutputs(PsuedoFluid2GasRecipe recipe) {
		if (recipe.outputCylinder.isEmpty()) {
			return Collections.emptyList();
		}
		return Arrays.asList(recipe.outputCylinder);
	}

}
