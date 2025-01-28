package electrodynamics.client.render.model.armor.types;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.Electrodynamics;
import electrodynamics.client.render.model.armor.GenericArmorModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class ModelNightVisionGoggles<T extends LivingEntity> extends GenericArmorModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Electrodynamics.rl("night_vision_goggles"), "main");

    public ModelNightVisionGoggles(ModelPart root) {
        super(root);

        parentHat.visible = false;
        parentHead.visible = true;
        parentChest.visible = false;
        parentRightArm.visible = false;
        parentLeftArm.visible = false;
        parentRightLeg.visible = false;
        parentLeftLeg.visible = false;
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        part.addOrReplaceChild(HAT, CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        part.addOrReplaceChild(HEAD, CubeListBuilder.create().texOffs(11, 28)
                //
                .addBox(-3.0F, -5.0F, -8.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 28)
                //
                .addBox(2.0F, -5.0F, -8.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(22, 0)
                //
                .addBox(-2.0F, -3.0F, -8.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 22)
                //
                .addBox(-2.0F, -6.0F, -8.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(17, 22)
                //
                .addBox(-2.0F, -5.0F, -8.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                //
                .addBox(-1.0F, -7.0F, -6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 12)
                //
                .addBox(2.0F, -9.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                //
                .addBox(-3.0F, -9.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(11, 3)
                //
                .addBox(-3.0F, -8.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 0)
                //
                .addBox(2.0F, -8.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                //
                .addBox(2.0F, -8.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 3)
                //
                .addBox(-3.0F, -8.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 2)
                //
                .addBox(4.0F, -7.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(22, 12)
                //
                .addBox(-4.0F, -7.0F, 4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                //
                .addBox(-5.0F, -7.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(22, 6)
                //
                .addBox(-4.0F, -7.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        part.addOrReplaceChild(CHEST, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(LEFT_ARM, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int light) {
        parentHead.render(poseStack, buffer, packedLight, packedOverlay);
    }
}