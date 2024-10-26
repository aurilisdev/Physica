package electrodynamics.common.tile.machines.charger;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileChargerMV extends GenericTileCharger {

	public TileChargerMV(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTileTypes.TILE_CHARGERMV.get(), 2, SubtypeMachine.chargermv, worldPosition, blockState);
	}

}
