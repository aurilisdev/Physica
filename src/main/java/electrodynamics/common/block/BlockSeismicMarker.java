package electrodynamics.common.block;

import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;

import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import electrodynamics.common.tile.machines.quarry.TileSeismicMarker;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;

public class BlockSeismicMarker extends GenericMachineBlock {

    private static final VoxelShapeProvider AABB = VoxelShapeProvider.createOmni(
            //
            Stream.of(
                    //
                    Block.box(4, 0, 4, 12, 1, 12),
                    //
                    Block.box(6, 1, 6, 10, 8, 10),
                    //
                    Block.box(6, 9, 6, 7, 11, 7),
                    //
                    Block.box(9, 9, 6, 10, 11, 7),
                    //
                    Block.box(9, 9, 9, 10, 11, 10),
                    //
                    Block.box(6, 9, 9, 7, 11, 10),
                    //
                    Block.box(5, 11, 5, 11, 12, 11),
                    //
                    Block.box(5, 8, 5, 11, 9, 11),
                    //
                    Block.box(7, 9, 7, 9, 11, 9)
                    //
            ).reduce(Shapes::or).get());

    public BlockSeismicMarker() {
        super(TileSeismicMarker::new, AABB);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return canSupportCenter(reader, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState other, LevelAccessor world, BlockPos pos, BlockPos otherpos) {
        return dir == Direction.DOWN && !canSurvive(state, world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, other, world, pos, otherpos);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
