package electrodynamics.client.render.event.levelstage;

import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;

public class HandlerMarkerLines extends AbstractLevelStageHandler {

	public static final HandlerMarkerLines INSTANCE = new HandlerMarkerLines();

	private final HashMap<BlockPos, List<AABB>> markerLines = new HashMap<>();

	@Override
	public boolean shouldRender(Stage stage) {
		return stage == Stage.AFTER_TRIPWIRE_BLOCKS;
	}

	@Override
	public void render(Camera camera, Frustum frustum, LevelRenderer renderer, PoseStack stack, Matrix4f projectionMatrix, Minecraft minecraft, int renderTick, DeltaTracker deltaTracker) {

		MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
		RenderType beaconType = RenderingUtils.beaconType();
		VertexConsumer sheetBuilder = buffer.getBuffer(beaconType);
		Vec3 camPos = camera.getPosition();

		markerLines.forEach((pos, list) -> list.forEach(aabb -> {
			stack.pushPose();
			stack.translate(-camPos.x, -camPos.y, -camPos.z);
			RenderingUtils.renderSolidColorBox(stack, minecraft, sheetBuilder, aabb, 1.0F, 0F, 0F, 1.0F, 255, 0, RenderingUtils.ALL_FACES);
			stack.popPose();
		}));
		buffer.endBatch(beaconType);

	}

	@Override
	public void clear() {
		markerLines.clear();
	}

	public static boolean containsLines(BlockPos pos) {
		return INSTANCE.markerLines.containsKey(pos);
	}

	public static void addLines(BlockPos pos, List<AABB> lines) {
		INSTANCE.markerLines.put(pos, lines);
	}

	public static void removeLines(BlockPos pos) {
		INSTANCE.markerLines.remove(pos);
	}

}
