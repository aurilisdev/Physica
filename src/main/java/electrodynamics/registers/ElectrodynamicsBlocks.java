package electrodynamics.registers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import electrodynamics.api.ISubtype;
import electrodynamics.api.References;
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
import electrodynamics.common.block.connect.BlockFluidPipe;
import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.common.block.connect.BlockLogisticalWire;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.connect.util.BlockScaffold;
import electrodynamics.common.block.gastransformer.BlockGasTransformerAddonTank;
import electrodynamics.common.block.gastransformer.BlockGasTransformerSide;
import electrodynamics.common.block.gastransformer.compressor.BlockCompressor;
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
    public static final HashMap<ISubtype, DeferredHolder<Block, ? extends Block>> SUBTYPEBLOCKREGISTER_MAPPINGS = new HashMap<>();

    public static BlockMultiSubnode multi;
    public static BlockSeismicMarker blockSeismicMarker;
    public static BlockFrame blockFrame;
    public static BlockFrame blockFrameCorner;
    public static BlockLogisticalManager blockLogisticalManager;

    public static BlockCompressor blockCompressor;
    public static BlockCompressor blockDecompressor;

    public static BlockThermoelectricManipulator blockThermoelectricManipulator;

    public static BlockGasTransformerSide blockGasTransformerSide;
    public static BlockGasTransformerAddonTank blockGasTransformerAddonTank;

    public static BlockValve blockGasValve;
    public static BlockValve blockFluidValve;

    public static BlockPipePump blockGasPipePump;
    public static BlockPipePump blockFluidPipePump;

    public static BlockPipeFilter blockGasPipeFilter;
    public static BlockPipeFilter blockFluidPipeFilter;

    public static BlockScaffold blockSteelScaffold;

    static {
        for (SubtypeOre subtype : SubtypeOre.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new BlockOre(subtype)));
        }
        for (SubtypeOreDeepslate subtype : SubtypeOreDeepslate.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new BlockOre(subtype)));
        }
        for (SubtypeRawOreBlock subtype : SubtypeRawOreBlock.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new Block(Blocks.STONE.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F))));
        }
        for (SubtypeMachine subtype : SubtypeMachine.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new BlockMachine(subtype)));
        }

        for (SubtypeWire subtype : SubtypeWire.values()) {
            if (subtype.wireClass == WireClass.LOGISTICAL) {
                SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new BlockLogisticalWire(subtype)));
            } else {
                SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new BlockWire(subtype)));
            }
        }
        for (SubtypeFluidPipe subtype : SubtypeFluidPipe.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new BlockFluidPipe(subtype)));
        }
        for (SubtypeGlass subtype : SubtypeGlass.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new BlockCustomGlass(subtype)));
        }
        for (SubtypeResourceBlock subtype : SubtypeResourceBlock.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), () -> new Block(subtype.getProperties().strength(subtype.getHardness(), subtype.getResistance()).sound(subtype.getSoundType()))));
        }
        for (SubtypeGasPipe pipe : SubtypeGasPipe.values()) {
            SUBTYPEBLOCKREGISTER_MAPPINGS.put(pipe, BLOCKS.register(pipe.tag(), () -> new BlockGasPipe(pipe)));
        }
    }

    public static final DeferredHolder<Block, Block> BLOCK_MULTISUBNODE = BLOCKS.register("multisubnode", () -> multi = new BlockMultiSubnode());
    public static final DeferredHolder<Block, Block> BLOCK_SEISMICMARKER = BLOCKS.register("seismicmarker", () -> blockSeismicMarker = new BlockSeismicMarker());
    public static final DeferredHolder<Block, Block> BLOCK_FRAME = BLOCKS.register("frame", () -> blockFrame = new BlockFrame(0));
    public static final DeferredHolder<Block, Block> BLOCK_FRAME_CORNER = BLOCKS.register("framecorner", () -> blockFrameCorner = new BlockFrame(1));
    public static final DeferredHolder<Block, Block> BLOCK_LOGISTICALMANAGER = BLOCKS.register("logisticalmanager", () -> blockLogisticalManager = new BlockLogisticalManager());
    public static final DeferredHolder<Block, Block> BLOCK_COMPRESSOR = BLOCKS.register("compressor", () -> blockCompressor = new BlockCompressor(false));
    public static final DeferredHolder<Block, Block> BLOCK_DECOMPRESSOR = BLOCKS.register("decompressor", () -> blockDecompressor = new BlockCompressor(true));
    public static final DeferredHolder<Block, Block> BLOCK_COMPRESSOR_SIDE = BLOCKS.register("compressorside", () -> blockGasTransformerSide = new BlockGasTransformerSide());
    public static final DeferredHolder<Block, Block> BLOCK_COMPRESSOR_ADDONTANK = BLOCKS.register("compressoraddontank", () -> blockGasTransformerAddonTank = new BlockGasTransformerAddonTank());
    public static final DeferredHolder<Block, Block> BLOCK_THERMOELECTRICMANIPULATOR = BLOCKS.register("thermoelectricmanipulator", () -> blockThermoelectricManipulator = new BlockThermoelectricManipulator());
    public static final DeferredHolder<Block, Block> BLOCK_GASVALVE = BLOCKS.register("gasvalve", () -> blockGasValve = new BlockValve(true));
    public static final DeferredHolder<Block, Block> BLOCK_FLUIDVALVE = BLOCKS.register("fluidvalve", () -> blockFluidValve = new BlockValve(false));
    public static final DeferredHolder<Block, Block> BLOCK_GASPIPEPUMP = BLOCKS.register("gaspipepump", () -> blockGasPipePump = new BlockPipePump(true));
    public static final DeferredHolder<Block, Block> BLOCK_FLUIDPIPEPUMP = BLOCKS.register("fluidpipepump", () -> blockFluidPipePump = new BlockPipePump(false));
    public static final DeferredHolder<Block, Block> BLOCK_GASPIPEFILTER = BLOCKS.register("gaspipefilter", () -> blockGasPipeFilter = new BlockPipeFilter(false));
    public static final DeferredHolder<Block, Block> BLOCK_FLUIDPIPEFILTER = BLOCKS.register("fluidpipefilter", () -> blockFluidPipeFilter = new BlockPipeFilter(true));

    public static final DeferredHolder<Block, Block> BLOCK_STEELSCAFFOLDING = BLOCKS.register("steelscaffold", () -> blockSteelScaffold = new BlockScaffold(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.METAL).noOcclusion()));

    public static Block[] getAllBlockForSubtype(ISubtype[] values) {
        List<Block> list = new ArrayList<>();
        for (ISubtype value : values) {
            list.add(SUBTYPEBLOCKREGISTER_MAPPINGS.get(value).get());
        }
        return list.toArray(new Block[] {});
    }

    public static Block getBlock(ISubtype value) {
        return SUBTYPEBLOCKREGISTER_MAPPINGS.get(value).get();
    }

}
