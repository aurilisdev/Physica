package electrodynamics.common.block.gastransformer.thermoelectricmanipulator;

import com.mojang.serialization.MapCodec;

import electrodynamics.common.block.gastransformer.util.BlockGenericAdvancedGasTransformer;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates.ManipulatorHeatingStatus;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.TileAdvancedThermoelectricManipulator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

public class BlockAdvancedThermoelectricManipulator extends BlockGenericAdvancedGasTransformer {

    public BlockAdvancedThermoelectricManipulator() {
        super(TileAdvancedThermoelectricManipulator::new);
        registerDefaultState(stateDefinition.any().setValue(ElectrodynamicsBlockStates.LIT, false).setValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.hasProperty(ElectrodynamicsBlockStates.LIT) && state.getValue(ElectrodynamicsBlockStates.LIT)) {
            return 15;
        }
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(ElectrodynamicsBlockStates.LIT, false).setValue(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS, ManipulatorHeatingStatus.OFF);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ElectrodynamicsBlockStates.LIT);
        builder.add(ElectrodynamicsBlockStates.MANIPULATOR_HEATING_STATUS);
    }
    
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
