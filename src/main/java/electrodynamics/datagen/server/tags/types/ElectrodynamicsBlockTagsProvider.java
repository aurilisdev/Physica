package electrodynamics.datagen.server.tags.types;

import java.util.concurrent.CompletableFuture;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ElectrodynamicsBlockTagsProvider extends BlockTagsProvider {

    public ElectrodynamicsBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, References.ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                //
                .add(ElectrodynamicsBlocks.BLOCKS_ORE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RAWORE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_MACHINE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_WIRE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_FLUIDPIPE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_GASVALVE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FLUIDVALVE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_GASPIPEPUMP.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_GASPIPEFILTER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FLUIDPIPEFILTER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FLUIDPIPEPUMP.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_MIDDLE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_TOP.get());

        tag(BlockTags.MINEABLE_WITH_HOE)
                //
                .add(ElectrodynamicsBlocks.BLOCK_FRAME.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get());

        tag(Tags.Blocks.NEEDS_WOOD_TOOL)
                //
                .add(ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_ORE.getSpecificValuesArray(SubtypeOre.getOreForMiningLevel(0)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getSpecificValuesArray(SubtypeOreDeepslate.getOreForMiningLevel(0)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RAWORE.getSpecificValuesArray(SubtypeRawOreBlock.getForMiningLevel(0)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getSpecificValuesArray(SubtypeResourceBlock.getForMiningLevel(0)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_WIRE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FRAME.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                //
                .add(ElectrodynamicsBlocks.BLOCKS_MACHINE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_FLUIDPIPE.getAllValuesArray())
                //
                .add(ElectrodynamicsBlocks.BLOCKS_ORE.getSpecificValuesArray(SubtypeOre.getOreForMiningLevel(1)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getSpecificValuesArray(SubtypeOreDeepslate.getOreForMiningLevel(1)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RAWORE.getSpecificValuesArray(SubtypeRawOreBlock.getForMiningLevel(1)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getSpecificValuesArray(SubtypeResourceBlock.getForMiningLevel(1)))
                //
                .add(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_GASVALVE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FLUIDVALVE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_GASPIPEPUMP.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_GASPIPEFILTER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FLUIDPIPEFILTER.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_FLUIDPIPEPUMP.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_MIDDLE.get())
                //
                .add(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_TOP.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                //
                .add(ElectrodynamicsBlocks.BLOCKS_ORE.getSpecificValuesArray(SubtypeOre.getOreForMiningLevel(2)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getSpecificValuesArray(SubtypeOreDeepslate.getOreForMiningLevel(2)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RAWORE.getSpecificValuesArray(SubtypeRawOreBlock.getForMiningLevel(2)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getSpecificValuesArray(SubtypeResourceBlock.getForMiningLevel(2)));

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                //
                .add(ElectrodynamicsBlocks.BLOCKS_ORE.getSpecificValuesArray(SubtypeOre.getOreForMiningLevel(3)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getSpecificValuesArray(SubtypeOreDeepslate.getOreForMiningLevel(3)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RAWORE.getSpecificValuesArray(SubtypeRawOreBlock.getForMiningLevel(3)))
                //
                .add(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getSpecificValuesArray(SubtypeResourceBlock.getForMiningLevel(3)));

        for (SubtypeOre ore : SubtypeOre.values()) {
            tag(ore.blockTag).add(ElectrodynamicsBlocks.BLOCKS_ORE.getValue(ore));
        }

        for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
            tag(ore.blockTag).add(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getValue(ore));
        }

        for (SubtypeResourceBlock storage : SubtypeResourceBlock.values()) {
            tag(storage.blockTag).add(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getValue(storage));
        }

        for (SubtypeRawOreBlock block : SubtypeRawOreBlock.values()) {
            tag(block.blockTag).add(ElectrodynamicsBlocks.BLOCKS_RAWORE.getValue(block));
        }

        TagAppender<Block> ores = tag(ElectrodynamicsTags.Blocks.ORES);

        for (SubtypeOre ore : SubtypeOre.values()) {
            ores.addTag(ore.blockTag);
        }

        tag(ElectrodynamicsTags.Blocks.ELECTRIC_DRILL_BLOCKS).addTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL);
    }

}
