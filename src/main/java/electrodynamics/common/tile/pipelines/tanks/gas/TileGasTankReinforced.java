package electrodynamics.common.tile.pipelines.tanks.gas;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasTankReinforced extends GenericTileGasTank {

	public TileGasTankReinforced(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTileTypes.TILE_GASTANK_REINFORCED.get(), pos, state, SubtypeMachine.gastankreinforced, 32000, 1024, 1000);
	}

}
