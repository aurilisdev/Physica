package electrodynamics.api.network.util;

import java.util.HashSet;
import java.util.List;

import electrodynamics.prefab.network.AbstractNetwork;
import electrodynamics.prefab.tile.types.GenericRefreshingConnectTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AbstractNetworkFinder<C extends GenericRefreshingConnectTile<T, C, ?>, T, P> {
	private final Level level;
	private final BlockPos start;
	private final AbstractNetwork<C, T, P, ?> net;
	private final HashSet<C> iteratedTiles = new HashSet<>();
	private final HashSet<BlockPos> toIgnore = new HashSet<>();

	public AbstractNetworkFinder(Level world, BlockPos start, AbstractNetwork<C, T, P, ?> net, BlockPos... ignore) {
		level = world;
		this.start = start;
		this.net = net;
		if (ignore.length > 0) {
			toIgnore.addAll(List.of(ignore));
		}
	}

	private void loopAll(BlockPos location) {
		BlockEntity curr = level.getBlockEntity(location);
		if (net.isConductor(curr, (C) curr)) {
			iteratedTiles.add((C) curr);
		}
		for (Direction direction : Direction.values()) {
			BlockPos relative = new BlockPos(location).relative(direction);
			if(toIgnore.contains(relative) || !level.hasChunkAt(relative)) {
				continue;
			}
			BlockEntity tileEntity = level.getBlockEntity(relative);
			if(iteratedTiles.contains(tileEntity) || !net.isConductor(tileEntity, (C) curr)) {
				continue;
			}
			loopAll((C) tileEntity);
		}

	}

	private void loopAll(C cable) {
		iteratedTiles.add(cable);
		for (BlockEntity connection : cable.getConnectedCables()) {
			if(connection == null) {
				continue;
			}
			BlockPos pos = connection.getBlockPos();
			if (iteratedTiles.contains(connection) || toIgnore.contains(pos) || !net.isConductor(connection, cable)) {
				continue;
			}
			loopAll((C) connection);
		}
	}

	public HashSet<C> exploreNetwork() {
		loopAll(start);
		return iteratedTiles;
	}
}
