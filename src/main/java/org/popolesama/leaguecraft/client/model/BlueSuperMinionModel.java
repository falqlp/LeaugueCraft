package org.popolesama.leaguecraft.client.model;

import org.popolesama.leaguecraft.entity.LeagueMonster;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlueSuperMinionModel extends HierarchicalModel<LeagueMonster> {
  private final ModelPart root;
  private final ModelPart helm;
  private final ModelPart body;
  private final ModelPart rightArm;
  private final ModelPart leftArm;
  private final ModelPart rightLeg;
  private final ModelPart leftLeg;
  private final ModelPart crest;

  public BlueSuperMinionModel(ModelPart root) {
    this.root = root;
    this.helm = root.getChild("helm");
    this.body = root.getChild("body");
    this.rightArm = root.getChild("right_arm");
    this.leftArm = root.getChild("left_arm");
    this.rightLeg = root.getChild("right_leg");
    this.leftLeg = root.getChild("left_leg");
    this.crest = root.getChild("crest");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    root.addOrReplaceChild("helm", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 10.0F)
        .texOffs(40, 0).addBox(-6.0F, -9.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.15F)),
        PartPose.offset(0.0F, 7.0F, 0.0F));
    root.addOrReplaceChild("crest", CubeListBuilder.create()
        .texOffs(0, 56).addBox(-1.5F, -13.0F, -1.0F, 3.0F, 6.0F, 2.0F),
        PartPose.offset(0.0F, 7.0F, -1.5F));
    root.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 20).addBox(-6.0F, -1.0F, -3.5F, 12.0F, 11.0F, 7.0F)
        .texOffs(38, 20).addBox(-7.0F, 0.0F, -4.5F, 14.0F, 5.0F, 9.0F, new CubeDeformation(0.2F)),
        PartPose.offset(0.0F, 8.0F, 0.0F));
    root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 39).addBox(-3.5F, -1.0F, -2.5F, 4.0F, 11.0F, 5.0F), PartPose.offset(-6.5F, 9.0F, 0.0F));
    root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(18, 39).addBox(-0.5F, -1.0F, -2.5F, 4.0F, 11.0F, 5.0F), PartPose.offset(6.5F, 9.0F, 0.0F));
    root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(36, 39).addBox(-3.0F, 0.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offset(-2.5F, 16.0F, 0.0F));
    root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(54, 39).addBox(-1.0F, 0.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offset(2.5F, 16.0F, 0.0F));

    return LayerDefinition.create(mesh, 96, 64);
  }

  @Override
  public ModelPart root() {
    return root;
  }

  @Override
  public void setupAnim(LeagueMonster monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    root.getAllParts().forEach(ModelPart::resetPose);
    helm.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    helm.xRot = headPitch * Mth.DEG_TO_RAD * 0.75F;
    crest.yRot = helm.yRot;
    crest.xRot = helm.xRot;
    float walk = Math.min(limbSwingAmount, 1.0F);
    rightLeg.xRot = Mth.cos(limbSwing * 0.65F) * 0.8F * walk;
    leftLeg.xRot = Mth.cos(limbSwing * 0.65F + Mth.PI) * 0.8F * walk;
    rightArm.xRot = Mth.cos(limbSwing * 0.65F + Mth.PI) * 0.45F * walk - 0.25F;
    leftArm.xRot = Mth.cos(limbSwing * 0.65F) * 0.45F * walk - 0.25F;
    body.y = Mth.sin(ageInTicks * 0.12F) * 0.12F;
  }
}
