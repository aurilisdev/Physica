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

public class ModelJetpack<T extends LivingEntity> extends GenericArmorModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Electrodynamics.rl("jetpack"), "main");

    public ModelJetpack(ModelPart root) {
        super(root);

        parentHat.visible = false;
        parentHead.visible = false;
        parentChest.visible = true;
        parentRightArm.visible = false;
        parentLeftArm.visible = false;
        parentRightLeg.visible = false;
        parentLeftLeg.visible = false;

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        part.addOrReplaceChild(HAT, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(HEAD, CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        part.addOrReplaceChild(CHEST, CubeListBuilder.create().texOffs(15, 24)
                //
                .addBox(-3.0F, 0.0F, -3.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 27)
                //
                .addBox(-4.0F, 5.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 18)
                //
                .addBox(3.0F, 5.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(31, 6)
                //
                .addBox(3.0F, 6.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 30)
                //
                .addBox(-4.0F, 6.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15)
                //
                .addBox(4.01F, 6.0F, -3.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(9, 9)
                //
                .addBox(-4.0F, 6.0F, -3.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(9, 2)
                //
                .addBox(-3.0F, 0.0F, -3.0F, 1.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 8)
                //
                .addBox(2.0F, 0.0F, -3.0F, 1.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(10, 24)
                //
                .addBox(2.0F, 0.0F, -3.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 17)
                //
                .addBox(2.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 9)
                //
                .addBox(-3.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                //
                .addBox(-3.0F, 2.0F, 2.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 17)
                //
                .addBox(1.5F, 4.5F, 3.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 23)
                //
                .addBox(2.0F, 4.5F, 3.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 17)
                //
                .addBox(-1.0F, 3.5F, 3.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(22, 9)
                //
                .addBox(-0.5F, 3.5F, 3.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(29, 3)
                //
                .addBox(-0.5F, 7.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 8)
                //
                .addBox(-0.5F, 0.5F, 3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15)
                //
                .addBox(2.0F, 7.5F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 9)
                //
                .addBox(1.5F, 10.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 24)
                //
                .addBox(2.0F, 10.0F, 2.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(5, 30)
                //
                .addBox(2.0F, 9.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 0)
                //
                .addBox(-3.5F, 4.5F, 3.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 23)
                //
                .addBox(-3.0F, 4.5F, 3.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 9)
                //
                .addBox(-3.0F, 7.5F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 23)
                //
                .addBox(-3.5F, 10.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 5)
                //
                .addBox(-3.0F, 10.0F, 2.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 30)
                //
                .addBox(-3.0F, 9.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 15)
                //
                .addBox(0.5F, 2.5F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(25, 0)
                //
                .addBox(-2.5F, 2.5F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 28)
                //
                .addBox(2.0F, 3.5F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 12)
                //
                .addBox(-3.0F, 3.5F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        part.addOrReplaceChild(LEFT_ARM, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int light) {
        parentChest.render(poseStack, buffer, packedLight, packedOverlay);
    }
}