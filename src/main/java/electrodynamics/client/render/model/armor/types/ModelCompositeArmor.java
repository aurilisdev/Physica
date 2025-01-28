package electrodynamics.client.render.model.armor.types;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.Electrodynamics;
import electrodynamics.client.render.model.armor.GenericArmorModel;
import electrodynamics.common.item.gear.armor.types.ItemCompositeArmor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class ModelCompositeArmor<T extends LivingEntity> extends GenericArmorModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Electrodynamics.rl("composite_armor"), "main");

    public ModelCompositeArmor(ModelPart root, EquipmentSlot slot) {
        super(root);

        parentHat.visible = false;

        switch (slot) {
            case HEAD -> {
                parentHead.visible = true;
                parentChest.visible = false;
                parentRightArm.visible = false;
                parentLeftArm.visible = false;
                parentRightLeg.visible = false;
                parentLeftLeg.visible = false;
            }
            case CHEST -> {
                parentHead.visible = false;
                parentChest.visible = true;
                parentRightArm.visible = true;
                parentLeftArm.visible = true;
                parentRightLeg.visible = false;
                parentLeftLeg.visible = false;
            }
            case LEGS, FEET -> {
                parentHead.visible = false;
                parentChest.visible = false;
                parentRightArm.visible = false;
                parentLeftArm.visible = false;
                parentRightLeg.visible = true;
                parentLeftLeg.visible = true;
            }
            default -> {
            }
        }
    }

    public static LayerDefinition createBodyLayer(int modelType, boolean noChestplate) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        part.addOrReplaceChild(HAT, CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        // head
        part.addOrReplaceChild(HEAD, CubeListBuilder.create().texOffs(19, 27)
                //
                .addBox(-6.0F, -6.0F, -4.0F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(30, 27)
                //
                .addBox(-5.0F, -6.0F, 4.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 10)
                //
                .addBox(4.0F, -6.0F, 4.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 27)
                //
                .addBox(5.0F, -6.0F, -4.0F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 27)
                //
                .addBox(-5.0F, -6.0F, -5.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                //
                .addBox(3.0F, -6.0F, -5.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(55, 34)
                //
                .addBox(-4.0F, -6.0F, 5.0F, 8.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(25, 0)
                //
                .addBox(-3.0F, -8.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                //
                .addBox(-4.0F, -9.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(19, 10)
                //
                .addBox(4.0F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(17, 41)
                //
                .addBox(-4.0F, -8.0F, 4.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                //
                .addBox(-5.0F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(44, 3)
                //
                .addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 0)
                //
                .addBox(-5.0F, -2.0F, -5.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(71, 69)
                //
                .addBox(-4.0F, -8.0F, -5.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 85)
                //
                .addBox(-4.0F, 0.05F, -4.0F, 8.0F, 0.1F, 8.0F, new CubeDeformation(0.0F)).texOffs(42, 84)
                //
                .addBox(-3.0F, -6.0F, -5.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        if (noChestplate) {
            part.addOrReplaceChild(CHEST, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        } else {
            // chest
            part.addOrReplaceChild(CHEST, CubeListBuilder.create().texOffs(38, 21)
                    //
                    .addBox(-4.0F, 0.5F, -3.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 8)
                    //
                    .addBox(-4.0F, 0.5F, 2.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(73, 6)
                    //
                    .addBox(-4.0F, 0.5F, -4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 10)
                    //
                    .addBox(-3.0F, 1.5F, -4.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                    //
                    .addBox(-1.0F, 1.5F, -5.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 59)
                    //
                    .addBox(-1.0F, 7.5F, -4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(57, 23)
                    //
                    .addBox(-4.0F, 0.5F, 3.0F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(79, 9)
                    //
                    .addBox(-2.0F, 4F, 3.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 41)
                    //
                    .addBox(-4.0F, 0F, -2.0F, 8.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(51, 55)
                    //
                    .addBox(-4.0F, 0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(42, 55)
                    //
                    .addBox(4.0F, 0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(38, 34)
                    //
                    .addBox(-4.0F, 0F, 2.0F, 8.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(0, 95)
                    //
                    .addBox(-4.0F, -0.05F, -2.0F, 8.0F, 0.1F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 101)
                    //
                    .addBox(-4.0F, 12.0F, -2.0F, 8.0F, 0.1F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        }
        // right arm
        part.addOrReplaceChild(RIGHT_ARM, CubeListBuilder.create().texOffs(0, 54)
                //
                .addBox(-4.0F, -2.0F, -2.0F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(33, 54)
                //
                .addBox(1.0F, -2.0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 71)
                //
                .addBox(-3.0F, -2.0F, -3.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 69)
                //
                .addBox(-3.0F, -2.0F, 2.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 0)
                //
                .addBox(-3.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        // left arm
        part.addOrReplaceChild(LEFT_ARM, CubeListBuilder.create().texOffs(24, 51)
                //
                .addBox(-1.0F, -2.0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(13, 51)
                //
                .addBox(3.0F, -2.0F, -2.0F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(22, 68)
                //
                .addBox(-1.0F, -2.0F, -3.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 68)
                //
                .addBox(-1.0F, -2.0F, 2.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(57, 28)
                //
                .addBox(-1.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        switch (modelType) {
            case 1 -> {
                // right leg only
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(77, 43)
                        //
                        .addBox(-2.1F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(77, 17)
                        //
                        .addBox(-2.1F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 41)
                        //
                        .addBox(-3.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(71, 55)
                        //
                        .addBox(2.0F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(51, 79)
                        //
                        .addBox(-1.6F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 107)
                        //
                        .addBox(-2.0F, -0.05F, -2.0F, 4.0F, 0.1F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
                // left leg only
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(74, 32)
                        //
                        .addBox(-1.9F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(71, 73)
                        //
                        .addBox(-1.9F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 71)
                        //
                        .addBox(-2F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(60, 55)
                        //
                        .addBox(2.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(42, 79)
                        //
                        .addBox(-1.4F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 113)
                        //
                        .addBox(-2.0F, -0.05F, -2.0F, 4.0F, 0.1F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));
            }
            case 2 -> {
                // right shoe only
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(70, 22)
                        //
                        .addBox(2.0F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(57, 13)
                        //
                        .addBox(-3.0F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(76, 54)
                        //
                        .addBox(-2.0F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 72)
                        //
                        .addBox(-2.0F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(45, 48)
                        //
                        .addBox(-2.0F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 17.0F, 0.0F));
                // left shoe only
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(57, 3)
                        //
                        .addBox(2.2F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(66, 7)
                        //
                        .addBox(-2F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(11, 27)
                        //
                        .addBox(-1.8F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(55, 41)
                        //
                        .addBox(-1.8F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 47)
                        //
                        .addBox(-1.8F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 17.0F, 0.0F));
            }
            case 3 -> {
                // right leg combined
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(77, 43)
                        //
                        .addBox(-2.1F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(77, 17)
                        //
                        .addBox(-2.1F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 41)
                        //
                        .addBox(-3.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(71, 55)
                        //
                        .addBox(2.0F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(51, 79)
                        //
                        .addBox(-1.6F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 107)
                        //
                        .addBox(-2.0F, -0.05F, -2.0F, 4.0F, 0.1F, 4.0F, new CubeDeformation(0.0F)).texOffs(70, 22)
                        //
                        .addBox(2.0F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(57, 13)
                        //
                        .addBox(-3.0F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(76, 54)
                        //
                        .addBox(-2.0F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 72)
                        //
                        .addBox(-2.0F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(45, 48)
                        //
                        .addBox(-2.0F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
                // left leg combined
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(74, 32)
                        //
                        .addBox(-1.9F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(71, 73)
                        //
                        .addBox(-1.9F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 71)
                        //
                        .addBox(-2F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(60, 55)
                        //
                        .addBox(2.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(42, 79)
                        //
                        .addBox(-1.4F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 113)
                        //
                        .addBox(-2.0F, -0.05F, -2.0F, 4.0F, 0.1F, 4.0F, new CubeDeformation(0.0F)).texOffs(30, 47)
                        //
                        .addBox(-1.8F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(55, 41)
                        //
                        .addBox(-1.8F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 27)
                        //
                        .addBox(-1.8F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(66, 7)
                        //
                        .addBox(-2F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(57, 3)
                        //
                        .addBox(2.2F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));
            }
            default -> {
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
            }
        }

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    // Call me a butcher, because I am hacking this game
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int light) {
        if (parentHead.visible) {
            parentHead.render(poseStack, getCustomConsumer(RenderType.entityTranslucent(ItemCompositeArmor.ARMOR_TEXTURE_LOCATION)), packedLight, packedOverlay);
        }
        if (parentChest.visible) {
            parentChest.render(poseStack, buffer, packedLight, packedOverlay);
            parentRightArm.render(poseStack, buffer, packedLight, packedOverlay);
            parentLeftArm.render(poseStack, buffer, packedLight, packedOverlay);
        }
        if (parentRightLeg.visible) {
            parentRightLeg.render(poseStack, buffer, packedLight, packedOverlay);
            parentLeftLeg.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }

}