package electrodynamics.common.tile.pipelines.gas.tank;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasTankSteel extends GenericTileGasTank {

	public TileGasTankSteel(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_GASTANK_STEEL.get(), pos, state, SubtypeMachine.gastanksteel, 8000, 1024, 1000);
	}

}
