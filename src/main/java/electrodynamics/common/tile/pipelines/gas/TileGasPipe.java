package electrodynamics.common.tile.pipelines.gas;

import electrodynamics.api.network.cable.type.IGasPipe;
import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasPipe extends GenericTileGasPipe {

	public IGasPipe pipe = null;

	public TileGasPipe(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_GAS_PIPE.get(), worldPos, blockState);
	}

	@Override
	public IGasPipe getCableType() {
		if (pipe == null) {
			pipe = ((BlockGasPipe) getBlockState().getBlock()).pipe;
		}
		return pipe;
	}

}
