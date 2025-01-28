package electrodynamics.common.block.connect.util;

import java.util.Locale;

public enum EnumConnectType {
	NONE,
	WIRE,
	INVENTORY;

	@Override
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}
}
