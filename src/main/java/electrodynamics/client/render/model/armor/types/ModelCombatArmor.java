package electrodynamics.client.render.model.armor.types;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.client.render.model.armor.GenericArmorModel;
import electrodynamics.common.item.gear.armor.types.ItemCombatArmor;
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

public class ModelCombatArmor<T extends LivingEntity> extends GenericArmorModel<T> {

    public ModelCombatArmor(ModelPart root, EquipmentSlot slot) {
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

        part.addOrReplaceChild(HEAD, CubeListBuilder.create().texOffs(30, 28)
                //
                .addBox(-6.0F, -6.0F, -4.0F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(7, 92)
                //
                .addBox(-5.0F, -6.0F, 4.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(90, 91)
                //
                .addBox(4.0F, -6.0F, 4.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 11)
                //
                .addBox(5.0F, -6.0F, -4.0F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(66, 87)
                //
                .addBox(-5.0F, -6.0F, -5.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 66)
                //
                .addBox(3.0F, -6.0F, -5.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(55, 42)
                //
                .addBox(-4.0F, -6.0F, 5.0F, 8.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(25, 0)
                //
                .addBox(-3.0F, -8.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                //
                .addBox(-4.0F, -9.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(19, 19)
                //
                .addBox(4.0F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(41, 25)
                //
                .addBox(-4.0F, -8.0F, 4.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19)
                //
                .addBox(-5.0F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(44, 3)
                //
                .addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 0)
                //
                .addBox(-5.0F, -2.0F, -5.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(75, 30)
                //
                .addBox(-4.0F, -8.0F, -5.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                //
                .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(12, 93)
                //
                .addBox(2.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(62, 92)
                //
                .addBox(2.0F, -6.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(91, 17)
                //
                .addBox(-3.0F, -6.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(91, 27)
                //
                .addBox(-3.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 82)
                //
                .addBox(-3.0F, -5.0F, -8.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(71, 0)
                //
                .addBox(-2.0F, -6.0F, -8.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(69, 24)
                //
                .addBox(-2.0F, -3.0F, -8.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(25, 10)
                //
                .addBox(2.0F, -5.0F, -8.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(83, 34)
                //
                .addBox(-2.0F, -5.0F, -8.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        if (noChestplate) {
            part.addOrReplaceChild(CHEST, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        } else {
            part.addOrReplaceChild(CHEST, CubeListBuilder.create().texOffs(19, 42)
                    //
                    .addBox(-4.0F, 0.5F, -3.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 36)
                    //
                    .addBox(-4.0F, 0.5F, 2.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 6)
                    //
                    .addBox(-4.0F, 0.5F, -4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 19)
                    //
                    .addBox(-3.0F, 1.5F, -4.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                    //
                    .addBox(-1.0F, 1.5F, -5.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(91, 40)
                    //
                    .addBox(-1.0F, 7.5F, -4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 35)
                    //
                    .addBox(-4.0F, 0.5F, 3.0F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 49)
                    //
                    .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(62, 49)
                    //
                    .addBox(-4.0F, 0.0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 62)
                    //
                    .addBox(4.0F, 0.0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(38, 42)
                    //
                    .addBox(-4.0F, 0.0F, 2.0F, 8.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(41, 13)
                    //
                    .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(41, 8)
                    //
                    .addBox(-4.0F, 12.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(20, 72)
                    //
                    .addBox(2.0F, 8.0F, 3.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 86)
                    //
                    .addBox(1.5F, 5.0F, 4.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(77, 20)
                    //
                    .addBox(1.5F, 10.5F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(93, 2)
                    //
                    .addBox(2.0F, 10.5F, 3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(86, 27)
                    //
                    .addBox(2.0F, 9.5F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(85, 91)
                    //
                    .addBox(2.0F, 5.0F, 4.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 17)
                    //
                    .addBox(2.0F, 4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(74, 46)
                    //
                    .addBox(0.5F, 3.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 18)
                    //
                    .addBox(-0.5F, 1.0F, 4.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 90)
                    //
                    .addBox(-0.5F, 4.0F, 4.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19)
                    //
                    .addBox(-1.0F, 4.0F, 4.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(57, 80)
                    //
                    .addBox(-3.0F, 4.5F, 3.0F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(71, 63)
                    //
                    .addBox(-2.5F, 3.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 79)
                    //
                    .addBox(-3.0F, 4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 55)
                    //
                    .addBox(-3.0F, 5.0F, 4.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                    //
                    .addBox(-3.5F, 5.0F, 4.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(55, 92)
                    //
                    .addBox(-3.0F, 10.5F, 3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 8)
                    //
                    .addBox(-3.5F, 10.5F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 75)
                    //
                    .addBox(-3.0F, 9.5F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 62)
                    //
                    .addBox(-3.0F, 8.0F, 3.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(73, 40)
                    //
                    .addBox(-0.5F, 8.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 24.0F, 0.0F));
        }

        part.addOrReplaceChild(RIGHT_ARM, CubeListBuilder.create().texOffs(17, 55)
                //
                .addBox(-4.0F, -2.0F, -2.0F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(37, 55)
                //
                .addBox(1.0F, -2.0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(9, 72)
                //
                .addBox(-3.0F, -2.0F, -3.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(71, 49)
                //
                .addBox(-3.0F, -2.0F, 2.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(78, 59)
                //
                .addBox(-3.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        part.addOrReplaceChild(LEFT_ARM, CubeListBuilder.create().texOffs(28, 55)
                //
                .addBox(-1.0F, -2.0F, -2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(51, 51)
                //
                .addBox(3.0F, -2.0F, -2.0F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(69, 66)
                //
                .addBox(-1.0F, -2.0F, -3.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 68)
                //
                .addBox(-1.0F, -2.0F, 2.0F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(74, 40)
                //
                .addBox(-1.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        switch (modelType) {
            case 1 -> {
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(85, 75)
                        //
                        .addBox(-2.1F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 46)
                        //
                        .addBox(-2.1F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(58, 66)
                        //
                        .addBox(-3.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(77, 6)
                        //
                        .addBox(2.0F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(57, 87)
                        //
                        .addBox(-1.6F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 70)
                        //
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(73, 90)
                        //
                        .addBox(-4.1F, 1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 89)
                        //
                        .addBox(-4.1F, 5.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(30, 92)
                        //
                        .addBox(-4.1F, 1.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(49, 35)
                        //
                        .addBox(-4.1F, 3.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 72)
                        //
                        .addBox(-3.1F, 1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 13)
                        //
                        .addBox(-2.1F, 1.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(46, 82)
                        //
                        .addBox(-1.9F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(35, 82)
                        //
                        .addBox(-1.9F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(76, 76)
                        //
                        .addBox(-2F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(66, 10)
                        //
                        .addBox(2.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(85, 86)
                        //
                        .addBox(-1.4F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 65)
                        //
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(25, 89)
                        //
                        .addBox(3.1F, 1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(18, 89)
                        //
                        .addBox(3.1F, 5.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(91, 59)
                        //
                        .addBox(3.1F, 1.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(60, 25)
                        //
                        .addBox(3.1F, 3.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(63, 3)
                        //
                        .addBox(2.1F, 1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 9)
                        //
                        .addBox(-1.9F, 1.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));
            }
            case 2 -> {
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(33, 72)
                        //
                        .addBox(2.1F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(62, 0)
                        //
                        .addBox(-3.0F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(25, 0)
                        //
                        .addBox(-4.0F, 9.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 72)
                        //
                        .addBox(-2.0F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 62)
                        //
                        .addBox(-2.0F, 9.5F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 20)
                        //
                        .addBox(-2.0F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(91, 55)
                        //
                        .addBox(-3.0F, 10.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(58, 49)
                        //
                        .addBox(-4.0F, 10.0F, 0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(49, 35)
                        //
                        .addBox(-2.0F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(60, 25)
                        //
                        .addBox(2.2F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(32, 9)
                        //
                        .addBox(3.2F, 9.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 72)
                        //
                        .addBox(-1.8F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(20, 82)
                        //
                        .addBox(-1.8F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(19, 36)
                        //
                        .addBox(-1.8F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 0)
                        //
                        .addBox(-1.8F, 9.5F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 55)
                        //
                        .addBox(2.2F, 10.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(24, 55)
                        //
                        .addBox(3.2F, 10.0F, 0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(49, 18)
                        //
                        .addBox(-1.8F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));
            }
            case 3 -> {
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create().texOffs(85, 75)
                        //
                        .addBox(-2.1F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 46)
                        //
                        .addBox(-2.1F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(58, 66)
                        //
                        .addBox(-3.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(77, 6)
                        //
                        .addBox(2.0F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(57, 87)
                        //
                        .addBox(-1.6F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 70)
                        //
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(73, 90)
                        //
                        .addBox(-4.1F, 1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 89)
                        //
                        .addBox(-4.1F, 5.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(30, 92)
                        //
                        .addBox(-4.1F, 1.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(49, 35)
                        //
                        .addBox(-4.1F, 3.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 72)
                        //
                        .addBox(-3.1F, 1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 13)
                        //
                        .addBox(-2.1F, 1.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 72)
                        //
                        .addBox(2.0F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(62, 0)
                        //
                        .addBox(-3.0F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(25, 0)
                        //
                        .addBox(-4.0F, 9.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 72)
                        //
                        .addBox(-2.0F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 62)
                        //
                        .addBox(-2.0F, 9.5F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 20)
                        //
                        .addBox(-2.0F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(91, 55)
                        //
                        .addBox(-3.0F, 10.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(58, 49)
                        //
                        .addBox(-4.0F, 10.0F, 0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(49, 35)
                        //
                        .addBox(-2.0F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create().texOffs(46, 82)
                        //
                        .addBox(-1.9F, 0.0F, -3.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(35, 82)
                        //
                        .addBox(-1.9F, 0.0F, 2.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(76, 76)
                        //
                        .addBox(-2F, 0.0F, -2.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(66, 10)
                        //
                        .addBox(2.1F, 0.0F, -2.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(85, 86)
                        //
                        .addBox(-1.4F, 3.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 65)
                        //
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(25, 89)
                        //
                        .addBox(3.1F, 1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(18, 89)
                        //
                        .addBox(3.1F, 5.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(91, 59)
                        //
                        .addBox(3.1F, 1.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(60, 25)
                        //
                        .addBox(3.1F, 3.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(63, 3)
                        //
                        .addBox(2.1F, 1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 9)
                        //
                        .addBox(-1.9F, 1.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 25)
                        //
                        .addBox(2.2F, 9.0F, -4.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(32, 9)
                        //
                        .addBox(3.2F, 9.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 72)
                        //
                        .addBox(-1.8F, 9.0F, -4.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(20, 82)
                        //
                        .addBox(-1.8F, 9.0F, -4.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(19, 36)
                        //
                        .addBox(-1.8F, 9.0F, 2.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 0)
                        //
                        .addBox(-1.8F, 9.5F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 55)
                        //
                        .addBox(2.2F, 10.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(24, 55)
                        //
                        .addBox(3.2F, 10.0F, 0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(49, 18)
                        //
                        .addBox(-1.8F, 12.1F, -4.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));
            }
            default -> {
                part.addOrReplaceChild(RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
                part.addOrReplaceChild(LEFT_LEG, CubeListBuilder.create(), PartPose.offset(0, 0, 0));
            }
        }

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int light) {
        if (parentHead.visible) {
            parentHead.render(poseStack, getCustomConsumer(RenderType.entityTranslucent(ItemCombatArmor.ARMOR_TEXTURE_LOCATION)), packedLight, packedOverlay);
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
