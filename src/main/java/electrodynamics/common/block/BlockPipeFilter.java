package electrodynamics.common.block;

import com.mojang.serialization.MapCodec;

import electrodynamics.common.tile.pipelines.fluid.TileFluidPipeFilter;
import electrodynamics.common.tile.pipelines.gas.TileGasPipeFilter;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPipeFilter extends GenericMachineBlock {

    private static final VoxelShape GAS_NS = Shapes.or(Block.box(4, 4, 0, 12, 12, 16), Block.box(4, 3, 4, 12, 15, 12));
    private static final VoxelShape GAS_EW = Shapes.or(Block.box(0, 4, 4, 16, 12, 12), Block.box(4, 3, 4, 12, 15, 12));

    private static final VoxelShape FLUID_NS = Shapes.or(Block.box(5, 5, 0, 11, 11, 16), Block.box(4, 3, 4, 12, 15, 12));
    private static final VoxelShape FLUID_EW = Shapes.or(Block.box(0, 5, 5, 16, 11, 11), Block.box(4, 3, 4, 12, 15, 12));

    private final boolean isFluid;

    public BlockPipeFilter(boolean isFluid) {
        super(isFluid ? TileFluidPipeFilter::new : TileGasPipeFilter::new);
        this.isFluid = isFluid;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        Direction facing = state.getValue(GenericEntityBlock.FACING);

        if (facing == Direction.EAST || facing == Direction.WEST) {

            if (isFluid) {
                return FLUID_EW;
            }

            return GAS_EW;

        }
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {

            if (isFluid) {
                return FLUID_NS;
            }

            return GAS_NS;

        }

        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }
}
