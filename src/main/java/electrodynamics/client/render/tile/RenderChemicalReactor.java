package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.machines.TileChemicalReactor;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class RenderChemicalReactor extends AbstractTileRenderer<TileChemicalReactor> {
    public RenderChemicalReactor(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileChemicalReactor tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        poseStack.pushPose();

        switch (tile.getBlockState().getValue(GenericEntityBlock.FACING)) {
            case NORTH -> {
                poseStack.mulPose(MathUtils.rotQuaternionDeg(0, 90, 0));
                // matrixStackIn.mulPose(new Quaternion(0, 90, 0, true));
                poseStack.translate(-1, 0, 0);
            }
            case SOUTH -> {
                poseStack.mulPose(MathUtils.rotQuaternionDeg(0, 270, 0));
                // matrixStackIn.mulPose(new Quaternion(0, 270, 0, true));
                poseStack.translate(0, 0, -1);
            }
            case WEST -> {
                poseStack.mulPose(MathUtils.rotQuaternionDeg(0, 180, 0));
                // matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));
                poseStack.translate(-1, 0, -1);
            }
            default -> {
            }
        }
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.translate(0, 1.0, 0);
        RenderingUtils.renderModel(getModel(ClientRegister.MODEL_CHEMICALREACTOR_BASE), tile, RenderType.solid(), poseStack, bufferSource, packedLight, packedOverlay);
        RenderingUtils.renderModel(getModel(ClientRegister.MODEL_CHEMICALREACTOR_ROTOR), tile, RenderType.solid(), poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.popPose();

    }

    @Override
    public AABB getRenderBoundingBox(TileChemicalReactor blockEntity) {
        return super.getRenderBoundingBox(blockEntity).expandTowards(0, 2, 0);
    }
}
