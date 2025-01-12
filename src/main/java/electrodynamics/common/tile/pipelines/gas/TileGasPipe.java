package electrodynamics.common.tile.pipelines.gas;

import net.minecraft.core.HolderLookup;

import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasPipe extends GenericTileGasPipe {

	public SubtypeGasPipe pipe = null;

	public TileGasPipe(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_GAS_PIPE.get(), worldPos, blockState);
	}

	@Override
	public SubtypeGasPipe getCableType() {
		if (pipe == null) {
			pipe = ((BlockGasPipe) getBlockState().getBlock()).pipe;
		}
		return pipe;
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putInt("ord", getCableType().ordinal());
	}

	@Override
	protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		pipe = SubtypeGasPipe.values()[compound.getInt("ord")];
	}

}
