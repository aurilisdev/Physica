package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.common.tile.electricitygrid.transformer.TileAdvancedTransformer;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public abstract class RenderAdvancedTransformer extends AbstractTileRenderer<TileAdvancedTransformer> {

    public RenderAdvancedTransformer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileAdvancedTransformer tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        Direction facing = tile.getFacing();

        Font font = Minecraft.getInstance().font;

        Component transfer = Component.literal(getTurnsString(tile));

        float scale = 0.0215f / (font.width(transfer) / 16f);

        float textX = -font.width(transfer) / 2.0f;



        poseStack.pushPose();

        Direction clockwise = facing.getClockWise();

        poseStack.translate(0.5 + clockwise.getStepX() * 0.438, 0.55 + clockwise.getStepY() / 2.0, 0.5 + clockwise.getStepZ() * 0.438);

        rotateMatrix(poseStack, clockwise);

        poseStack.scale(-scale, -scale, -scale);

        Matrix4f matrix4f = poseStack.last().pose();

        font.drawInBatch(transfer, textX, 0, Color.WHITE.color(), false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();




        poseStack.pushPose();

        Direction counterClockwise = facing.getCounterClockWise();

        poseStack.translate(0.5 - clockwise.getStepX() * 0.438, 0.55 + clockwise.getStepY() / 2.0, 0.5 - clockwise.getStepZ() * 0.438);

        rotateMatrix(poseStack, counterClockwise);

        poseStack.scale(-scale, -scale, -scale);

        matrix4f = poseStack.last().pose();

        font.drawInBatch(transfer, textX, 0, Color.WHITE.color(), false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();

    }

    public abstract String getTurnsString(TileAdvancedTransformer transformer);

    private void rotateMatrix(PoseStack stack, Direction dir) {
        switch (dir) {
            case EAST -> stack.mulPose(MathUtils.rotQuaternionDeg(0, -90, 0));// stack.mulPose(new Quaternion(0, -90, 0, true));
            case SOUTH -> stack.mulPose(MathUtils.rotQuaternionDeg(0, 180, 0));// stack.mulPose(new Quaternion(0, 180, 0, true));
            case WEST -> stack.mulPose(MathUtils.rotQuaternionDeg(0, 90, 0));// stack.mulPose(new Quaternion(0, 90, 0, true));
            default -> {
            }
        }
    }

    public static class RenderAdvancedUpgradeTransformer extends RenderAdvancedTransformer {

        public RenderAdvancedUpgradeTransformer(BlockEntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public String getTurnsString(TileAdvancedTransformer transformer) {
            int ratio = transformer.coilRatio.get().intValue();

            return "1 : " + ratio;
        }
    }

    public static class RenderAdvancedDowngradeTransformer extends RenderAdvancedTransformer {

        public RenderAdvancedDowngradeTransformer(BlockEntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public String getTurnsString(TileAdvancedTransformer transformer) {
            int ratio = (int) (1.0 / transformer.coilRatio.get());

            return ratio + " : 1";
        }
    }
}
