package electrodynamics.common.tile.pipelines.fluid;

import electrodynamics.api.network.cable.type.IFluidPipe;
import electrodynamics.common.block.connect.BlockFluidPipe;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileFluidPipe extends GenericTileFluidPipe {
	public Property<Double> transmit = property(new Property<>(PropertyTypes.DOUBLE, "transmit", 0.0));

	public TileFluidPipe(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_PIPE.get(), pos, state);
	}

	public IFluidPipe pipe = null;

	@Override
	public IFluidPipe getCableType() {
		if (pipe == null) {
			pipe = ((BlockFluidPipe) getBlockState().getBlock()).pipe;
		}
		return pipe;
	}

}
