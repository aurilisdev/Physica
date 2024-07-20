package electrodynamics.prefab.utilities;

import java.util.Collections;
import java.util.List;

import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NBTUtils {

	public static final String TIMER = "timer";
	public static final String MODE = "mode";
	public static final String XP = "xp";
	public static final String USED = "used";
	public static final String ON = "on";
	public static final String SMART = "smart";
	public static final String SIZE = "size";
	public static final String DIRECTION = "dir";
	public static final String LOCATION = "loc";
	public static final String PLATES = "plates";
	public static final String SUCESS = "sucess";
	public static final String PLAYING_SOUND = "false";
	public static final String DIMENSION = "dimension";
	public static final String TEMPERATURE = "temperature";

	public static final String FORTUNE_ENCHANT = "fortuneenchant";
	public static final String SILK_TOUCH_ENCHANT = "silktouchenchant";
	public static final String SPEED_ENCHANT = "speedenchant";

	public static List<Direction> readDirectionList(ItemStack item) {
		return item.getOrDefault(ElectrodynamicsDataComponentTypes.DIRECTIONS, Collections.emptyList());
	}

	public static void writeDirectionList(List<Direction> dirs, ItemStack item) {
		item.set(ElectrodynamicsDataComponentTypes.DIRECTIONS, dirs);
	}

	public static void clearDirectionList(ItemStack item) {
		item.remove(ElectrodynamicsDataComponentTypes.DIRECTIONS);
	}

	public static CompoundTag writeDimensionToTag(ResourceKey<Level> level) {
		CompoundTag tag = new CompoundTag();
		tag.putString(DIMENSION, level.location().toString());
		return tag;
	}

	public static ResourceKey<Level> readDimensionFromTag(CompoundTag tag) {
		return ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString(DIMENSION)));
	}

}
