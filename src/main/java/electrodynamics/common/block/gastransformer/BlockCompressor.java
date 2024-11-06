package electrodynamics.common.block.gastransformer;

import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.GenericTileBasicCompressor;
import electrodynamics.prefab.block.GenericMachineBlock;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockCompressor extends GenericMachineBlock {

    public BlockCompressor(boolean isDecompressor) {
        super(isDecompressor ? GenericTileBasicCompressor.TileDecompressor::new : GenericTileBasicCompressor.TileCompressor::new);
        registerDefaultState(stateDefinition.any().setValue(BlockMachine.ON, false).setValue(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockMachine.ON);
        builder.add(ElectrodynamicsBlockStates.COMPRESSORSIDE_HAS_TOPTANK);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getStatusFromTop(context.getLevel(), context.getClickedPos(), super.getStateForPlacement(context).setValue(BlockMachine.ON, false));
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
        if (state.hasProperty(BlockMachine.ON) && state.getValue(BlockMachine.ON)) {
            return 15;
        }
        return super.getLightEmission(state, level, pos);
    }

}
