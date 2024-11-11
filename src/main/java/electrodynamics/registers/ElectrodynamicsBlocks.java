package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.common.block.BlockCustomGlass;
import electrodynamics.common.block.BlockFrame;
import electrodynamics.common.block.BlockLogisticalManager;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.block.BlockMultiSubnode;
import electrodynamics.common.block.BlockOre;
import electrodynamics.common.block.BlockPipeFilter;
import electrodynamics.common.block.BlockPipePump;
import electrodynamics.common.block.BlockSeismicMarker;
import electrodynamics.common.block.BlockValve;
import electrodynamics.common.block.chemicalreactor.BlockChemicalReactor;
import electrodynamics.common.block.chemicalreactor.BlockChemicalReactorExtra;
import electrodynamics.common.block.connect.BlockFluidPipe;
import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.common.block.connect.BlockLogisticalWire;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.connect.util.BlockScaffold;
import electrodynamics.common.block.gastransformer.BlockCompressor;
import electrodynamics.common.block.gastransformer.thermoelectricmanipulator.BlockAdvancedThermoelectricManipulator;
import electrodynamics.common.block.gastransformer.util.BlockGasTransformerAddonTank;
import electrodynamics.common.block.gastransformer.util.BlockGasTransformerSide;
import electrodynamics.common.block.gastransformer.BlockAdvancedCompressor;
import electrodynamics.common.block.gastransformer.thermoelectricmanipulator.BlockThermoelectricManipulator;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, References.ID);

    public static final BulkDeferredHolder<Block, BlockOre, SubtypeOre> BLOCKS_ORE = new BulkDeferredHolder<>(SubtypeOre.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockOre(subtype)));
    public static final BulkDeferredHolder<Block, BlockOre, SubtypeOreDeepslate> BLOCKS_DEEPSLATEORE = new BulkDeferredHolder<>(SubtypeOreDeepslate.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockOre(subtype)));
    public static final BulkDeferredHolder<Block, Block, SubtypeRawOreBlock> BLOCKS_RAWORE = new BulkDeferredHolder<>(SubtypeRawOreBlock.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new Block(Blocks.STONE.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F))));
    public static final BulkDeferredHolder<Block, BlockMachine, SubtypeMachine> BLOCKS_MACHINE = new BulkDeferredHolder<>(SubtypeMachine.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockMachine(subtype)));
    public static final DeferredHolder<Block, BlockChemicalReactor> BLOCK_CHEMICALREACTOR = BLOCKS.register("chemicalreactor", BlockChemicalReactor::new);
    public static final BulkDeferredHolder<Block, BlockWire, SubtypeWire> BLOCKS_WIRE = new BulkDeferredHolder<>(SubtypeWire.values(), subtype -> {
        if(subtype.wireClass == WireClass.LOGISTICAL) {
            return BLOCKS.register(subtype.tag(), () -> new BlockLogisticalWire(subtype));
        } else {
            return BLOCKS.register(subtype.tag(), () -> new BlockWire(subtype));
        }
    });
    public static final BulkDeferredHolder<Block, BlockFluidPipe, SubtypeFluidPipe> BLOCKS_FLUIDPIPE = new BulkDeferredHolder<>(SubtypeFluidPipe.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockFluidPipe(subtype)));
    public static final BulkDeferredHolder<Block, BlockCustomGlass, SubtypeGlass> BLOCKS_CUSTOMGLASS = new BulkDeferredHolder<>(SubtypeGlass.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockCustomGlass(subtype)));
    public static final BulkDeferredHolder<Block, Block, SubtypeResourceBlock> BLOCKS_RESOURCE = new BulkDeferredHolder<>(SubtypeResourceBlock.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new Block(subtype.getProperties().strength(subtype.getHardness(), subtype.getResistance()).sound(subtype.getSoundType()))));
    public static final BulkDeferredHolder<Block, BlockGasPipe, SubtypeGasPipe> BLOCKS_GASPIPE = new BulkDeferredHolder<>(SubtypeGasPipe.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockGasPipe(subtype)));

    public static final DeferredHolder<Block, BlockMultiSubnode> BLOCK_MULTISUBNODE = BLOCKS.register("multisubnode", () -> new BlockMultiSubnode());
    public static final DeferredHolder<Block, BlockSeismicMarker> BLOCK_SEISMICMARKER = BLOCKS.register("seismicmarker", () -> new BlockSeismicMarker());
    public static final DeferredHolder<Block, BlockFrame> BLOCK_FRAME = BLOCKS.register("frame", () -> new BlockFrame(0));
    public static final DeferredHolder<Block, BlockFrame> BLOCK_FRAME_CORNER = BLOCKS.register("framecorner", () -> new BlockFrame(1));
    public static final DeferredHolder<Block, BlockLogisticalManager> BLOCK_LOGISTICALMANAGER = BLOCKS.register("logisticalmanager", () -> new BlockLogisticalManager());
    public static final DeferredHolder<Block, BlockCompressor> BLOCK_COMPRESSOR = BLOCKS.register("compressor", () -> new BlockCompressor(false));
    public static final DeferredHolder<Block, BlockCompressor> BLOCK_DECOMPRESSOR = BLOCKS.register("decompressor", () -> new BlockCompressor(true));
    public static final DeferredHolder<Block, BlockAdvancedCompressor> BLOCK_ADVANCEDCOMPRESSOR = BLOCKS.register("advancedcompressor", () -> new BlockAdvancedCompressor(false));
    public static final DeferredHolder<Block, BlockAdvancedCompressor> BLOCK_ADVANCEDDECOMPRESSOR = BLOCKS.register("advanceddecompressor", () -> new BlockAdvancedCompressor(true));
    public static final DeferredHolder<Block, BlockGasTransformerSide> BLOCK_COMPRESSOR_SIDE = BLOCKS.register("compressorside", () -> new BlockGasTransformerSide());
    public static final DeferredHolder<Block, BlockGasTransformerAddonTank> BLOCK_COMPRESSOR_ADDONTANK = BLOCKS.register("compressoraddontank", () -> new BlockGasTransformerAddonTank());
    public static final DeferredHolder<Block, BlockThermoelectricManipulator> BLOCK_THERMOELECTRICMANIPULATOR = BLOCKS.register("thermoelectricmanipulator", () -> new BlockThermoelectricManipulator());
    public static final DeferredHolder<Block, BlockAdvancedThermoelectricManipulator> BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR = BLOCKS.register("advancedthermoelectricmanipulator", () -> new BlockAdvancedThermoelectricManipulator());
    public static final DeferredHolder<Block, BlockValve> BLOCK_GASVALVE = BLOCKS.register("gasvalve", () -> new BlockValve(true));
    public static final DeferredHolder<Block, BlockValve> BLOCK_FLUIDVALVE = BLOCKS.register("fluidvalve", () -> new BlockValve(false));
    public static final DeferredHolder<Block, BlockPipePump> BLOCK_GASPIPEPUMP = BLOCKS.register("gaspipepump", () -> new BlockPipePump(true));
    public static final DeferredHolder<Block, BlockPipePump> BLOCK_FLUIDPIPEPUMP = BLOCKS.register("fluidpipepump", () -> new BlockPipePump(false));
    public static final DeferredHolder<Block, BlockPipeFilter> BLOCK_GASPIPEFILTER = BLOCKS.register("gaspipefilter", () -> new BlockPipeFilter(false));
    public static final DeferredHolder<Block, BlockPipeFilter> BLOCK_FLUIDPIPEFILTER = BLOCKS.register("fluidpipefilter", () -> new BlockPipeFilter(true));
    public static final DeferredHolder<Block, BlockScaffold> BLOCK_STEELSCAFFOLDING = BLOCKS.register("steelscaffold", () -> new BlockScaffold(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.METAL).noOcclusion()));
    public static final DeferredHolder<Block, BlockChemicalReactorExtra> BLOCK_CHEMICALREACTOREXTRA_MIDDLE = BLOCKS.register("chemicalreactorextramiddle", () -> new BlockChemicalReactorExtra(BlockChemicalReactorExtra.Location.MIDDLE));
    public static final DeferredHolder<Block, BlockChemicalReactorExtra> BLOCK_CHEMICALREACTOREXTRA_TOP = BLOCKS.register("chemicalreactorextratop", () -> new BlockChemicalReactorExtra(BlockChemicalReactorExtra.Location.TOP));

}
