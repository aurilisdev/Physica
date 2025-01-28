package electrodynamics.common.tile;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentTile;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class TileMultiSubnode extends GenericTile {

	public final Property<BlockPos> parentPos = property(new Property<>(PropertyTypes.BLOCK_POS, "nodePos", BlockEntityUtils.OUT_OF_REACH));
	public final Property<Integer> nodeIndex = property(new Property<>(PropertyTypes.INTEGER, "nodeIndex", 0));

	public VoxelShape shapeCache;

	public TileMultiSubnode(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_MULTI.get(), worldPosition, blockState);
		addComponent(new ComponentPacketHandler(this));
	}

	@Override
	@Nullable
	public ICapabilityElectrodynamic getElectrodynamicCapability(@Nullable Direction side) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			return node.getSubnodeElectrodynamicCapability(this, side);
		}
		return null;
	}

	@Override
	@Nullable
	public IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			return node.getSubnodeFluidHandlerCapability(this, side);
		}
		return null;
	}

	@Override
	@Nullable
	public IGasHandler getGasHandlerCapability(@Nullable Direction side) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			return node.getSubnodeGasHandlerCapability(this, side);
		}
		return null;
	}

	@Override
	@Nullable
	public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			return node.getSubnodeItemHandlerCapability(this, side);
		}
		return null;
	}

	public void setData(BlockPos parentPos, int subnodeIndex) {

		this.parentPos.set(parentPos);
		nodeIndex.set(subnodeIndex);

		setChanged();

	}

	public VoxelShape getShape() {

		if (shapeCache != null) {
			return shapeCache;
		}

		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {

			return shapeCache = node.getSubNodes().getSubnodes(node.getFacingDirection())[nodeIndex.get()].getShape(node.getFacingDirection());

		}

		return Shapes.block();
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			node.onSubnodeNeighborChange(this, neighbor, blockStateTrigger);
		}
	}

	@Override
	public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			return node.onSubnodeUseWithoutItem(player, hit, this);
		}
		return super.useWithoutItem(player, hit);
	}

	@Override
	public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			return node.onSubnodeUseWithItem(used, player, hand, hit, this);
		}
		return super.useWithItem(used, player, hand, hit);
	}

	@Override
	public void onPlace(BlockState oldState, boolean isMoving) {
		super.onPlace(oldState, isMoving);
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			node.onSubnodePlace(this, oldState, isMoving);
		}
	}

	@Override
	public int getComparatorSignal() {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			return node.getSubdnodeComparatorSignal(this);
		}
		return 0;
	}

	@Override
	public void onBlockDestroyed() {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile node) {
			node.onSubnodeDestroyed(this);
		}
		super.onBlockDestroyed();
	}

	@Override
	public int getDirectSignal(Direction dir) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile parent) {
			return parent.getDirectSignal(this, dir);
		}
		return 0;
	}

	@Override
	public int getSignal(Direction dir) {
		if (level.getBlockEntity(parentPos.get()) instanceof IMultiblockParentTile parent) {
			return parent.getSignal(this, dir);
		}
		return 0;
	}

}
