package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.Electrodynamics;
import electrodynamics.api.fluid.PropertyFluidTank;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.particle.fluiddrop.ParticleOptionFluidDrop;
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactor;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class RenderChemicalReactor extends AbstractTileRenderer<TileChemicalReactor> {
    public RenderChemicalReactor(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileChemicalReactor tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        poseStack.pushPose();

        switch (tile.getFacing()) {
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

        boolean active = processor.isActive();

        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);

        poseStack.translate(0, 1.0, 0);

        float progress = (float) (processor.operatingTicks.get() / processor.requiredTicks.get());

        float rotation = progress * 90.0F;

        poseStack.mulPose(MathUtils.rotVectorQuaternionDeg(rotation, MathUtils.YP));

        RenderingUtils.renderModel(getModel(ClientRegister.MODEL_CHEMICALREACTOR_ROTOR), tile, RenderType.solid(), poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.popPose();


        //TODO

        poseStack.pushPose();

        ComponentInventory inv = tile.getComponent(IComponentType.Inventory);


        if (tile.hasItemInputs.get()) {

            poseStack.translate(0.5, 0.5, 0.5);

            poseStack.translate(0, 0.75, 0);

            ItemStack input1 = inv.getItem(0);
            ItemStack input2 = inv.getItem(1);

            poseStack.pushPose();

            if (!input1.isEmpty()) {

                if (!input2.isEmpty()) {
                    poseStack.translate(0.1875, 0, 0.1875);
                }

                renderItem(input1, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
            }

            poseStack.popPose();

            poseStack.pushPose();

            if (!input2.isEmpty()) {

                if (!input1.isEmpty()) {
                    poseStack.translate(-0.1875, 0, -0.1875);
                }

                renderItem(input2, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, tile.getLevel(), 0);
            }

            poseStack.popPose();


        }

        poseStack.popPose();

        //render fluids


        if (tile.hasFluidInputs.get()) {

            ComponentFluidHandlerMulti multi = tile.getComponent(IComponentType.FluidHandler);

            PropertyFluidTank[] tanks = multi.getInputTanks();

            poseStack.pushPose();

            FluidStack stack1 = tanks[0].getFluid();
            FluidStack stack2 = tanks[1].getFluid();

            if (tile.hasItemInputs.get() && active && tile.getLevel().random.nextDouble() < 0.4) {

                Color color;

                if (stack1.isEmpty()) {
                    IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(stack2.getFluid());

                    TextureAtlasSprite sp = minecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getStillTexture());

                    color = new Color(sp.getPixelRGBA(0, 5, 5));
                    color = color.multiply(new Color(attributes.getTintColor()));
                } else if (stack2.isEmpty()) {

                    IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(stack1.getFluid());

                    TextureAtlasSprite sp = minecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getStillTexture());

                    color = new Color(sp.getPixelRGBA(0, 5, 5));
                    color = color.multiply(new Color(attributes.getTintColor()));

                } else {

                    if (Electrodynamics.RANDOM.nextBoolean()) {
                        IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(stack1.getFluid());

                        TextureAtlasSprite sp = minecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getStillTexture());

                        color = new Color(sp.getPixelRGBA(0, 5, 5));
                        color = color.multiply(new Color(attributes.getTintColor()));
                    } else {
                        IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(stack2.getFluid());

                        TextureAtlasSprite sp = minecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getStillTexture());

                        color = new Color(sp.getPixelRGBA(0, 5, 5));
                        color = color.multiply(new Color(attributes.getTintColor()));
                    }

                }


                double x = tile.getBlockPos().getX();
                double y = tile.getBlockPos().getY();
                double z = tile.getBlockPos().getZ();

                x += Electrodynamics.RANDOM.nextDouble(0.5) + 0.25;
                y += 1.6875;//Electrodynamics.RANDOM.nextDouble(0.375) + 1.3125;
                z += Electrodynamics.RANDOM.nextDouble(0.5) + 0.25;


                minecraft().particleEngine.createParticle(new ParticleOptionFluidDrop().setParameters(color.rFloat(), color.gFloat(), color.bFloat(), 0.5F), x, y, z, 0, -0.1, 0);

            } else if (!tile.hasItemInputs.get()) {

                if (stack1.isEmpty()) {
                    poseStack.translate(0, 1, 0);
                    RenderingUtils.renderFluidBox(poseStack, minecraft(), bufferSource.getBuffer(RenderType.translucentMovingBlock()), new AABB(0.0625, 0.25, 0.0625, 0.9375, 1, 0.9375), stack2, packedLight, packedOverlay, RenderingUtils.ALL_FACES);
                } else if (stack2.isEmpty()) {
                    poseStack.translate(0, 1, 0);
                    RenderingUtils.renderFluidBox(poseStack, minecraft(), bufferSource.getBuffer(RenderType.translucentMovingBlock()), new AABB(0.0625, 0.25, 0.0625, 0.9375, 1, 0.9375), stack1, packedLight, packedOverlay, RenderingUtils.ALL_FACES);
                } else {

                    poseStack.pushPose();
                    poseStack.translate(0, 1, 0);
                    RenderingUtils.renderFluidBox(poseStack, minecraft(), bufferSource.getBuffer(RenderType.translucentMovingBlock()), new AABB(0.0625, 0.25, 0.0625, 0.9375, 1, 0.9375), stack1, packedLight, packedOverlay, RenderingUtils.ALL_FACES);
                    poseStack.popPose();
                    if (!stack2.isEmpty() && tile.getLevel().getRandom().nextDouble() < 0.1) {

                        double x = tile.getBlockPos().getX();
                        double y = tile.getBlockPos().getY();
                        double z = tile.getBlockPos().getZ();

                        x += Electrodynamics.RANDOM.nextDouble(0.5) + 0.25;
                        y += Electrodynamics.RANDOM.nextDouble(0.375) + 1.3125;
                        z += Electrodynamics.RANDOM.nextDouble(0.5) + 0.25;

                        minecraft().particleEngine.createParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);

                    }


                }


            }

            poseStack.popPose();


        }


        //render gases

        poseStack.pushPose();

        if (tile.hasGasInputs.get() && active && tile.getLevel().getRandom().nextDouble() < 0.8) {

            double x = tile.getBlockPos().getX();
            double y = tile.getBlockPos().getY();
            double z = tile.getBlockPos().getZ();

            y += 1.25;

            if (Electrodynamics.RANDOM.nextBoolean()) {

                x += Electrodynamics.RANDOM.nextDouble(0.875) + 0.0625;

                if (Electrodynamics.RANDOM.nextBoolean()) {
                    z += Electrodynamics.RANDOM.nextDouble(0.125) + 0.0625;
                } else {
                    z += Electrodynamics.RANDOM.nextDouble(0.125) + 0.8125;
                }


            } else {
                z += Electrodynamics.RANDOM.nextDouble(0.875) + 0.0625;

                if (Electrodynamics.RANDOM.nextBoolean()) {
                    x += Electrodynamics.RANDOM.nextDouble(0.125) + 0.0625;
                } else {
                    x += Electrodynamics.RANDOM.nextDouble(0.125) + 0.8125;
                }
            }


            //x += Electrodynamics.RANDOM.nextDouble(0.5) + 0.25;
            //y += 1.6875;//Electrodynamics.RANDOM.nextDouble(0.375) + 1.3125;
            //z += Electrodynamics.RANDOM.nextDouble(0.5) + 0.25;

            minecraft().particleEngine.createParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);

        }


        poseStack.popPose();


        //end

        poseStack.popPose();


    }

    @Override
    public AABB getRenderBoundingBox(TileChemicalReactor blockEntity) {
        return super.getRenderBoundingBox(blockEntity).expandTowards(0, 2, 0);
    }
}
