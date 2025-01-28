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

public class ModelServoLeggings<T extends LivingEntity> extends GenericArmorModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Electrodynamics.rl("servoleggings"), "main");

    public ModelServoLeggings(ModelPart root) {
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

        part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(0, 20)
                //
                .addBox(-2.1F, 0.0F, -3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(19, 19)
                //
                .addBox(-2.1F, 0.0F, 2.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 5)
                //
                .addBox(-3.1F, 0.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(14, 0)
                //
                .addBox(2.0F, 0.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(23, 4)
                //
                .addBox(-4.1F, 1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(7, 0)
                //
                .addBox(-2.1F, 1.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 25)
                //
                .addBox(-3.1F, 1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 15)
                //
                .addBox(-4.1F, 1.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(19, 9)
                //
                .addBox(-1.9F, 0.0F, -3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 14)
                //
                .addBox(-1.9F, 0.0F, 2.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                //
                .addBox(-1.8F, 0.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                //
                .addBox(2.1F, 0.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(11, 20)
                //
                .addBox(3.1F, 1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(19, 0)
                //
                .addBox(-1.9F, 1.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 25)
                //
                .addBox(2.1F, 1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 25)
                //
                .addBox(3.1F, 1.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int light) {
        rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
        leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
    }
}