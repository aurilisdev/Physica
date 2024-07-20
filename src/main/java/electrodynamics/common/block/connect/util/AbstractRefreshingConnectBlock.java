package electrodynamics.common.block.connect.util;

import javax.annotation.Nullable;

import electrodynamics.api.network.cable.IRefreshableCable;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public abstract class AbstractRefreshingConnectBlock extends AbstractConnectBlock {

	public AbstractRefreshingConnectBlock(Properties properties, double radius) {
		super(properties, radius);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState stateIn, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, stateIn, placer, stack);
		BlockPos relPos;
		for (Direction dir : Direction.values()) {
			relPos = pos.relative(dir);
			stateIn = refreshConnections(worldIn.getBlockState(relPos), worldIn.getBlockEntity(relPos), stateIn, worldIn.getBlockEntity(pos), dir);
		}
		worldIn.setBlockAndUpdate(pos, stateIn);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		return refreshConnections(facingState, world.getBlockEntity(facingPos), super.updateShape(stateIn, facing, facingState, world, currentPos, facingPos), world.getBlockEntity(currentPos), facing);
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		if (worldIn.isClientSide) {
			return;
		}
		BlockEntity tile = worldIn.getBlockEntity(pos);
		IRefreshableCable conductor = getCableIfValid(tile);
		if (conductor == null) {
			return;
		}
		conductor.refreshNetwork();
		BlockPos relPos;
		for (Direction dir : Direction.values()) {
			relPos = pos.relative(dir);
			state = refreshConnections(worldIn.getBlockState(relPos), worldIn.getBlockEntity(relPos), state, worldIn.getBlockEntity(pos), dir);
		}
		state = state.setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING));
		worldIn.setBlockAndUpdate(pos, state);
	}

	@Override
	public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, world, pos, neighbor);
		if (world.isClientSide()) {
			return;
		}
		BlockEntity tile = world.getBlockEntity(pos);
		IRefreshableCable conductor = getCableIfValid(tile);
		if (conductor == null) {
			return;
		}
		conductor.refreshNetworkIfChange();
	}

	public abstract BlockState refreshConnections(BlockState otherState, BlockEntity otherTile, BlockState thisState, BlockEntity thisTile, Direction dir);

	@Nullable
	public abstract IRefreshableCable getCableIfValid(BlockEntity tile);

}
