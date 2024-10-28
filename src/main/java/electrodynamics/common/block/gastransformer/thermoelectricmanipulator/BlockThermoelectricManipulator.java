package electrodynamics.common.block.gastransformer.thermoelectricmanipulator;

import com.mojang.serialization.MapCodec;

import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.block.gastransformer.BlockGenericGasTransformer;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates.ManipulatorHeatingStatus;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.TileChemicalReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

public class BlockThermoelectricManipulator extends BlockGenericGasTransformer {

    public BlockThermoelectricManipulator() {
        super(TileChemicalReactor::new);
        registerDefaultState(stateDefinition.any().setValue(BlockMachine.ON, false).setValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.hasProperty(BlockMachine.ON) && state.getValue(BlockMachine.ON)) {
            return 15;
        }
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(BlockMachine.ON, false).setValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockMachine.ON);
        builder.add(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS);
    }
    
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
