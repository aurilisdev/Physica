package electrodynamics.client.render.entity;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.Electrodynamics;
import electrodynamics.common.entity.projectile.types.EntityEnergyBlast;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class RenderEnergyBlast extends EntityRenderer<EntityEnergyBlast> {

	public RenderEnergyBlast(Context renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntityEnergyBlast entity, float entityYaw, float partialTicks, PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight) {

		if (entity.tickCount < 1) {
			return;
		}

		/*
		 * int r = Electrodynamics.RANDOM.nextInt(0, 50); int g = Electrodynamics.RANDOM.nextInt(10, 40); int b = Electrodynamics.RANDOM.nextInt(60, 100);
		 * 
		 * matrixStack.pushPose(); float u0 = 0; float u1 = 1; float v0 = 0; float v1 = 1; int red = 235 - r; int green = 120 - g; int blue = 245 - b; int alpha = 255; matrixStack.translate(0.0D, 0.1F, 0.0D); matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation()); matrixStack.mulPose(MathUtils.rotVectorQuaternionDeg(180.0F, MathUtils.YP)); //matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F)); matrixStack.scale(2.0F, 2.0F, 2.0F); VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.beaconBeam(new ResourceLocation(References.ID, "textures/custom/plasmaorb.png"), true)); PoseStack.Pose posestack$pose = matrixStack.last(); Matrix4f matrix4f = posestack$pose.pose(); Matrix3f matrix3f = posestack$pose.normal(); vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, -0.25F, red, green, blue, alpha, u0, v1, packedLight); vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, -0.25F, red, green, blue, alpha, u1, v1, packedLight); vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, 0.75F, red, green, blue, alpha, u1, v0, packedLight); vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, 0.75F, red, green, blue, alpha, u0, v0, packedLight); matrixStack.popPose();
		 */

		matrixStack.pushPose();

		// matrixStack.translate(0.5, 0.5, 0.5);

		int r = Electrodynamics.RANDOM.nextInt(0, 50);
		int g = Electrodynamics.RANDOM.nextInt(10, 40);
		int b = Electrodynamics.RANDOM.nextInt(60, 100);

		float red = (235 - r) / 256.0F;
		float green = (120 - g) / 256.0F;
		float blue = (245 - b) / 256.0F;
		float alpha = 0.1F;

		int distance = entity.level().getRandom().nextIntBetweenInclusive(1, 10);

		long gameTime = entity.level().getGameTime();

		float scale = (float) Math.abs(Math.sin((gameTime + partialTicks) / 40.0)) * 0.001f + 0.001f;
		matrixStack.scale(scale, scale, scale);

		RenderingUtils.renderStar(matrixStack, buffer, gameTime + partialTicks, 250 / distance, red, green, blue, alpha, false);
		RenderingUtils.renderStar(matrixStack, buffer, gameTime + 20f + partialTicks, 250 / distance, red, green, blue, alpha, false);
		RenderingUtils.renderStar(matrixStack, buffer, gameTime + 40f + partialTicks, 250 / distance, red, green, blue, alpha, false);

		RenderingUtils.renderStar(matrixStack, buffer, gameTime + 60f + partialTicks, 250 / distance, red, green, blue, alpha, false);

		matrixStack.popPose();
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull EntityEnergyBlast entity) {
		return Electrodynamics.rl("custom/plasmaorb");
	}

}
