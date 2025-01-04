package electrodynamics.prefab.utilities;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class BlockEntityUtils {

	public static final BlockPos OUT_OF_REACH = new BlockPos(0, -1000, 0);
	public static final int[][] RELATIVE_MATRIX = {
			//DUNSWE
			{ 3, 2, 1, 0, 5, 4 }, //D Note these really don't work; you need two variables to track this - skip999
			{ 4, 5, 0, 1, 2, 3 }, //U Note these really don't work; you need two variables to track this - skip999
			{ 0, 1, 2, 3, 4, 5 }, //N
			{ 0, 1, 3, 2, 5, 4 }, //S
			{ 0, 1, 4, 5, 3, 2 },
			{ 0, 1, 5, 4, 2, 3 }, //W
			  //E
	};

	public static Direction getRelativeSide(Direction facingDirection, Direction relativeDirection) {
		if (facingDirection == null || relativeDirection == null) {
			return Direction.UP;
		}
		return Direction.values()[RELATIVE_MATRIX[facingDirection.ordinal()][relativeDirection.ordinal()]];
	}

	public static void updateLit(GenericTile tile, Boolean value) {
		Level world = tile.getLevel();
		BlockPos pos = tile.getBlockPos();
		if (tile.getBlockState().hasProperty(ElectrodynamicsBlockStates.LIT)) {
			world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ElectrodynamicsBlockStates.LIT, value));
		}
	}

	public static boolean isLit(GenericTile tile) {
		if (tile.getBlockState().hasProperty(ElectrodynamicsBlockStates.LIT)) {
			return tile.getBlockState().getValue(ElectrodynamicsBlockStates.LIT);
		}
		return false;
	}

	public static Direction directionFromPos(BlockPos thisPos, BlockPos otherPos){
		return Direction.fromDelta(otherPos.getX() - thisPos.getX(), otherPos.getY() - thisPos.getY(), otherPos.getZ() - thisPos.getZ());
	}


	/**
	 * This enum is used as a wrapper for the Vanilla directions to make the directions used by machine IO a little
	 * easier to understand. The perspectives are defined from that of the machine i.e. the front of the machine is
	 * facing north.
	 *
	 *
	 * @author skip999
	 */
	public static enum MachineDirection {
		BOTTOM(Direction.DOWN), TOP(Direction.UP), FRONT(Direction.NORTH), BACK(Direction.SOUTH), LEFT(Direction.WEST), RIGHT(Direction.EAST);

		public final Direction mappedDir;
		private MachineDirection(Direction mappedDir) {
			this.mappedDir = mappedDir;
		}

		public static Direction[] toDirectionArray(MachineDirection...machineDirections){
			Direction[] dirs = new Direction[machineDirections.length];
			for(int i = 0; i < machineDirections.length; i++) {
				dirs[i] = machineDirections[i].mappedDir;
			}
			return dirs;
		}

		public static MachineDirection fromDirection(Direction dir) {
			return values()[dir.ordinal()];
		}
	}

}
