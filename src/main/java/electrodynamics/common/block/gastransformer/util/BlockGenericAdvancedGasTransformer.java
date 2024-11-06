package electrodynamics.common.block.gastransformer.util;

import com.mojang.serialization.MapCodec;

import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericMachineBlock;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class BlockGenericAdvancedGasTransformer extends GenericMachineBlock {

    public BlockGenericAdvancedGasTransformer(BlockEntitySupplier<BlockEntity> blockEntitySupplier) {
        super(blockEntitySupplier);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!state.hasProperty(GenericEntityBlock.FACING)) {
            return false;
        }
        Direction facing = state.getValue(GenericEntityBlock.FACING);
        BlockState left = level.getBlockState(pos.relative(BlockEntityUtils.getRelativeSide(facing, Direction.WEST)));
        BlockState right = level.getBlockState(pos.relative(BlockEntityUtils.getRelativeSide(facing, Direction.EAST)));
        return left.isAir() && right.isAir() && super.canSurvive(state, level, pos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        Direction facing = state.getValue(GenericEntityBlock.FACING);
        level.setBlockAndUpdate(pos.relative(BlockEntityUtils.getRelativeSide(facing, Direction.WEST)), ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false).setValue(GenericEntityBlock.FACING, facing));
        level.setBlockAndUpdate(pos.relative(BlockEntityUtils.getRelativeSide(facing, Direction.EAST)), ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false).setValue(GenericEntityBlock.FACING, facing));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
