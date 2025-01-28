package electrodynamics.datagen.client;

import javax.annotation.Nullable;

import electrodynamics.Electrodynamics;
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
import electrodynamics.common.block.subtype.SubtypeWire.InsulationMaterial;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import electrodynamics.common.block.subtype.SubtypeWire.WireColor;
import electrodynamics.common.block.subtype.SubtypeWire.WireMaterial;
import electrodynamics.datagen.DataGenerators;
import electrodynamics.datagen.utils.model.SlaveNodeModelBuilder;
import electrodynamics.datagen.utils.model.WireModelBuilder;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
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
            glassBlock(ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(glass), blockLoc("glass/" + glass.tag()), true);
        }

        for (SubtypeOre ore : SubtypeOre.values()) {
            simpleBlock(ElectrodynamicsBlocks.BLOCKS_ORE.getValue(ore), blockLoc("ore/" + ore.tag()), true);
        }

        for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
            simpleBlock(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getValue(ore), blockLoc("deepslateore/" + ore.tag()), true);
        }

        for (SubtypeRawOreBlock raw : SubtypeRawOreBlock.values()) {
            simpleBlock(ElectrodynamicsBlocks.BLOCKS_RAWORE.getValue(raw), blockLoc("raworeblock/" + raw.tag()), true);
        }

        for (SubtypeResourceBlock resource : SubtypeResourceBlock.values()) {
            simpleBlock(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getValue(resource), blockLoc("resource/" + resource.tag()), true);
        }

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedsolarpanel), existingBlock(blockLoc("advancedsolarpanelbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.batterybox), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.batterybox)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lithiumbatterybox), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lithiumbatterybox)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.carbynebatterybox), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.carbynebatterybox)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerlv), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerlv)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargermv), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargermv)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerhv), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerhv)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalcrystallizer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalcrystallizer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalmixer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalmixer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitbreaker), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitbreaker)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coalgenerator), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coalgenerator)), existingBlock(blockLoc("coalgeneratorrunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.combustionchamber), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.combustionchamber)), true).transforms().transform(ItemDisplayContext.GUI).rotation(35, 40, 0).scale(0.665F).end();
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coolantresavoir), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coolantresavoir)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativefluidsource), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativefluidsource)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativepowersource), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativepowersource)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativegassource), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativegassource)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.downgradetransformer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.downgradetransformer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.upgradetransformer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.upgradetransformer)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnace), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnace)), existingBlock(blockLoc("electricfurnacerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacedouble), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacedouble)), existingBlock(blockLoc("electricfurnacedoublerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacetriple), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacetriple)), existingBlock(blockLoc("electricfurnacetriplerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnace), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnace)), existingBlock(blockLoc("electricarcfurnacerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacedouble), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacedouble)), existingBlock(blockLoc("electricarcfurnacedoublerunning")), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacetriple), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacetriple)), existingBlock(blockLoc("electricarcfurnacetriplerunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricpump), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricpump)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.energizedalloyer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.energizedalloyer)), existingBlock(blockLoc("energizedalloyerrunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fermentationplant), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fermentationplant)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvoid), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvoid)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCK_FRAME, existingBlock(ElectrodynamicsBlocks.BLOCK_FRAME.get()), true);
        simpleBlock(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER, existingBlock(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get()), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.hydroelectricgenerator), existingBlock(blockLoc("hydroelectricgeneratorengine")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lathe), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lathe)), true);
        wire(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get(), existingBlock(blockLoc("logisticalmanager_none")), existingBlock(blockLoc("logisticalmanager_inventory")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusher), existingBlock(blockLoc("mineralcrusherbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusherdouble), existingBlock(blockLoc("mineralcrusherdoublebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple), existingBlock(blockLoc("mineralcrushertriplebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinder), existingBlock(blockLoc("mineralgrinderbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinderdouble), existingBlock(blockLoc("mineralgrinderdoublebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrindertriple), existingBlock(blockLoc("mineralgrindertriplebase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralwasher), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralwasher)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.motorcomplex), existingBlock(blockLoc("motorcomplexbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.multimeterblock), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.multimeterblock)), true);
        airBlock(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE, "block/multisubnode", false);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.oxidationfurnace), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.oxidationfurnace)), existingBlock(blockLoc("oxidationfurnacerunning")), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.quarry), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.quarry)), true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.reinforcedalloyer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.reinforcedalloyer)), existingBlock(blockLoc("reinforcedalloyerrunning")), true);
        simpleBlock(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER, existingBlock(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER.get()), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.seismicrelay), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.seismicrelay)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.solarpanel), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.solarpanel)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankreinforced), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankreinforced)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankhsla), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankhsla)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.thermoelectricgenerator), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.thermoelectricgenerator)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.windmill), existingBlock(blockLoc("windmillbase")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremill), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremill)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilldouble), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilldouble)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilltriple), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilltriple)), true);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastanksteel), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastanksteel)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankreinforced), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankreinforced)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankhsla), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankhsla)), true);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCK_ADVANCEDCOMPRESSOR, existingBlock(ElectrodynamicsBlocks.BLOCK_ADVANCEDCOMPRESSOR.get()), 180, 0, false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCK_ADVANCEDDECOMPRESSOR, existingBlock(ElectrodynamicsBlocks.BLOCK_ADVANCEDDECOMPRESSOR.get()), 180, 0, false);
        //Compressor Addon Tank
        getVariantBuilder(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK.get())//
                .partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.BOTTOMANDTOPTANK).modelForState().modelFile(existingBlock(blockLoc("compressoraddontanktab"))).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.TOPTANK).modelForState().modelFile(existingBlock(blockLoc("compressoraddontankt"))).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.BOTTOMTANK).modelForState().modelFile(existingBlock(blockLoc("compressoraddontankb"))).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.ADDONTANK_NEIGHBOR_STATUS, AddonTankNeighborType.NONE).modelForState().modelFile(existingBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK)).addModel();

        blockItem(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK.get(), existingBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK));

        //Compressor Side Block
        ModelFile none = existingBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get());
        ModelFile top = existingBlock(blockLoc("compressorsidet"));

        getVariantBuilder(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get())//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(270).addModel();

        simpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gasvent), blockLoc("gasvent"), true);

        // Advanced Thermoelectric Manipulator
        ModelFile off = existingBlock(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get());
        ModelFile cool = existingBlock(blockLoc("advancedthermoelectricmanipulatorcool"));
        ModelFile heat = existingBlock(blockLoc("advancedthermoelectricmanipulatorheat"));

        getVariantBuilder(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get())
                //
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).modelForState().modelFile(off).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).modelForState().modelFile(cool).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).modelForState().modelFile(heat).rotationY(0).addModel();

        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gasvalve), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gasvalve)), existingBlock(blockLoc("gasvalveon")), 90, 0, true);
        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvalve), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvalve)), existingBlock(blockLoc("fluidvalveon")), 90, 0, true);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipepump), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipepump)), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipepump), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipepump)), false);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipefilter), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipefilter)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipefilter), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipefilter)), true);

        horrRotatedLitBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.relay), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.relay)), existingBlock(blockLoc("relayon")), 180, 0, true);

        glassBlock(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING, blockLoc("steelscaffold"), true);

        simpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.potentiometer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.potentiometer)), true);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advanceddowngradetransformer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advanceddowngradetransformer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedupgradetransformer), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedupgradetransformer)), true);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitmonitor), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitmonitor)), 90, 0, true);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.currentregulator), existingBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.currentregulator)), 180, 0, true);

        redstoneToggleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gascollector), //
                models().cube(SubtypeMachine.gascollector.tag(), blockLoc("gascollector_bottom"), blockLoc("gascollector_fanoff"), blockLoc("gascollector_fanoff"), blockLoc("gascollector_back"), blockLoc("gascollector_fanoff"), blockLoc("gascollector_fanoff")).texture("particle", blockLoc("multisubnode")), //
                models().cube(SubtypeMachine.gascollector.tag() + "on", blockLoc("gascollector_bottom"), blockLoc("gascollector_fanon"), blockLoc("gascollector_fanon"), blockLoc("gascollector_back"), blockLoc("gascollector_fanon"), blockLoc("gascollector_fanon")).texture("particle", blockLoc("multisubnode")), //
                true);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR, existingBlock(blockLoc("chemicalreactorbottom")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_MIDDLE, existingBlock(blockLoc("chemicalreactormiddle")), false);
        horrRotatedBlock(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_TOP, existingBlock(blockLoc("chemicalreactortop")), false);

        // Compressor
        none = existingBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get());
        top = existingBlock(blockLoc("compressortoptank"));

        getVariantBuilder(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get())//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(270).addModel();
        blockItem(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get(), existingBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR));

        //Decompressor
        none = existingBlock(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get());
        top = existingBlock(blockLoc("decompressortoptank"));

        getVariantBuilder(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get())//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(none).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(top).rotationY(270).addModel();
        blockItem(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get(), existingBlock(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR));

        //Thermoelectric Manipulator
        off = existingBlock(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get());
        cool = existingBlock(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get());
        heat = existingBlock(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get());

        ModelFile offtop = existingBlock(blockLoc("thermoelectricmanipulatortoptank"));
        ModelFile cooltop = existingBlock(blockLoc("thermoelectricmanipulatortoptank"));
        ModelFile heattop = existingBlock(blockLoc("thermoelectricmanipulatortoptank"));
        getVariantBuilder(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get())
                //
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(off).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(off).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(off).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(off).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(cool).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(cool).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(cool).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(cool).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(heat).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(heat).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(heat).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false).modelForState().modelFile(heat).rotationY(270).addModel()//
                //
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(offtop).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(offtop).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(offtop).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(offtop).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(cooltop).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(cooltop).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(cooltop).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.COOL).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(cooltop).rotationY(270).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(heattop).rotationY(0).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(heattop).rotationY(90).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(heattop).rotationY(180).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.HEAT).with(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true).modelForState().modelFile(heattop).rotationY(270).addModel();
        blockItem(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get(), existingBlock(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR));

        slaveNode(ElectrodynamicsBlocks.BLOCK_MULTIBLOCK_SLAVE.get(), "block/multisubnode");

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolosischamber),
                //
                models().cube(
                                //
                                name(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolosischamber)),
                                //
                                blockLoc("multiblock/electrolosischamberbulkhead"),
                                //
                                blockLoc("multiblock/electrolosischamberbulkhead"),
                                //
                                blockLoc("electrolosischamberfront"),
                                //
                                blockLoc("multiblock/electrolosischambercontacts"),
                                //
                                blockLoc("multiblock/electrolosischamberbulkhead"),
                                //
                                blockLoc("multiblock/electrolosischamberbulkhead"))
                        //
                        .texture("particle", modLoc("block/multisubnode")),
                //
                90, 0, true);

        horrRotatedBlock(ElectrodynamicsBlocks.BLOCK_ROTARYUNIFIER, existingBlock(ElectrodynamicsBlocks.BLOCK_ROTARYUNIFIER), true);


        genWires();
        genFluidPipes();
        genGasPipes();

    }

    private void genWires() {

        String parent = "parent/";
        String name = "block/wire/";
        String texture = "wire/";

        // bare
        for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE)) {
            wire(ElectrodynamicsBlocks.BLOCKS_WIRE.getValue(wire), //
                    models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "wire_none")).texture("conductor", blockLoc(texture + wire.getWireMaterial().toString())).texture("particle", "#conductor").renderType("cutout"), //
                    models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "wire_side")).texture("conductor", blockLoc(texture + wire.getWireMaterial().toString())).texture("particle", "#conductor").renderType("cutout"), //
                    false);
        }

        // insulated
        for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())) {
            wire(ElectrodynamicsBlocks.BLOCKS_WIRE.getValue(wire), //
                    models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "insulatedwire_none")).texture("conductor", blockLoc(texture + wire.getWireMaterial().toString() + "_center")).texture("insulation", blockLoc(texture + "insulationwool_center")).texture("particle", "#insulation").renderType("cutout"), //
                    models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "insulatedwire_side")).texture("insulation", blockLoc(texture + "insulationwool")).texture("particle", "#insulation").renderType("cutout"), //
                    false);
        }

        // logistical
        for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())) {
            wire(ElectrodynamicsBlocks.BLOCKS_WIRE.getValue(wire), //
                    models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "logisticswire_none")).texture("conductor", blockLoc(texture + "logisticswire" + wire.getWireMaterial().toString())).texture("insulation", blockLoc(texture + "logisticswireinsulation_center")).texture("particle", "#insulation").texture("redstone", blockLoc(texture + "logisticswireredstone_center")).renderType("cutout"), //
                    models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "logisticswire_side")).texture("insulation", blockLoc(texture + "logisticswireinsulation_side")).texture("particle", "#insulation").texture("redstone", blockLoc(texture + "logisticswireredstone_side")).renderType("cutout"), //
                    false);
        }

        // ceramic
        for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())) {
            wire(ElectrodynamicsBlocks.BLOCKS_WIRE.getValue(wire), //
                    models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "ceramicinsulatedwire_none")).texture("conductor", blockLoc(texture + wire.getWireMaterial().toString() + "_center")).texture("insulation", blockLoc(texture + "insulationceramic_center_base")).texture("particle", "#insulation").renderType("cutout"), //
                    models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "ceramicinsulatedwire_side")).texture("insulationbase", blockLoc(texture + "insulationceramic_base")).texture("insulationcolor", blockLoc(texture + "insulationceramic")).texture("particle", "#insulationcolor").renderType("cutout"), //
                    false);
        }

        // highly insulated
        for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())) {
            wire(ElectrodynamicsBlocks.BLOCKS_WIRE.getValue(wire), //
                    models().withExistingParent(name + wire.tag() + "_none", modLoc(parent + "highlyinsulatedwire_none")).texture("conductor", blockLoc(texture + wire.getWireMaterial().toString() + "_center")).texture("insulation", blockLoc(texture + "insulationwool_center")).texture("particle", "#insulation").renderType("cutout"), //
                    models().withExistingParent(name + wire.tag() + "_side", modLoc(parent + "highlyinsulatedwire_side")).texture("insulation", blockLoc(texture + "insulationwool")).texture("particle", "#insulation").renderType("cutout"), //
                    false);
        }

    }

    private void genFluidPipes() {

        String parent = "parent/";
        String name = "block/pipe/";
        String texture = "pipe/";

        for (SubtypeFluidPipe pipe : SubtypeFluidPipe.values()) {
            wire(ElectrodynamicsBlocks.BLOCKS_FLUIDPIPE.getValue(pipe), //
                    models().withExistingParent(name + pipe.tag() + "_none", modLoc(parent + "pipe_none")).texture("texture", blockLoc(texture + pipe.tag())).texture("particle", "#texture"), //
                    models().withExistingParent(name + pipe.tag() + "_side", modLoc(parent + "pipe_side")).texture("texture", blockLoc(texture + pipe.tag())).texture("particle", "#texture"), //
                    false);
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
        wire(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(pipe), //
                models().withExistingParent(name + pipe.tag() + "center", modLoc(parent + "uninsulatedcenter")).texture("texture", blockLoc(texture + pipe.tag() + "center")).texture("particle", "#texture"), //
                models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "uninsulatedside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), //
                false);
    }

    private void gasPipeUninsulatedPlastic(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(pipe), //
                models().withExistingParent(name + pipe.tag() + "center", modLoc(parent + "uninsulatedcenter")).texture("texture", blockLoc(texture + pipe.tag() + "center")).texture("particle", "#texture"), //
                models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "uninsulatedplasticside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), //
                false);
    }

    @SuppressWarnings("unused")
    private void gasPipeWoolInsulated(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(pipe), //
                models().getExistingFile(modLoc(parent + "woolinsulatedcenter")), //
                models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), //
                false);
    }

    @SuppressWarnings("unused")
    private void gasPipeWoolInsulatedPlastic(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(pipe), //
                models().getExistingFile(modLoc(parent + "woolinsulatedcenter")), //
                models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedplasticside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), //
                false);
    }

    @SuppressWarnings("unused")
    private void gasPipeCeramicInsulated(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(pipe), //
                models().getExistingFile(modLoc(parent + "ceramicinsulatedcenter")), //
                models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), //
                false);
    }

    @SuppressWarnings("unused")
    private void gasPipeCeramicInsulatedPlastic(SubtypeGasPipe pipe) {
        String parent = "parent/gaspipe";
        String name = "block/gaspipe/";
        String texture = "gaspipe/";
        wire(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(pipe),//
                models().getExistingFile(modLoc(parent + "ceramicinsulatedcenter")), //
                models().withExistingParent(name + pipe.tag() + "side", modLoc(parent + "insulatedplasticside")).texture("texture", blockLoc(texture + pipe.tag() + "side")).texture("particle", "#texture"), //
                false);
    }

    public ItemModelBuilder simpleBlock(DeferredHolder<Block, ? extends Block> block, ModelFile file, boolean registerItem) {
        return simpleBlock(block.get(), file, registerItem);
    }

    public ItemModelBuilder simpleBlock(Block block, ModelFile file, boolean registerItem) {
        simpleBlock(block, file);
        if (registerItem) {
            return blockItem(block, file);
        }
        return null;
    }

    public ItemModelBuilder simpleBlock(DeferredHolder<Block, ? extends Block> block, ResourceLocation texture, boolean registerItem) {
        return simpleBlock(block.get(), texture, registerItem);
    }

    public ItemModelBuilder simpleBlock(Block block, ResourceLocation texture, boolean registerItem) {
        return simpleBlock(block, models().cubeAll(name(block), texture), registerItem);
    }

    public ItemModelBuilder glassBlock(DeferredHolder<Block, ? extends Block> block, ResourceLocation texture, boolean registerItem) {
        return glassBlock(block.get(), texture, registerItem);
    }

    public ItemModelBuilder glassBlock(Block block, ResourceLocation texture, boolean registerItem) {
        return simpleBlockCustomRenderType(block, texture, Electrodynamics.vanillarl("cutout"), registerItem);
    }

    public ItemModelBuilder simpleBlockCustomRenderType(Block block, ResourceLocation texture, ResourceLocation renderType, boolean registerItem) {
        BlockModelBuilder builder = models().cubeAll(name(block), texture).renderType(renderType);
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(builder));
        if (registerItem) {
            return blockItem(block, builder);
        }
        return null;
    }

    public ItemModelBuilder simpleBlockCustomRenderType(DeferredHolder<Block, ? extends Block> block, ResourceLocation texture, ResourceLocation renderType, boolean registerItem) {
        return simpleBlockCustomRenderType(block.get(), texture, renderType, registerItem);
    }

    public ItemModelBuilder airBlock(DeferredHolder<Block, ? extends Block> block, String particleTexture, boolean registerItem) {
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

    public ItemModelBuilder bottomSlabBlock(DeferredHolder<Block, ? extends Block> block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, boolean registerItem) {
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

    public ItemModelBuilder horrRotatedBlock(DeferredHolder<Block, ? extends Block> block, ModelFile modelFile, boolean registerItem) {
        return horrRotatedBlock(block, modelFile, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedBlock(DeferredHolder<Block, ? extends Block> block, ModelFile modelFile, int yRotationOffset, int xRotation, boolean registerItem) {
        return horrRotatedBlock(block.get(), modelFile, yRotationOffset, xRotation, registerItem);
    }

    public ItemModelBuilder horrRotatedBlock(Block block, ModelFile file, boolean registerItem) {
        return horrRotatedBlock(block, file, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedBlock(Block block, ModelFile file, int yRotationOffset, int xRotation, boolean registerItem) {
        getVariantBuilder(block)//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).modelForState().modelFile(file).rotationY((270 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).modelForState().modelFile(file).rotationY((0 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).modelForState().modelFile(file).rotationY((90 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).modelForState().modelFile(file).rotationY((180 + yRotationOffset) % 360).rotationX(xRotation).addModel();
        if (registerItem) {
            return blockItem(block, file);
        }
        return null;
    }

    public ItemModelBuilder horrRotatedLitBlock(DeferredHolder<Block, ? extends Block> block, ModelFile off, ModelFile on, boolean registerItem) {
        return horrRotatedLitBlock(block, off, on, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedLitBlock(DeferredHolder<Block, ? extends Block> block, ModelFile off, ModelFile on, int yRotationOffset, int xRotation, boolean registerItem) {
        return horrRotatedLitBlock(block.get(), off, on, yRotationOffset, xRotation, registerItem);
    }

    public ItemModelBuilder horrRotatedLitBlock(Block block, ModelFile off, ModelFile on, boolean registerItem) {
        return horrRotatedLitBlock(block, off, on, 0, 0, registerItem);
    }

    public ItemModelBuilder horrRotatedLitBlock(Block block, ModelFile off, ModelFile on, int yRotationOffset, int xRotation, boolean registerItem) {
        getVariantBuilder(block)//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.LIT, false).modelForState().modelFile(off).rotationY((270 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.LIT, false).modelForState().modelFile(off).rotationY((0 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.LIT, false).modelForState().modelFile(off).rotationY((90 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.LIT, false).modelForState().modelFile(off).rotationY((180 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH).with(ElectrodynamicsBlockStates.LIT, true).modelForState().modelFile(on).rotationY((270 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST).with(ElectrodynamicsBlockStates.LIT, true).modelForState().modelFile(on).rotationY((0 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH).with(ElectrodynamicsBlockStates.LIT, true).modelForState().modelFile(on).rotationY((90 + yRotationOffset) % 360).rotationX(xRotation).addModel()//
                .partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST).with(ElectrodynamicsBlockStates.LIT, true).modelForState().modelFile(on).rotationY((180 + yRotationOffset) % 360).rotationX(xRotation).addModel();
        if (registerItem) {
            return blockItem(block, off);
        }
        return null;

    }

    public ItemModelBuilder redstoneToggleBlock(DeferredHolder<Block, ? extends Block> block, ModelFile off, ModelFile on, boolean registerItem) {
        return redstoneToggleBlock(block.get(), off, on, registerItem);
    }

    public ItemModelBuilder redstoneToggleBlock(Block block, ModelFile off, ModelFile on, boolean registerItem) {
        getVariantBuilder(block).partialState().with(ElectrodynamicsBlockStates.LIT, false).modelForState().modelFile(off).addModel().partialState().with(ElectrodynamicsBlockStates.LIT, true).modelForState().modelFile(on).addModel();
        if (registerItem) {
            return blockItem(block, off);
        }
        return null;

    }

    /*
     * private void omniDirBlock(DeferredHolder<Block, Block> block, ModelFile model, boolean registerItem) { getVariantBuilder(block.get()).partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(0).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(90).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(180).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.NONE).modelForState().modelFile(model) .rotationY(270).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.NORTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.UP).modelForState().modelFile(model) .rotationY(0).rotationX(270).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST) .with(OverdriveBlockStates.VERTICAL_FACING,
     * VerticalFacing.UP).modelForState().modelFile(model) .rotationY(90).rotationX(270).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.UP).modelForState().modelFile(model) .rotationY(180).rotationX(270).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.UP).modelForState().modelFile(model) .rotationY(270).rotationX(270).addModel().partialState() .with(ElectrodynamicsBlockStates.FACING, Direction.NORTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(0).rotationX(90).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.EAST) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(90).rotationX(90).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.SOUTH) .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(180).rotationX(90).addModel().partialState().with(ElectrodynamicsBlockStates.FACING, Direction.WEST)
     * .with(OverdriveBlockStates.VERTICAL_FACING, VerticalFacing.DOWN).modelForState().modelFile(model) .rotationY(270).rotationX(90).addModel(); if (registerItem) simpleBlockItem(block.get(), model); }
     *
     */
    public void wire(Block block, ModelFile none, ModelFile side, boolean registerItem) {
        ModelFile model = models().withExistingParent(name(block), Electrodynamics.vanillarl("cube")).customLoader(WireModelBuilder::begin).models(none, side, side).end().renderType(Electrodynamics.vanillarl("cutout"));

        getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));

        if (registerItem) {
            simpleBlockItem(block, none);
        }

    }

    public void slaveNode(Block block, String particleTexture) {
        BlockModelBuilder builder = models().getBuilder(name(block)).customLoader(SlaveNodeModelBuilder::begin).model(existingBlock(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE.get())).end().texture("particle", modLoc(particleTexture)).renderType("cutout");
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(builder));
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

    public ItemModelBuilder crossBlock(DeferredHolder<Block, ? extends Block> block, ResourceLocation texture, @Nullable ResourceLocation renderType, boolean registerItem) {
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

    public BlockModelBuilder blockTopBottom(DeferredHolder<Block, ? extends Block> block, String top, String bottom, String side) {
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

    public ExistingModelFile existingBlock(DeferredHolder<Block, ? extends Block> block) {
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
