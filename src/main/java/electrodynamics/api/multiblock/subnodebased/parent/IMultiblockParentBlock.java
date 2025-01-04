package electrodynamics.api.multiblock.subnodebased.parent;

import electrodynamics.api.multiblock.subnodebased.Subnode;
import net.minecraft.core.BlockPos;
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
}
