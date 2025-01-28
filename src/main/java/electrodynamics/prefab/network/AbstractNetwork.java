package electrodynamics.prefab.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import electrodynamics.api.network.ITickableNetwork;
import electrodynamics.api.network.util.AbstractNetworkFinder;
import electrodynamics.common.network.NetworkRegistry;
import electrodynamics.prefab.tile.types.GenericRefreshingConnectTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 *
 * A graph network representing a type of cable that will update when something changes
 *
 * @param <C> The type of the cable
 * @param <T> The type of the cable categories
 * @param <P> The value this network will transport
 * @param <TYPE> The type of this network
 *
 * @author aurilisdev
 * @author skip999
 */
public abstract class AbstractNetwork<C extends GenericRefreshingConnectTile<T, C, TYPE>, T, P, TYPE extends AbstractNetwork<C, T, P, TYPE>> implements ITickableNetwork {

    private final UUID id = UUID.randomUUID();

    public final HashSet<C> conductorSet = new HashSet<>();
    public final HashSet<BlockEntity> acceptorSet = new HashSet<>();
    public final HashMap<BlockEntity, HashSet<Direction>> acceptorInputMap = new HashMap<>();
    public double networkMaxTransfer;
    public double transmittedLastTick;
    public double transmittedThisTick;


    /**
     * Updates this network with the passed in receivers
     *
     * @param receivers the receivers that have changed
     */
    public void updateRecievers(List<GenericRefreshingConnectTile.UpdatedReceiver> receivers) {

        boolean updateRecieverStatistics = false;

        for (GenericRefreshingConnectTile.UpdatedReceiver receiver : receivers) {

            if (receiver.reciever() == null) {
                continue;
            }

            updateRecieverStatistics = updateReceiver(receiver.reciever(), receiver.removed(), receiver.dir());
        }

        // check if we need to recheck the overall statistics
        if(updateRecieverStatistics) {

            resetReceiverStatistics();

            for(BlockEntity entity : acceptorSet) {

                for(Direction dir : acceptorInputMap.getOrDefault(entity, new HashSet<>())) {
                    updateRecieverStatistics(entity, dir);
                }

            }

        }

    }

    /**
     * Updates this network with the passed in receiver
     *
     * @param entity the receiver
     * @param remove whether the receiver was removed
     * @param dir the direction this network is connected to the receiver on
     * @return whether the overall receiver statistics need to be updated
     */
    private boolean updateReceiver(BlockEntity entity, boolean remove, Direction dir) {

        HashSet<Direction> dirs;

        if(remove) {

            if (!entity.isRemoved() && acceptorInputMap.containsKey(entity)) {

                dirs = acceptorInputMap.get(entity);
                dirs.remove(dir.getOpposite());
                acceptorInputMap.put(entity, dirs);

            } else if (entity.isRemoved()) {

                acceptorSet.remove(entity);
                acceptorInputMap.remove(entity);
                return true;

            }

        } else {

            acceptorSet.add(entity);
            dirs = acceptorInputMap.getOrDefault(entity, new HashSet<>());
            dirs.add(dir.getOpposite());
            acceptorInputMap.put(entity, dirs);
            updateRecieverStatistics(entity, dir);

        }

        return false;

    }

    /**
     * Updates this network with the passed in cables
     *
     * @param conductors the cables that have changed
     */
    public void updateConductors(List<GenericRefreshingConnectTile.UpdatedConductor<C>> conductors) {

        boolean updateConductorStatistics = false;

        for(GenericRefreshingConnectTile.UpdatedConductor<C> conductor : conductors) {
            if(conductor.conductor() == null) {
                continue;
            }

            updateConductorStatistics = updateConductor(conductor.conductor(), conductor.removed());

        }

        if(updateConductorStatistics) {

            resetConductorStatistics();

            for(C conductor : conductorSet) {

                updateConductorStatistics(conductor, false);

            }

        }

    }

    /**
     * Updates this network with the passed in cable
     *
     * @param conductor the cable
     * @param remove whether the cable was removed
     * @return whether the overall cable statistics need to be updated
     */
    public boolean updateConductor(C conductor, boolean remove) {

        boolean updateConductorStatistics = false;

        if(remove) {

            updateConductorStatistics = true;

        } else {

            conductorSet.add(conductor);

            conductor.setNetwork((TYPE) this);

            updateConductorStatistics(conductor, false);

        }



        int index = 0;

        List<GenericRefreshingConnectTile.UpdatedReceiver> receivers = new ArrayList<>();

        for(BlockEntity entity : conductor.getConectedRecievers()) {

            if(entity == null) {
                index++;
                continue;
            }

            receivers.add(new GenericRefreshingConnectTile.UpdatedReceiver(entity, remove, Direction.values()[index]));

            index++;

        }

        updateRecievers(receivers);

        return updateConductorStatistics;

    }

    /**
     * This method will refresh the entire network, so use it if you know what you're doing
     */
    public void refreshNewNetwork() {
        Iterator<C> it = conductorSet.iterator();
        acceptorSet.clear();
        acceptorInputMap.clear();
        while (it.hasNext()) {
            C conductor = it.next();
            if (conductor == null || conductor.isRemoved()) {
                it.remove();
            } else {
                conductor.setNetwork((TYPE) this);

                int i = 0;
                Direction dir;

                for (BlockEntity acceptor : conductor.getConectedRecievers()) {

                    if (acceptor == null) {
                        i++;
                        continue;
                    }

                    dir = Direction.values()[i];

                    acceptorSet.add(acceptor);

                    updateRecieverStatistics(acceptor, dir);

                    HashSet<Direction> directions = acceptorInputMap.getOrDefault(acceptor, new HashSet<>());

                    directions.add(dir.getOpposite());

                    acceptorInputMap.put(acceptor, directions);

                    i++;

                }


            }
        }
        updateNewNetworkStatistics();
    }

    /**
     * Updates the statics of the entire network
     */
    public void updateNewNetworkStatistics() {
        for (C wire : conductorSet) {
            networkMaxTransfer = networkMaxTransfer == 0 ? wire.getMaxTransfer() : Math.min(networkMaxTransfer, wire.getMaxTransfer());
            updateConductorStatistics(wire, false);
        }
        for (BlockEntity reciever : acceptorSet) {
            for (Direction dir : acceptorInputMap.getOrDefault(reciever, new HashSet<>())) {
                updateRecieverStatistics(reciever, dir);
            }
        }
    }

    /**
     * Override method to define statistics per-cable
     *
     * @param cable
     */
    public void updateConductorStatistics(C cable, boolean remove) {

    }

    /**
     * Override to reset specific receiver parameters when #updateReceiver returns true
     */
    public void resetReceiverStatistics() {

    }

    /**
     * Override to reset specific conductor parameters when #updateConductor returns true
     */
    public void resetConductorStatistics() {

    }

    /**
     * Override method to define statistics per-receiver
     *
     * @param reciever
     */
    public void updateRecieverStatistics(BlockEntity reciever, Direction dir) {

    }

    /**
     * Splits this network at the specified cable
     *
     * @param splitPoint the cable this network is being split at
     */
    public void split(@Nonnull C splitPoint) {

        removeFromNetwork(splitPoint);

        BlockEntity[] connectedTiles = new BlockEntity[6];

        boolean[] dealtWith = {false, false, false, false, false, false};

        BlockPos relative;
        BlockEntity sideTile;
        Level world = splitPoint.getLevel();
        int ordinal;

        for (Direction direction : Direction.values()) {

            ordinal = direction.ordinal();

            relative = splitPoint.getBlockPos().relative(direction);

            if (!world.hasChunkAt(relative)) {
                continue;
            }

            sideTile = world.getBlockEntity(relative);

            if (sideTile == null) {
                continue;
            }

            connectedTiles[ordinal] = sideTile;
        }

        for(int index = 0; index < 6; index++) {

            BlockEntity tile = connectedTiles[index];

            if (tile == null || !isConductor(tile, splitPoint) || dealtWith[index]) {
                continue;
            }

            Set<C> explored = new AbstractNetworkFinder<>(world, tile.getBlockPos(), this, splitPoint.getBlockPos()).exploreNetwork();

            for (int i = index + 1; i < 6; i++) {

                BlockEntity connection = connectedTiles[i];

                if (isConductor(connection, (C) tile) && !dealtWith[i] && explored.contains(connection)) {

                    dealtWith[i] = true;

                }
            }

            explored.remove(splitPoint);

            TYPE newNetwork = createInstanceConductor(explored);

            newNetwork.refreshNewNetwork();

        }

        deregister();
    }

    /**
     * removes the specified cable from the network
     *
     * @param conductor the cable to be removed
     */
    public void removeFromNetwork(C conductor) {
        conductorSet.remove(conductor);
        updateConductorStatistics(conductor, true);
        if (conductorSet.isEmpty()) {
            deregister();
        }
    }

    /**
     * Removes this network from the registry
     */
    public void deregister() {
        conductorSet.clear();
        acceptorSet.clear();
        acceptorInputMap.clear();
        NetworkRegistry.deregister(this);
    }

    /**
     * returns the size of this network
     *
     * @return the size of this network
     */
    @Override
    public int getSize() {
        return conductorSet.size();
    }

    /**
     * This fires every tick on the server
     */
    @Override
    public void tick() {
        transmittedLastTick = transmittedThisTick;
        transmittedThisTick = 0;
    }

    /**
     * returns the amount of the specified value that is currently being transmitted over this network
     *
     * @return the amount currently being transmitted
     */
    public double getActiveTransmitted() {
        return transmittedLastTick;
    }

    /**
     * returns the maximum amount this network can transport per tick
     *
     * @return the maximum amount this network can transport per tick
     */
    public double getNetworkMaxTransfer() {
        return networkMaxTransfer;
    }

    /**
     * takes the input value and emits it to valid receivers on the network
     *
     * @param transfer the value to be emitted
     * @param ignored the receivers to be ignored
     * @param debug whether this should be simulated or not
     *
     * @return the amount that was accepted
     */
    public abstract P emit(P transfer, ArrayList<BlockEntity> ignored, boolean debug);

    /**
     * Checks if the tile is a cable or not
     *
     * @param tile the tile to check
     * @param requestingCable the cable in this network requesting the check
     *
     * @return whether the tile is a cable or not
     */
    public abstract boolean isConductor(BlockEntity tile, C requestingCable);

    /**
     * Creates a new instance of this network from the input set of cables
     *
     * @param conductors the set of cables
     *
     * @return a new instance of this network
     */
    public abstract TYPE createInstanceConductor(Set<C> conductors);

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AbstractNetwork<?,?,?,?> network) {
            return network.id.equals(id);
        }
        return false;
    }

    /**
     * returns the ID of this network
     *
     * @return this network's ID
     */
    @Override
    public UUID getId() {
        return id;
    }
}