package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.electricitygrid.batteries.TileBatteryBox;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.phys.AABB;

public class RenderBatteryBox extends AbstractTileRenderer<TileBatteryBox> {

	public RenderBatteryBox(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileBatteryBox tileEntityIn, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		BakedModel ibakedmodel;
		ComponentElectrodynamic el = tileEntityIn.getComponent(IComponentType.Electrodynamic);
		int stored = (int) (el.getJoulesStored() / el.getMaxJoulesStored() * 6);
		ibakedmodel = switch (stored) {
		case 1 -> getModel(ClientRegister.MODEL_BATTERYBOX2);
		case 2 -> getModel(ClientRegister.MODEL_BATTERYBOX3);
		case 3 -> getModel(ClientRegister.MODEL_BATTERYBOX4);
		case 4 -> getModel(ClientRegister.MODEL_BATTERYBOX5);
		case 5 -> getModel(ClientRegister.MODEL_BATTERYBOX6);
		case 6 -> getModel(ClientRegister.MODEL_BATTERYBOX7);
		default -> getModel(ClientRegister.MODEL_BATTERYBOX);
		};

		switch (tileEntityIn.getFacing()) {
		case NORTH -> {
			matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 90, 0));
			// matrixStackIn.mulPose(new Quaternion(0, 90, 0, true));
			matrixStackIn.translate(-1, 0, 0);
		}
		case SOUTH -> {
			matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 270, 0));
			// matrixStackIn.mulPose(new Quaternion(0, 270, 0, true));
			matrixStackIn.translate(0, 0, -1);
		}
		case WEST -> {
			matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 180, 0));
			// matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));
			matrixStackIn.translate(-1, 0, -1);
		}
		default -> {
		}
		}
		matrixStackIn.translate(0.5, 0.5, 0.5);
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}
	
	@Override
	public AABB getRenderBoundingBox(TileBatteryBox blockEntity) {
	    return super.getRenderBoundingBox(blockEntity).inflate(2);
	}
}
