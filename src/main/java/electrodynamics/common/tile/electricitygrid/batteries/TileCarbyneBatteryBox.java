package electrodynamics.common.tile.electricitygrid.batteries;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTileTypes;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileCarbyneBatteryBox extends TileBatteryBox {

	public TileCarbyneBatteryBox(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTileTypes.TILE_CARBYNEBATTERYBOX.get(), SubtypeMachine.carbynebatterybox, 480, 359.0 * (4 * ElectrodynamicsCapabilities.DEFAULT_VOLTAGE) / 20.0, 80000000, worldPosition, blockState);
	}

}