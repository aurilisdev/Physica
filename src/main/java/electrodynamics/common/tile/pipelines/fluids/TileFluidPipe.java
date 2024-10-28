package electrodynamics.common.tile.pipelines.fluids;

import electrodynamics.prefab.properties.PropertyTypes;
import net.minecraft.core.HolderLookup;

import electrodynamics.common.block.connect.BlockFluidPipe;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.prefab.properties.Property;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class TileFluidPipe extends GenericTileFluidPipe {
	public Property<Double> transmit = property(new Property<>(PropertyTypes.DOUBLE, "transmit", 0.0));

	public TileFluidPipe(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_PIPE.get(), pos, state);
	}

	public SubtypeFluidPipe pipe = null;

	@Override
	public SubtypeFluidPipe getPipeType() {
		if (pipe == null) {
			pipe = ((BlockFluidPipe) getBlockState().getBlock()).pipe;
		}
		return pipe;
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		compound.putInt("ord", getPipeType().ordinal());
		super.saveAdditional(compound, registries);
	}

	@Override
	protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		pipe = SubtypeFluidPipe.values()[compound.getInt("ord")];
	}

}
