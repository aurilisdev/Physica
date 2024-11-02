package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import electrodynamics.api.fluid.PropertyFluidTank;
import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactor;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
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

        ComponentProcessor processor = tile.getComponent(IComponentType.Processor);

        if(processor.isActive()){
            poseStack.pushPose();

            poseStack.translate(0.5, 0.5, 0.5);

            poseStack.translate(0, 1.0, 0);

            float progress = (float) (processor.operatingTicks.get() / processor.requiredTicks.get());

            float rotation = progress * 90.0F;

            poseStack.mulPose(MathUtils.rotVectorQuaternionDeg(rotation, MathUtils.YP));

            RenderingUtils.renderModel(getModel(ClientRegister.MODEL_CHEMICALREACTOR_ROTOR), tile, RenderType.solid(), poseStack, bufferSource, packedLight, packedOverlay);

            poseStack.popPose();
        }

        //TODO

        poseStack.pushPose();

        ComponentInventory inv = tile.getComponent(IComponentType.Inventory);

        if(!inv.areInputsEmpty()) {

            poseStack.translate(0.5, 0.5, 0.5);

            poseStack.translate(0, 0.75, 0);

            ItemStack input1 = inv.getItem(0);
            ItemStack input2 = inv.getItem(1);

            poseStack.pushPose();

            if(!input1.isEmpty()) {

                if(!input2.isEmpty()) {
                    poseStack.translate(0.1875, 0, 0.1875);
                }

                renderItem(input1, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
            }

            poseStack.popPose();

            poseStack.pushPose();

            if(!input2.isEmpty()) {

                if(!input1.isEmpty()) {
                    poseStack.translate(-0.1875, 0, -0.1875);
                }

                renderItem(input2, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
            }

            poseStack.popPose();


        }

        poseStack.popPose();

        //render fluids

        ComponentFluidHandlerMulti multi = tile.getComponent(IComponentType.FluidHandler);

        PropertyFluidTank[] tanks = multi.getInputTanks();

        poseStack.pushPose();

        if(!tanks[0].isEmpty()){

            poseStack.translate(0, 1, 0);

            RenderingUtils.renderFluidBox(poseStack, minecraft(), bufferSource.getBuffer(RenderType.translucent()), new AABB(0.0625, 0.25, 0.0625, 0.9375, 1, 0.9375), tanks[0].getFluid(), packedLight, packedOverlay);


        } else if (!tanks[1].isEmpty()){

        }

        poseStack.popPose();

        //render gases

        poseStack.popPose();



    }

    @Override
    public AABB getRenderBoundingBox(TileChemicalReactor blockEntity) {
        return super.getRenderBoundingBox(blockEntity).expandTowards(0, 2, 0);
    }
}
