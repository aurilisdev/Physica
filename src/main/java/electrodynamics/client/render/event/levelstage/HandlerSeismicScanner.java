package electrodynamics.client.render.event.levelstage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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

public class HandlerSeismicScanner extends AbstractLevelStageHandler {

	public static final HandlerSeismicScanner INSTANCE = new HandlerSeismicScanner();

	private final HashMap<BlockPos, Long> pingedBlocks = new HashMap<>();

	@Override
	public boolean shouldRender(Stage stage) {
		return stage == Stage.AFTER_TRIPWIRE_BLOCKS;
	}

	@Override
	public void render(Camera camera, Frustum frustum, LevelRenderer renderer, PoseStack stack, Matrix4f projectionMatrix, Minecraft minecraft, int renderTick, DeltaTracker deltaTracker) {

		MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
		VertexConsumer builder = buffer.getBuffer(RenderType.LINES);
		Vec3 camPos = camera.getPosition();

		Iterator<Entry<BlockPos, Long>> it = pingedBlocks.entrySet().iterator();
		while (it.hasNext()) {
			Entry<BlockPos, Long> entry = it.next();
			AABB box = new AABB(entry.getKey());
			stack.pushPose();
			stack.translate(-camPos.x, -camPos.y, -camPos.z);
			LevelRenderer.renderLineBox(stack, builder, box, 1.0F, 1.0F, 1.0F, 1.0F);
			stack.popPose();
			if (System.currentTimeMillis() - entry.getValue() > 10000 || minecraft.level.getBlockState(entry.getKey()).isAir()) {
				it.remove();
			}
		}
		buffer.endBatch(RenderType.LINES);
	}

	@Override
	public void clear() {
		pingedBlocks.clear();
	}

	public static void addBlock(BlockPos pos) {
		INSTANCE.pingedBlocks.put(pos, System.currentTimeMillis());
	}

}
