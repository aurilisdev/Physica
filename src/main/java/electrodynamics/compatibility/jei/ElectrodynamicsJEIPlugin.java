package electrodynamics.compatibility.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import electrodynamics.Electrodynamics;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.screen.tile.ScreenChemicalCrystallizer;
import electrodynamics.client.screen.tile.ScreenChemicalMixer;
import electrodynamics.client.screen.tile.ScreenChemicalReactor;
import electrodynamics.client.screen.tile.ScreenDO2OProcessor;
import electrodynamics.client.screen.tile.ScreenElectricArcFurnace;
import electrodynamics.client.screen.tile.ScreenElectricArcFurnaceDouble;
import electrodynamics.client.screen.tile.ScreenElectricArcFurnaceTriple;
import electrodynamics.client.screen.tile.ScreenElectricFurnace;
import electrodynamics.client.screen.tile.ScreenElectricFurnaceDouble;
import electrodynamics.client.screen.tile.ScreenElectricFurnaceTriple;
import electrodynamics.client.screen.tile.ScreenElectrolosisChamber;
import electrodynamics.client.screen.tile.ScreenElectrolyticSeparator;
import electrodynamics.client.screen.tile.ScreenFermentationPlant;
import electrodynamics.client.screen.tile.ScreenGasCollector;
import electrodynamics.client.screen.tile.ScreenMineralWasher;
import electrodynamics.client.screen.tile.ScreenO2OProcessor;
import electrodynamics.client.screen.tile.ScreenO2OProcessorDouble;
import electrodynamics.client.screen.tile.ScreenO2OProcessorTriple;
import electrodynamics.client.screen.tile.ScreenThermoelectricManipulator;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.reloadlistener.CombustionFuelRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.electricitygrid.generators.TileCoalGenerator;
import electrodynamics.compatibility.jei.recipecategories.fluid2fluid.specificmachines.ElectrolosisChamberRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.fluid2gas.specificmachines.ElectrolyticSeparatorRecipeCategory;
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
import electrodynamics.compatibility.jei.recipecategories.misc.ChemicalReactorRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.misc.GasCollectorRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.modfurnace.ElectricArcFurnaceRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.modfurnace.ElectricFurnaceRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.thermomanipulator.CondensingGasRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.thermomanipulator.EvaporatingFluidRecipeCategory;
import electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes.ElectrodynamicsPsuedoRecipes;
import electrodynamics.compatibility.jei.screenhandlers.ScreenHandlerGuidebook;
import electrodynamics.compatibility.jei.screenhandlers.ScreenHandlerMaterialScreen;
import electrodynamics.compatibility.jei.utils.ingredients.ElectrodynamicsJeiTypes;
import electrodynamics.compatibility.jei.utils.ingredients.IngredientHelperGasStack;
import electrodynamics.compatibility.jei.utils.ingredients.IngredientRendererGasStack;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.object.CombustionFuelSource;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsGases;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;

@JeiPlugin
public class ElectrodynamicsJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = Electrodynamics.rl( "jei");

    public static List<mezz.jei.api.recipe.RecipeType<?>> O2O_CLICK_AREAS = new ArrayList<>();
    public static List<mezz.jei.api.recipe.RecipeType<?>> DO2O_CLICK_AREAS = new ArrayList<>();

    private static final int FULL_FLUID_SQUARE = 1600;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(ElectrodynamicsJeiTypes.GAS_STACK, new ArrayList<>(), new IngredientHelperGasStack(), IngredientRendererGasStack.LIST_RENDERER, GasStack.CODEC);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(ElectricFurnaceRecipeCategory.INPUT_MACHINE, ElectricFurnaceRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacedouble)), ElectricFurnaceRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacetriple)), ElectricFurnaceRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(WireMillRecipeCategory.INPUT_MACHINE, WireMillRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilldouble)), WireMillRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilltriple)), WireMillRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(MineralCrusherRecipeCategory.INPUT_MACHINE, MineralCrusherRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusherdouble)), MineralCrusherRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple)), MineralCrusherRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(MineralGrinderRecipeCategory.INPUT_MACHINE, MineralGrinderRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinderdouble)), MineralGrinderRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrindertriple)), MineralGrinderRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ElectricArcFurnaceRecipeCategory.INPUT_MACHINE, ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacedouble)), ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacetriple)), ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(OxidationFurnaceRecipeCategory.INPUT_MACHINE, OxidationFurnaceRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(EnergizedAlloyerRecipeCategory.INPUT_MACHINE, EnergizedAlloyerRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(LatheRecipeCategory.INPUT_MACHINE, LatheRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(MineralWasherRecipeCategory.INPUT_MACHINE, MineralWasherRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ChemicalCrystallizerRecipeCategory.INPUT_MACHINE, ChemicalCrystallizerRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ChemicalMixerRecipeCategory.INPUT_MACHINE, ChemicalMixerRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(FermentationPlantRecipeCategory.INPUT_MACHINE, FermentationPlantRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ReinforcedAlloyerRecipeCategory.INPUT_MACHINE, ReinforcedAlloyerRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ElectrolyticSeparatorRecipeCategory.INPUT_MACHINE, ElectrolyticSeparatorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(CondensingGasRecipeCategory.INPUT_MACHINE, CondensingGasRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get()), CondensingGasRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(EvaporatingFluidRecipeCategory.INPUT_MACHINE, EvaporatingFluidRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get()), EvaporatingFluidRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ChemicalReactorRecipeCategory.INPUT_MACHINE, ChemicalReactorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(GasCollectorRecipeCategory.INPUT_MACHINE, GasCollectorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ElectrolosisChamberRecipeCategory.INPUT_MACHINE, ElectrolosisChamberRecipeCategory.RECIPE_TYPE);

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = Objects.requireNonNull(mc.level);
        RecipeManager recipeManager = world.getRecipeManager();

        ElectrodynamicsPsuedoRecipes.initRecipes();

        // Electric Furnace
        registration.addRecipes(ElectricFurnaceRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeType.SMELTING).stream().map(val -> val.value()).toList());

        // Electric Arc Furnace
        registration.addRecipes(ElectricArcFurnaceRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(RecipeType.BLASTING).stream().map(val -> val.value()).toList());

        // Wire Mill
        registration.addRecipes(WireMillRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.WIRE_MILL_TYPE.get()).stream().map(val -> val.value()).toList());

        // Mineral Crusher
        registration.addRecipes(MineralCrusherRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.MINERAL_CRUSHER_TYPE.get()).stream().map(val -> val.value()).toList());

        // Mineral Grinder
        registration.addRecipes(MineralGrinderRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.MINERAL_GRINDER_TYPE.get()).stream().map(val -> val.value()).toList());

        // Oxidation Furnace
        registration.addRecipes(OxidationFurnaceRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.OXIDATION_FURNACE_TYPE.get()).stream().map(val -> val.value()).toList());

        // Energized Alloyer
        registration.addRecipes(EnergizedAlloyerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.ENERGIZED_ALLOYER_TYPE.get()).stream().map(val -> val.value()).toList());

        // Lathe
        registration.addRecipes(LatheRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.LATHE_TYPE.get()).stream().map(val -> val.value()).toList());

        // Mineral Washer
        registration.addRecipes(MineralWasherRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.MINERAL_WASHER_TYPE.get()).stream().map(val -> val.value()).toList());

        // Chemical Crystallizer
        registration.addRecipes(ChemicalCrystallizerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.CHEMICAL_CRYSTALIZER_TYPE.get()).stream().map(val -> val.value()).toList());

        // Chemical Mixer
        registration.addRecipes(ChemicalMixerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.CHEMICAL_MIXER_TYPE.get()).stream().map(val -> val.value()).toList());

        // Fermentation Chamber
        registration.addRecipes(FermentationPlantRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.FERMENTATION_PLANT_TYPE.get()).stream().map(val -> val.value()).toList());

        // Reinforced Alloyer
        registration.addRecipes(ReinforcedAlloyerRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.REINFORCED_ALLOYER_TYPE.get()).stream().map(val -> val.value()).toList());

        // Electrolytic Separator
        registration.addRecipes(ElectrolyticSeparatorRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.ELECTROLYTIC_SEPERATOR_TYPE.get()).stream().map(val -> val.value()).toList());

        // Thermoelectric Manipulator Condensing
        registration.addRecipes(CondensingGasRecipeCategory.RECIPE_TYPE, new ArrayList<>(ElectrodynamicsPsuedoRecipes.CONDENSATION_RECIPES));

        // Thermoelectric Manipulator Evaporating
        registration.addRecipes(EvaporatingFluidRecipeCategory.RECIPE_TYPE, new ArrayList<>(ElectrodynamicsPsuedoRecipes.EVAPORATION_RECIPES));

        // Chemical Reactor
        registration.addRecipes(ChemicalReactorRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_TYPE.get()).stream().map(val -> val.value()).toList());

        // Gas Collector
        registration.addRecipes(GasCollectorRecipeCategory.RECIPE_TYPE, new ArrayList<>(ElectrodynamicsPsuedoRecipes.GAS_COLLECTOR_RECIPES));

        // Electrolosis Chamber
        registration.addRecipes(ElectrolosisChamberRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ElectrodynamicsRecipeInit.ELECTROLOSIS_CHAMBER_TYPE.get()).stream().map(val -> val.value()).toList());

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
        registration.addRecipeCategories(new CondensingGasRecipeCategory(guiHelper));
        registration.addRecipeCategories(new EvaporatingFluidRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ChemicalReactorRecipeCategory(guiHelper));
        registration.addRecipeCategories(new GasCollectorRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ElectrolosisChamberRecipeCategory(guiHelper));

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
        registry.addRecipeClickArea(ScreenElectricArcFurnace.class, 85, 35, 22, 15, ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenElectricArcFurnaceDouble.class, 85, 25, 22, 35, ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenElectricArcFurnaceTriple.class, 85, 25, 22, 55, ElectricArcFurnaceRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenChemicalMixer.class, 42, 31, 22, 15, ChemicalMixerRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenChemicalMixer.class, 97, 31, 22, 15, ChemicalMixerRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenFermentationPlant.class, 42, 31, 22, 15, FermentationPlantRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenFermentationPlant.class, 97, 31, 22, 15, FermentationPlantRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenMineralWasher.class, 42, 31, 22, 15, MineralWasherRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenMineralWasher.class, 97, 31, 22, 15, MineralWasherRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenChemicalCrystallizer.class, 42, 35, 64, 15, ChemicalCrystallizerRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenElectrolyticSeparator.class, 38, 30, 22, 15, ElectrolyticSeparatorRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenElectrolyticSeparator.class, 78, 30, 22, 15, ElectrolyticSeparatorRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenThermoelectricManipulator.class, 62, 19, 32, 47, CondensingGasRecipeCategory.RECIPE_TYPE, EvaporatingFluidRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenChemicalReactor.class, 66, 52, 22, 15, ChemicalReactorRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenGasCollector.class, 57, 34, 17, 17, GasCollectorRecipeCategory.RECIPE_TYPE);
        registry.addRecipeClickArea(ScreenElectrolosisChamber.class, 56, 31, 64, 15, ElectrolosisChamberRecipeCategory.RECIPE_TYPE);

        registry.addGenericGuiContainerHandler(ScreenGuidebook.class, new ScreenHandlerGuidebook());
        registry.addGenericGuiContainerHandler(GenericMaterialScreen.class, new ScreenHandlerMaterialScreen());
    }

    private static void electrodynamicsInfoTabs(IRecipeRegistration registration) {
        // Items
        for (Item item : TileCoalGenerator.getValidItems()) {
            ItemStack fuelStack = new ItemStack(item);
            registration.addIngredientInfo(fuelStack, VanillaTypes.ITEM_STACK, ElectroTextUtils.jeiItemTranslated("coalgeneratorfuelsource", ChatFormatter.getChatDisplayShort(fuelStack.getBurnTime(null) / 20.0, DisplayUnit.TIME_SECONDS)));
        }

        // Fluids
        for (CombustionFuelSource fuel : CombustionFuelRegister.INSTANCE.getFuels()) {
            for (FluidStack fluid : fuel.getFuels()) {
                registration.addIngredientInfo(new FluidStack(fluid.getFluidHolder(), FULL_FLUID_SQUARE), NeoForgeTypes.FLUID_STACK, ElectroTextUtils.jeiFluidTranslated("combustionchamberfuel", ChatFormatter.getChatDisplayShort(fuel.getPowerMultiplier() * Constants.COMBUSTIONCHAMBER_JOULES_PER_TICK * 20 / 1000.0, DisplayUnit.WATT), ChatFormatter.formatFluidMilibuckets(fluid.getAmount())));
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

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        List<FluidStack> fluids = new ArrayList<>();
        for (DeferredHolder<Fluid, ? extends Fluid> fluid : ElectrodynamicsFluids.FLUIDS.getEntries()) {
            fluids.add(new FluidStack(fluid.get(), 1000));
        }
        registration.addExtraIngredients(NeoForgeTypes.FLUID_STACK, fluids);

        List<GasStack> gases = new ArrayList<>();
        for(DeferredHolder<Gas, ? extends Gas> gas : ElectrodynamicsGases.GASES.getEntries()) {
            if(gas.get() == ElectrodynamicsGases.EMPTY.value()) {
                continue;
            }

            gases.add(new GasStack(gas.get(), 1000, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL));
        }
        registration.addExtraIngredients(ElectrodynamicsJeiTypes.GAS_STACK, gases);
    }

}
