package electrodynamics.client;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.keys.event.AbstractKeyPressHandler;
import electrodynamics.client.keys.event.HandlerModeSwitchJetpack;
import electrodynamics.client.keys.event.HandlerModeSwitchServoLegs;
import electrodynamics.client.keys.event.HandlerSwapBattery;
import electrodynamics.client.keys.event.HandlerToggleNVGoggles;
import electrodynamics.client.keys.event.HandlerToggleServoLegs;
import electrodynamics.client.render.event.guipost.AbstractPostGuiOverlayHandler;
import electrodynamics.client.render.event.guipost.HandlerArmorData;
import electrodynamics.client.render.event.guipost.HandlerRailgunTemperature;
import electrodynamics.client.render.event.levelstage.AbstractLevelStageHandler;
import electrodynamics.client.render.event.levelstage.HandlerMarkerLines;
import electrodynamics.client.render.event.levelstage.HandlerQuarryArm;
import electrodynamics.client.render.event.levelstage.HandlerSeismicScanner;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.InputEvent.Key;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.GAME, value = { Dist.CLIENT })
public class ClientEvents {

	private static final List<AbstractKeyPressHandler> KEY_PRESS_HANDLERS = new ArrayList<>();
	private static final List<AbstractLevelStageHandler> LEVEL_STAGE_RENDER_HANDLERS = new ArrayList<>();
	private static final List<AbstractPostGuiOverlayHandler> POST_GUI_OVERLAY_HANDLERS = new ArrayList<>();

	public static void init() {
		KEY_PRESS_HANDLERS.add(new HandlerToggleNVGoggles());
		KEY_PRESS_HANDLERS.add(new HandlerToggleServoLegs());
		KEY_PRESS_HANDLERS.add(new HandlerModeSwitchServoLegs());
		KEY_PRESS_HANDLERS.add(new HandlerModeSwitchJetpack());
		KEY_PRESS_HANDLERS.add(new HandlerSwapBattery());

		LEVEL_STAGE_RENDER_HANDLERS.add(HandlerQuarryArm.INSTANCE);
		LEVEL_STAGE_RENDER_HANDLERS.add(HandlerMarkerLines.INSTANCE);
		LEVEL_STAGE_RENDER_HANDLERS.add(HandlerSeismicScanner.INSTANCE);

		POST_GUI_OVERLAY_HANDLERS.add(new HandlerRailgunTemperature());
		POST_GUI_OVERLAY_HANDLERS.add(new HandlerArmorData());
		//POST_GUI_OVERLAY_HANDLERS.add(new HandlerJetpackMode());
	}

	@SubscribeEvent
	public static void handlerGuiOverlays(RenderGuiEvent.Post event) {
		POST_GUI_OVERLAY_HANDLERS.forEach(handler -> handler.renderToScreen(event.getGuiGraphics(), event.getPartialTick(), Minecraft.getInstance()));
	}

	@SubscribeEvent
	public static void handleRenderEvents(RenderLevelStageEvent event) {
		LEVEL_STAGE_RENDER_HANDLERS.forEach(handler -> {
			if (handler.shouldRender(event.getStage())) {
				handler.render(event.getCamera(), event.getFrustum(), event.getLevelRenderer(), event.getPoseStack(), event.getProjectionMatrix(), Minecraft.getInstance(), event.getRenderTick(), event.getPartialTick());
			}
		});
	}

	@SubscribeEvent
	public static void wipeRenderHashes(ClientPlayerNetworkEvent.LoggingOut event) {
		ScreenGuidebook.setInitNotHappened();
		Player player = event.getPlayer();
		if (player != null) {
			LEVEL_STAGE_RENDER_HANDLERS.forEach(AbstractLevelStageHandler::clear);
		}
	}

	@SubscribeEvent
	public static void handleKeyPress(Key event) {
		KEY_PRESS_HANDLERS.forEach(handler -> handler.handler(event, Minecraft.getInstance()));
	}


}