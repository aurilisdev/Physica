package electrodynamics.common.tile.pipelines.gas.tank;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasTankHSLA extends GenericTileGasTank {

	public TileGasTankHSLA(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_GASTANK_HSLA.get(), pos, state, SubtypeMachine.gastankhsla, 128000, 1024, 1000);
	}

}
