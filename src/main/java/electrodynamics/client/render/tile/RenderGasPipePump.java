package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.pipelines.gas.TileGasPipePump;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

public class RenderGasPipePump extends AbstractTileRenderer<TileGasPipePump> {

	public RenderGasPipePump(Context context) {
		super(context);
	}

	@Override
	public void render(@NotNull TileGasPipePump tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

		poseStack.pushPose();

		Direction dir = tile.getFacing();

		AABB box1, box2;

		double offset1 = 3;
		double offset2 = 0;

		if (tile.isPowered()) {

			offset2 = 3;

			long time = System.currentTimeMillis();

			int time1 = (int) (time % 1000);
			int time2 = (int) ((time + 500) % 1000);

			offset1 -= Mth.sin((float) (time1 * Math.PI / 1000.0F)) * 3.0D;
			offset2 -= Mth.sin((float) (time2 * Math.PI / 1000.0F)) * 3.0D;

		}

		if (dir == Direction.NORTH) {
			box1 = aabb(2, 13 - offset1, 5, 4, 17 - offset1, 7);
			box2 = aabb(2, 13 - offset2, 9, 4, 17 - offset2, 11);
		} else if (dir == Direction.SOUTH) {
			box1 = aabb(12, 13 - offset1, 9, 14, 17 - offset1, 11);
			box2 = aabb(12, 13 - offset2, 5, 14, 17 - offset2, 7);
		} else if (dir == Direction.EAST) {
			box1 = aabb(9, 13 - offset1, 2, 11, 17 - offset1, 4);
			box2 = aabb(5, 13 - offset2, 2, 7, 17 - offset2, 4);
		} else {
			box1 = aabb(5, 13 - offset1, 12, 7, 17 - offset1, 14);
			box2 = aabb(9, 13 - offset2, 12, 11, 17 - offset2, 14);
		}

		TextureAtlasSprite whiteTexture = ClientRegister.getSprite(ClientRegister.TEXTURE_WHITE);
		float u0 = whiteTexture.getU0();
		float u1 = whiteTexture.getU1();
		float v0 = whiteTexture.getV0();
		float v1 = whiteTexture.getV1();

		VertexConsumer builder = bufferSource.getBuffer(Sheets.solidBlockSheet());

		RenderingUtils.renderFilledBox(poseStack, builder, box1, 67.0F / 256.0F, 67.0F / 256.0F, 67.0F / 256.0F, 1, u0, v0, u1, v1, packedLight, packedOverlay, RenderingUtils.ALL_FACES);
		RenderingUtils.renderFilledBox(poseStack, builder, box2, 67.0F / 256.0F, 67.0F / 256.0F, 67.0F / 256.0F, 1, u0, v0, u1, v1, packedLight, packedOverlay, RenderingUtils.ALL_FACES);

		poseStack.popPose();

	}

}
