package electrodynamics.common.tile.pipelines.tanks.fluid;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileFluidTankReinforced extends GenericTileFluidTank {

	public static final int CAPACITY = 32000;

	public TileFluidTankReinforced(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTileTypes.TILE_TANKREINFORCED.get(), CAPACITY, SubtypeMachine.tankreinforced, pos, state);
	}
}
