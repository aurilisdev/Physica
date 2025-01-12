package electrodynamics.api.network.cable;

import electrodynamics.prefab.network.AbstractNetwork;
import electrodynamics.prefab.tile.types.GenericRefreshingConnectTile;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IAbstractCable<CONDUCTORTYPE, T extends AbstractNetwork<? extends GenericRefreshingConnectTile<?, ?, ?>, ?, ?, ?>> {

	void removeFromNetwork();

	T getNetwork();

	void createNetworkFromThis();

	void setNetwork(T aValueNetwork);

	BlockEntity[] getConectedRecievers();

	BlockEntity[] getConnectedCables();

	CONDUCTORTYPE getCableType();

	double getMaxTransfer();
}
