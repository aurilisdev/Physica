package electrodynamics.common.block.connect.util;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import electrodynamics.prefab.tile.types.GenericConnectTile;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractConnectBlock extends GenericEntityBlockWaterloggable {

	protected final VoxelShape[] boundingBoxes = new VoxelShape[7];

	// 6 possible directions
	int maxValue = 0b1000000;
	protected VoxelShape[] shapestates = new VoxelShape[maxValue];
	public AbstractConnectBlock(Properties properties, double radius) {
		super(properties);
		generateBoundingBoxes(radius);
		stateDefinition.any().setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, false);
	}

	public void generateBoundingBoxes(double radius) {
		double w = radius;
		double sm = 8 - w;
		double lg = 8 + w;
		// down
		boundingBoxes[0] = Block.box(sm, 0, sm, lg, lg, lg);
		// up
		boundingBoxes[1] = Block.box(sm, sm, sm, lg, 16, lg);
		// north
		boundingBoxes[2] = Block.box(sm, sm, 0, lg, lg, lg);
		// south
		boundingBoxes[3] = Block.box(sm, sm, sm, lg, lg, 16);
		// west
		boundingBoxes[4] = Block.box(0, sm, sm, lg, lg, lg);
		// east
		boundingBoxes[5] = Block.box(sm, sm, sm, 16, lg, lg);
		// center
		boundingBoxes[6] = Block.box(sm, sm, sm, lg, lg, lg);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

		VoxelShape camoShape = Shapes.empty();

		if (state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING) && worldIn.getBlockEntity(pos) instanceof GenericConnectTile connect) {

			if (connect.isCamoAir()) {
				camoShape = connect.getScaffoldBlock().getShape(worldIn, pos, context);
			} else {
				camoShape = connect.getCamoBlock().getShape(worldIn, pos, context);
			}

		}

		BlockEntity entity = worldIn.getBlockEntity(pos);

		if(!(entity instanceof GenericConnectTile)) {
			return Shapes.empty();
		}

		EnumConnectType[] connections = ((GenericConnectTile) entity).readConnections();
		int hash = hashPresentSides(connections);
		// Check for existing shape
		if (shapestates[hash] != null) {
			return getCamoShape(shapestates[hash], camoShape);
		}
		// Create new shape for connections
		VoxelShape shape = boundingBoxes[6];
		for (int i = 0; i < 6; i++) {
			if (connections[i] == EnumConnectType.NONE) {
				continue;
			}

			shape = Shapes.join(shape, boundingBoxes[i], BooleanOp.OR);
		}
		shapestates[hash] = shape;
		if (shape == null) {
			return Shapes.empty();
		}
		return getCamoShape(shape, camoShape);
	}

	private VoxelShape getCamoShape(VoxelShape wireShape, VoxelShape camoShape) {
		if (camoShape == Shapes.empty()) return wireShape;
		if (camoShape == Shapes.block()) return camoShape;
		return Shapes.join(wireShape, camoShape, BooleanOp.OR);
	}

	public static int hashPresentSides(EnumConnectType[] connections) {
		int flag = 0;
		for (short i = 0; i < 6; i++) {
			if (connections[i] != EnumConnectType.NONE) {
				flag = flag | (1 << i);
			}
		}
		return flag;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ElectrodynamicsBlockStates.HAS_SCAFFOLDING);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (level.getBlockState(pos).getBlock() instanceof BlockScaffold) {
			return true;
		}
		return super.canSurvive(state, level, pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState superState = super.getStateForPlacement(context);
		Level world = context.getPlayer().level();
		boolean set = false;
		if (world.getBlockState(context.getClickedPos()).getBlock() instanceof BlockScaffold) {
			superState = superState.setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, true);
			set = true;
		} else {
			superState = superState.setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, false);
		}
		if (!world.isClientSide() && set) {
			world.setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
		}
		return superState;
	}

	/*
	@Override
	public void onPlace(BlockState newState, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(newState, level, pos, oldState, isMoving);
		if (newState.hasProperty(ElectrodynamicsBlockStates.HAS_SCAFFOLDING) && oldState.hasProperty(ElectrodynamicsBlockStates.HAS_SCAFFOLDING)) {
			newState = newState.setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, oldState.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING));
		}
	}

	 */

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.isEmpty()) {
			return ItemInteractionResult.FAIL;
		}

		if (stack.getItem() instanceof BlockItem blockitem && level.getBlockEntity(pos) instanceof GenericConnectTile connect) {

			BlockPlaceContext newCtx = new BlockPlaceContext(player, hand, stack, hitResult);

			if (blockitem.getBlock() instanceof BlockScaffold scaffold) {
				if (!state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING)) {
					if (!level.isClientSide) {
						if (!player.isCreative()) {
							stack.shrink(1);
							player.setItemInHand(hand, stack);
						}
						state = state.setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, true);
						level.setBlockAndUpdate(pos, state);
						connect.setScaffoldBlock(scaffold.getStateForPlacement(newCtx));
						level.playSound(null, pos, blockitem.getBlock().defaultBlockState().getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
					}
					return ItemInteractionResult.CONSUME;
				}

			} else if (!(blockitem.getBlock() instanceof AbstractConnectBlock) && state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING)) {
				if (connect.isCamoAir()) {
					if (!level.isClientSide) {
						connect.setCamoBlock(blockitem.getBlock().getStateForPlacement(newCtx));
						if (!player.isCreative()) {
							stack.shrink(1);
							player.setItemInHand(hand, stack);
						}
						level.playSound(null, pos, blockitem.getBlock().defaultBlockState().getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
						level.getChunkSource().getLightEngine().checkBlock(pos);
					}
					return ItemInteractionResult.CONSUME;
				}
				if (!connect.getCamoBlock().is(blockitem.getBlock())) {
					if (!level.isClientSide) {
						if (!player.isCreative()) {
							if (!player.addItem(new ItemStack(connect.getCamoBlock().getBlock()))) {
								level.addFreshEntity(new ItemEntity(player.level(), (int) player.getX(), (int) player.getY(), (int) player.getZ(), new ItemStack(connect.getCamoBlock().getBlock())));
							}
							stack.shrink(1);
							player.setItemInHand(hand, stack);
						}
						connect.setCamoBlock(blockitem.getBlock().getStateForPlacement(newCtx));
						level.playSound(null, pos, blockitem.getBlock().defaultBlockState().getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
					}
					return ItemInteractionResult.CONSUME;

				}
			}

		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
		ArrayList<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));
		if (state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING) && builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof GenericConnectTile connect) {
			drops.add(new ItemStack(connect.getScaffoldBlock().getBlock()));
			if (!connect.isCamoAir()) {
				drops.add(new ItemStack(connect.getCamoBlock().getBlock()));
			}
		}
		return drops;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
		if (!state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING)) {
			return true;
		}

		if (level.getBlockEntity(pos) instanceof GenericConnectTile connect) {
			if (connect.isCamoAir()) {
				return connect.getScaffoldBlock().propagatesSkylightDown(level, pos);
			}
			return connect.getCamoBlock().propagatesSkylightDown(level, pos);
		}

		return true;
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		if (!state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING)) {
			return 0;
		}

		if (level.getBlockEntity(pos) instanceof GenericConnectTile connect) {
			if (connect.isCamoAir()) {
				return connect.getScaffoldBlock().getBlock().getLightEmission(connect.getScaffoldBlock(), level, pos);
			}
			return connect.getCamoBlock().getBlock().getLightEmission(connect.getCamoBlock(), level, pos);
		}

		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (!state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING)) {
			return super.getVisualShape(state, level, pos, context);
		}
		if (level.getBlockEntity(pos) instanceof GenericConnectTile connect) {
			if (connect.isCamoAir()) {
				return connect.getScaffoldBlock().getVisualShape(level, pos, context);
			}
			return connect.getCamoBlock().getVisualShape(level, pos, context);
		}
		return super.getVisualShape(state, level, pos, context);
	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, Player player) {
		Level level = player.level();
		if (level.isClientSide()) {
			return;
		}
		if (level.getBlockEntity(pos) instanceof GenericConnectTile connect) {

			if (!connect.isCamoAir()) {
				Block camo = connect.getCamoBlock().getBlock();

				connect.setCamoBlock(Blocks.AIR.defaultBlockState());

				if (!player.isCreative()) {
					if (!player.addItem(new ItemStack(camo))) {
						level.addFreshEntity(new ItemEntity(player.level(), (int) player.getX(), (int) player.getY(), (int) player.getZ(), new ItemStack(camo)));
					}
				}

				return;
			}
			if (!connect.isScaffoldAir()) {
				Block scaffold = connect.getScaffoldBlock().getBlock();

				connect.setScaffoldBlock(Blocks.AIR.defaultBlockState());

				level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, false));

				if (!player.isCreative()) {
					if (!player.addItem(new ItemStack(scaffold))) {
						level.addFreshEntity(new ItemEntity(player.level(), (int) player.getX(), (int) player.getY(), (int) player.getZ(), new ItemStack(scaffold)));
					}
				}

				return;
			}

		}

		super.onRotate(stack, pos, player);

	}

}
