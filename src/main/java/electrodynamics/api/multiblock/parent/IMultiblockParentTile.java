package electrodynamics.api.multiblock.parent;

import org.jetbrains.annotations.NotNull;

import electrodynamics.api.multiblock.Subnode;
import electrodynamics.api.multiblock.child.IMultiblockChildBlock;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public interface IMultiblockParentTile {

	/*
	 * Okay so hear me out:
	 * 
	 * Yes, using an array is a little more confusing, however it gives a fixed index to a subnode
	 * 
	 * This is useful as it means a subnode can look itself up on the parent tile really quickly and reliably if there's a data point
	 * it needs to reference
	 * 
	 * 
	 */

	default void onNodeReplaced(Level world, BlockPos pos, boolean update) {

		Subnode[] subnodes = getSubNodes();

		Subnode subnode;

		BlockPos offset, subnodePos;

		BlockState subnodeState;

		Block subnodeBlock;

		for (int i = 0; i < subnodes.length; i++) {

			subnode = subnodes[i];

			subnodePos = subnode.pos();
			offset = pos.offset(subnodePos);
			subnodeState = world.getBlockState(offset);
			subnodeBlock = subnodeState.getBlock();

			if (update) {
				if (subnodeState.canBeReplaced()) {
					world.setBlockAndUpdate(offset, ElectrodynamicsBlocks.multi.defaultBlockState());
				}
				TileMultiSubnode subnodeTile = (TileMultiSubnode) world.getBlockEntity(offset);
				if (subnodeTile != null) {
					subnodeTile.setData(pos, i);
				}
			} else if (subnodeBlock instanceof IMultiblockChildBlock) {
				TileMultiSubnode subnodeTile = (TileMultiSubnode) world.getBlockEntity(offset);
				if (subnodeTile != null && subnodeTile.parentPos.get().equals(pos)) {
					world.setBlockAndUpdate(offset, Blocks.AIR.defaultBlockState());
				}
			}

		}

		/*
		 * 
		 * Iterator<Subnode> it = getSubNodes().iterator(); while (it.hasNext()) { BlockPos inPos = it.next().pos(); BlockPos offset =
		 * pos.offset(inPos); BlockState inState = world.getBlockState(offset); Block inBlock = inState.getBlock(); if (update) { if
		 * (inState.getMaterial().isReplaceable()) { world.setBlockAndUpdate(offset, ElectrodynamicsBlocks.multi.defaultBlockState()); }
		 * TileMultiSubnode subnode = (TileMultiSubnode) world.getBlockEntity(offset); if (subnode != null) { subnode.nodePos.set(pos); }
		 * } else if (inBlock instanceof IMultiblockChildBlock) { TileMultiSubnode subnode = (TileMultiSubnode)
		 * world.getBlockEntity(offset); if (subnode != null && subnode.nodePos.get().equals(pos)) { world.setBlockAndUpdate(offset,
		 * Blocks.AIR.defaultBlockState()); } } }
		 * 
		 */
	}

	default void onNodePlaced(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		onNodeReplaced(world, pos, true);
	}

	Subnode[] getSubNodes();

	default void onSubnodeNeighborChange(TileMultiSubnode subnode, BlockPos subnodeChangingNeighbor, boolean blockStateTrigger) {

	}

	default InteractionResult onSubnodeUse(Player player, InteractionHand hand, BlockHitResult hit, TileMultiSubnode subnode) {
		return InteractionResult.FAIL;
	}

	default void onSubnodePlace(TileMultiSubnode subnode, BlockState oldSubnodeState, boolean isSubnodeMoving) {

	}

	default int getSubdnodeComparatorSignal(TileMultiSubnode subnode) {
		return 0;
	}

	void onSubnodeDestroyed(TileMultiSubnode subnode);

	default Direction getFacingDirection() {
		return Direction.NORTH;
	}

	default int getDirectSignal(TileMultiSubnode subnode, Direction dir) {
		return 0;
	}

	default int getSignal(TileMultiSubnode subnode, Direction dir) {
		return 0;
	}

	default public <T> @NotNull LazyOptional<T> getSubnodeCapability(@NotNull Capability<T> cap, Direction side) {
		return LazyOptional.empty();
	}

}
