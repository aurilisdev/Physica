package electrodynamics.common.block.connect.util;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import electrodynamics.api.network.cable.IRefreshableCable;
import electrodynamics.api.network.cable.type.IConductor;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.types.GenericConnectTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ElectricityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public abstract class AbstractRefreshingConnectBlock extends AbstractConnectBlock {

	public AbstractRefreshingConnectBlock(Properties properties, double radius) {
		super(properties, radius);
	}

	/*
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState stateIn, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, stateIn, placer, stack);
		BlockPos relPos;
		for (Direction dir : Direction.values()) {
			relPos = pos.relative(dir);
			stateIn = refreshConnections(worldIn.getBlockState(relPos), worldIn.getBlockEntity(relPos), stateIn, worldIn.getBlockEntity(pos), dir);
		}
		//worldIn.setBlockAndUpdate(pos, stateIn);
	}

	 */

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

		/*
		for (Direction dir : Direction.values()) {
			relPos = pos.relative(dir);
			state = refreshConnections(worldIn.getBlockState(relPos), worldIn.getBlockEntity(relPos), state, worldIn.getBlockEntity(pos), dir);
		}

		 */




		if(worldIn.getBlockEntity(pos) instanceof GenericConnectTile connect) {

			Pair<Direction, EnumConnectType>[] connections = new Pair[Direction.values().length];

			for (Direction dir : Direction.values()) {
				relPos = pos.relative(dir);
				connections[dir.ordinal()] = Pair.of(dir, getConnectionForFace(worldIn.getBlockState(relPos), worldIn.getBlockEntity(relPos), state, connect, dir));
				//state = refreshConnections(worldIn.getBlockState(relPos), worldIn.getBlockEntity(relPos), state, worldIn.getBlockEntity(pos), dir);
			}
			connect.writeConnections(connections);
		}





		//state = state.setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, state.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING));
		//.worldIn.setBlockAndUpdate(pos, state);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return refreshConnections(facingState, worldIn.getBlockEntity(facingPos), super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos), worldIn.getBlockEntity(currentPos), facing);
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


		if(world.getBlockEntity(pos) instanceof GenericConnectTile connect) {
			Direction dir = BlockEntityUtils.directionFromPos(pos, neighbor);
			connect.writeConnection(dir, getConnectionForFace(world.getBlockState(neighbor), world.getBlockEntity(neighbor), world.getBlockState(pos), connect, dir));
		}



		//refreshConnections(world.getBlockState(neighbor), world.getBlockEntity(neighbor), world.getBlockState(pos), world.getBlockEntity(pos), BlockEntityUtils.directionFromPos(pos, neighbor));
		conductor.refreshNetworkIfChange();
	}

	public abstract EnumConnectType getConnectionForFace(BlockState otherState, BlockEntity otherTile, BlockState thisState, GenericConnectTile thisTile, Direction dir);

	public abstract BlockState refreshConnections(BlockState otherState, BlockEntity otherTile, BlockState thisState, BlockEntity thisTile, Direction dir);

	@Nullable
	public abstract IRefreshableCable getCableIfValid(BlockEntity tile);

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean isMoving) {
		super.neighborChanged(state, level, pos, block, neighbor, isMoving);
		//refreshConnections(level.getBlockState(neighbor), level.getBlockEntity(neighbor), level.getBlockState(pos), level.getBlockEntity(pos), BlockEntityUtils.directionFromPos(pos, neighbor));
	}

}
