package electrodynamics.datagen.client;

import javax.annotation.Nullable;

import electrodynamics.api.References;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates.AddonTankNeighborType;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates.ManipulatorHeatingStatus;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.Conductor;
import electrodynamics.common.block.subtype.SubtypeWire.InsulationMaterial;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import electrodynamics.common.block.subtype.SubtypeWire.WireColor;
import electrodynamics.datagen.utils.model.WireModelBuilder;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ElectrodynamicsBlockStateProvider extends BlockStateProvider {

    public final String modID;

    public ElectrodynamicsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper, String modID) {
        super(output, modID, exFileHelper);
        this.modID = modID;
    }

    public ElectrodynamicsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        this(output, exFileHelper, References.ID);
    }

    @Override
    protected void registerStatesAndModels() {

        for (SubtypeGlass glass : SubtypeGlass.values()) {
            glassBlock(ElectrodynamicsBlocks.getBlock(glass), blockLoc("glass/" + glass.tag()), true);
        }

        for (SubtypeOre ore : SubtypeOre.values()) {
            simpleBlock(ElectrodynamicsBlocks.getBlock(ore), blockLoc("ore/" + ore.tag()), true);
        }

        for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
            simpleBlock(ElectrodynamicsBlocks.getBlock(ore), blockLoc("deepslateore/" + ore.tag()), true);
        }

        for (SubtypeRawOreBlock raw : SubtypeRawOreBlock.values()) {
            simpleBlock(ElectrodynamicsBlocks.getBlock(raw), blockLoc("raworeblock/" + raw.tag()), true);
        }

        for (SubtypeResourceBlock resource : SubtypeResourceBlock.values()) {
            simpleBlock(ElectrodynamicsBlocks.getBlock(resource), blockLoc("resource/" + resource.tag()), true);
        }

        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advancedsolarpanel), existingBlock(blockLoc("advancedsolarpanelbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.batterybox), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.batterybox)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.lithiumbatterybox), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.lithiumbatterybox)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.carbynebatterybox), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.carbynebatterybox)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerlv), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerlv)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargermv), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargermv)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerhv), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerhv)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalcrystallizer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalcrystallizer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalmixer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalmixer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.circuitbreaker), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.circuitbreaker)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.coalgenerator), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.coalgenerator)), existingBlock(blockLoc("coalgeneratorrunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.combustionchamber), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.combustionchamber)), true).transforms().transform(ItemDisplayContext.GUI).rotation(35, 40, 0).scale(0.665F).end();
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.coolantresavoir), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.coolantresavoir)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativefluidsource), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativefluidsource)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativepowersource), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativepowersource)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.downgradetransformer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.downgradetransformer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.upgradetransformer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.upgradetransformer)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnace), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnace)), existingBlock(blockLoc("electricfurnacerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacedouble), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacedouble)), existingBlock(blockLoc("electricfurnacedoublerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacetriple), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacetriple)), existingBlock(blockLoc("electricfurnacetriplerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnace), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnace)), existingBlock(blockLoc("electricarcfurnacerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacedouble), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacedouble)), existingBlock(blockLoc("electricarcfurnacedoublerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacetriple), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacetriple)), existingBlock(blockLoc("electricarcfurnacetriplerunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricpump), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricpump)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electrolyticseparator), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electrolyticseparator)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.energizedalloyer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.energizedalloyer)), existingBlock(blockLoc("energizedalloyerrunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.fermentationplant), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.fermentationplant)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.fluidvoid), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.fluidvoid)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.blockFrame, existingBlock(ElectrodynamicsBlocks.blockFrame), true);
        simpleBlock(ElectrodynamicsBlocks.blockFrameCorner, existingBlock(ElectrodynamicsBlocks.blockFrameCorner), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.hydroelectricgenerator), existingBlock(blockLoc("hydroelectricgeneratorengine")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.lathe), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.lathe)), true);
        wire(ElectrodynamicsBlocks.blockLogisticalManager, existingBlock(blockLoc("logisticalmanager_none")), existingBlock(blockLoc("logisticalmanager_inventory")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusher), existingBlock(blockLoc("mineralcrusherbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusherdouble), existingBlock(blockLoc("mineralcrusherdoublebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrushertriple), existingBlock(blockLoc("mineralcrushertriplebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinder), existingBlock(blockLoc("mineralgrinderbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinderdouble), existingBlock(blockLoc("mineralgrinderdoublebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrindertriple), existingBlock(blockLoc("mineralgrindertriplebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralwasher), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralwasher)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.motorcomplex), existingBlock(blockLoc("motorcomplexbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.multimeterblock), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.multimeterblock)), true);
        airBlock(ElectrodynamicsBlocks.multi, "block/multisubnode", false);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.oxidationfurnace), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.oxidationfurnace)), existingBlock(blockLoc("oxidationfurnacerunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.quarry), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.quarry)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.reinforcedalloyer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.reinforcedalloyer)), existingBlock(blockLoc("reinforcedalloyerrunning")), true);
        simpleBlock(ElectrodynamicsBlocks.blockSeismicMarker, existingBlock(ElectrodynamicsBlocks.blockSeismicMarker), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.seismicrelay), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.seismicrelay)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.solarpanel), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.solarpanel)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tanksteel), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tanksteel)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankreinforced), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankreinforced)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankhsla), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankhsla)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.thermoelectricgenerator), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.thermoelectricgenerator)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.windmill), existingBlock(blockLoc("windmillbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremill), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremill)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilldouble), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilldouble)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilltriple), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilltriple)), true);

        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastanksteel), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastanksteel)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankreinforced), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankreinforced)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankhsla), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankhsla)), true);

        horrRotatedBlock(ElectrodynamicsBlocks.blockCompressor, existingBlock(ElectrodynamicsBlocks.blockCompressor), 180, 0, false);
        horrRotatedBlock(ElectrodynamicsBlocks.blockDecompressor, existingBlock(ElectrodynamicsBlocks.blockDecompressor), 180, 0, false);
        genCompressorAddonTank();
        genCompressorSide();
        simpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gasvent), blockLoc("gasvent"), true);
        genThermoelectricManipulator();

        horrRotatedLitBlock(ElectrodynamicsBlocks.blockGasValve, existingBlock(ElectrodynamicsBlocks.blockGasValve), existingBlock(blockLoc("gasvalveon")), 90, 0, true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.blockFluidValve, existingBlock(ElectrodynamicsBlocks.blockFluidValve), existingBlock(blockLoc("fluidvalveon")), 90, 0, true);

        horrRotatedBlock(ElectrodynamicsBlocks.blockGasPipePump, existingBlock(ElectrodynamicsBlocks.blockGasPipePump), false);
        horrRotatedBlock(ElectrodynamicsBlocks.blockFluidPipePump, existingBlock(ElectrodynamicsBlocks.blockFluidPipePump), false);

        horrRotatedBlock(ElectrodynamicsBlocks.blockGasPipeFilter, existingBlock(ElectrodynamicsBlocks.blockGasPipeFilter), true);
        horrRotatedBlock(ElectrodynamicsBlocks.blockFluidPipeFilter, existingBlock(ElectrodynamicsBlocks.blockFluidPipeFilter), true);

        horrRotatedLitBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.relay), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.relay)), existingBlock(blockLoc("relayon")), 180, 0, true);

        glassBlock(ElectrodynamicsBlocks.blockSteelScaffold, blockLoc("steelscaffold"), true);

        simpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.potentiometer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.potentiometer)), true);

        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advanceddowngradetransformer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advanceddowngradetransformer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advancedupgradetransformer), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advancedupgradetransformer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.circuitmonitor), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.circuitmonitor)), 90, 0, true);

        horrRotatedBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.currentregulator), existingBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.currentregulator)), 180, 0, true);

        redstoneToggleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gascollector),
                models().cube(SubtypeMachine.gascollector.tag(), blockLoc("gascollector_bottom"), blockLoc("gascollector_fanoff"), blockLoc("gascollector_fanoff"), blockLoc("gascollector_back"), blockLoc("gascollector_fanoff"), blockLoc("gascollector_fanoff")).texture("particle", blockLoc("multisubnode")),
                models().cube(SubtypeMachine.gascollector.tag() + "on", blockLoc("gascollector_bottom"), blockLoc("gascollector_fanon"), blockLoc("gascollector_fanon"), blockLoc("gascollector_back"), blockLoc("gascollector_fanon"), blockLoc("gascollector_fanon")).texture("particle", blockLoc("multisubnode")),
                true);

        airBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalreactor), "block/steelcasing", false);

        genWires();
        genPipes();
        genGasPipes();

    }

    private void genWires() {

        String parent = "parent/";
        String name = "block/wire/";
        String texture = "wire/";

        // bare
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE)) {
            wire(ElectrodynamicsBlocks.getBlock(wire), models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "wire_none")).texture("conductor", blockLoc(texture + wire.conductor.toString())).texture("particle", "#conductor").renderType("cutout"), models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "wire_side")).texture("conductor", blockLoc(texture + wire.conductor.toString())).texture("particle", "#conductor").renderType("cutout"), false);
        }

        // insulated
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())) {
            wire(ElectrodynamicsBlocks.getBlock(wire), models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "insulatedwire_none")).texture("conductor", blockLoc(texture + wire.conductor.toString() + "_center")).texture("insulation", blockLoc(texture + "insulationwool_center")).texture("particle", "#insulation").renderType("cutout"), models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "insulatedwire_side")).texture("insulation", blockLoc(texture + "insulationwool")).texture("particle", "#insulation").renderType("cutout"), false);
        }

        // logistical
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())) {
            wire(ElectrodynamicsBlocks.getBlock(wire), models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "logisticswire_none")).texture("conductor", blockLoc(texture + "logisticswire" + wire.conductor.toString())).texture("insulation", blockLoc(texture + "logisticswireinsulation_center")).texture("particle", "#insulation").texture("redstone", blockLoc(texture + "logisticswireredstone_center")).renderType("cutout"), models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "logisticswire_side")).texture("insulation", blockLoc(texture + "logisticswireinsulation_side")).texture("particle", "#insulation").texture("redstone", blockLoc(texture + "logisticswireredstone_side")).renderType("cutout"), false);
        }

        // ceramic
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())) {
            wire(ElectrodynamicsBlocks.getBlock(wire), models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "ceramicinsulatedwire_none")).texture("conductor", blockLoc(texture + wire.conductor.toString() + "_center")).texture("insulation", blockLoc(texture + "insulationceramic_center_base")).texture("particle", "#insulation").renderType("cutout"), models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "ceramicinsulatedwire_side")).texture("insulationbase", blockLoc(texture + "insulationceramic_base")).texture("insulationcolor", blockLoc(texture + "insulationceramic")).texture("particle", "#insulationcolor").renderType("cutout"), false);
        }

        // highly insulated
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())) {
            wire(ElectrodynamicsBlocks.getBlock(wire), models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "highlyinsulatedwire_none")).texture("conductor", blockLoc(texture + wire.conductor.toString() + "_center")).texture("insulation", blockLoc(texture + "insulationwool_center")).texture("particle", "#insulation").renderType("cutout"), models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "highlyinsulatedwire_side")).texture("insulation", blockLoc(texture + "insulationwool")).texture("particle", "#insulation").renderType("cutout"), false);
        }

    }

    private void genPipes() {

        String parent = "parent/";
        String name = "block/pipe/";
        String texture = "pipe/";

        for (SubtypeFluidPipe pipe : SubtypeFluidPipe.values()) {
            wire(ElectrodynamicsBlocks.getBlock(pipe), models().withExistingParent(name + pipe.tag() + "_none", modLoc(parent + "pipe_none")).texture("texture", blockLoc(texture + pipe.tag())).texture("particle", "#texture"), models().withExistingParent(name + pipe.tag() + "_side", modLoc(parent + "pipe_side")).texture("texture", blockLoc(texture + pipe.tag())).texture("particle", "#texture"), false);
        }

    }

    private void genGasPipes() {
        gasPipeUninsulated(SubtypeGasPipe.UNINSULATEDCOPPER);
        gasPipeUninsulated(SubtypeGasPipe.UNINSULATEDSTEEL);
        gasPipeUninsulatedPlastic(SubtypeGasPipe.UNINSULATEDPLASTIC);

        // gasPipeWoolInsulated(SubtypeGasPipe.WOOLINSULATEDCOPPER);
        // gasPipeWoolInsulated(SubtypeGasPipe.WOOLINSULATEDSTEEL);
        // gasPipeWoolInsulatedPlastic(SubtypeGasPipe.WOOLINSULATEDPLASTIC);

        // gasPipeCeramicInsulated(SubtypeGasPipe.CERAMICINSULATEDCOPPER);
        // gasPipeCeramicInsulated(SubtypeGasPipe.CERAMICINSULATEDSTEEL);
        // gasPipeCeramicInsulatedPlastic(SubtypeGasPipe.CERAMICINSULATEDPLASTIC);
    }

    private void gasPipeUninsulated(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.getBlock(pipe), models().withExistingParent(name + pipe.tag() + "center", modLoc(parent + "uninsulatedcenter")).texture("texture", blockLoc(texture + pipe.tag() + "center")).texture("particle", "#texture"), models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "uninsulatedside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), false);
    }

    private void gasPipeUninsulatedPlastic(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.getBlock(pipe), models().withExistingParent(name + pipe.tag() + "center", modLoc(parent + "uninsulatedcenter")).texture("texture", blockLoc(texture + pipe.tag() + "center")).texture("particle", "#texture"), models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "uninsulatedplasticside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), false);
    }

    @SuppressWarnings("unused")
    private void gasPipeWoolInsulated(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.getBlock(pipe), models().getExistingFile(modLoc(parent + "woolinsulatedcenter")), models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), false);
    }

    @SuppressWarnings("unused")
    private void gasPipeWoolInsulatedPlastic(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.getBlock(pipe), models().getExistingFile(modLoc(parent + "woolinsulatedcenter")), models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedplasticside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), false);
    }

    @SuppressWarnings("unused")
    private void gasPipeCeramicInsulated(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.getBlock(pipe), models().getExistingFile(modLoc(parent + "ceramicinsulatedcenter")), models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), false);
    }

    @SuppressWarnings("unused")
    private void gasPipeCeramicInsulatedPlastic(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.getBlock(pipe), models().getExistingFile(modLoc(parent + "ceramicinsulatedcenter")), models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedplasticside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), false);
    }

    private void genCompressorAddonTank() {
        getVariantBuilder(ElectrodynamicsBlocks.blockGasTransformerAddonTank).partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.BOTTOMANDTOPTANK).modelForState().modelFile(existingBlock(blockLoc("compressoraddontanktab"))).addModel().partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.TOPTANK).modelForState().modelFile(existingBlock(blockLoc("compressoraddontankt"))).addModel().partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.BOTTOMTANK).modelForState().modelFile(existingBlock(blockLoc("compressoraddontankb"))).addModel().partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.NONE).modelForState().modelFile(existingBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK)).addModel();

        blockItem(ElectrodynamicsBlocks.blockGasTransformerAddonTank, existingBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK));
    }

    private void genCompressorSide() {
        Block block = ElectrodynamicsBlocks.blockGasTransformerSide;

        ModelFile none = existingBlock(block);
        ModelFile top = existingBlock(blockLoc("compressorsidet"));

        getVariantBuilder(block).partialState().with(GenericEntityBlock.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(0).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(180).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(0).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(180).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(270).addModel();
    }

    private void genThermoelectricManipulator() {
        Block block = ElectrodynamicsBlocks.blockThermoelectricManipulator;

        ModelFile off = existingBlock(block);
        ModelFile cool = existingBlock(blockLoc("thermoelectricmanipulatorcool"));
        ModelFile heat = existingBlock(blockLoc("thermoelectricmanipulatorheat"));

        getVariantBuilder(block)

                .partialState().with(GenericEntityBlock.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(180).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(0).addModel()

                .partialState().with(GenericEntityBlock.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(180).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(0).addModel()

                .partialState().with(GenericEntityBlock.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(180).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(0).addModel();
    }


	public ItemModelBuilder simpleBlock(DeferredHolder<Block, Block> block, ModelFile file, boolean registerItem) {
		return simpleBlock(block.get(), file, registerItem);
	}
    public ItemModelBuilder simpleBlock(Block block, ModelFile file, boolean registerItem) {
        simpleBlock(block, file);
        if (registerItem) {
            return blockItem(block, file);
        }
        return null;
    }

    public ItemModelBuilder simpleBlock(DeferredHolder<Block, Block> block, ResourceLocation texture, boolean registerItem) {
        return simpleBlock(block.get(), texture, registerItem);
    }

    public ItemModelBuilder simpleBlock(Block block, ResourceLocation texture, boolean registerItem) {
        return simpleBlock(block, models().cubeAll(name(block), texture), registerItem);
    }

    public ItemModelBuilder glassBlock(DeferredHolder<Block, Block> block, ResourceLocation texture, boolean registerItem) {
        return glassBlock(block.get(), texture, registerItem);
    }

    public ItemModelBuilder glassBlock(Block block, ResourceLocation texture, boolean registerItem) {
        return simpleBlockCustomRenderType(block, texture, ResourceLocation.withDefaultNamespace("cutout"), registerItem);
    }

    public ItemModelBuilder simpleBlockCustomRenderType(Block block, ResourceLocation texture, ResourceLocation renderType, boolean registerItem) {
        BlockModelBuilder builder = models().cubeAll(name(block), texture).renderType(renderType);
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(builder));
        if (registerItem) {
            return blockItem(block, builder);
        }
        return null;
    }

    public ItemModelBuilder simpleBlockCustomRenderType(DeferredHolder<Block, Block> block, ResourceLocation texture, ResourceLocation renderType, boolean registerItem) {
        return simpleBlockCustomRenderType(block.get(), texture, renderType, registerItem);
    }

    public ItemModelBuilder airBlock(DeferredHolder<Block, Block> block, String particleTexture, boolean registerItem) {
        return airBlock(block.get(), particleTexture, registerItem);
    }

    public ItemModelBuilder airBlock(Block block, String particleTexture, boolean registerItem) {
        BlockModelBuilder builder = models().getBuilder(name(block)).texture("particle", modLoc(particleTexture)).renderType("cutout");
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(builder));
        if (registerItem) {
            return blockItem(block, builder);
        }
        return null;
    }

    public ItemModelBuilder bottomSlabBlock(DeferredHolder<Block, Block> block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, boolean registerItem) {
        return bottomSlabBlock(block.get(), side, bottom, top, registerItem);
    }

    public ItemModelBuilder bottomSlabBlock(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, boolean registerItem) {
        BlockModelBuilder builder = models().slab(name(block), side, bottom, top);
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(builder));
        if (registerItem) {
            return blockItem(block, builder);
        }
        return null;
    }

    public ItemModelBuilder horrRotatedBlock(DeferredHolder<Block, Block> block, ModelFile modelFile, boolean registerItem) {
        return horrRotatedBlock(block, modelFile, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedBlock(DeferredHolder<Block, Block> block, ModelFile modelFile, int yRotationOffset, int xRotation, boolean registerItem) {
        return horrRotatedBlock(block.get(), modelFile, yRotationOffset, xRotation, registerItem);
    }

    public ItemModelBuilder horrRotatedBlock(Block block, ModelFile file, boolean registerItem) {
        return horrRotatedBlock(block, file, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedBlock(Block block, ModelFile file, int yRotationOffset, int xRotation, boolean registerItem) {
        getVariantBuilder(block).partialState().with(GenericEntityBlock.FACING, Direction.NORTH).modelForState().modelFile(file).rotationY((270 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).modelForState().modelFile(file).rotationY((0 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).modelForState().modelFile(file).rotationY((90 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).modelForState().modelFile(file).rotationY((180 + yRotationOffset) % 360).rotationX(xRotation).addModel();
        if (registerItem) {
            return blockItem(block, file);
        }
        return null;
    }

    public ItemModelBuilder horrRotatedLitBlock(DeferredHolder<Block, Block> block, ModelFile off, ModelFile on, boolean registerItem) {
        return horrRotatedBlock(block, on, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedLitBlock(DeferredHolder<Block, Block> block, ModelFile off, ModelFile on, int yRotationOffset, int xRotation, boolean registerItem) {
        return horrRotatedLitBlock(block.get(), off, on, yRotationOffset, xRotation, registerItem);
    }

    public ItemModelBuilder horrRotatedLitBlock(Block block, ModelFile off, ModelFile on, boolean registerItem) {
        return horrRotatedLitBlock(block, off, on, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedLitBlock(Block block, ModelFile off, ModelFile on, int yRotationOffset, int xRotation, boolean registerItem) {
        getVariantBuilder(block).partialState().with(GenericEntityBlock.FACING, Direction.NORTH).with(BlockStateProperties.LIT, false).modelForState().modelFile(off).rotationY((270 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).with(BlockStateProperties.LIT, false).modelForState().modelFile(off).rotationY((0 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).with(BlockStateProperties.LIT, false).modelForState().modelFile(off).rotationY((90 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).with(BlockStateProperties.LIT, false).modelForState().modelFile(off).rotationY((180 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.NORTH).with(BlockStateProperties.LIT, true).modelForState().modelFile(on).rotationY((270 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST).with(BlockStateProperties.LIT, true).modelForState().modelFile(on).rotationY((0 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH).with(BlockStateProperties.LIT, true).modelForState().modelFile(on).rotationY((90 + yRotationOffset) % 360).rotationX(xRotation).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST).with(BlockStateProperties.LIT, true).modelForState().modelFile(on).rotationY((180 + yRotationOffset) % 360).rotationX(xRotation).addModel();
        if (registerItem) {
            return blockItem(block, off);
        }
        return null;

    }

    public ItemModelBuilder redstoneToggleBlock(DeferredHolder<Block, Block> block, ModelFile off, ModelFile on, boolean registerItem) {
        return redstoneToggleBlock(block.get(), off, on, registerItem);
    }

    public ItemModelBuilder redstoneToggleBlock(Block block, ModelFile off, ModelFile on, boolean registerItem) {
        getVariantBuilder(block).partialState().with(BlockStateProperties.LIT, false).modelForState().modelFile(off).addModel().partialState().with(BlockStateProperties.LIT, true).modelForState().modelFile(on).addModel();
        if (registerItem) {
            return blockItem(block, off);
        }
        return null;

    }

    /*
     * private void omniDirBlock(DeferredHolder<Block, Block> block, ModelFile model, boolean registerItem) { getVariantBuilder(block.get()).partialState().with(GenericEntityBlock.FACING, Direction.NORTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(0).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(180).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.NORTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.UP).modelForState().modelFile(model) .rotationY(0).rotationX(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST) .with(OverdriveBlockStates.VERTICAL_FACING,
     * VerticalFacing.UP).modelForState().modelFile(model) .rotationY(90).rotationX(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.UP).modelForState().modelFile(model) .rotationY(180).rotationX(270).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.UP).modelForState().modelFile(model) .rotationY(270).rotationX(270).addModel().partialState() .with(GenericEntityBlock.FACING, Direction.NORTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(0).rotationX(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.EAST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(90).rotationX(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.SOUTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(180).rotationX(90).addModel().partialState().with(GenericEntityBlock.FACING, Direction.WEST)
     * .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(270).rotationX(90).addModel(); if (registerItem) simpleBlockItem(block.get(), model); }
     *
     */
    public void wire(Block block, ModelFile none, ModelFile side, boolean registerItem) {
        ModelFile model = models().withExistingParent(name(block), ResourceLocation.withDefaultNamespace("cube")).customLoader(WireModelBuilder::begin).models(none, side, side).end().renderType(ResourceLocation.withDefaultNamespace("cutout"));

        getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));

        if (registerItem) {
            simpleBlockItem(block, none);
        }

    }

    public ItemModelBuilder snowyBlock(Block block, ModelFile noSnow, ModelFile withSnow, boolean registerItem) {
        getVariantBuilder(block).partialState().with(SnowyDirtBlock.SNOWY, false).modelForState().modelFile(noSnow).addModel().partialState().with(SnowyDirtBlock.SNOWY, true).modelForState().modelFile(withSnow).rotationY(0).addModel();

        if (registerItem) {
            return blockItem(block, noSnow);
        }
        return null;
    }

    // gotta love dealing with mojank
    public ItemModelBuilder pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture, @Nullable ResourceLocation renderType, boolean registerItem) {
        ModelFile pressurePlate = models().pressurePlate(name(block), texture);
        ModelFile pressurePlateDown = models().pressurePlateDown(name(block) + "_down", texture);
        if (renderType != null) {
            pressurePlate = models().pressurePlate(name(block), texture).renderType(renderType);
            pressurePlateDown = models().pressurePlateDown(name(block) + "_down", texture).renderType(renderType);
        }
        return pressurePlateBlock(block, pressurePlate, pressurePlateDown, renderType, registerItem);
    }

    public ItemModelBuilder pressurePlateBlock(PressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown, @Nullable ResourceLocation renderType, boolean registerItem) {
        getVariantBuilder(block).partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown)).partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));
        if (registerItem) {
            return blockItem(block, pressurePlate);
        }
        return null;
    }

    public ItemModelBuilder simpleColumnBlock(Block block, ResourceLocation side, ResourceLocation top, boolean registerItem) {
        BlockModelBuilder builder = models().cubeColumn(name(block), side, top);
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(builder));
        if (registerItem) {
            return blockItem(block, builder);
        }
        return null;
    }

    public ItemModelBuilder crossBlock(DeferredHolder<Block, Block> block, ResourceLocation texture, @Nullable ResourceLocation renderType, boolean registerItem) {
        return crossBlock(block.get(), texture, renderType, registerItem);
    }

    public ItemModelBuilder crossBlock(Block block, ResourceLocation texture, @Nullable ResourceLocation renderType, boolean registerItem) {
        ModelFile cross;
        if (renderType == null) {
            cross = models().cross(name(block), texture);
        } else {
            cross = models().cross(name(block), texture).renderType(renderType);
        }
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(cross));
        if (registerItem) {
            return blockItem(block, cross);
        }
        return null;
    }

    public BlockModelBuilder getObjModel(String name, String modelLoc) {
        return models().withExistingParent("block/" + name, "cube").customLoader(ObjModelBuilder::begin).flipV(true).modelLocation(modLoc("models/" + modelLoc + ".obj")).end();
    }

    public BlockModelBuilder blockTopBottom(DeferredHolder<Block, Block> block, String top, String bottom, String side) {
        return models().cubeBottomTop(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), ResourceLocation.fromNamespaceAndPath(modID, side), ResourceLocation.fromNamespaceAndPath(modID, bottom), ResourceLocation.fromNamespaceAndPath(modID, top));
    }

    public ItemModelBuilder blockItem(Block block, ModelFile model) {
        return itemModels().getBuilder(key(block).getPath()).parent(model);
    }

    public ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public String name(Block block) {
        return key(block).getPath();
    }

    public ExistingModelFile existingBlock(DeferredHolder<Block, Block> block) {
        return existingBlock(block.getId());
    }

    public ExistingModelFile existingBlock(Block block) {
        return existingBlock(BuiltInRegistries.BLOCK.getKey(block));
    }

    public ExistingModelFile existingBlock(ResourceLocation loc) {
        return models().getExistingFile(loc);
    }

    public ResourceLocation blockLoc(String texture) {
        return modLoc("block/" + texture);
    }

    public ResourceLocation modelLoc(String texture) {
        return modLoc("model/" + texture);
    }

}
