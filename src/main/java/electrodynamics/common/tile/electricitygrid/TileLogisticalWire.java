package electrodynamics.common.tile.electricitygrid;

import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileLogisticalWire extends TileWire {

	public boolean isPowered = false;

	public TileLogisticalWire(BlockPos pos, BlockState state) {
		super(ElectrodynamicsBlockTypes.TILE_LOGISTICALWIRE.get(), pos, state);
		forceComponent(new ComponentTickable(this).tickServer(this::tickServer));
	}

	protected void tickServer(ComponentTickable component) {
		if (component.getTicks() % 10 == 0) {
			boolean shouldPower = getNetwork().getActiveTransmitted() > 0;
			if (shouldPower != isPowered) {
				isPowered = shouldPower;
				level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
				BlockEntityUtils.updateLit(this, isPowered);
			}
		}
	}
}
