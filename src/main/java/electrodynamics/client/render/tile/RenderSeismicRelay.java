package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.machines.quarry.TileQuarry;
import electrodynamics.common.tile.machines.quarry.TileSeismicRelay;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class RenderSeismicRelay extends AbstractTileRenderer<TileSeismicRelay> {

	private static final AABB LEFT = new AABB(0.1875F, 0.9375F, 0.1875F, 0.8125F, 0.98F, 0.3125F);
	private static final AABB UP = new AABB(0.1875F, 0.9375F, 0.3125F, 0.3125F, 0.98F, 0.6875F);
	private static final AABB RIGHT = new AABB(0.1875F, 0.9375F, 0.6875F, 0.8125F, 0.98F, 0.8125F);
	private static final AABB DOWN = new AABB(0.6875F, 0.9375F, 0.3125F, 0.8125F, 0.98F, 0.6875F);

	public RenderSeismicRelay(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileSeismicRelay tile, float tick, @NotNull PoseStack stack, @NotNull MultiBufferSource source, int light, int overlay) {
		if (tile.markerLocs.get().size() > 3) {
			Minecraft minecraft = Minecraft.getInstance();
			VertexConsumer sheetBuilder = source.getBuffer(RenderingUtils.beaconType());
			RenderingUtils.renderSolidColorBox(stack, minecraft, sheetBuilder, LEFT, 1.0F, 0F, 0F, 1.0F, 255, 0, RenderingUtils.ALL_FACES);
			RenderingUtils.renderSolidColorBox(stack, minecraft, sheetBuilder, UP, 1.0F, 0F, 0F, 1.0F, 255, 0, RenderingUtils.ALL_FACES);
			RenderingUtils.renderSolidColorBox(stack, minecraft, sheetBuilder, RIGHT, 1.0F, 0F, 0F, 1.0F, 255, 0, RenderingUtils.ALL_FACES);
			RenderingUtils.renderSolidColorBox(stack, minecraft, sheetBuilder, DOWN, 1.0F, 0F, 0F, 1.0F, 255, 0, RenderingUtils.ALL_FACES);

			Direction facing = tile.getFacing().getOpposite();

			boolean doesQuarryHaveRing = false;
			BlockEntity entity = level().getBlockEntity(tile.getBlockPos().relative(facing.getClockWise()));
			if (entity instanceof TileQuarry quarry) {
				doesQuarryHaveRing = quarry.hasRing.get();
			}
			entity = level().getBlockEntity(tile.getBlockPos().relative(facing.getCounterClockWise()));
			if (entity instanceof TileQuarry quarry) {
				doesQuarryHaveRing = quarry.hasRing.get();
			}

			if (!doesQuarryHaveRing) {
				return;
			}

			AABB beam;
			if (facing == Direction.NORTH) {
				beam = new AABB(0.4375, 0.5625, 0, 0.5625, 0.6875, -0.375);
			} else if (facing == Direction.SOUTH) {
				beam = new AABB(0.4375, 0.5625, 1, 0.5625, 0.6875, 1.375);
			} else if (facing == Direction.EAST) {
				beam = new AABB(1, 0.5625, 0.4375, 1.375, 0.6875, 0.5625);
			} else {
				beam = new AABB(0, 0.5625, 0.4375, -0.375, 0.6875, 0.5625);
			}

			int time = 200;
			int cutoff = 180;
			int half = (time - cutoff) / 2;

			float alpha = getGameTime() % time;

			if (alpha < cutoff) {
				return;
			}

			alpha = time - alpha;
			if (alpha <= half) {
				alpha = alpha / half;
			} else {
				alpha = alpha - half;
				alpha = 1.0F - alpha / half;
			}

			TextureAtlasSprite whiteTexture = ClientRegister.getSprite(ClientRegister.TEXTURE_WHITE);

			RenderingUtils.renderFilledBoxNoOverlay(stack, sheetBuilder, beam, 1.0F, 0, 0, alpha, whiteTexture.getU0(), whiteTexture.getV0(), whiteTexture.getV1(), whiteTexture.getV1(), 255, RenderingUtils.ALL_FACES);
		}
	}

}
