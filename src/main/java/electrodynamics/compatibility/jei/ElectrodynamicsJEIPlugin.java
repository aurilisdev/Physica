package electrodynamics.compatibility.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import javax.annotation.Nullable;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.screen.tile.ScreenChemicalCrystallizer;
import electrodynamics.client.screen.tile.ScreenChemicalMixer;
import electrodynamics.client.screen.tile.ScreenDO2OProcessor;
import electrodynamics.client.screen.tile.ScreenElectricFurnace;
import electrodynamics.client.screen.tile.ScreenElectricFurnaceDouble;
import electrodynamics.client.screen.tile.ScreenElectricFurnaceTriple;
import electrodynamics.client.screen.tile.ScreenElectrolyticSeparator;
import electrodynamics.client.screen.tile.ScreenFermentationPlant;
import electrodynamics.client.screen.tile.ScreenMineralWasher;
import electrodynamics.client.screen.tile.ScreenO2OProcessor;
import electrodynamics.client.screen.tile.ScreenO2OProcessorDouble;
import electrodynamics.client.screen.tile.ScreenO2OProcessorTriple;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.reloadlistener.CombustionFuelRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.electricitygrid.generators.TileCoalGenerator;
import electrodynamics.compatibility.jei.recipecategories.fluid2fluid.specificmachines.ElectrolyticSeparatorRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.fluid2item.specificmachines.ChemicalCrystallizerRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.fluiditem2fluid.specificmachines.ChemicalMixerRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.fluiditem2fluid.specificmachines.FermentationPlantRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.fluiditem2fluid.specificmachines.MineralWasherRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines.EnergizedAlloyerRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines.LatheRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines.MineralCrusherRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines.MineralGrinderRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines.OxidationFurnaceRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines.ReinforcedAlloyerRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines.WireMillRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.modfurnace.ElectricArcFurnaceRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.modfurnace.ElectricFurnaceRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes.ElectrodynamicsPsuedoRecipes;
//import electrodynamics.compatibility.jei.recipemanagers.RecipeManagerPluginCanister;
import electrodynamics.compatibility.jei.screenhandlers.ScreenHandlerGuidebook;
import electrodynamics.compatibility.jei.screenhandlers.ScreenHandlerMaterialScreen;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.object.CombustionFuelSource;
import electrodynamics.registers.ElectrodynamicsBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
//import mezz.jei.api.runtime.IJeiRuntime;
//import mezz.jei.common.runtime.JeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

@JeiPlugin
public class ElectrodynamicsJEIPlugin implements IModPlugin {

	public static List<mezz.jei.api.recipe.RecipeType<?>> O2O_CLICK_AREAS = new ArrayList<>();
	public static List<mezz.jei.api.recipe.RecipeType<?>> DO2O_CLICK_AREAS = new ArrayList<>();

	private static final int FULL_FLUID_SQUARE = 1600;

	// private static IJeiRuntime RUNTIME = null;
	
	private static FluidStack makeFluidStack(RegistryObject<Fluid> fluid)
	{
		FluidStack fluidStack;
		fluidStack = new FluidStack(fluid.get(), 1000);
		return fluidStack;
	}

	/*
	@Override
	public void registerExtraIngredients(IExtraIngredientRegistration registration)
	{
		Collection<RegistryObject<Fluid>> fluids = ElectrodynamicsFluids.FLUIDS.getEntries();
		Collection<FluidStack> modFluids = new ArrayList<>();
		for (RegistryObject<Fluid> fluid : fluids)
		{
			modFluids.add(makeFluidStack(fluid));
		}
		registration.addExtraIngredients(ForgeTypes.FLUID_STACK, modFluids);
	}
	*/

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(electrodynamics.api.References.ID, "jei");
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

		registration.addRecipeCatalyst(ElectricFurnaceRecipeCategory.INPUT_MACHINE, ElectricFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacedouble)), ElectricFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacetriple)), ElectricFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(WireMillRecipeCategory.INPUT_MACHINE, WireMillRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilldouble)), WireMillRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilltriple)), WireMillRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(MineralCrusherRecipeCategory.INPUT_MACHINE, MineralCrusherRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusherdouble)), MineralCrusherRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrushertriple)), MineralCrusherRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(MineralGrinderRecipeCategory.INPUT_MACHINE, MineralGrinderRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinderdouble)), MineralGrinderRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrindertriple)), MineralGrinderRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(ElectricArcFurnaceRecipeCategory.INPUT_MACHINE, ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacedouble)), ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacetriple)), ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(OxidationFurnaceRecipeCategory.INPUT_MACHINE, OxidationFurnaceRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(EnergizedAlloyerRecipeCategory.INPUT_MACHINE, EnergizedAlloyerRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(LatheRecipeCategory.INPUT_MACHINE, LatheRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(MineralWasherRecipeCategory.INPUT_MACHINE, MineralWasherRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(ChemicalCrystallizerRecipeCategory.INPUT_MACHINE, ChemicalCrystallizerRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(ChemicalMixerRecipeCategory.INPUT_MACHINE, ChemicalMixerRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(FermentationPlantRecipeCategory.INPUT_MACHINE, FermentationPlantRecipeCategory.RECIPE_TYPE);
		registration.addRecipeCatalyst(ReinforcedAlloyerRecipeCategory.INPUT_MACHINE, ReinforcedAlloyerRecipeCategory.RECIPE_TYPE);

	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel world = Objects.requireNonNull(mc.level);
		RecipeManager recipeManager = world.getRecipeManager();

		ElectrodynamicsPsuedoRecipes.initRecipes();

		// Electric Furnace
		registration.addRecipes(ElectricFurnaceRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeType.SMELTING));

		// Electric Arc Furnace
		registration.addRecipes(ElectricArcFurnaceRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeType.BLASTING));

		// Wire Mill
		registration.addRecipes(WireMillRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.WIRE_MILL_TYPE.get()));

		// Mineral Crusher
		registration.addRecipes(MineralCrusherRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.MINERAL_CRUSHER_TYPE.get()));

		// Mineral Grinder
		registration.addRecipes(MineralGrinderRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.MINERAL_GRINDER_TYPE.get()));

		// Oxidation Furnace
		registration.addRecipes(OxidationFurnaceRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.OXIDATION_FURNACE_TYPE.get()));

		// Energized Alloyer
		registration.addRecipes(EnergizedAlloyerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.ENERGIZED_ALLOYER_TYPE.get()));

		// Lathe
		registration.addRecipes(LatheRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.LATHE_TYPE.get()));

		// Mineral Washer
		registration.addRecipes(MineralWasherRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.MINERAL_WASHER_TYPE.get()));

		// Chemical Crystallizer
		registration.addRecipes(ChemicalCrystallizerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.CHEMICAL_CRYSTALIZER_TYPE.get()));

		// Chemical Mixer
		registration.addRecipes(ChemicalMixerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.CHEMICAL_MIXER_TYPE.get()));

		// Fermentation Chamber
		registration.addRecipes(FermentationPlantRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.FERMENTATION_PLANT_TYPE.get()));

		// Reinforced Alloyer
		registration.addRecipes(ReinforcedAlloyerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.REINFORCED_ALLOYER_TYPE.get()));

		// Electrolytic Separator
		registration.addRecipes(ElectrolyticSeparatorRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.ELECTROLYTIC_SEPERATOR_TYPE.get()));

		electrodynamicsInfoTabs(registration);

	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {

		IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(new ElectricFurnaceRecipeCategory(guiHelper));
		registration.addRecipeCategories(new ElectricArcFurnaceRecipeCategory(guiHelper));
		registration.addRecipeCategories(new WireMillRecipeCategory(guiHelper));
		registration.addRecipeCategories(new MineralGrinderRecipeCategory(guiHelper));
		registration.addRecipeCategories(new MineralCrusherRecipeCategory(guiHelper));
		registration.addRecipeCategories(new OxidationFurnaceRecipeCategory(guiHelper));
		registration.addRecipeCategories(new EnergizedAlloyerRecipeCategory(guiHelper));
		registration.addRecipeCategories(new LatheRecipeCategory(guiHelper));
		registration.addRecipeCategories(new MineralWasherRecipeCategory(guiHelper));
		registration.addRecipeCategories(new ChemicalCrystallizerRecipeCategory(guiHelper));
		registration.addRecipeCategories(new ChemicalMixerRecipeCategory(guiHelper));
		registration.addRecipeCategories(new FermentationPlantRecipeCategory(guiHelper));
		registration.addRecipeCategories(new ReinforcedAlloyerRecipeCategory(guiHelper));
		registration.addRecipeCategories(new ElectrolyticSeparatorRecipeCategory(guiHelper));

	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		registry.addRecipeClickArea(ScreenO2OProcessor.class, 48, 35, 22, 15, O2O_CLICK_AREAS.toArray(new mezz.jei.api.recipe.RecipeType[O2O_CLICK_AREAS.size()]));
		registry.addRecipeClickArea(ScreenO2OProcessorDouble.class, 48, 25, 22, 35, O2O_CLICK_AREAS.toArray(new mezz.jei.api.recipe.RecipeType[O2O_CLICK_AREAS.size()]));
		registry.addRecipeClickArea(ScreenO2OProcessorTriple.class, 48, 25, 22, 55, O2O_CLICK_AREAS.toArray(new mezz.jei.api.recipe.RecipeType[O2O_CLICK_AREAS.size()]));
		registry.addRecipeClickArea(ScreenDO2OProcessor.class, 48, 35, 22, 15, DO2O_CLICK_AREAS.toArray(new mezz.jei.api.recipe.RecipeType[DO2O_CLICK_AREAS.size()]));
		registry.addRecipeClickArea(ScreenElectricFurnace.class, 85, 35, 22, 15, ElectricFurnaceRecipeCategory.RECIPE_TYPE);
		registry.addRecipeClickArea(ScreenElectricFurnaceDouble.class, 85, 25, 22, 35, ElectricFurnaceRecipeCategory.RECIPE_TYPE);
		registry.addRecipeClickArea(ScreenElectricFurnaceTriple.class, 85, 25, 22, 55, ElectricFurnaceRecipeCategory.RECIPE_TYPE);
		registry.addRecipeClickArea(ScreenChemicalMixer.class, 97, 31, 22, 15, ChemicalMixerRecipeCategory.RECIPE_TYPE);
		registry.addRecipeClickArea(ScreenFermentationPlant.class, 97, 31, 22, 15, FermentationPlantRecipeCategory.RECIPE_TYPE);
		registry.addRecipeClickArea(ScreenMineralWasher.class, 97, 31, 22, 15, MineralWasherRecipeCategory.RECIPE_TYPE);
		registry.addRecipeClickArea(ScreenChemicalCrystallizer.class, 41, 35, 22, 15, ChemicalCrystallizerRecipeCategory.RECIPE_TYPE);
		registry.addRecipeClickArea(ScreenElectrolyticSeparator.class, 38, 30, 22, 15, ElectrolyticSeparatorRecipeCategory.RECIPE_TYPE);

		registry.addGenericGuiContainerHandler(ScreenGuidebook.class, new ScreenHandlerGuidebook());
		registry.addGenericGuiContainerHandler(GenericMaterialScreen.class, new ScreenHandlerMaterialScreen());
	}

	private static void electrodynamicsInfoTabs(IRecipeRegistration registration) {
		// Items
		for (Item item : TileCoalGenerator.getValidItems()) {
			ItemStack fuelStack = new ItemStack(item);
			registration.addIngredientInfo(fuelStack, VanillaTypes.ITEM_STACK, ElectroTextUtils.jeiItemTranslated("coalgeneratorfuelsource", ChatFormatter.getChatDisplayShort(ForgeHooks.getBurnTime(fuelStack, null) / 20.0, DisplayUnit.TIME_SECONDS)));
		}

		// Fluids
		for (CombustionFuelSource fuel : CombustionFuelRegister.INSTANCE.getFuels()) {
			for (FluidStack fluid : fuel.getFuels()) {
				registration.addIngredientInfo(new FluidStack(fluid, FULL_FLUID_SQUARE), ForgeTypes.FLUID_STACK, ElectroTextUtils.jeiFluidTranslated("combustionchamberfuel", ChatFormatter.getChatDisplayShort(fuel.getPowerMultiplier() * Constants.COMBUSTIONCHAMBER_JOULES_PER_TICK * 20 / 1000.0, DisplayUnit.WATT), ChatFormatter.formatFluidMilibuckets(fluid.getAmount())));
			}
		}

	}

	public static void addO2OClickArea(mezz.jei.api.recipe.RecipeType<?> type) {
		O2O_CLICK_AREAS.add(type);
	}

	public static void addDO2OClickArea(mezz.jei.api.recipe.RecipeType<?> type) {
		DO2O_CLICK_AREAS.add(type);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {

		// registration.addRecipeManagerPlugin(new RecipeManagerPluginCanister());

	}

}
