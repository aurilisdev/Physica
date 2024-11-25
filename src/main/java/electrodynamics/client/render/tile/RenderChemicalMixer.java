package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.machines.TileChemicalMixer;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class RenderChemicalMixer extends AbstractTileRenderer<TileChemicalMixer> {

    public RenderChemicalMixer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileChemicalMixer tileEntityIn, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        BakedModel ibakedmodel = getModel(ClientRegister.MODEL_CHEMICALMIXERBASE);
        matrixStackIn.pushPose();
        RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
        matrixStackIn.translate(0, 1 / 16.0, 0);
        RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        ibakedmodel = getModel(ClientRegister.MODEL_CHEMICALMIXERBLADES);
        matrixStackIn.translate(0.5, 7.0 / 16.0, 0.5);

        ComponentProcessor proc = tileEntityIn.getComponent(IComponentType.Processor);

        float degrees = 0.0F;

        if (proc.isActive()) {
            degrees = proc.operatingTicks.get().floatValue() / Math.max(proc.requiredTicks.get().floatValue(), 1.0F) * 360.0F * proc.operatingSpeed.get().floatValue() * 2.0F;
        }

        matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, degrees, 0));
        // matrixStackIn.mulPose(new Quaternion(0, degrees, 0, true));
        RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        ComponentFluidHandlerMulti multi = tileEntityIn.getComponent(IComponentType.FluidHandler);
        FluidStack fluid = null;
        for (FluidTank tank : multi.getInputTanks()) {
            if (!tank.isEmpty()) {
                fluid = tank.getFluid();
                break;
            }
        }
        if (fluid == null) {
            for (FluidTank tank : multi.getOutputTanks()) {
                if (!tank.isEmpty()) {
                    fluid = tank.getFluid();
                    break;
                }
            }
        }
        if (fluid != null) {
            AABB box = new AABB(2.0D / 16.0D, 7.0D / 16.0D, 2.0D / 16.0D, 14.0D / 16.0D, 10.0D / 16.0D, 14.0D / 16.0D);
            VertexConsumer builder = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
            RenderingUtils.renderFluidBox(matrixStackIn, Minecraft.getInstance(), builder, box, fluid, combinedLightIn, combinedOverlayIn, RenderingUtils.ALL_FACES);
        }
        matrixStackIn.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(TileChemicalMixer blockEntity) {
        return super.getRenderBoundingBox(blockEntity).inflate(1);
    }

}
