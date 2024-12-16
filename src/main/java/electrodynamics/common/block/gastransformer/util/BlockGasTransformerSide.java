package electrodynamics.common.block.gastransformer.util;

import com.mojang.serialization.MapCodec;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerSideBlock;
import electrodynamics.prefab.block.GenericMachineBlock;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

//Separate class so item isn't registered
public class BlockGasTransformerSide extends GenericMachineBlock {

    public BlockGasTransformerSide() {
        super(TileGasTransformerSideBlock::new, VoxelShapeProvider.DEFAULT);
        registerDefaultState(stateDefinition.any().setValue(ElectrodynamicsBlockStates.LIT, false).setValue(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ElectrodynamicsBlockStates.LIT);
        builder.add(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getStatusFromTop(context.getLevel(), context.getClickedPos(), super.getStateForPlacement(context).setValue(ElectrodynamicsBlockStates.LIT, false));
    }

    public BlockState getStatusFromTop(Level world, BlockPos pos, BlockState baseState) {
        if (!baseState.hasProperty(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK)) {
            return baseState;
        }
        if (world.getBlockState(pos.above()).is(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK)) {
            return baseState.setValue(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, true);
        }
        return baseState.setValue(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        if (level instanceof Level world) {
            world.setBlockAndUpdate(pos, getStatusFromTop(world, pos, state));
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.hasProperty(ElectrodynamicsBlockStates.LIT) && state.getValue(ElectrodynamicsBlockStates.LIT)) {
            return 15;
        }
        return super.getLightEmission(state, level, pos);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
