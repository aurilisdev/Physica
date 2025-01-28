package electrodynamics.common.network.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.datafixers.util.Pair;

import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.network.cable.type.IGasPipe;
import electrodynamics.common.network.NetworkRegistry;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.common.tile.pipelines.gas.GenericTileGasPipe;
import electrodynamics.common.tile.pipelines.gas.TileGasPipePump;
import electrodynamics.prefab.network.AbstractNetwork;
import electrodynamics.prefab.utilities.Scheduler;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

// NOTE to add in pipe heat loss, uncomment commented code
public class GasNetwork extends AbstractNetwork<GenericTileGasPipe, IGasPipe, GasStack, GasNetwork> {

    // public double heatLossPerBlock = 0;
    public int maxPressure = 0;

    public double pressureOfTransmitted = 0;
    public double temperatureOfTransmitted = 0;

    public ConcurrentHashMap<Integer, HashSet<TileGasPipePump>> priorityPumpMap = new ConcurrentHashMap<>();
    private final HashMap<Integer, HashSet<GenericTileGasPipe>> pressureToGasPipeMap = new HashMap<>();
    private final HashSet<GenericTileGasPipe> destroyedByCorrosion = new HashSet<>();

    public GasNetwork(Collection<GenericTileGasPipe> pipes) {
        conductorSet.addAll(pipes);
        NetworkRegistry.register(this);
    }

    public GasNetwork(Set<GasNetwork> networks) {
        for (GasNetwork network : networks) {
            if (network != null) {
                conductorSet.addAll(network.conductorSet);
                network.deregister();
            }
        }
        NetworkRegistry.register(this);
    }

    @Override
    public void refreshNewNetwork() {
        priorityPumpMap.clear();
        maxPressure = 0;
        networkMaxTransfer = 0;
        pressureToGasPipeMap.clear();
        destroyedByCorrosion.clear();
        super.refreshNewNetwork();
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
    public GasStack emit(GasStack inserted, ArrayList<BlockEntity> ignored, boolean debug) {

        GasStack transfer = new GasStack(inserted.getGas(), Math.min(inserted.getAmount(), (int) networkMaxTransfer), inserted.getTemperature(), inserted.getPressure());

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

        boolean isCorrosive = stack.is(ElectrodynamicsTags.Gases.IS_CORROSIVE);

        if (stack.getPressure() <= maxPressure && !isCorrosive) {
            return false;
        }

        HashSet<GenericTileGasPipe> overloadedPipes = new HashSet<>();

        for (Map.Entry<Integer, HashSet<GenericTileGasPipe>> entry : pressureToGasPipeMap.entrySet()) {

            if (entry.getKey() < stack.getPressure()) {

                overloadedPipes.addAll(entry.getValue());

            }

        }

        if(isCorrosive) {
            overloadedPipes.addAll(destroyedByCorrosion);
        }

        if(overloadedPipes.isEmpty()) {
            return false;
        }

        if(!live) {
            return true;
        }

        for(GenericTileGasPipe pipe : overloadedPipes) {
            Scheduler.schedule(1, pipe::destroyViolently);
        }

        return true;

    }

    @Override
    public void updateConductorStatistics(GenericTileGasPipe cable, boolean remove) {

        super.updateConductorStatistics(cable, remove);

        if (remove) {

            int pressure = cable.getCableType().getPipeMaterial().getMaxPressuire();

            if(pressureToGasPipeMap.containsKey(pressure)) {
                HashSet<GenericTileGasPipe> set = pressureToGasPipeMap.get(pressure);
                set.remove(cable);
                pressureToGasPipeMap.put(pressure, set);
            }

            destroyedByCorrosion.remove(cable);

        } else {
            IGasPipe pipe = cable.getCableType();

            // if (heatLossPerBlock == 0) {
            // heatLossPerBlock = pipe.effectivePipeHeatLoss;
            // }

            // heatLossPerBlock = (heatLossPerBlock + pipe.effectivePipeHeatLoss) / 2.0;

            if (networkMaxTransfer == 0 || networkMaxTransfer < pipe.getMaxTransfer()) {
                networkMaxTransfer = pipe.getMaxTransfer();
            }

            if (maxPressure == 0 || pipe.getPipeMaterial().getMaxPressuire() < maxPressure) {
                maxPressure = pipe.getPipeMaterial().getMaxPressuire();
            }

            int pressure = pipe.getPipeMaterial().getMaxPressuire();

            HashSet<GenericTileGasPipe> set = pressureToGasPipeMap.getOrDefault(pressure, new HashSet<>());

            set.add(cable);

            pressureToGasPipeMap.put(pressure, set);


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

    @Override
    public void resetConductorStatistics() {
        networkMaxTransfer = 0;
        maxPressure = 0;
        pressureToGasPipeMap.clear();
        destroyedByCorrosion.clear();
        super.resetConductorStatistics();
    }

    @Override
    public void resetReceiverStatistics() {
        priorityPumpMap.clear();
        super.resetReceiverStatistics();
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

		/*

		Iterator<GenericTileGasPipe> it = conductorSet.iterator();
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

		 */
        if (getSize() == 0) {
            deregister();
        }

    }

    @Override
    public void deregister() {
        pressureToGasPipeMap.clear();
        super.deregister();
    }

    @Override
    public boolean isConductor(BlockEntity tile, GenericTileGasPipe requesterCable) {
        return tile instanceof GenericTileGasPipe;
    }

    @Override
    public GasNetwork createInstanceConductor(Set<GenericTileGasPipe> conductors) {
        return new GasNetwork(conductors);
    }

}
