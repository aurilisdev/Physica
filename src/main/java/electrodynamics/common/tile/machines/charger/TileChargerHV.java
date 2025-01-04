package electrodynamics.common.tile.machines.charger;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileChargerHV extends GenericTileCharger {

	public TileChargerHV(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_CHARGERHV.get(), 4, SubtypeMachine.chargerhv, worldPosition, blockState);
	}

}
