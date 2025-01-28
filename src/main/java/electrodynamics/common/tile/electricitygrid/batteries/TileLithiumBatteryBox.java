package electrodynamics.common.tile.electricitygrid.batteries;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileLithiumBatteryBox extends TileBatteryBox {

	public TileLithiumBatteryBox(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_LITHIUMBATTERYBOX.get(), SubtypeMachine.lithiumbatterybox, 240, 359.0 * (2 * ElectrodynamicsCapabilities.DEFAULT_VOLTAGE) / 20.0, 40000000, worldPosition, blockState);
	}

}