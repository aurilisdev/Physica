package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.common.tile.machines.TileFermentationPlant;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class RenderFermentationPlant extends AbstractTileRenderer<TileFermentationPlant> {

	public RenderFermentationPlant(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileFermentationPlant tileEntityIn, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.pushPose();
		ComponentFluidHandlerMulti multi = tileEntityIn.getComponent(IComponentType.FluidHandler);

		Direction facing = tileEntityIn.getFacing();

		FluidStack input = null;
		for (FluidTank tank : multi.getInputTanks()) {
			if (!tank.isEmpty()) {
				input = tank.getFluid();
				break;
			}
		}
		if (input != null) {
			AABB box;
			if (facing == Direction.WEST) {
				box = new AABB(7.0D / 16.0D, 9.0D / 16.0D, 11.0D / 16.0D, 9.0D / 16.0D, 14.0D / 16.0D, 12.0D / 16.0D);
			} else if (facing == Direction.EAST) {
				box = new AABB(7.0D / 16.0D, 9.0D / 16.0D, 4.0D / 16.0D, 9.0D / 16.0D, 14.0D / 16.0D, 5.0D / 16.0D);
			} else if (facing == Direction.SOUTH) {
				box = new AABB(11.0D / 16.0D, 9.0D / 16.0D, 7.0D / 16.0D, 12.0D / 16.0D, 14.0D / 16.0D, 9.0D / 16.0D);
			} else {
				box = new AABB(4.0D / 16.0D, 9.0D / 16.0D, 7.0D / 16.0D, 5.0D / 16.0D, 14.0D / 16.0D, 9.0D / 16.0D);
			}
			VertexConsumer builder = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			RenderingUtils.renderFluidBox(matrixStackIn, Minecraft.getInstance(), builder, box, input, combinedLightIn, combinedOverlayIn, RenderingUtils.ALL_FACES);
		}

		FluidStack output = null;
		for (FluidTank tank : multi.getOutputTanks()) {
			if (!tank.isEmpty()) {
				output = tank.getFluid();
				break;
			}
		}
		if (output != null) {
			AABB box;
			if (facing == Direction.WEST) {
				box = new AABB(6.0D / 16.0D, 5.0D / 16.0D, 2.0D / 16.0D, 10.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D);
			} else if (facing == Direction.EAST) {
				box = new AABB(6.0D / 16.0D, 5.0D / 16.0D, 9.0D / 16.0D, 10.0D / 16.0D, 7.0D / 16.0D, 14.0D / 16.0D);
			} else if (facing == Direction.SOUTH) {
				box = new AABB(2.0D / 16.0D, 5.0D / 16.0D, 6.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D, 10.0D / 16.0D);
			} else {
				box = new AABB(9.0D / 16.0D, 5.0D / 16.0D, 6.0D / 16.0D, 14.0D / 16.0D, 7.0D / 16.0D, 10.0D / 16.0D);
			}
			VertexConsumer builder = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			RenderingUtils.renderFluidBox(matrixStackIn, Minecraft.getInstance(), builder, box, output, combinedLightIn, combinedOverlayIn, RenderingUtils.ALL_FACES);
		}
		matrixStackIn.popPose();

	}
	
	@Override
	public AABB getRenderBoundingBox(TileFermentationPlant blockEntity) {
	    return super.getRenderBoundingBox(blockEntity).inflate(1);
	}

}
