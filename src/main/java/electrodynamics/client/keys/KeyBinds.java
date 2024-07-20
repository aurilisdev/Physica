package electrodynamics.client.keys;

import com.mojang.blaze3d.platform.InputConstants;

import electrodynamics.api.References;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class KeyBinds {

	// Category
	private static final String ELECTRODYNAMICS_CATEGORY = "keycategory.electrodynamics";

	// KEYS
	public static KeyMapping jetpackAscend;
	public static KeyMapping switchJetpackMode;
	public static KeyMapping toggleNvgs;
	public static KeyMapping switchServoLeggingsMode;
	public static KeyMapping toggleServoLeggings;
	public static KeyMapping swapBattery;

	private KeyBinds() {
	}

	@SubscribeEvent
	public static void keyEVent(RegisterKeyMappingsEvent event) {
		jetpackAscend = registerKey("jetpackascend", InputConstants.KEY_SPACE, event);
		switchJetpackMode = registerKey("jetpackmode", InputConstants.KEY_M, event);
		toggleNvgs = registerKey("togglenvgs", InputConstants.KEY_N, event);
		switchServoLeggingsMode = registerKey("servoleggingsmode", InputConstants.KEY_L, event);
		toggleServoLeggings = registerKey("toggleservoleggings", InputConstants.KEY_K, event);
		swapBattery = registerKey("swapbattery", InputConstants.KEY_R, event);

	}

	private static KeyMapping registerKey(String name, int keyCode, RegisterKeyMappingsEvent event) {
		final var key = new KeyMapping("key." + References.ID + "." + name, keyCode, KeyBinds.ELECTRODYNAMICS_CATEGORY);
		event.register(key);
		return key;
	}

}
