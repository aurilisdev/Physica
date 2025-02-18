package electrodynamics.prefab.block;

import java.util.Arrays;
import java.util.List;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.IWrenchable;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

public abstract class GenericEntityBlock extends BaseEntityBlock implements IWrenchable {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	protected GenericEntityBlock(Properties properties) {
		super(properties);
	}

	@Override
	public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state, BlockEntityType<T> type) {
		return (l, pos, s, tile) -> {
			if (tile instanceof GenericTile generic && generic.getComponent(IComponentType.Tickable) instanceof ComponentTickable tickable) {
				tickable.performTick(l);
			}
		};
	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, Player player) {
		if (player.level().getBlockState(pos).hasProperty(FACING)) {
			BlockState state = rotate(player.level().getBlockState(pos), Rotation.CLOCKWISE_90);
			player.level().setBlockAndUpdate(pos, state);
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		if (state.hasProperty(FACING)) {
			return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
		}
		return super.rotate(state, rot);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		if (state.hasProperty(FACING)) {
			return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
		}
		return super.mirror(state, mirrorIn);
	}

	@Override
	public void onPickup(ItemStack stack, BlockPos pos, Player player) {
		Level world = player.level();
		world.destroyBlock(pos, true, player);
	}

	// TODO get this to work
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		BlockEntity tile = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (tile instanceof GenericTile machine) {
			ItemStack stack = new ItemStack(this);
			ComponentInventory inv = machine.getComponent(IComponentType.Inventory);
			if (inv != null) {
				Containers.dropContents(machine.getLevel(), machine.getBlockPos(), inv.getItems());
				tile.getCapability(ElectrodynamicsCapabilities.ELECTRODYNAMIC).ifPresent(el -> {
					double joules = el.getJoulesStored();
					if (joules > 0) {
						stack.getOrCreateTag().putDouble("joules", joules);
					}
				});
			}
			return Arrays.asList(stack);

		}
		return super.getDrops(state, builder);
	}

	// TODO get this to work

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (level.getBlockEntity(pos) instanceof GenericTile generic) {
			if (newState.isAir() || !newState.is(state.getBlock())) {
				generic.onBlockDestroyed();
			} else {
				generic.onBlockStateUpdate(state, newState);
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	/**
	 * Fired when a neighboring tile changes
	 */
	@Override
	public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
		if (level.getBlockEntity(pos) instanceof GenericTile generic) {
			generic.onNeightborChanged(neighbor, false);
		}
	}

	@Override
	public void onPlace(BlockState newState, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(newState, level, pos, oldState, isMoving);
		if (level.getBlockEntity(pos) instanceof GenericTile generic) {
			generic.onPlace(oldState, isMoving);
		}
	}

	/**
	 * Fired when a neighboring blockstate changes
	 */
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean isMoving) {
		super.neighborChanged(state, level, pos, block, neighbor, isMoving);
		if (level.getBlockEntity(pos) instanceof GenericTile generic) {
			generic.onNeightborChanged(neighbor, true);
		}

	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState pState) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof GenericTile generic) {
			return generic.getComparatorSignal();
		}
		return super.getAnalogOutputSignal(state, level, pos);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (worldIn.getBlockEntity(pos) instanceof GenericTile generic) {
			return generic.use(player, handIn, hit);
		}

		return super.use(state, worldIn, pos, player, handIn, hit);
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		if (level.getBlockEntity(pos) instanceof GenericTile generic) {
			return generic.getDirectSignal(direction);
		}
		return super.getDirectSignal(state, level, pos, direction);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		if (level.getBlockEntity(pos) instanceof GenericTile generic) {
			return generic.getSignal(direction);
		}
		return super.getSignal(state, level, pos, direction);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		super.entityInside(state, level, pos, entity);
		if (level.getBlockEntity(pos) instanceof GenericTile tile) {
			tile.onEntityInside(state, level, pos, entity);
		}
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (level.getBlockEntity(pos) instanceof GenericTile tile) {
			tile.setPlacedBy(placer, stack);
		}
	}

}
