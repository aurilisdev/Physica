package electrodynamics.datagen.server.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import electrodynamics.api.multiblock.assemblybased.Multiblock;
import electrodynamics.api.multiblock.assemblybased.MultiblockSlaveNode;
import electrodynamics.client.ClientRegister;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.tile.machines.TileElectrolosisChamber;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

public class ElectrodynamicsMultiblockProvider extends JsonCodecProvider<Multiblock> {

    public ElectrodynamicsMultiblockProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper, String modid) {
        super(output, PackOutput.Target.DATA_PACK, References.ID + "/" + Multiblock.FOLDER, PackType.SERVER_DATA, Multiblock.CODEC, lookupProvider, modid, existingFileHelper);
    }

    public ElectrodynamicsMultiblockProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        this(output, lookupProvider, existingFileHelper, References.ID);
    }

    @Override
    protected void gather() {

        BlockState slave = ElectrodynamicsBlocks.BLOCK_MULTIBLOCK_SLAVE.get().defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false);
        BlockState scaffold = ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get().defaultBlockState();

        ResourceLocation ecWindow = Electrodynamics.rl("multiblockmodels/electrolosischamberwindow");
        ResourceLocation ecWindowTopC1 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindowc1");
        ResourceLocation ecWindowTopC2 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindowc2");
        ResourceLocation ecWindowTopC3 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindowc3");
        ResourceLocation ecWindowTopC4 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindowc4");
        ResourceLocation ecWindowTopS1 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindows1");
        ResourceLocation ecWindowTopS2 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindows2");
        ResourceLocation ecWindowTopS3 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindows3");
        ResourceLocation ecWindowTopS4 = Electrodynamics.rl("multiblockmodels/electrolosischamberwindows4");
        ResourceLocation ecCorner = Electrodynamics.rl("multiblockmodels/electrolosischambercorner");
        ResourceLocation ecVertSide = Electrodynamics.rl("multiblockmodels/electrolosischambervertside");
        ResourceLocation ecHorSideFB = Electrodynamics.rl("multiblockmodels/electrolosischamberhorsidefb");
        ResourceLocation ecHorSideLR = Electrodynamics.rl("multiblockmodels/electrolosischamberhorsidelr");
        ResourceLocation ecBottom = Electrodynamics.rl("multiblockmodels/electrolosischamberbottom");
        ResourceLocation ecPowerIn = Electrodynamics.rl("multiblockmodels/electrolosischamberpowerin");
        ResourceLocation ecFluidIn = Electrodynamics.rl("multiblockmodels/electrolosischamberfluidin");
        ResourceLocation ecFluidOut = Electrodynamics.rl("multiblockmodels/electrolosischamberfluidout");
        ResourceLocation ecCoilC1 = Electrodynamics.rl("multiblockmodels/electrolosischambercoilc1");
        ResourceLocation ecCoilC2 = Electrodynamics.rl("multiblockmodels/electrolosischambercoilc2");
        ResourceLocation ecCoilC3 = Electrodynamics.rl("multiblockmodels/electrolosischambercoilc3");
        ResourceLocation ecCoilC4 = Electrodynamics.rl("multiblockmodels/electrolosischambercoilc4");
        ResourceLocation ecCoilS1 = Electrodynamics.rl("multiblockmodels/electrolosischambercoils1");
        ResourceLocation ecCoilS2 = Electrodynamics.rl("multiblockmodels/electrolosischambercoils2");
        ResourceLocation ecCoilS3 = Electrodynamics.rl("multiblockmodels/electrolosischambercoils3");
        ResourceLocation ecCoilS4 = Electrodynamics.rl("multiblockmodels/electrolosischambercoils4");

        addMultiblock(Electrodynamics.rl("testing"), List.of(new MultiblockSlaveNode(slave, Blocks.OAK_LOG.defaultBlockState(), BlockTags.ACACIA_LOGS, new Vec3i(0, 0, 1), Shapes.block(), ClientRegister.MODEL_BATTERYBOX.id())));

        addMultiblock(TileElectrolosisChamber.ID, List.of(

                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, -1, -4), Shapes.block(), ecCorner),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-1, -1, -4), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(0, -1, -4), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(1, -1, -4), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, -1, -4), Shapes.block(), ecCorner),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, 0, -4), Shapes.block(), ecVertSide),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 0, -4), Shapes.block(), ecWindow),
                //
                new MultiblockSlaveNode(slave, Blocks.GOLD_BLOCK.defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(0, 0, -4), Shapes.block(), ecPowerIn),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(1, 0, -4), Shapes.block(), ecWindow),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, 0, -4), Shapes.block(), ecVertSide),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, 1, -4), Shapes.block(), ecCorner),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-1, 1, -4), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(0, 1, -4), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(1, 1, -4), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, 1, -4), Shapes.block(), ecCorner),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, -1, -3), Shapes.block(), ecHorSideLR),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-1, -1, -3), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(0, -1, -3), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(1, -1, -3), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, -1, -3), Shapes.block(), ecHorSideLR),
                //
                //
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(-2, 0, -3), Shapes.block(), ecWindow),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 0, -3), Shapes.block(), ecCoilC1),
                //
                new MultiblockSlaveNode(slave, Blocks.GOLD_BLOCK.defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(0, 0, -3), Shapes.block(), ecCoilS1),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(1, 0, -3), Shapes.block(), ecCoilC2),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(2, 0, -3), Shapes.block(), ecWindow),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, 1, -3), Shapes.block(), ecHorSideLR),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 1, -3), Shapes.block(), ecWindowTopC3),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(0, 1, -3), Shapes.block(), ecWindowTopS4),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(1, 1, -3), Shapes.block(), ecWindowTopC4),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, 1, -3), Shapes.block(), ecHorSideLR),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, -1, -2), Shapes.block(), ecHorSideLR),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-1, -1, -2), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(0, -1, -2), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(1, -1, -2), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, -1, -2), Shapes.block(), ecHorSideLR),
                //
                //
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(-2, 0, -2), Shapes.block(), ecFluidIn),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 0, -2), Shapes.block(), ecCoilS4),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(0, 0, -2), Shapes.block(), MultiblockSlaveNode.NOMODEL),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(1, 0, -2), Shapes.block(), ecCoilS2),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(2, 0, -2), Shapes.block(), ecFluidOut),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, 1, -2), Shapes.block(), ecHorSideLR),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 1, -2), Shapes.block(), ecWindowTopS3),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(0, 1, -2), Shapes.block(), MultiblockSlaveNode.NOMODEL),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(1, 1, -2), Shapes.block(), ecWindowTopS1),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, 1, -2), Shapes.block(), ecHorSideLR),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, -1, -1), Shapes.block(), ecHorSideLR),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-1, -1, -1), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(0, -1, -1), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(1, -1, -1), Shapes.block(), ecBottom),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, -1, -1), Shapes.block(), ecHorSideLR),
                //
                //
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(-2, 0, -1), Shapes.block(), ecWindow),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 0, -1), Shapes.block(), ecCoilC4),
                //
                new MultiblockSlaveNode(slave, Blocks.GOLD_BLOCK.defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(0, 0, -1), Shapes.block(), ecCoilS3),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.LIT, false), MultiblockSlaveNode.NOTAG, new Vec3i(1, 0, -1), Shapes.block(), ecCoilC3),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(2, 0, -1), Shapes.block(), ecWindow),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, 1, -1), Shapes.block(), ecHorSideLR),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 1, -1), Shapes.block(), ecWindowTopC2),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(0, 1, -1), Shapes.block(), ecWindowTopS2),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(1, 1, -1), Shapes.block(), ecWindowTopC1),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, 1, -1), Shapes.block(), ecHorSideLR),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, -1, 0), Shapes.block(), ecCorner),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-1, -1, 0), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(0, -1, 0), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(1, -1, 0), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, -1, 0), Shapes.block(), ecCorner),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, 0, 0), Shapes.block(), ecVertSide),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(-1, 0, 0), Shapes.block(), ecWindow),
                //
                new MultiblockSlaveNode(slave, ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).defaultBlockState(), MultiblockSlaveNode.NOTAG, new Vec3i(1, 0, 0), Shapes.block(), ecWindow),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, 0, 0), Shapes.block(), ecVertSide),
                //
                //
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-2, 1, 0), Shapes.block(), ecCorner),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(-1, 1, 0), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(0, 1, 0), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(1, 1, 0), Shapes.block(), ecHorSideFB),
                //
                new MultiblockSlaveNode(slave, scaffold, MultiblockSlaveNode.NOTAG, new Vec3i(2, 1, 0), Shapes.block(), ecCorner)


        ));


    }

    public void addMultiblock(ResourceLocation id, List<MultiblockSlaveNode> northFacingNodes) {

        HashMap<Direction, List<MultiblockSlaveNode>> nodeMap = new HashMap<>();

        nodeMap.put(Direction.NORTH, northFacingNodes);

        nodeMap.put(Direction.WEST, getRotatedNodes(northFacingNodes, Direction.WEST.get2DDataValue() - Direction.NORTH.get2DDataValue()));

        nodeMap.put(Direction.SOUTH, getRotatedNodes(nodeMap.get(Direction.WEST), Direction.SOUTH.get2DDataValue() - Direction.WEST.get2DDataValue()));

        nodeMap.put(Direction.EAST, getRotatedNodes(nodeMap.get(Direction.SOUTH), Direction.EAST.get2DDataValue() - Direction.SOUTH.get2DDataValue()));

        if (conditions.containsKey(id)) {
            throw new UnsupportedOperationException("Multiblock with id " + id.toString() + " already provided!");
        }

        conditions.put(id, new WithConditions<>(List.of(), new Multiblock(nodeMap)));
    }

    public static List<MultiblockSlaveNode> getRotatedNodes(List<MultiblockSlaveNode> slaveNodes, int delta2d) {

        List<MultiblockSlaveNode> returner = new ArrayList<>();
        BlockState placeState, replaceState;

        VoxelShape shape;

        final VoxelShape[] buffer = {Shapes.empty(), Shapes.empty()};

        Vec3i offset;

        int times = (delta2d + 4) % 4;

        for (MultiblockSlaveNode slaveNode : slaveNodes) {

            placeState = slaveNode.placeState();

            if (placeState.hasProperty(ElectrodynamicsBlockStates.FACING)) {
                placeState = placeState.setValue(ElectrodynamicsBlockStates.FACING, placeState.getValue(ElectrodynamicsBlockStates.FACING).getCounterClockWise());
            }

            replaceState = slaveNode.replaceState();

            if (replaceState.hasProperty(ElectrodynamicsBlockStates.FACING)) {
                replaceState = replaceState.setValue(ElectrodynamicsBlockStates.FACING, replaceState.getValue(ElectrodynamicsBlockStates.FACING).getCounterClockWise());
            }

            shape = slaveNode.renderShape();

            buffer[0] = shape;
            buffer[1] = Shapes.empty();

            for (int i = 0; i < times; i++) {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }

            shape = buffer[0];

            offset = rotateVector(Rotation.COUNTERCLOCKWISE_90, slaveNode.offset());

            returner.add(new MultiblockSlaveNode(placeState, replaceState, slaveNode.taggedBlocks(), offset, shape, slaveNode.model()));
        }

        return returner;

    }

    public static Vec3i rotateVector(Rotation rot, Vec3i original) {

        switch (rot) {
            case NONE:
            default:
                return original;
            case CLOCKWISE_90:
                return new Vec3i(-original.getZ(), original.getY(), original.getX());
            case CLOCKWISE_180:
                return new Vec3i(-original.getX(), original.getY(), -original.getZ());
            case COUNTERCLOCKWISE_90:
                return new Vec3i(original.getZ(), original.getY(), -original.getX());
        }

    }

}
