package electrodynamics.common.block.connect.util;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public enum EnumConnectType {
	NONE,
	WIRE,
	INVENTORY;

	@Override
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}
}
