package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.tile.electricitygrid.TileLogisticalWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;

public class RenderLogisticalWire extends AbstractTileRenderer<TileLogisticalWire> {

	public RenderLogisticalWire(Context context) {
		super(context);
	}

	@Override
	public void render(@NotNull TileLogisticalWire tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

		if (!tile.getBlockState().getValue(ElectrodynamicsBlockStates.LIT)) {
			return;
		}

		Minecraft minecraft = minecraft();

		BlockPos pos = tile.getBlockPos();

		RandomSource random = minecraft.level.getRandom();

		if (random.nextFloat() > 0.02) {
			return;
		}

		minecraft.level.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, random.nextFloat()), pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0, 0);

	}

}
