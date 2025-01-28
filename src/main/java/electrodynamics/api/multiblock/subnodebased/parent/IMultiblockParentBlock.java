package electrodynamics.api.multiblock.subnodebased.parent;

import java.util.HashMap;

import javax.annotation.Nullable;

import electrodynamics.api.multiblock.subnodebased.Subnode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface IMultiblockParentBlock {

	boolean hasMultiBlock();

	default boolean isValidMultiblockPlacement(BlockState state, LevelReader worldIn, BlockPos pos, Subnode[] nodes) {
		for (Subnode sub : nodes) {
			BlockPos check = pos.offset(sub.pos());
			if (!worldIn.getBlockState(check).canBeReplaced()) {
				return false;
			}
		}
		return true;
	}

	public static class SubnodeWrapper {

		public static final SubnodeWrapper EMPTY = new SubnodeWrapper(new Subnode[0]);
		private HashMap<Direction, Subnode[]> subnodeMap = new HashMap<>();
		private Subnode[] omni = null;

		private SubnodeWrapper(Subnode[] omni) {
			this.omni = omni;
		}

		private SubnodeWrapper(Subnode[] north, Subnode[] east, Subnode[] south, Subnode[] west) {
			subnodeMap.put(Direction.NORTH, north);
			subnodeMap.put(Direction.EAST, east);
			subnodeMap.put(Direction.SOUTH, south);
			subnodeMap.put(Direction.WEST, west);
		}

		public Subnode[] getSubnodes(@Nullable Direction dir) {
			if(omni != null) {
				return omni;
			}
			return subnodeMap.getOrDefault(dir, new Subnode[0]);
		}

		public static SubnodeWrapper createOmni(Subnode[] omni) {
			return new SubnodeWrapper(omni);
		}

		public static SubnodeWrapper createDirectional(Subnode[] north, Subnode[] east, Subnode[] south, Subnode[] west) {
			return new SubnodeWrapper(north, east, south, west);
		}


	}

}
