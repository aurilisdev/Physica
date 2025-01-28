package electrodynamics.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import electrodynamics.client.guidebook.ModuleElectrodynamics;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.misc.SWBFClientExtensions;
import electrodynamics.client.modelbakers.bakerytypes.CableModelLoader;
import electrodynamics.client.modelbakers.bakerytypes.MultiblockModelLoader;
import electrodynamics.client.modelbakers.bakerytypes.SlaveNodeModelLoader;
import electrodynamics.client.particle.fluiddrop.ParticleFluidDrop;
import electrodynamics.client.particle.lavawithphysics.ParticleLavaWithPhysics;
import electrodynamics.client.particle.plasmaball.ParticlePlasmaBall;
import electrodynamics.client.reloadlistener.ReloadListenerResetGuidebook;
import electrodynamics.client.render.entity.RenderEnergyBlast;
import electrodynamics.client.render.entity.RenderMetalRod;
import electrodynamics.client.render.itemdecorators.ItemDecoratorCombatHelmet;
import electrodynamics.client.render.itemdecorators.ItemDecoratorCombatLeggings;
import electrodynamics.client.render.itemdecorators.ItemDecoratorRailgun;
import electrodynamics.client.render.model.armor.types.ModelCombatArmor;
import electrodynamics.client.render.model.armor.types.ModelCompositeArmor;
import electrodynamics.client.render.model.armor.types.ModelHydraulicBoots;
import electrodynamics.client.render.model.armor.types.ModelJetpack;
import electrodynamics.client.render.model.armor.types.ModelNightVisionGoggles;
import electrodynamics.client.render.model.armor.types.ModelServoLeggings;
import electrodynamics.client.render.tile.RenderAdvancedSolarPanel;
import electrodynamics.client.render.tile.RenderAdvancedTransformer;
import electrodynamics.client.render.tile.RenderBatteryBox;
import electrodynamics.client.render.tile.RenderCarbyneBatteryBox;
import electrodynamics.client.render.tile.RenderChargerGeneric;
import electrodynamics.client.render.tile.RenderChemicalMixer;
import electrodynamics.client.render.tile.RenderChemicalReactor;
import electrodynamics.client.render.tile.RenderCombustionChamber;
import electrodynamics.client.render.tile.RenderConnectBlock;
import electrodynamics.client.render.tile.RenderCoolantResavoir;
import electrodynamics.client.render.tile.RenderElectrolosisChamber;
import electrodynamics.client.render.tile.RenderElectrolyticSeparator;
import electrodynamics.client.render.tile.RenderFermentationPlant;
import electrodynamics.client.render.tile.RenderFluidPipePump;
import electrodynamics.client.render.tile.RenderGasPipePump;
import electrodynamics.client.render.tile.RenderHydroelectricGenerator;
import electrodynamics.client.render.tile.RenderLathe;
import electrodynamics.client.render.tile.RenderLithiumBatteryBox;
import electrodynamics.client.render.tile.RenderLogisticalWire;
import electrodynamics.client.render.tile.RenderMineralCrusher;
import electrodynamics.client.render.tile.RenderMineralCrusherDouble;
import electrodynamics.client.render.tile.RenderMineralCrusherTriple;
import electrodynamics.client.render.tile.RenderMineralGrinder;
import electrodynamics.client.render.tile.RenderMineralGrinderDouble;
import electrodynamics.client.render.tile.RenderMineralGrinderTriple;
import electrodynamics.client.render.tile.RenderMineralWasher;
import electrodynamics.client.render.tile.RenderMotorComplex;
import electrodynamics.client.render.tile.RenderMultimeterBlock;
import electrodynamics.client.render.tile.RenderSeismicRelay;
import electrodynamics.client.render.tile.RenderTankGeneric;
import electrodynamics.client.render.tile.RenderWindmill;
import electrodynamics.client.screen.item.ScreenElectricDrill;
import electrodynamics.client.screen.item.ScreenSeismicScanner;
import electrodynamics.client.screen.tile.ScreenAdvancedCompressor;
import electrodynamics.client.screen.tile.ScreenAdvancedDecompressor;
import electrodynamics.client.screen.tile.ScreenAdvancedDowngradeTransformer;
import electrodynamics.client.screen.tile.ScreenAdvancedUpgradeTransformer;
import electrodynamics.client.screen.tile.ScreenBatteryBox;
import electrodynamics.client.screen.tile.ScreenChargerGeneric;
import electrodynamics.client.screen.tile.ScreenChemicalCrystallizer;
import electrodynamics.client.screen.tile.ScreenChemicalMixer;
import electrodynamics.client.screen.tile.ScreenChemicalReactor;
import electrodynamics.client.screen.tile.ScreenCircuitMonitor;
import electrodynamics.client.screen.tile.ScreenCoalGenerator;
import electrodynamics.client.screen.tile.ScreenCombustionChamber;
import electrodynamics.client.screen.tile.ScreenCompressor;
import electrodynamics.client.screen.tile.ScreenCoolantResavoir;
import electrodynamics.client.screen.tile.ScreenCreativeFluidSource;
import electrodynamics.client.screen.tile.ScreenCreativeGasSource;
import electrodynamics.client.screen.tile.ScreenCreativePowerSource;
import electrodynamics.client.screen.tile.ScreenDO2OProcessor;
import electrodynamics.client.screen.tile.ScreenDecompressor;
import electrodynamics.client.screen.tile.ScreenElectricArcFurnace;
import electrodynamics.client.screen.tile.ScreenElectricArcFurnaceDouble;
import electrodynamics.client.screen.tile.ScreenElectricArcFurnaceTriple;
import electrodynamics.client.screen.tile.ScreenElectricFurnace;
import electrodynamics.client.screen.tile.ScreenElectricFurnaceDouble;
import electrodynamics.client.screen.tile.ScreenElectricFurnaceTriple;
import electrodynamics.client.screen.tile.ScreenElectrolosisChamber;
import electrodynamics.client.screen.tile.ScreenElectrolyticSeparator;
import electrodynamics.client.screen.tile.ScreenFermentationPlant;
import electrodynamics.client.screen.tile.ScreenFluidPipeFilter;
import electrodynamics.client.screen.tile.ScreenFluidPipePump;
import electrodynamics.client.screen.tile.ScreenFluidTankGeneric;
import electrodynamics.client.screen.tile.ScreenFluidVoid;
import electrodynamics.client.screen.tile.ScreenGasCollector;
import electrodynamics.client.screen.tile.ScreenGasPipeFilter;
import electrodynamics.client.screen.tile.ScreenGasPipePump;
import electrodynamics.client.screen.tile.ScreenGasTankGeneric;
import electrodynamics.client.screen.tile.ScreenGasVent;
import electrodynamics.client.screen.tile.ScreenHydroelectricGenerator;
import electrodynamics.client.screen.tile.ScreenMineralWasher;
import electrodynamics.client.screen.tile.ScreenMotorComplex;
import electrodynamics.client.screen.tile.ScreenO2OProcessor;
import electrodynamics.client.screen.tile.ScreenO2OProcessorDouble;
import electrodynamics.client.screen.tile.ScreenO2OProcessorTriple;
import electrodynamics.client.screen.tile.ScreenPotentiometer;
import electrodynamics.client.screen.tile.ScreenQuarry;
import electrodynamics.client.screen.tile.ScreenSeismicRelay;
import electrodynamics.client.screen.tile.ScreenSolarPanel;
import electrodynamics.client.screen.tile.ScreenThermoelectricManipulator;
import electrodynamics.client.screen.tile.ScreenWindmill;
import electrodynamics.client.texture.atlas.AtlasHolderElectrodynamicsCustom;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.item.gear.tools.electric.ItemElectricBaton;
import electrodynamics.common.item.gear.tools.electric.ItemElectricChainsaw;
import electrodynamics.common.item.gear.tools.electric.ItemElectricDrill;
import electrodynamics.compatibility.mekanism.MekanismClientHandler;
import electrodynamics.registers.ElectrodynamicsEntities;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import electrodynamics.registers.ElectrodynamicsParticles;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.ModelEvent.RegisterAdditional;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientRegister {

    // sometimes I fucking hate this game
    public static final LayerDefinition COMPOSITE_ARMOR_LAYER_LEG_NOCHEST = ModelCompositeArmor.createBodyLayer(1, true);
    public static final LayerDefinition COMPOSITE_ARMOR_LAYER_BOOTS = ModelCompositeArmor.createBodyLayer(2, false);
    public static final LayerDefinition COMPOSITE_ARMOR_LAYER_COMB_NOCHEST = ModelCompositeArmor.createBodyLayer(3, true);
    public static final LayerDefinition COMPOSITE_ARMOR_LAYER_LEG_CHEST = ModelCompositeArmor.createBodyLayer(1, false);
    public static final LayerDefinition COMPOSITE_ARMOR_LAYER_COMB_CHEST = ModelCompositeArmor.createBodyLayer(3, false);

    public static final LayerDefinition NIGHT_VISION_GOGGLES = ModelNightVisionGoggles.createBodyLayer();

    public static final LayerDefinition HYDRAULIC_BOOTS = ModelHydraulicBoots.createBodyLayer();

    public static final LayerDefinition JETPACK = ModelJetpack.createBodyLayer();

    public static final LayerDefinition SERVO_LEGGINGS = ModelServoLeggings.createBodyLayer();

    public static final LayerDefinition COMBAT_ARMOR_LAYER_LEG_NOCHEST = ModelCombatArmor.createBodyLayer(1, true);
    public static final LayerDefinition COMBAT_ARMOR_LAYER_BOOTS = ModelCombatArmor.createBodyLayer(2, false);
    public static final LayerDefinition COMBAT_ARMOR_LAYER_COMB_NOCHEST = ModelCombatArmor.createBodyLayer(3, true);
    public static final LayerDefinition COMBAT_ARMOR_LAYER_LEG_CHEST = ModelCombatArmor.createBodyLayer(1, false);
    public static final LayerDefinition COMBAT_ARMOR_LAYER_COMB_CHEST = ModelCombatArmor.createBodyLayer(3, false);


    public static final ResourceLocation ON = Electrodynamics.vanillarl("on");

    public static final ModelResourceLocation MODEL_ADVSOLARTOP = ModelResourceLocation.standalone(Electrodynamics.rl("block/advancedsolarpaneltop"));
    public static final ModelResourceLocation MODEL_BATTERYBOX = ModelResourceLocation.standalone(Electrodynamics.rl("block/batterybox"));
    public static final ModelResourceLocation MODEL_BATTERYBOX2 = ModelResourceLocation.standalone(Electrodynamics.rl("block/batterybox2"));
    public static final ModelResourceLocation MODEL_BATTERYBOX3 = ModelResourceLocation.standalone(Electrodynamics.rl("block/batterybox3"));
    public static final ModelResourceLocation MODEL_BATTERYBOX4 = ModelResourceLocation.standalone(Electrodynamics.rl("block/batterybox4"));
    public static final ModelResourceLocation MODEL_BATTERYBOX5 = ModelResourceLocation.standalone(Electrodynamics.rl("block/batterybox5"));
    public static final ModelResourceLocation MODEL_BATTERYBOX6 = ModelResourceLocation.standalone(Electrodynamics.rl("block/batterybox6"));
    public static final ModelResourceLocation MODEL_BATTERYBOX7 = ModelResourceLocation.standalone(Electrodynamics.rl("block/batterybox7"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX = ModelResourceLocation.standalone(Electrodynamics.rl("block/lithiumbatterybox"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX2 = ModelResourceLocation.standalone(Electrodynamics.rl("block/lithiumbatterybox2"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX3 = ModelResourceLocation.standalone(Electrodynamics.rl("block/lithiumbatterybox3"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX4 = ModelResourceLocation.standalone(Electrodynamics.rl("block/lithiumbatterybox4"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX5 = ModelResourceLocation.standalone(Electrodynamics.rl("block/lithiumbatterybox5"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX6 = ModelResourceLocation.standalone(Electrodynamics.rl("block/lithiumbatterybox6"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX7 = ModelResourceLocation.standalone(Electrodynamics.rl("block/lithiumbatterybox7"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX = ModelResourceLocation.standalone(Electrodynamics.rl("block/carbynebatterybox"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX2 = ModelResourceLocation.standalone(Electrodynamics.rl("block/carbynebatterybox2"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX3 = ModelResourceLocation.standalone(Electrodynamics.rl("block/carbynebatterybox3"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX4 = ModelResourceLocation.standalone(Electrodynamics.rl("block/carbynebatterybox4"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX5 = ModelResourceLocation.standalone(Electrodynamics.rl("block/carbynebatterybox5"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX6 = ModelResourceLocation.standalone(Electrodynamics.rl("block/carbynebatterybox6"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX7 = ModelResourceLocation.standalone(Electrodynamics.rl("block/carbynebatterybox7"));
    public static final ModelResourceLocation MODEL_CHEMICALMIXERBASE = ModelResourceLocation.standalone(Electrodynamics.rl("block/chemicalmixerbase"));
    public static final ModelResourceLocation MODEL_HYDROELECTRICGENERATORBLADES = ModelResourceLocation.standalone(Electrodynamics.rl("block/hydroelectricgeneratorblades"));
    public static final ModelResourceLocation MODEL_WINDMILLBLADES = ModelResourceLocation.standalone(Electrodynamics.rl("block/windmillblades"));
    public static final ModelResourceLocation MODEL_MINERALCRUSHERHANDLE = ModelResourceLocation.standalone(Electrodynamics.rl("block/mineralcrusherhandle"));
    public static final ModelResourceLocation MODEL_MINERALCRUSHERDOUBLEHANDLE = ModelResourceLocation.standalone(Electrodynamics.rl("block/mineralcrusherdoublehandle"));
    public static final ModelResourceLocation MODEL_MINERALCRUSHERTRIPLEHANDLE = ModelResourceLocation.standalone(Electrodynamics.rl("block/mineralcrushertriplehandle"));
    public static final ModelResourceLocation MODEL_MINERALGRINDERWHEEL = ModelResourceLocation.standalone(Electrodynamics.rl("block/mineralgrinderwheel"));
    public static final ModelResourceLocation MODEL_CHEMICALMIXERBLADES = ModelResourceLocation.standalone(Electrodynamics.rl("block/chemicalmixerblades"));
    public static final ModelResourceLocation MODEL_LATHESHAFT = ModelResourceLocation.standalone(Electrodynamics.rl("block/latheshaft"));
    public static final ModelResourceLocation MODEL_MOTORCOMPLEXROTOR = ModelResourceLocation.standalone(Electrodynamics.rl("block/motorcomplexrotor"));
    public static final ModelResourceLocation MODEL_CHEMICALREACTOR_ROTOR = ModelResourceLocation.standalone(Electrodynamics.rl("block/chemicalreactormodelrotor"));

    public static final ModelResourceLocation MODEL_RODSTEEL = ModelResourceLocation.standalone(Electrodynamics.rl("entity/rodsteel"));
    public static final ModelResourceLocation MODEL_RODSTAINLESSSTEEL = ModelResourceLocation.standalone(Electrodynamics.rl("entity/rodstainlesssteel"));
    public static final ModelResourceLocation MODEL_RODHSLASTEEL = ModelResourceLocation.standalone(Electrodynamics.rl("entity/rodhslasteel"));

    public static final ModelResourceLocation MODEL_QUARRYWHEEL_STILL = ModelResourceLocation.standalone(Electrodynamics.rl("block/quarrywheelstill"));
    public static final ModelResourceLocation MODEL_QUARRYWHEEL_ROT = ModelResourceLocation.standalone(Electrodynamics.rl("block/quarrywheelrot"));

    // Custom Textures
    public static final ResourceLocation TEXTURE_WHITE = Electrodynamics.forgerl("white");
    public static final ResourceLocation TEXTURE_QUARRYARM = Electrodynamics.rl("block/custom/quarryarm");
    public static final ResourceLocation TEXTURE_QUARRYARM_DARK = Electrodynamics.rl("block/custom/quarrydark");
    public static final ResourceLocation TEXTURE_MERCURY = Electrodynamics.rl("block/custom/mercury");
    public static final ResourceLocation TEXTURE_GAS = Electrodynamics.rl("block/custom/gastexture");
    public static final ResourceLocation TEXTURE_MULTISUBNODE = Electrodynamics.rl("block/multisubnode");

    private static final String MULTIBLOCK_API_MODEL_FOLDER = "multiblockmodels";

    private static final HashMap<ResourceLocation, TextureAtlasSprite> CACHED_TEXTUREATLASSPRITES = new HashMap<>();
    // for registration purposes only!
    private static final List<ResourceLocation> CUSTOM_TEXTURES = List.of(ClientRegister.TEXTURE_WHITE, ClientRegister.TEXTURE_MERCURY, ClientRegister.TEXTURE_QUARRYARM, ClientRegister.TEXTURE_QUARRYARM_DARK, ClientRegister.TEXTURE_GAS, ClientRegister.TEXTURE_MULTISUBNODE);

    public static void setup() {
        ClientEvents.init();

        ItemProperties.register(ElectrodynamicsItems.ITEM_ELECTRICBATON.get(), ON, (stack, world, entity, call) -> entity != null && (entity.getMainHandItem() == stack || entity.getOffhandItem() == stack) && ((ItemElectricBaton) stack.getItem()).getJoulesStored(stack) > ((ItemElectricBaton) stack.getItem()).getElectricProperties().extract.getJoules() ? 1 : 0);
        ItemProperties.register(ElectrodynamicsItems.ITEM_ELECTRICDRILL.get(), ON, (stack, world, entity, call) -> entity != null && (entity.getMainHandItem() == stack || entity.getOffhandItem() == stack) && ((ItemElectricDrill) stack.getItem()).getJoulesStored(stack) > ((ItemElectricDrill) stack.getItem()).getElectricProperties().extract.getJoules() ? 1 : 0);
        ItemProperties.register(ElectrodynamicsItems.ITEM_ELECTRICCHAINSAW.get(), ON, (stack, world, entity, call) -> entity != null && (entity.getMainHandItem() == stack || entity.getOffhandItem() == stack) && ((ItemElectricChainsaw) stack.getItem()).getJoulesStored(stack) > ((ItemElectricChainsaw) stack.getItem()).getElectricProperties().extract.getJoules() ? 1 : 0);

        ScreenGuidebook.addGuidebookModule(new ModuleElectrodynamics());
    }

    @SubscribeEvent
    public static void onModelEvent(RegisterAdditional event) {
        event.register(MODEL_ADVSOLARTOP);
        event.register(MODEL_BATTERYBOX);
        event.register(MODEL_BATTERYBOX2);
        event.register(MODEL_BATTERYBOX3);
        event.register(MODEL_BATTERYBOX4);
        event.register(MODEL_BATTERYBOX5);
        event.register(MODEL_BATTERYBOX6);
        event.register(MODEL_BATTERYBOX7);
        event.register(MODEL_LITHIUMBATTERYBOX);
        event.register(MODEL_LITHIUMBATTERYBOX2);
        event.register(MODEL_LITHIUMBATTERYBOX3);
        event.register(MODEL_LITHIUMBATTERYBOX4);
        event.register(MODEL_LITHIUMBATTERYBOX5);
        event.register(MODEL_LITHIUMBATTERYBOX6);
        event.register(MODEL_LITHIUMBATTERYBOX7);
        event.register(MODEL_CHEMICALMIXERBASE);
        event.register(MODEL_CARBYNEBATTERYBOX);
        event.register(MODEL_CARBYNEBATTERYBOX2);
        event.register(MODEL_CARBYNEBATTERYBOX3);
        event.register(MODEL_CARBYNEBATTERYBOX4);
        event.register(MODEL_CARBYNEBATTERYBOX5);
        event.register(MODEL_CARBYNEBATTERYBOX6);
        event.register(MODEL_CARBYNEBATTERYBOX7);
        event.register(MODEL_HYDROELECTRICGENERATORBLADES);
        event.register(MODEL_WINDMILLBLADES);
        event.register(MODEL_MINERALCRUSHERHANDLE);
        event.register(MODEL_MINERALCRUSHERDOUBLEHANDLE);
        event.register(MODEL_MINERALCRUSHERTRIPLEHANDLE);
        event.register(MODEL_MINERALGRINDERWHEEL);
        event.register(MODEL_CHEMICALMIXERBLADES);
        event.register(MODEL_RODSTEEL);
        event.register(MODEL_RODSTAINLESSSTEEL);
        event.register(MODEL_RODHSLASTEEL);
        event.register(MODEL_LATHESHAFT);
        event.register(MODEL_MOTORCOMPLEXROTOR);
        event.register(MODEL_QUARRYWHEEL_STILL);
        event.register(MODEL_QUARRYWHEEL_ROT);
        event.register(MODEL_CHEMICALREACTOR_ROTOR);

        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        FileToIdConverter converter = FileToIdConverter.json("models/" + MULTIBLOCK_API_MODEL_FOLDER);
        converter.listMatchingResources(manager).forEach((location, resource) -> event.register(ModelResourceLocation.standalone(converter.fileToId(location).withPrefix(MULTIBLOCK_API_MODEL_FOLDER + "/"))));
    }

    @SubscribeEvent
    public static void registerMenus(RegisterMenuScreensEvent event) {
        event.register(ElectrodynamicsMenuTypes.CONTAINER_COALGENERATOR.get(), ScreenCoalGenerator::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICFURNACE.get(), ScreenElectricFurnace::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICFURNACEDOUBLE.get(), ScreenElectricFurnaceDouble::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICFURNACETRIPLE.get(), ScreenElectricFurnaceTriple::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICARCFURNACE.get(), ScreenElectricArcFurnace::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICARCFURNACEDOUBLE.get(), ScreenElectricArcFurnaceDouble::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICARCFURNACETRIPLE.get(), ScreenElectricArcFurnaceTriple::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_O2OPROCESSOR.get(), ScreenO2OProcessor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_O2OPROCESSORDOUBLE.get(), ScreenO2OProcessorDouble::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_O2OPROCESSORTRIPLE.get(), ScreenO2OProcessorTriple::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_DO2OPROCESSOR.get(), ScreenDO2OProcessor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_BATTERYBOX.get(), ScreenBatteryBox::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_FERMENTATIONPLANT.get(), ScreenFermentationPlant::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_MINERALWASHER.get(), ScreenMineralWasher::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CHEMICALMIXER.get(), ScreenChemicalMixer::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CHEMICALCRYSTALLIZER.get(), ScreenChemicalCrystallizer::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CHARGER.get(), ScreenChargerGeneric::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_TANK.get(), ScreenFluidTankGeneric::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_COMBUSTION_CHAMBER.get(), ScreenCombustionChamber::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_SOLARPANEL.get(), ScreenSolarPanel::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_WINDMILL.get(), ScreenWindmill::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_HYDROELECTRICGENERATOR.get(), ScreenHydroelectricGenerator::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CREATIVEPOWERSOURCE.get(), ScreenCreativePowerSource::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CREATIVEFLUIDSOURCE.get(), ScreenCreativeFluidSource::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_FLUIDVOID.get(), ScreenFluidVoid::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_SEISMICSCANNER.get(), ScreenSeismicScanner::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTROLYTICSEPARATOR.get(), ScreenElectrolyticSeparator::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_SEISMICRELAY.get(), ScreenSeismicRelay::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_COOLANTRESAVOIR.get(), ScreenCoolantResavoir::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_MOTORCOMPLEX.get(), ScreenMotorComplex::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_QUARRY.get(), ScreenQuarry::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_GUIDEBOOK.get(), ScreenGuidebook::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_GASTANK.get(), ScreenGasTankGeneric::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_COMPRESSOR.get(), ScreenCompressor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_DECOMPRESSOR.get(), ScreenDecompressor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ADVANCEDCOMPRESSOR.get(), ScreenAdvancedCompressor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ADVANCEDDECOMPRESSOR.get(), ScreenAdvancedDecompressor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_GASVENT.get(), ScreenGasVent::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_THERMOELECTRICMANIPULATOR.get(), ScreenThermoelectricManipulator::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_GASPIPEPUMP.get(), ScreenGasPipePump::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_FLUIDPIPEPUMP.get(), ScreenFluidPipePump::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_GASPIPEFILTER.get(), ScreenGasPipeFilter::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_FLUIDPIPEFILTER.get(), ScreenFluidPipeFilter::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICDRILL.get(), ScreenElectricDrill::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_POTENTIOMETER.get(), ScreenPotentiometer::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ADVANCEDDOWNGRADETRANSFORMER.get(), ScreenAdvancedDowngradeTransformer::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ADVANCEDUPGRADETRANSFORMER.get(), ScreenAdvancedUpgradeTransformer::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CIRCUITMONITOR.get(), ScreenCircuitMonitor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_GASCOLLECTOR.get(), ScreenGasCollector::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CHEMICALREACTOR.get(), ScreenChemicalReactor::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_CREATIVEGASSOURCE.get(), ScreenCreativeGasSource::new);
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ELECTROLOSISCHAMBER.get(), ScreenElectrolosisChamber::new);

        if(ModList.get().isLoaded(References.MEKANISM_ID)) {
            MekanismClientHandler.registerMenus(event);
        }
    }

    @SubscribeEvent
    public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ElectrodynamicsEntities.ENTITY_ENERGYBLAST.get(), RenderEnergyBlast::new);
        event.registerEntityRenderer(ElectrodynamicsEntities.ENTITY_METALROD.get(), RenderMetalRod::new);

        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_ADVANCEDSOLARPANEL.get(), RenderAdvancedSolarPanel::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_BATTERYBOX.get(), RenderBatteryBox::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_LITHIUMBATTERYBOX.get(), RenderLithiumBatteryBox::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_CARBYNEBATTERYBOX.get(), RenderCarbyneBatteryBox::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_HYDROELECTRICGENERATOR.get(), RenderHydroelectricGenerator::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_WINDMILL.get(), RenderWindmill::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MINERALCRUSHER.get(), RenderMineralCrusher::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MINERALCRUSHERDOUBLE.get(), RenderMineralCrusherDouble::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MINERALCRUSHERTRIPLE.get(), RenderMineralCrusherTriple::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MINERALGRINDER.get(), RenderMineralGrinder::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MINERALGRINDERDOUBLE.get(), RenderMineralGrinderDouble::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MINERALGRINDERTRIPLE.get(), RenderMineralGrinderTriple::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_FERMENTATIONPLANT.get(), RenderFermentationPlant::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_COMBUSTIONCHAMBER.get(), RenderCombustionChamber::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MINERALWASHER.get(), RenderMineralWasher::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_CHEMICALMIXER.get(), RenderChemicalMixer::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MULTIMETERBLOCK.get(), RenderMultimeterBlock::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_LATHE.get(), RenderLathe::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_CHARGERLV.get(), RenderChargerGeneric::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_CHARGERMV.get(), RenderChargerGeneric::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_CHARGERHV.get(), RenderChargerGeneric::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_SEISMICRELAY.get(), RenderSeismicRelay::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_COOLANTRESAVOIR.get(), RenderCoolantResavoir::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_TANKHSLA.get(), RenderTankGeneric::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_TANKREINFORCED.get(), RenderTankGeneric::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_TANKSTEEL.get(), RenderTankGeneric::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_MOTORCOMPLEX.get(), RenderMotorComplex::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_ELECTROLYTICSEPARATOR.get(), RenderElectrolyticSeparator::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_GASPIPEPUMP.get(), RenderGasPipePump::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_FLUIDPIPEPUMP.get(), RenderFluidPipePump::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_LOGISTICALWIRE.get(), RenderLogisticalWire::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_ELECTROLOSISCHAMBER.get(), RenderElectrolosisChamber::new);

        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_WIRE.get(), RenderConnectBlock::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_LOGISTICALWIRE.get(), RenderConnectBlock::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_PIPE.get(), RenderConnectBlock::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_GAS_PIPE.get(), RenderConnectBlock::new);

        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_ADVANCEDUPGRADETRANSFORMER.get(), RenderAdvancedTransformer.RenderAdvancedUpgradeTransformer::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_ADVANCEDDOWNGRADETRANSFORMER.get(), RenderAdvancedTransformer.RenderAdvancedDowngradeTransformer::new);

        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_CHEMICALREACTOR.get(), RenderChemicalReactor::new);
    }

    @SubscribeEvent
    public static void cacheCustomTextureAtlases(TextureAtlasStitchedEvent event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            CACHED_TEXTUREATLASSPRITES.clear();
            for (ResourceLocation loc : CUSTOM_TEXTURES) {
                ClientRegister.CACHED_TEXTUREATLASSPRITES.put(loc, event.getAtlas().getSprite(loc));
            }
        }
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ElectrodynamicsParticles.PARTICLE_PLASMA_BALL.get(), ParticlePlasmaBall.Factory::new);
        event.registerSpriteSet(ElectrodynamicsParticles.PARTICLE_LAVAWITHPHYSICS.get(), ParticleLavaWithPhysics.Factory::new);
        event.registerSpriteSet(ElectrodynamicsParticles.PARTICLE_FLUIDDROP.get(), ParticleFluidDrop.Factory::new);
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(AtlasHolderElectrodynamicsCustom.INSTANCE = new AtlasHolderElectrodynamicsCustom(Minecraft.getInstance().getTextureManager()));
        event.registerReloadListener(new ReloadListenerResetGuidebook());
    }

    @SubscribeEvent
    public static void registerGeometryLoaders(final ModelEvent.RegisterGeometryLoaders event) {
        event.register(CableModelLoader.ID, CableModelLoader.INSTANCE);
        event.register(SlaveNodeModelLoader.ID, SlaveNodeModelLoader.INSTANCE);
        event.register(MultiblockModelLoader.ID, MultiblockModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItemDecorators(final RegisterItemDecorationsEvent event) {
        event.register(ElectrodynamicsItems.ITEM_COMBATHELMET.get(), new ItemDecoratorCombatHelmet());
        event.register(ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get(), new ItemDecoratorCombatLeggings());

        event.register(ElectrodynamicsItems.ITEM_KINETICRAILGUN.get(), new ItemDecoratorRailgun());
        event.register(ElectrodynamicsItems.ITEM_PLASMARAILGUN.get(), new ItemDecoratorRailgun());
    }

    @SubscribeEvent
    public static void registerClientExtensions(final RegisterClientExtensionsEvent event) {

        /* ITEMS */

        //Combat Armor
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {
                ItemStack[] armorPiecesArray = new ItemStack[]{new ItemStack(ElectrodynamicsItems.ITEM_COMBATHELMET.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATBOOTS.get())};

                List<ItemStack> armorPieces = new ArrayList<>();
                entity.getArmorSlots().forEach(armorPieces::add);

                boolean isBoth = armorPieces.get(0).getItem() == armorPiecesArray[3].getItem() && armorPieces.get(1).getItem() == armorPiecesArray[2].getItem();

                boolean hasChest = armorPieces.get(2).getItem() == armorPiecesArray[1].getItem();

                ModelCombatArmor<LivingEntity> model;

                if (isBoth) {
                    if (hasChest) {
                        model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_COMB_CHEST.bakeRoot(), armorSlot);
                    } else {
                        model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_COMB_NOCHEST.bakeRoot(), armorSlot);
                    }
                } else if (armorSlot == EquipmentSlot.FEET) {
                    model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_BOOTS.bakeRoot(), armorSlot);
                } else if (hasChest) {
                    model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_LEG_CHEST.bakeRoot(), armorSlot);
                } else {
                    model = new ModelCombatArmor<>(ClientRegister.COMBAT_ARMOR_LAYER_LEG_NOCHEST.bakeRoot(), armorSlot);
                }

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        }, ElectrodynamicsItems.ITEM_COMBATHELMET, ElectrodynamicsItems.ITEM_COMBATCHESTPLATE, ElectrodynamicsItems.ITEM_COMBATLEGGINGS, ElectrodynamicsItems.ITEM_COMBATBOOTS);

        //Composite Armor
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {

                ItemStack[] armorPiecesArray = new ItemStack[]{new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITEHELMET.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITEBOOTS.get())};

                List<ItemStack> armorPieces = new ArrayList<>();
                entity.getArmorSlots().forEach(armorPieces::add);

                boolean isBoth = armorPieces.get(0).getItem() == armorPiecesArray[3].getItem() && armorPieces.get(1).getItem() == armorPiecesArray[2].getItem();

                boolean hasChest = armorPieces.get(2).getItem() == armorPiecesArray[1].getItem();

                ModelCompositeArmor<LivingEntity> model;

                if (isBoth) {
                    if (hasChest) {
                        model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_COMB_CHEST.bakeRoot(), armorSlot);
                    } else {
                        model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_COMB_NOCHEST.bakeRoot(), armorSlot);
                    }
                } else if (armorSlot == EquipmentSlot.FEET) {
                    model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_BOOTS.bakeRoot(), armorSlot);
                } else if (hasChest) {
                    model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_LEG_CHEST.bakeRoot(), armorSlot);
                } else {
                    model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_LEG_NOCHEST.bakeRoot(), armorSlot);
                }

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        }, ElectrodynamicsItems.ITEM_COMPOSITEHELMET, ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE, ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS, ElectrodynamicsItems.ITEM_COMPOSITEBOOTS);

        //Night Vision Goggles
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {
                ModelNightVisionGoggles<LivingEntity> model = new ModelNightVisionGoggles<>(ClientRegister.NIGHT_VISION_GOGGLES.bakeRoot());

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        }, ElectrodynamicsItems.ITEM_NIGHTVISIONGOGGLES);

        //Jetpack
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {

                ModelJetpack<LivingEntity> model = new ModelJetpack<>(ClientRegister.JETPACK.bakeRoot());

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        }, ElectrodynamicsItems.ITEM_JETPACK);

        //Servo Leggings
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {

                ModelServoLeggings<LivingEntity> model = new ModelServoLeggings<>(ClientRegister.SERVO_LEGGINGS.bakeRoot());

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        }, ElectrodynamicsItems.ITEM_SERVOLEGGINGS);

        //Hydraulic Boots
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {

                ModelHydraulicBoots<LivingEntity> model = new ModelHydraulicBoots<>(ClientRegister.HYDRAULIC_BOOTS.bakeRoot());

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        }, ElectrodynamicsItems.ITEM_HYDRAULICBOOTS);

        /* FLUIDS */

        ElectrodynamicsFluids.FLUIDS.getEntries().forEach(fluid -> {
            event.registerFluidType(new SWBFClientExtensions((SimpleWaterBasedFluidType) fluid.get().getFluidType()), fluid.get().getFluidType());
        });


    }

    public static TextureAtlasSprite getSprite(ResourceLocation sprite) {
        return CACHED_TEXTUREATLASSPRITES.getOrDefault(sprite, CACHED_TEXTUREATLASSPRITES.get(TEXTURE_WHITE));
    }

}
