package electrodynamics.common.tile.pipelines.fluid.tank;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileFluidTankSteel extends GenericTileFluidTank {

	public static final int CAPACITY = 8000;

	public TileFluidTankSteel(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_TANKSTEEL.get(), CAPACITY, SubtypeMachine.tanksteel, pos, state);
	}

}
