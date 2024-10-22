package electrodynamics.common.block.connect.util;

import javax.annotation.Nullable;

import electrodynamics.api.network.cable.IRefreshableCable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractRefreshingConnectBlock extends AbstractConnectBlock {

	public AbstractRefreshingConnectBlock(Properties properties, double radius) {
		super(properties, radius);
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		if (worldIn.isClientSide()) {
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

	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState superState = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		if(worldIn.isClientSide()){
			return superState;
		}
		return refreshConnections(facingState, worldIn.getBlockEntity(facingPos), superState, worldIn.getBlockEntity(currentPos), facing);
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

		refreshConnections(world.getBlockState(neighbor), world.getBlockEntity(neighbor), world.getBlockState(pos), world.getBlockEntity(pos), BlockEntityUtils.directionFromPos(pos, neighbor));
		conductor.refreshNetworkIfChange();
	}

	public abstract BlockState refreshConnections(BlockState otherState, BlockEntity otherTile, BlockState thisState, BlockEntity thisTile, Direction dir);

	@Nullable
	public abstract IRefreshableCable getCableIfValid(BlockEntity tile);

}
