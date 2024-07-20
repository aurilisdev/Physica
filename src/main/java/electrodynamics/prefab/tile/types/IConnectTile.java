package electrodynamics.prefab.tile.types;

import electrodynamics.common.block.connect.util.EnumConnectType;

public interface IConnectTile {

    public EnumConnectType[] readConnections();

}
