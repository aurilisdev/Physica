package electrodynamics.api.network.cable;

import electrodynamics.prefab.network.AbstractNetwork;
import electrodynamics.prefab.tile.types.GenericRefreshingConnectTile;
import net.minecraft.core.Direction;

public interface IRefreshableCable<CONDUCTORTYPE, T extends AbstractNetwork<? extends GenericRefreshingConnectTile<?, ?, ?>, ?, ?, ?>> extends IAbstractCable<CONDUCTORTYPE, T> {

	void updateNetwork(Direction...dirs);

	void destroyViolently();

}
