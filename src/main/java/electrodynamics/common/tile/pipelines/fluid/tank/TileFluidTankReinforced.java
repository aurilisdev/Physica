package electrodynamics.common.tile.pipelines.fluid.tank;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileFluidTankReinforced extends GenericTileFluidTank {

	public static final int CAPACITY = 32000;

	public TileFluidTankReinforced(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_TANKREINFORCED.get(), CAPACITY, SubtypeMachine.tankreinforced, pos, state);
	}
}
