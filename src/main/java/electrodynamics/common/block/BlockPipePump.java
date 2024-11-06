package electrodynamics.common.block;

import com.mojang.serialization.MapCodec;

import electrodynamics.common.tile.pipelines.fluid.TileFluidPipePump;
import electrodynamics.common.tile.pipelines.gas.TileGasPipePump;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPipePump extends GenericMachineBlock {

    private final boolean isGas;

    private static final VoxelShape GAS_NS = Block.box(4, 4, 0, 12, 12, 16);
    private static final VoxelShape GAS_EW = Block.box(0, 4, 4, 16, 12, 12);

    private static final VoxelShape FLUID_NS = Block.box(5, 5, 0, 11, 11, 16);
    private static final VoxelShape FLUID_EW = Block.box(0, 5, 5, 16, 11, 11);

    public BlockPipePump(boolean isGas) {
        super(isGas ? TileGasPipePump::new : TileFluidPipePump::new);
        this.isGas = isGas;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        Direction facing = state.getValue(GenericEntityBlock.FACING);

        if (facing == Direction.EAST || facing == Direction.WEST) {

            if (isGas) {
                return GAS_EW;
            }

            return FLUID_EW;

        }
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {

            if (isGas) {
                return GAS_NS;
            }

            return FLUID_NS;

        }

        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
