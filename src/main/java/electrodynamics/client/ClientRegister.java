package electrodynamics.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.client.guidebook.ModuleElectrodynamics;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.modelbakers.bakerytypes.CableModelLoader;
import electrodynamics.client.particle.fluiddrop.ParticleFluidDrop;
import electrodynamics.client.particle.lavawithphysics.ParticleLavaWithPhysics;
import electrodynamics.client.particle.plasmaball.ParticlePlasmaBall;
import electrodynamics.client.reloadlistener.ReloadListenerResetGuidebook;
import electrodynamics.client.render.entity.RenderEnergyBlast;
import electrodynamics.client.render.entity.RenderMetalRod;
import electrodynamics.client.render.model.armor.types.ModelCombatArmor;
import electrodynamics.client.render.model.armor.types.ModelCompositeArmor;
import electrodynamics.client.render.model.armor.types.ModelHydraulicBoots;
import electrodynamics.client.render.model.armor.types.ModelJetpack;
import electrodynamics.client.render.model.armor.types.ModelNightVisionGoggles;
import electrodynamics.client.render.model.armor.types.ModelServoLeggings;
import electrodynamics.client.render.tile.*;
import electrodynamics.client.screen.item.ScreenElectricDrill;
import electrodynamics.client.screen.item.ScreenSeismicScanner;
import electrodynamics.client.screen.tile.*;
import electrodynamics.client.texture.atlas.AtlasHolderElectrodynamicsCustom;
import electrodynamics.common.item.gear.tools.electric.ItemElectricBaton;
import electrodynamics.common.item.gear.tools.electric.ItemElectricChainsaw;
import electrodynamics.common.item.gear.tools.electric.ItemElectricDrill;
import electrodynamics.registers.ElectrodynamicsTiles;
import electrodynamics.registers.ElectrodynamicsEntities;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import electrodynamics.registers.ElectrodynamicsParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.event.ModelEvent.RegisterAdditional;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class ClientRegister {

    private static final String BLOCK_LOC = References.ID + ":block/";

    // sometimes I fucking hate this game
    public static LayerDefinition COMPOSITE_ARMOR_LAYER_LEG_NOCHEST = ModelCompositeArmor.createBodyLayer(1, true);
    public static LayerDefinition COMPOSITE_ARMOR_LAYER_BOOTS = ModelCompositeArmor.createBodyLayer(2, false);
    public static LayerDefinition COMPOSITE_ARMOR_LAYER_COMB_NOCHEST = ModelCompositeArmor.createBodyLayer(3, true);
    public static LayerDefinition COMPOSITE_ARMOR_LAYER_LEG_CHEST = ModelCompositeArmor.createBodyLayer(1, false);
    public static LayerDefinition COMPOSITE_ARMOR_LAYER_COMB_CHEST = ModelCompositeArmor.createBodyLayer(3, false);

    public static LayerDefinition NIGHT_VISION_GOGGLES = ModelNightVisionGoggles.createBodyLayer();

    public static LayerDefinition HYDRAULIC_BOOTS = ModelHydraulicBoots.createBodyLayer();

    public static LayerDefinition JETPACK = ModelJetpack.createBodyLayer();

    public static LayerDefinition SERVO_LEGGINGS = ModelServoLeggings.createBodyLayer();

    public static LayerDefinition COMBAT_ARMOR_LAYER_LEG_NOCHEST = ModelCombatArmor.createBodyLayer(1, true);
    public static LayerDefinition COMBAT_ARMOR_LAYER_BOOTS = ModelCombatArmor.createBodyLayer(2, false);
    public static LayerDefinition COMBAT_ARMOR_LAYER_COMB_NOCHEST = ModelCombatArmor.createBodyLayer(3, true);
    public static LayerDefinition COMBAT_ARMOR_LAYER_LEG_CHEST = ModelCombatArmor.createBodyLayer(1, false);
    public static LayerDefinition COMBAT_ARMOR_LAYER_COMB_CHEST = ModelCombatArmor.createBodyLayer(3, false);

    public static HashMap<ResourceLocation, TextureAtlasSprite> CACHED_TEXTUREATLASSPRITES = new HashMap<>();
    // for registration purposes only!
    private static final List<ResourceLocation> CUSTOM_TEXTURES = new ArrayList<>();

    public static final ResourceLocation ON = ResourceLocation.withDefaultNamespace("on");

    public static final ModelResourceLocation MODEL_ADVSOLARTOP = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "advancedsolarpaneltop"));
    public static final ModelResourceLocation MODEL_BATTERYBOX = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "batterybox"));
    public static final ModelResourceLocation MODEL_BATTERYBOX2 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "batterybox2"));
    public static final ModelResourceLocation MODEL_BATTERYBOX3 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "batterybox3"));
    public static final ModelResourceLocation MODEL_BATTERYBOX4 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "batterybox4"));
    public static final ModelResourceLocation MODEL_BATTERYBOX5 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "batterybox5"));
    public static final ModelResourceLocation MODEL_BATTERYBOX6 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "batterybox6"));
    public static final ModelResourceLocation MODEL_BATTERYBOX7 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "batterybox7"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "lithiumbatterybox"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX2 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "lithiumbatterybox2"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX3 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "lithiumbatterybox3"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX4 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "lithiumbatterybox4"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX5 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "lithiumbatterybox5"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX6 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "lithiumbatterybox6"));
    public static final ModelResourceLocation MODEL_LITHIUMBATTERYBOX7 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "lithiumbatterybox7"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "carbynebatterybox"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX2 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "carbynebatterybox2"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX3 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "carbynebatterybox3"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX4 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "carbynebatterybox4"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX5 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "carbynebatterybox5"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX6 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "carbynebatterybox6"));
    public static final ModelResourceLocation MODEL_CARBYNEBATTERYBOX7 = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "carbynebatterybox7"));
    public static final ModelResourceLocation MODEL_CHEMICALMIXERBASE = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "chemicalmixerbase"));
    public static final ModelResourceLocation MODEL_HYDROELECTRICGENERATORBLADES = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "hydroelectricgeneratorblades"));
    public static final ModelResourceLocation MODEL_WINDMILLBLADES = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "windmillblades"));
    public static final ModelResourceLocation MODEL_MINERALCRUSHERHANDLE = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "mineralcrusherhandle"));
    public static final ModelResourceLocation MODEL_MINERALCRUSHERDOUBLEHANDLE = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "mineralcrusherdoublehandle"));
    public static final ModelResourceLocation MODEL_MINERALCRUSHERTRIPLEHANDLE = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "mineralcrushertriplehandle"));
    public static final ModelResourceLocation MODEL_MINERALGRINDERWHEEL = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "mineralgrinderwheel"));
    public static final ModelResourceLocation MODEL_CHEMICALMIXERBLADES = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "chemicalmixerblades"));
    public static final ModelResourceLocation MODEL_LATHESHAFT = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "latheshaft"));
    public static final ModelResourceLocation MODEL_MOTORCOMPLEXROTOR = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "motorcomplexrotor"));
    public static final ModelResourceLocation MODEL_CHEMICALREACTOR_ROTOR = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "chemicalreactormodelrotor"));

    public static final ModelResourceLocation MODEL_RODSTEEL = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/rodsteel"));
    public static final ModelResourceLocation MODEL_RODSTAINLESSSTEEL = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/rodstainlesssteel"));
    public static final ModelResourceLocation MODEL_RODHSLASTEEL = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/rodhslasteel"));

    public static final ModelResourceLocation MODEL_QUARRYWHEEL_STILL = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "quarrywheelstill"));
    public static final ModelResourceLocation MODEL_QUARRYWHEEL_ROT = ModelResourceLocation.standalone(ResourceLocation.parse(BLOCK_LOC + "quarrywheelrot"));

    public static final ResourceLocation TEXTURE_RODSTEEL = ResourceLocation.parse(References.ID + ":textures/entity/projectile/rodsteel.png");
    public static final ResourceLocation TEXTURE_RODSTAINLESSSTEEL = ResourceLocation.parse(References.ID + ":textures/entity/projectile/rodstainlesssteel.png");
    public static final ResourceLocation TEXTURE_RODHSLASTEEL = ResourceLocation.parse(References.ID + ":textures/entity/projectile/rodhslasteel.png");

    // Custom Textures
    public static final ResourceLocation TEXTURE_WHITE = ResourceLocation.fromNamespaceAndPath("forge", "white");
    public static final ResourceLocation TEXTURE_QUARRYARM = ResourceLocation.fromNamespaceAndPath(References.ID, "block/custom/quarryarm");
    public static final ResourceLocation TEXTURE_QUARRYARM_DARK = ResourceLocation.fromNamespaceAndPath(References.ID, "block/custom/quarrydark");
    public static final ResourceLocation TEXTURE_MERCURY = ResourceLocation.fromNamespaceAndPath(References.ID, "block/custom/mercury");
    public static final ResourceLocation TEXTURE_GAS = ResourceLocation.fromNamespaceAndPath(References.ID, "block/custom/gastexture");

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

        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_WIRE.get(), RenderConnectBlock::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_LOGISTICALWIRE.get(), RenderConnectBlock::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_PIPE.get(), RenderConnectBlock::new);
        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_GAS_PIPE.get(), RenderConnectBlock::new);

        event.registerBlockEntityRenderer(ElectrodynamicsTiles.TILE_CHEMICALREACTOR.get(), RenderChemicalReactor::new);
    }

    public static boolean shouldMultilayerRender(RenderType type) {
        return type == RenderType.translucent() || type == RenderType.solid();
    }

    static {
        CUSTOM_TEXTURES.add(ClientRegister.TEXTURE_WHITE);
        CUSTOM_TEXTURES.add(ClientRegister.TEXTURE_MERCURY);
        CUSTOM_TEXTURES.add(ClientRegister.TEXTURE_QUARRYARM);
        CUSTOM_TEXTURES.add(ClientRegister.TEXTURE_QUARRYARM_DARK);
        CUSTOM_TEXTURES.add(ClientRegister.TEXTURE_GAS);
    }

    @SubscribeEvent
    public static void cacheCustomTextureAtlases(TextureAtlasStitchedEvent event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
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
    }

}
