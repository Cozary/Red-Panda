package com.cozary.red_panda.client.model;

import com.cozary.red_panda.entity.RedPandaEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static com.cozary.red_panda.client.model.RedPandaModel.State.ON_SHOULDER;

public class RedPandaModel<T extends RedPandaEntity> extends AgeableListModel<T> {

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart sleep;
    private final ModelPart frontLeg_R;
    private final ModelPart frontLeg_L;
    private final ModelPart backLeg_R;
    private final ModelPart backLeg_L;
    private final ModelPart tail;
    private final ModelPart shoulder;

    public RedPandaModel(ModelPart root) {
        super(true, 8.0F, 3.35F);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.sleep = root.getChild("sleep");
        this.frontLeg_R = root.getChild("frontLeg_R");
        this.frontLeg_L = root.getChild("frontLeg_L");
        this.backLeg_R = root.getChild("backLeg_R");
        this.backLeg_L = root.getChild("backLeg_L");
        this.tail = root.getChild("tail");
        this.shoulder = root.getChild("shoulder");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -2.0F, -6.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(30, 13).addBox(-2.0F, 1.0F, -8.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(2.0F, -4.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -4.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, -5.0F));

        PartDefinition sleep = partdefinition.addOrReplaceChild("sleep", CubeListBuilder.create().texOffs(0, 40).addBox(-4.0F, -12.0F, -11.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(30, 13).addBox(-2.0F, -9.0F, -13.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(2.0F, -14.0F, -8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -14.0F, -8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition frontLeg_R = partdefinition.addOrReplaceChild("frontLeg_R", CubeListBuilder.create().texOffs(0, 27).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 19.0F, -3.0F));

        PartDefinition frontLeg_L = partdefinition.addOrReplaceChild("frontLeg_L", CubeListBuilder.create().texOffs(32, 5).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 19.0F, -3.0F));

        PartDefinition backLeg_R = partdefinition.addOrReplaceChild("backLeg_R", CubeListBuilder.create().texOffs(22, 0).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 19.0F, 4.0F));

        PartDefinition backLeg_L = partdefinition.addOrReplaceChild("backLeg_L", CubeListBuilder.create().texOffs(9, 32).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 19.0F, 4.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 19).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 5.0F));

        PartDefinition shoulder = partdefinition.addOrReplaceChild("shoulder", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition tail2 = shoulder.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 5.0F));

        PartDefinition cube_r1 = tail2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 19).addBox(-2.5F, -3.3733F, 0.5245F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -2.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition backLeg_L2 = shoulder.addOrReplaceChild("backLeg_L2", CubeListBuilder.create(), PartPose.offset(2.0F, -5.0F, 4.0F));

        PartDefinition cube_r2 = backLeg_L2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(9, 32).addBox(-1.5F, -0.1195F, -1.0086F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -3.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition backLeg_R2 = shoulder.addOrReplaceChild("backLeg_R2", CubeListBuilder.create(), PartPose.offset(-2.0F, -5.0F, 4.0F));

        PartDefinition cube_r3 = backLeg_R2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 0).addBox(-1.5F, -0.1391F, -1.122F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -3.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition frontLeg_L2 = shoulder.addOrReplaceChild("frontLeg_L2", CubeListBuilder.create(), PartPose.offset(2.0F, -10.0F, -3.0F));

        PartDefinition cube_r4 = frontLeg_L2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 5).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5272F, 0.0F, 0.0F));

        PartDefinition frontLeg_R2 = shoulder.addOrReplaceChild("frontLeg_R2", CubeListBuilder.create(), PartPose.offset(-2.0F, -5.0F, -3.0F));

        PartDefinition cube_r5 = frontLeg_R2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 27).addBox(-1.5F, -4.0F, 4.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head2 = shoulder.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -2.0F, -6.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(30, 13).addBox(-2.0F, 1.0F, -8.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(2.0F, -4.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -4.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -1.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition body2 = shoulder.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition cube_r6 = body2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.4399F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private static State getState(RedPandaEntity p_103210_) {
        return null;
    }

    @Override
    public void prepareMobModel(T entity, float p_102665_, float p_102666_, float p_102667_) {
        this.shoulder.visible = false;
        this.backLeg_R.visible = true;
        this.backLeg_L.visible = true;
        this.frontLeg_R.visible = true;
        this.frontLeg_L.visible = true;
        this.tail.yRot = 0;
        this.tail.y = 15;
        this.head.y = 15;
        this.head.z = -4;
        this.body.y = 17;
        this.body.z = 0;
        this.sleep.visible = false;
        this.head.visible = true;
        this.body.xRot = 0;
        this.frontLeg_R.xRot = 0;
        this.frontLeg_L.xRot = 0;
        this.frontLeg_R.yRot = 0;
        this.frontLeg_L.yRot = 0;
        this.frontLeg_R.zRot = 0;
        this.frontLeg_L.zRot = 0;
        this.backLeg_R.zRot = 0;
        this.backLeg_L.zRot = 0;
        this.frontLeg_R.y = 19;
        this.frontLeg_L.y = 19;
        this.frontLeg_R.z = -3;
        this.frontLeg_L.z = -3;
        this.frontLeg_R.x = -2;
        this.frontLeg_L.x = 2;
        this.backLeg_R.z = 5;
        this.backLeg_L.z = 5;
        boolean sleep = entity.isSleeping();
        if (sleep) {
            this.head.visible = false;
            this.sleep.visible = true;
            this.sleep.y = 30;
            this.backLeg_R.visible = false;
            this.backLeg_L.visible = false;
            this.frontLeg_R.visible = false;
            this.frontLeg_L.visible = false;
            this.tail.y = 20;
            this.tail.yRot = 90;
            this.body.y = 22;
        }

        boolean scared = entity.isScared();
        if (scared) {
            this.body.xRot = 30;
            this.body.y = 15;
            this.body.z = 2;
            this.frontLeg_R.y = 12;
            this.frontLeg_L.y = 12;
            this.frontLeg_R.z = 2;
            this.frontLeg_L.z = 2;
            this.frontLeg_R.x = -3;
            this.frontLeg_L.x = 3;
            this.frontLeg_R.yRot = 45;
            this.frontLeg_L.yRot = -45;
            this.head.y = 7;
            this.head.z = 3;
            this.backLeg_R.z = 2;
            this.backLeg_L.z = 2;

        }
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setupAnim(getState(entity), entity.tickCount, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.shoulder.visible = false;

        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.backLeg_R.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.backLeg_L.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.frontLeg_R.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.frontLeg_L.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.tail.xRot = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;

        boolean sleep = entity.isSleeping();
        if (sleep) {
            this.head.xRot = 0;
            this.head.yRot = 0;
        }

        boolean scared = entity.isScared();
        if (scared) {
            this.frontLeg_R.xRot = 30;
            this.frontLeg_L.xRot = 30;
            this.frontLeg_R.zRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
            this.frontLeg_L.zRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
        }

    }

    private void setupAnim(State state, int tickCount, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (state == ON_SHOULDER) {
            this.shoulder.visible = true;
            this.shoulder.y = 34;
            this.shoulder.z = 5;

            this.shoulder.getChild("tail2").xRot = Mth.cos(limbSwing * 0.6662F) * 0.3F * limbSwingAmount;
            this.shoulder.getChild("backLeg_R2").xRot = Mth.cos(limbSwing * 0.6662F) * 0.2F * limbSwingAmount;
            this.shoulder.getChild("backLeg_L2").xRot = Mth.cos(limbSwing * 0.6662F) * 0.2F * limbSwingAmount;
        }
    }

    public void renderOnShoulder(PoseStack poseStack, VertexConsumer p_103225_, int p_103226_, int p_103227_, float p_103228_, float p_103229_, float p_103230_, float p_103231_, int p_103232_) {
        this.setupAnim(State.ON_SHOULDER, p_103232_, p_103228_, p_103229_, 0.0F, p_103230_, p_103231_);
        shoulder.render(poseStack, p_103225_, p_103226_, p_103227_);

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
        head.render(poseStack, buffer, packedLight, packedOverlay);
        sleep.render(poseStack, buffer, packedLight, packedOverlay);
        frontLeg_R.render(poseStack, buffer, packedLight, packedOverlay);
        frontLeg_L.render(poseStack, buffer, packedLight, packedOverlay);
        backLeg_R.render(poseStack, buffer, packedLight, packedOverlay);
        backLeg_L.render(poseStack, buffer, packedLight, packedOverlay);
        tail.render(poseStack, buffer, packedLight, packedOverlay);
        shoulder.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    protected @NotNull Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.sleep);
    }

    @Override
    protected @NotNull Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.backLeg_L, this.backLeg_R, this.frontLeg_L, this.frontLeg_R, this.tail);
    }

    public enum State {
        ON_SHOULDER
    }

}