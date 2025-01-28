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

public class ModelHydraulicBoots<T extends LivingEntity> extends GenericArmorModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Electrodynamics.rl("hydraulic_boots"), "main");

    public ModelHydraulicBoots(ModelPart root) {
        super(root);

        parentHat.visible = false;
        parentHead.visible = false;
        parentChest.visible = false;
        parentRightArm.visible = false;
        parentLeftArm.visible = false;
        parentRightLeg.visible = true;
        parentLeftLeg.visible = true;

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        part.addOrReplaceChild(HAT, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(HEAD, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(CHEST, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(LEFT_ARM, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(18, 14)
                //
                .addBox(2.0F, 9.0F, -2.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(5, 26)
                //
                .addBox(2.0F, 4.0F, -2.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(11, 10)
                //
                .addBox(-3.0F, 9.0F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 18)
                //
                .addBox(-3.0F, 4.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(23, 12)
                //
                .addBox(-2.0F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 5)
                //
                .addBox(-2.0F, 4.0F, 2.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 22)
                //
                .addBox(-2.0F, 9.0F, -3.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 27)
                //
                .addBox(-2.0F, 4.0F, -3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 5)
                //
                .addBox(-2.0F, 12.1F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(24, 30)
                //
                .addBox(-3.6F, 5.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 30)
                //
                .addBox(-0.5F, 5.0F, 2.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(0, 0)
                //
                .addBox(-1.8F, 12.1F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(18, 7)
                //
                .addBox(-1.8F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 26)
                //
                .addBox(-1.8F, 4.0F, 2.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 0)
                //
                .addBox(-1.8F, 9.0F, -3.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 17)
                //
                .addBox(-1.8F, 4.0F, -3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 18)
                //
                .addBox(-1.8F, 9.0F, -2.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 24)
                //
                .addBox(-1.8F, 4.0F, -2.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                //
                .addBox(2.2F, 9.0F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(13, 1)
                //
                .addBox(2.2F, 4.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(19, 30)
                //
                .addBox(2.7F, 5.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 29)
                //
                .addBox(-0.5F, 5.0F, 2.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int light) {
        parentRightLeg.render(poseStack, buffer, packedLight, packedOverlay);
        parentLeftLeg.render(poseStack, buffer, packedLight, packedOverlay);
    }
}