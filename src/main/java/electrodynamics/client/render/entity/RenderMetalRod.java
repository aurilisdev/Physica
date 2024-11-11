package electrodynamics.client.render.entity;

import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.entity.projectile.types.EntityMetalRod;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderMetalRod extends EntityRenderer<EntityMetalRod> {

	public static final float[] STEEL_COLOR = { 134.0F / 255.0F, 134.0F / 255.0F, 134.0F / 255.0F };
	public static final float[] STAINLESS_COLOR = { 211.0F / 255.0F, 218.0F / 255.0F, 218.0F / 255.0F };
	public static final float[] HSLA_COLOR = { 191.0F / 255.0F, 211.0F / 255.0F, 228.0F / 255.0F };

	public static final AABB ROD = new AABB(0.0625, 0.46875, 0.46875, 0.9375, 0.53125, 0.53125);

	public RenderMetalRod(Context renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntityMetalRod entity, float entityYaw, float partialTicks, PoseStack matrixStack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {

		matrixStack.pushPose();

		TextureAtlasSprite sprite = ClientRegister.CACHED_TEXTUREATLASSPRITES.get(ClientRegister.TEXTURE_WHITE);

		matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

		matrixStack.translate(-0.5, -0.5, -0.5);

		//Electrodynamics.LOGGER.info("y: " + entity.yRotO);
		//Electrodynamics.LOGGER.info("y: " + entity.yRotO);

		//matrixStack.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.ZP));

		// matrixStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), (float) yaw, true));
		// matrixStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F), (float) pitch, true));
		// matrixStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F), 90.0F, true));

		float[] color = getColor(entity.getNumber());

		double offsetX = entity.getX() - (int) entity.getX();
		double offsetY = entity.getY() - (int) entity.getY();
		double offsetZ = entity.getZ() - (int) entity.getZ();

		RenderingUtils.renderFilledBoxNoOverlay(matrixStack, bufferIn.getBuffer(RenderType.solid()), ROD, color[0], color[1], color[2], 1.0F, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLightIn);
		//RenderingUtils.renderFilledBoxNoOverlay(matrixStack, bufferIn.getBuffer(RenderType.lightning()), new AABB(0, 0, 0, 1, 1, 1), color[0], color[1], color[2], 1.0F, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLightIn);

		matrixStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityMetalRod entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	public static float[] getColor(int number) {
		return switch (number) {
		case 2 -> HSLA_COLOR;
		case 1 -> STAINLESS_COLOR;
		default -> STEEL_COLOR;
		};
	}

}
