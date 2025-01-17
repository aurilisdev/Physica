package electrodynamics.prefab.utilities;

import electrodynamics.api.References;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class ElectroTextUtils {

	public static final String GUI_BASE = "gui";
	public static final String TOOLTIP_BASE = "tooltip";
	public static final String JEI_BASE = "jei";
	public static final String GUIDEBOOK_BASE = "guidebook";
	public static final String MESSAGE_BASE = "chat";
	public static final String JEI_INFO_ITEM = "info.item";
	public static final String JEI_INFO_FLUID = "info.fluid";
	public static final String BLOCK_BASE = "block";
	public static final String GAS_BASE = "gas";
	public static final String ADVANCEMENT_BASE = "advancement";
	public static final String DIMENSION = "dimension";
	public static final String CREATIVE_TAB = "creativetab";

	public static MutableComponent tooltip(String key, Object... additional) {
		return translated(TOOLTIP_BASE, key, additional);
	}

	public static MutableComponent guidebook(String key, Object... additional) {
		return translated(GUIDEBOOK_BASE, key, additional);
	}

	public static MutableComponent gui(String key, Object... additional) {
		return translated(GUI_BASE, key, additional);
	}

	public static MutableComponent chatMessage(String key, Object... additional) {
		return translated(MESSAGE_BASE, key, additional);
	}

	public static MutableComponent jeiTranslated(String key, Object... additional) {
		return Component.translatable(JEI_BASE + "." + key, additional);
	}

	public static MutableComponent jeiItemTranslated(String key, Object... additional) {
		return jeiTranslated(JEI_INFO_ITEM + "." + key, additional);
	}

	public static MutableComponent jeiFluidTranslated(String key, Object... additional) {
		return jeiTranslated(JEI_INFO_FLUID + "." + key, additional);
	}

	public static MutableComponent block(String key, Object... additional) {
		return translated(BLOCK_BASE, key, additional);
	}

	public static MutableComponent gas(String key, Object... additional) {
		return translated(GAS_BASE, key, additional);
	}

	public static MutableComponent advancement(String key, Object... additional) {
		return translated(ADVANCEMENT_BASE, key, additional);
	}

	public static MutableComponent dimension(String key, Object... additional) {
		return translated(DIMENSION, key, additional);
	}

	public static MutableComponent dimension(ResourceKey<Level> level, Object... additional) {
		return dimension(level.location().getPath(), additional);
	}

	public static MutableComponent creativeTab(String key, Object... additional) {
		return translated(CREATIVE_TAB, key, additional);
	}

	public static MutableComponent translated(String base, String key, Object... additional) {
		return Component.translatable(base + "." + References.ID + "." + key, additional);
	}

	public static boolean guiExists(String key) {
		return translationExists(GUI_BASE, key);
	}

	public static boolean tooltipExists(String key) {
		return translationExists(TOOLTIP_BASE, key);
	}

	public static boolean dimensionExistst(String key) {
		return translationExists(DIMENSION, key);
	}

	public static boolean dimensionExists(ResourceKey<Level> level) {
		return dimensionExistst(level.location().getPath());
	}

	public static boolean translationExists(String base, String key) {
		return I18n.exists(base + "." + References.ID + "." + key);
	}

	public static MutableComponent voltageTooltip(int voltage) {
		return tooltip("machine.voltage", ChatFormatter.getChatDisplayShort(voltage, DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY);
	}

	public static MutableComponent ratio(Component numerator, Component denominator) {
		return numerator.copy().append(" / ").append(denominator);
	}

}
