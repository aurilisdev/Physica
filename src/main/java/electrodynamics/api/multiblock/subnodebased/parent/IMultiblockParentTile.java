package electrodynamics.api.multiblock.subnodebased.parent;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.multiblock.subnodebased.Subnode;
import electrodynamics.api.multiblock.subnodebased.child.IMultiblockChildBlock;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public interface IMultiblockParentTile {

    /*
     * Okay so hear me out:
     *
     * Yes, using an array is a little more confusing, however it gives a fixed index to a subnode
     *
     * This is useful as it means a subnode can look itself up on the parent tile really quickly and reliably if there's a data point it needs to reference
     *
     *
     */

    default void onNodeReplaced(Level world, BlockPos pos, boolean update) {

        Subnode[] subnodes = getSubNodes().getSubnodes(getFacingDirection());

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
                    world.setBlockAndUpdate(offset, ElectrodynamicsBlocks.BLOCK_MULTISUBNODE.get().defaultBlockState());
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

    }

    default void onNodePlaced(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        onNodeReplaced(world, pos, true);
    }

    IMultiblockParentBlock.SubnodeWrapper getSubNodes();

    default void onSubnodeNeighborChange(TileMultiSubnode subnode, BlockPos subnodeChangingNeighbor, boolean blockStateTrigger) {

    }

    default InteractionResult onSubnodeUseWithoutItem(Player player, BlockHitResult hit, TileMultiSubnode subnode) {
        return InteractionResult.FAIL;
    }

    default ItemInteractionResult onSubnodeUseWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit, TileMultiSubnode subnode) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    default void onSubnodePlace(TileMultiSubnode subnode, BlockState oldSubnodeState, boolean isSubnodeMoving) {

    }

    default int getSubdnodeComparatorSignal(TileMultiSubnode subnode) {
        return 0;
    }

    void onSubnodeDestroyed(TileMultiSubnode subnode);

    Direction getFacingDirection();

    default int getDirectSignal(TileMultiSubnode subnode, Direction dir) {
        return 0;
    }

    default int getSignal(TileMultiSubnode subnode, Direction dir) {
        return 0;
    }

    @Nullable
    default ICapabilityElectrodynamic getSubnodeElectrodynamicCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return null;
    }

    @Nullable
    default IFluidHandler getSubnodeFluidHandlerCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return null;
    }

    @Nullable
    default IGasHandler getSubnodeGasHandlerCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return null;
    }

    @Nullable
    default IItemHandler getSubnodeItemHandlerCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return null;
    }

}
