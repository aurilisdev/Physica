package electrodynamics.common.tile.pipelines.tanks.fluid;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.registers.ElectrodynamicsTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileFluidTankSteel extends GenericTileFluidTank {

	public static final int CAPACITY = 8000;

	public TileFluidTankSteel(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTileTypes.TILE_TANKSTEEL.get(), CAPACITY, SubtypeMachine.tanksteel, pos, state);
	}

}
