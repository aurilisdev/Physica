package electrodynamics.common.network.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.datafixers.util.Pair;

import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.network.cable.type.IGasPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.network.NetworkRegistry;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.common.tile.pipelines.gas.TileGasPipePump;
import electrodynamics.prefab.network.AbstractNetwork;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntity;

// NOTE to add in pipe heat loss, uncomment commented code
public class GasNetwork extends AbstractNetwork<IGasPipe, SubtypeGasPipe, BlockEntity, GasStack> {

	// public double heatLossPerBlock = 0;
	public int maxPressure = 0;

	public double pressureOfTransmitted = 0;
	public double temperatureOfTransmitted = 0;

	public ConcurrentHashMap<Integer, HashSet<TileGasPipePump>> priorityPumpMap = new ConcurrentHashMap<>();

	public GasNetwork() {
		this(new HashSet<IGasPipe>());
	}

	public GasNetwork(Collection<? extends IGasPipe> pipes) {
		conductorSet.addAll(pipes);
		NetworkRegistry.register(this);
	}

	public GasNetwork(Set<AbstractNetwork<IGasPipe, SubtypeGasPipe, BlockEntity, GasStack>> networks) {
		for (AbstractNetwork<IGasPipe, SubtypeGasPipe, BlockEntity, GasStack> network : networks) {
			if (network != null) {
				conductorSet.addAll(network.conductorSet);
				network.deregister();
			}
		}
		refresh();
		NetworkRegistry.register(this);
	}

	public GasNetwork(Set<GasNetwork> networks, boolean sep) {
		for (GasNetwork network : networks) {
			if (network != null) {
				conductorSet.addAll(network.conductorSet);
				network.deregister();
			}
		}
		refresh();
		NetworkRegistry.register(this);
	}

	@Override
	public void refresh() {
		priorityPumpMap.clear();
		super.refresh();
	}

	@Override
	/**
	 * Returns a GasStack representing how much fluid was actually emitted through the network
	 * 
	 * @param transfer: The gas stack to be transfered
	 * @param ignored:  By convention, the first tile in the ignored list will be the transmitting tile, and will be the point used to determine the heat loss
	 * @param debug:    Whether or not this should be simulated
	 * 
	 * @return Empty if the transmitted pack is empty or if there is no transmitting tile (i.e. ignored is empty). All gas will be used if the network exploded
	 */
	public GasStack emit(GasStack transfer, ArrayList<BlockEntity> ignored, boolean debug) {

		if (transfer.getAmount() <= 0 || ignored.isEmpty()) {
			return GasStack.EMPTY;
		}

		if (checkForOverloadAndHandle(transfer, !debug)) {
			return transfer;
		}

		GasStack copy = transfer.copy();

		// BlockPos senderPos = ignored.get(0).getBlockPos();
		GasStack taken = new GasStack(transfer.getGas(), 0, transfer.getTemperature(), transfer.getPressure());

		Pair<GasStack, Set<TileGasPipePump>> priorityTaken = emitToPumps(transfer, ignored);

		copy.shrink(priorityTaken.getFirst().getAmount());
		taken.grow(priorityTaken.getFirst().getAmount());

		if (copy.isEmpty()) {
			return taken;
		}

		Set<BlockEntity> recievingTiles = ConcurrentHashMap.newKeySet();

		recievingTiles.addAll(acceptorSet);

		recievingTiles.removeAll(ignored);
		recievingTiles.removeAll(priorityTaken.getSecond());

		if (recievingTiles.isEmpty()) {
			return GasStack.EMPTY;
		}

		GasStack gasPerTile, preGasPerTile, gasPerConnection, preGasPerConnection;

		// double deltaT = copy.getTemperature() != Gas.ROOM_TEMPERATURE ? -Math.signum(copy.getTemperature() - Gas.ROOM_TEMPERATURE) : 0;

		int size = recievingTiles.size();

		HashSet<Direction> connections;
		int /* deltaDegreesKelvin, newTemperature, */ takenAmt, amtTaken;
		int connectionCount;

		// pre-defining all vars to eek out a little more performance since this method is already beefy

		// This algorithm is not perfect, but it helps deal with tiles that do not accept the full amount allotted to them

		for (BlockEntity tile : recievingTiles) {
			gasPerTile = new GasStack(copy.getGas(), copy.getAmount() / size, copy.getTemperature(), copy.getPressure());
			preGasPerTile = gasPerTile.copy();

			// deltaDegreesKelvin = ((double) (Math.abs(tile.getBlockPos().getX() - senderPos.getX()) + Math.abs(tile.getBlockPos().getY() -
			// senderPos.getY()) + Math.abs(tile.getBlockPos().getZ() - senderPos.getZ()))) * heatLossPerBlock * gasPerTile.getTemperature() *
			// deltaT;

			// newTemperature = gasPerTile.getTemperature() + deltaDegreesKelvin;

			// if (deltaT < 0 && newTemperature < Gas.ROOM_TEMPERATURE) {
			// newTemperature = Gas.ROOM_TEMPERATURE;
			// }

			// deltaDegreesKelvin = newTemperature - gasPerTile.getTemperature();

			connections = acceptorInputMap.getOrDefault(tile, new HashSet<>());

			connectionCount = connections.size();

			for (Direction dir : connections) {

				gasPerConnection = new GasStack(gasPerTile.getGas(), gasPerTile.getAmount() / connectionCount, gasPerTile.getTemperature(), gasPerTile.getPressure());
				preGasPerConnection = gasPerConnection.copy();

				// gasPerConnection.heat(deltaDegreesKelvin);

				amtTaken = GasUtilities.recieveGas(tile, dir, gasPerConnection, GasAction.EXECUTE);

				gasPerConnection.shrink(amtTaken);

				// if (gasPerConnection.getAmount() > 0) {
				// gasPerConnection.heat(-deltaDegreesKelvin);
				// }

				gasPerTile.shrink(preGasPerConnection.getAmount() - gasPerConnection.getAmount());

				connectionCount--;
			}

			takenAmt = preGasPerTile.getAmount() - gasPerTile.getAmount();

			copy.shrink(takenAmt);

			taken.setAmount(taken.getAmount() + takenAmt);
			size--;
		}

		transmittedThisTick = taken.getAmount();
		temperatureOfTransmitted = taken.getTemperature();
		pressureOfTransmitted = taken.getPressure();

		return taken;
	}

	/**
	 * 
	 * @param transfer The gas being emited
	 * @return how much gas was taken and the pumps that accepted gas
	 */
	private Pair<GasStack, Set<TileGasPipePump>> emitToPumps(GasStack transfer, ArrayList<BlockEntity> ignored) {

		GasStack taken = new GasStack(transfer.getGas(), 0, transfer.getTemperature(), transfer.getPressure());

		Set<TileGasPipePump> acceptedPumps = ConcurrentHashMap.newKeySet();

		if (priorityPumpMap.isEmpty()) {
			return Pair.of(taken, acceptedPumps);
		}

		Pair<GasStack, Set<TileGasPipePump>> accepted;

		Set<TileGasPipePump> prioritySet;

		GasStack copy = transfer.copy();

		for (int i = 9; i >= 0; i--) {

			if (copy.isEmpty()) {
				return Pair.of(taken, acceptedPumps);
			}

			prioritySet = priorityPumpMap.getOrDefault(i, new HashSet<>());

			if (prioritySet.isEmpty()) {
				continue;
			}

			accepted = emitToPumpSet(copy, prioritySet, ignored);

			acceptedPumps.addAll(accepted.getSecond());

			taken.grow(accepted.getFirst().getAmount());

			copy.shrink(accepted.getFirst().getAmount());

		}

		return Pair.of(taken, acceptedPumps);

	}

	private Pair<GasStack, Set<TileGasPipePump>> emitToPumpSet(GasStack transfer, Set<TileGasPipePump> recievingTiles, ArrayList<BlockEntity> ignored) {

		GasStack copy = transfer.copy();
		GasStack taken = new GasStack(transfer.getGas(), 0, transfer.getTemperature(), transfer.getPressure());

		GasStack gasPerTile, preGasPerTile, gasPerConnection, preGasPerConnection;

		Set<TileGasPipePump> filledPumps = ConcurrentHashMap.newKeySet();

		// BlockPos senderPos = ignored.get(0).getBlockPos();

		// double deltaT = copy.getTemperature() != Gas.ROOM_TEMPERATURE ? -Math.signum(copy.getTemperature() - Gas.ROOM_TEMPERATURE) : 0;

		int /* deltaDegreesKelvin, newTemperature, */ amtTaken, takenAmt;

		int connectionCount;

		HashSet<Direction> connections;

		int size = recievingTiles.size();

		for (TileGasPipePump tile : recievingTiles) {

			if (!tile.isPowered() || ignored.contains(tile)) {
				size--;
				continue;
			}

			gasPerTile = new GasStack(copy.getGas(), copy.getAmount() / size, copy.getTemperature(), copy.getPressure());
			preGasPerTile = gasPerTile.copy();

			// deltaDegreesKelvin = ((double) (Math.abs(tile.getBlockPos().getX() - senderPos.getX()) + Math.abs(tile.getBlockPos().getY() -
			// senderPos.getY()) + Math.abs(tile.getBlockPos().getZ() - senderPos.getZ()))) * heatLossPerBlock * gasPerTile.getTemperature() *
			// deltaT;

			// newTemperature = gasPerTile.getTemperature() + deltaDegreesKelvin;

			// if (deltaT < 0 && newTemperature < Gas.ROOM_TEMPERATURE) {
			// newTemperature = Gas.ROOM_TEMPERATURE;
			// }

			// deltaDegreesKelvin = newTemperature - gasPerTile.getTemperature();

			connections = acceptorInputMap.getOrDefault(tile, new HashSet<>());

			connectionCount = connections.size();

			for (Direction dir : connections) {

				gasPerConnection = new GasStack(gasPerTile.getGas(), gasPerTile.getAmount() / connectionCount, gasPerTile.getTemperature(), gasPerTile.getPressure());
				preGasPerConnection = gasPerConnection.copy();

				// gasPerConnection.heat(deltaDegreesKelvin);

				amtTaken = GasUtilities.recieveGas(tile, dir, gasPerConnection, GasAction.EXECUTE);

				gasPerConnection.shrink(amtTaken);

				// if (gasPerConnection.getAmount() > 0) {
				// gasPerConnection.heat(-deltaDegreesKelvin);
				// }

				gasPerTile.shrink(preGasPerConnection.getAmount() - gasPerConnection.getAmount());

				connectionCount--;
			}

			takenAmt = preGasPerTile.getAmount() - gasPerTile.getAmount();

			copy.shrink(takenAmt);

			taken.setAmount(taken.getAmount() + takenAmt);

			if (takenAmt > 0) {
				filledPumps.add(tile);
			}

			filledPumps.add(tile);

			size--;
		}

		transmittedThisTick = taken.getAmount();
		temperatureOfTransmitted = taken.getTemperature();
		pressureOfTransmitted = taken.getPressure();

		return Pair.of(taken, filledPumps);
	}

	private boolean checkForOverloadAndHandle(GasStack stack, boolean live) {

		if (stack.getPressure() <= maxPressure) {
			return false;
		}

		boolean isCorrosive = ElectrodynamicsGases.GAS_REGISTRY.getTag(ElectrodynamicsTags.Gases.IS_CORROSIVE).get().contains(new Holder.Direct<>(stack.getGas()));

		boolean exploded = false;
		HashSet<SubtypeGasPipe> overloadedPipes = new HashSet<>();

		for (SubtypeGasPipe pipe : SubtypeGasPipe.values()) {

			if (pipe.pipeMaterial.maxPressure < stack.getPressure() || (pipe.pipeMaterial.corrodedByAcid && isCorrosive)) {

				overloadedPipes.add(pipe);

			}

		}
		for (SubtypeGasPipe pipe : overloadedPipes) {
			for (IGasPipe gasPipe : conductorTypeMap.get(pipe)) {
				if (live) {
					gasPipe.destroyViolently();
				}
				exploded = true;
			}
		}

		return exploded;

	}

	@Override
	public void updateConductorStatistics(IGasPipe cable) {

		SubtypeGasPipe pipe = cable.getPipeType();

		// if (heatLossPerBlock == 0) {
		// heatLossPerBlock = pipe.effectivePipeHeatLoss;
		// }

		// heatLossPerBlock = (heatLossPerBlock + pipe.effectivePipeHeatLoss) / 2.0;

		if (maxPressure == 0) {
			maxPressure = pipe.pipeMaterial.maxPressure;
		}

		if (pipe.pipeMaterial.maxPressure < maxPressure) {
			maxPressure = pipe.pipeMaterial.maxPressure;
		}

	}

	@Override
	public void updateRecieverStatistics(BlockEntity reciever, Direction dir) {

		if (reciever instanceof TileGasPipePump pump) {
			int priority = pump.priority.get();
			HashSet<TileGasPipePump> set = priorityPumpMap.getOrDefault(priority, new HashSet<>());
			set.add(pump);
			priorityPumpMap.put(priority, set);

		}

	}

	public void updateGasPipePumpStats(TileGasPipePump changedPump, int newPriority, int prevPriority) {
		HashSet<TileGasPipePump> oldSet = priorityPumpMap.getOrDefault(prevPriority, new HashSet<>());
		oldSet.remove(changedPump);
		priorityPumpMap.put(prevPriority, oldSet);

		HashSet<TileGasPipePump> newSet = priorityPumpMap.getOrDefault(newPriority, new HashSet<>());
		newSet.add(changedPump);
		priorityPumpMap.put(newPriority, newSet);
	}

	@Override
	public void tick() {
		super.tick();
		pressureOfTransmitted = 0;
		temperatureOfTransmitted = 0;

		Iterator<IGasPipe> it = conductorSet.iterator();
		boolean broken = false;
		while (it.hasNext()) {
			IGasPipe conductor = it.next();
			if (conductor instanceof BlockEntity entity && entity.isRemoved() || conductor.getNetwork() != this) {
				broken = true;
				break;
			}
		}
		if (broken) {
			refresh();
		}
		if (getSize() == 0) {
			deregister();
		}

	}

	@Override
	public boolean isConductor(BlockEntity tile, IGasPipe requesterCable) {
		return tile instanceof IGasPipe;
	}

	@Override
	public boolean isConductorClass(BlockEntity tile) {
		return tile instanceof IGasPipe;
	}

	@Override
	public boolean isAcceptor(BlockEntity acceptor, Direction orientation) {
		return GasUtilities.isGasReciever(acceptor, orientation.getOpposite());
	}

	@Override
	public boolean canConnect(BlockEntity acceptor, Direction orientation) {
		return GasUtilities.isGasReciever(acceptor, orientation.getOpposite());
	}

	@Override
	public AbstractNetwork<IGasPipe, SubtypeGasPipe, BlockEntity, GasStack> createInstance() {
		return new GasNetwork();
	}

	@Override
	public AbstractNetwork<IGasPipe, SubtypeGasPipe, BlockEntity, GasStack> createInstanceConductor(Set<IGasPipe> conductors) {
		return new GasNetwork(conductors);
	}

	@Override
	public AbstractNetwork<IGasPipe, SubtypeGasPipe, BlockEntity, GasStack> createInstance(Set<AbstractNetwork<IGasPipe, SubtypeGasPipe, BlockEntity, GasStack>> networks) {
		return new GasNetwork(networks);
	}

	@Override
	public SubtypeGasPipe[] getConductorTypes() {
		return SubtypeGasPipe.values();
	}

}
