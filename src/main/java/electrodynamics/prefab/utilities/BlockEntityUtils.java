package electrodynamics.prefab.utilities;

import electrodynamics.common.block.BlockMachine;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class BlockEntityUtils {

	public static final BlockPos OUT_OF_REACH = new BlockPos(0, -1000, 0);
	public static final int[][] RELATIVE_MATRIX = { { 3, 2, 1, 0, 5, 4 }, { 4, 5, 0, 1, 2, 3 }, { 0, 1, 3, 2, 4, 5 }, { 0, 1, 2, 3, 5, 4 }, { 0, 1, 5, 4, 3, 2 }, { 0, 1, 4, 5, 2, 3 } };

	public static Direction getRelativeSide(Direction main, Direction relative) {
		if (main == null || relative == null) {
			return Direction.UP;
		}
		return Direction.from3DDataValue(RELATIVE_MATRIX[main.ordinal()][relative.ordinal()]);
	}

	public static void updateLit(GenericTile tile, Boolean value) {
		Level world = tile.getLevel();
		BlockPos pos = tile.getBlockPos();
		if (tile.getBlockState().hasProperty(BlockMachine.ON)) {
			world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(BlockMachine.ON, value));
		}
	}

	public static boolean isLit(GenericTile tile) {
		if (tile.getBlockState().hasProperty(BlockMachine.ON)) {
			return tile.getBlockState().getValue(BlockMachine.ON);
		}
		return false;
	}

	public static Direction directionFromPos(BlockPos thisPos, BlockPos otherPos){
		return Direction.fromDelta(otherPos.getX() - thisPos.getX(), otherPos.getY() - thisPos.getY(), otherPos.getZ() - thisPos.getZ());
	}

}
