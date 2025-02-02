package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.electricitygrid.generators.TileAdvancedSolarPanel;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.phys.AABB;

public class RenderAdvancedSolarPanel extends AbstractTileRenderer<TileAdvancedSolarPanel> {

	public RenderAdvancedSolarPanel(BlockEntityRendererProvider.Context context) {
		super(context);
	}
	
        @Override
        public AABB getRenderBoundingBox(TileAdvancedSolarPanel blockEntity) {
            return new AABB(blockEntity.getBlockPos()).inflate(1);
        }
        
	@Override
	public void render(TileAdvancedSolarPanel solarPanel, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

		matrixStackIn.translate(0.5, 2, 0.5);

		matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.YP));
		// matrixStackIn.mulPose(new Quaternion(new Vector3f(0, 1, 0), 90, true));

		long time = solarPanel.getLevel().getLevelData().getDayTime();

		if (time < 13000 || time > 23000) {
			solarPanel.currentRotation.setValue(solarPanel.currentRotation.getValue() + (time / 24000.0 * Math.PI * 2 - Math.PI / 2.0 - solarPanel.currentRotation.getValue()) / 40.0);
		}

		matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg((float) -solarPanel.currentRotation.getValue(), MathUtils.XP));
		// matrixStackIn.mulPose(new Quaternion(new Vector3f(1, 0, 0), (float) -solarPanel.currentRotation.getValue(), false));

		matrixStackIn.scale(2, 2, 2);

		BakedModel ibakedmodel = getModel(ClientRegister.MODEL_ADVSOLARTOP);

		RenderingUtils.renderModel(ibakedmodel, solarPanel, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}

}
