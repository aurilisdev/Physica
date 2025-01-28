package electrodynamics.prefab.tile.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import electrodynamics.api.network.cable.IRefreshableCable;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.prefab.network.AbstractNetwork;
import electrodynamics.prefab.utilities.Scheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GenericRefreshingConnectTile<CABLETYPE, CONDUCTOR extends GenericRefreshingConnectTile<CABLETYPE, CONDUCTOR, NETWORK>, NETWORK extends AbstractNetwork<CONDUCTOR, CABLETYPE, ?, NETWORK>> extends GenericConnectTile implements IRefreshableCable<CABLETYPE, NETWORK> {

    private final EnumConnectType[] previousConnections = {EnumConnectType.NONE, EnumConnectType.NONE, EnumConnectType.NONE, EnumConnectType.NONE, EnumConnectType.NONE, EnumConnectType.NONE};
    protected final BlockEntity[] recieverConnections = new BlockEntity[6];
    protected final BlockEntity[] prevRecieverConnections = new BlockEntity[6];
    protected final BlockEntity[] cableConnections = new BlockEntity[6];
    protected final BlockEntity[] prevCableConnections = new BlockEntity[6];
    protected final HashSet<CONDUCTOR> connectionSet = new HashSet<>();

    private NETWORK network;

    public boolean isQueued = false;

    public GenericRefreshingConnectTile(BlockEntityType<?> tile, BlockPos pos, BlockState state) {
        super(tile, pos, state);
    }

    public Pair<List<UpdatedReceiver>, List<UpdatedConductor<CONDUCTOR>>> updateAdjacent(Direction[] dirs) {

        boolean flag = false;
        int ordinal;
        EnumConnectType prevConnection, connection;

        List<UpdatedReceiver> updatedRecievers = new ArrayList<>();
        List<UpdatedConductor<CONDUCTOR>> updatedConductors = new ArrayList<>();

        for (Direction dir : dirs) {

            ordinal = dir.ordinal();
            connection = connectionsArr[ordinal];
            prevConnection = previousConnections[ordinal];

            BlockEntity entity = level.getBlockEntity(worldPosition.relative(dir));

            if (prevConnection == connection) {
                continue;
            }

            if (connection == EnumConnectType.NONE && prevConnection == EnumConnectType.WIRE) {
                updatedConductors.add(new UpdatedConductor<>((CONDUCTOR) prevCableConnections[ordinal], true));
            } else if (connection == EnumConnectType.WIRE && prevConnection == EnumConnectType.NONE) {
                updatedConductors.add(new UpdatedConductor<>((CONDUCTOR) entity, false));
            } else if (connection == EnumConnectType.NONE && prevConnection == EnumConnectType.INVENTORY) {
                updatedRecievers.add(new UpdatedReceiver(prevRecieverConnections[ordinal], true, dir));
            } else if (connection == EnumConnectType.INVENTORY && prevConnection == EnumConnectType.NONE) {
                updatedRecievers.add(new UpdatedReceiver(entity, false, dir));
            }

            prevCableConnections[ordinal] = cableConnections[ordinal];
            prevRecieverConnections[ordinal] = recieverConnections[ordinal];
            recieverConnections[ordinal] = null;
            cableConnections[ordinal] = null;

            if (connection == EnumConnectType.WIRE) {
                cableConnections[ordinal] = entity;
            } else if (connection == EnumConnectType.INVENTORY) {
                recieverConnections[ordinal] = entity;
            }
            previousConnections[ordinal] = connection;
            flag = true;

        }

        if (flag) {
            connectionSet.clear();
            for (BlockEntity entity : cableConnections) {
                if (entity == null) {
                    continue;
                }
                connectionSet.add((CONDUCTOR) entity);
            }
        }

        return Pair.of(updatedRecievers, updatedConductors);
    }

    @Override
    public NETWORK getNetwork() {
        return network;
    }

    @Override
    public void createNetworkFromThis() {
        network = createInstanceConductor(Sets.newHashSet((CONDUCTOR) this));
        network.refreshNewNetwork();
    }

    @Override
    public void setNetwork(NETWORK network) {
        if (this.network == null) {
            this.network = network;
        } else if (!this.network.equals(network)) {
            removeFromNetwork();
            this.network = network;
        }
    }

    @Override
    public void updateNetwork(Direction[] dirs) {
        if (isRemoved()) {
            return;
        }
        if (level == null) {
            if (!isQueued) {
                isQueued = true;
                Scheduler.schedule(1, () -> updateNetwork(dirs));
            }
        }

        isQueued = false;

        if(level.isClientSide) {
            return;
        }

        Pair<List<UpdatedReceiver>, List<UpdatedConductor<CONDUCTOR>>> changed = updateAdjacent(dirs);

        if (changed.getSecond().isEmpty()) {

            if (network == null) {
                createNetworkFromThis();
            }

            if (!changed.getFirst().isEmpty()) {

                network.updateRecievers(changed.getFirst());

            }

        } else {

            HashSet<NETWORK> adjacentNetworks = new HashSet<>();

            for (UpdatedConductor<CONDUCTOR> wire : changed.getSecond()) {

                if (wire.conductor() == null || wire.conductor().isRemoved() || wire.conductor().getNetwork() == null) {
                    continue;
                }

                adjacentNetworks.add((NETWORK) wire.conductor().getNetwork());

            }

            if (adjacentNetworks.isEmpty()) {

                if (network == null) {
                    createNetworkFromThis();
                }

                network.updateConductors(changed.getSecond());

            } else {

                List<NETWORK> networks = new ArrayList<>(adjacentNetworks);

                if (networks.size() > 1) {

                    if (network == null) {

                        createNetworkFromThis();

                    }

                    adjacentNetworks.add(network);

                    NETWORK newNetwork = createInstance(adjacentNetworks);

                    network = newNetwork;

                    newNetwork.refreshNewNetwork();


                } else {

                    if (network == null) {

                        network = networks.get(0);

                        network.updateConductor((CONDUCTOR) this, false);

                    } else {

                        adjacentNetworks.add(network);

                        NETWORK newNetwork = createInstance(adjacentNetworks);

                        network = newNetwork;

                        newNetwork.refreshNewNetwork();

                    }

                }

            }

        }
    }

    @Override
    public void removeFromNetwork() {
        if (network != null) {
            network.removeFromNetwork((CONDUCTOR) this);
        }
    }

    @Override
    public BlockEntity[] getConectedRecievers() {
        return recieverConnections;
    }

    @Override
    public BlockEntity[] getConnectedCables() {
        return cableConnections;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (!level.isClientSide && network != null) {
            getNetwork().split((CONDUCTOR) this);
        }

    }

    @Override
    public void onChunkUnloaded() {
        if (!level.isClientSide && network != null) {
            getNetwork().split((CONDUCTOR) this);
        }
        super.onChunkUnloaded();
    }

    public abstract NETWORK createInstanceConductor(Set<CONDUCTOR> conductors);

    public abstract NETWORK createInstance(Set<NETWORK> networks);


    @Override
    public void onLoad() {
        super.onLoad();
        updateNetwork(Direction.values());
    }

    public static record UpdatedReceiver(BlockEntity reciever, boolean removed, Direction dir) {

    }

    public static record UpdatedConductor<CONDUCTOR>(CONDUCTOR conductor, boolean removed) {

    }

}
