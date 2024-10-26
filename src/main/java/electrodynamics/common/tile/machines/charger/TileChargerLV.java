package electrodynamics.common.tile.machines.charger;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileChargerLV extends GenericTileCharger {

	public TileChargerLV(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTileTypes.TILE_CHARGERLV.get(), 1, SubtypeMachine.chargerlv, worldPosition, blockState);
	}

}
