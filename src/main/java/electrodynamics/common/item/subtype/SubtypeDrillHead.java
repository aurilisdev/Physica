package electrodynamics.common.item.subtype;

import java.util.Locale;

import electrodynamics.Electrodynamics;
import electrodynamics.api.ISubtype;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.resources.ResourceLocation;

public enum SubtypeDrillHead implements ISubtype {
	steel(200, false, new Color(194, 194, 194, 255), 1), // 194, 194, 194
	stainlesssteel(400, false, new Color(210, 255, 238, 255), 1.5), // 183, 199, 193
	hslasteel(600, false, new Color(133, 190, 255, 255), 1.75), // 133, 190, 255
	titanium(1000, false, new Color(186, 196, 205, 255), 2), // 186, 196, 205
	titaniumcarbide(1, true, new Color(228, 198, 173, 255), 2.5); // 228, 198, 173

	public final int durability;
	public final boolean isUnbreakable;
	public final ResourceLocation blockTextureLoc;
	public final Color color;
	public final double speedBoost;

	SubtypeDrillHead(int durability, boolean unbreakable, Color color, double speedBoost) {
		this.durability = durability;
		isUnbreakable = unbreakable;
		blockTextureLoc = Electrodynamics.rl("block/resource/resourceblock" + toString().toLowerCase(Locale.ROOT));
		this.color = color;
		this.speedBoost = speedBoost;
	}

	@Override
	public String tag() {
		return "drillhead" + name();
	}

	@Override
	public String forgeTag() {
		return "drillhead/" + name();
	}

	@Override
	public boolean isItem() {
		return true;
	}

}
