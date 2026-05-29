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
public class LeagueMinionModel extends HierarchicalModel<LeagueMonster> {
  private final ModelPart root;
  private final ModelPart head;
  private final ModelPart rightArm;
  private final ModelPart leftArm;
  private final ModelPart rightLeg;
  private final ModelPart leftLeg;

  public LeagueMinionModel(ModelPart root) {
    this.root = root;
    this.head = root.getChild("head");
    this.rightArm = root.getChild("right_arm");
    this.leftArm = root.getChild("left_arm");
    this.rightLeg = root.getChild("right_leg");
    this.leftLeg = root.getChild("left_leg");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    root.addOrReplaceChild(
        "head",
        CubeListBuilder.create()
            .texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F)
            .texOffs(32, 0).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.1F)),
        PartPose.offset(0.0F, 10.0F, 0.0F));
    root.addOrReplaceChild(
        "body",
        CubeListBuilder.create()
            .texOffs(0, 16).addBox(-4.0F, -1.0F, -2.5F, 8.0F, 8.0F, 5.0F)
            .texOffs(26, 16).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 4.0F, 6.0F, new CubeDeformation(0.15F)),
        PartPose.offset(0.0F, 10.0F, 0.0F));
    root.addOrReplaceChild(
        "right_arm",
        CubeListBuilder.create().texOffs(0, 30).addBox(-2.5F, -1.0F, -2.0F, 3.0F, 9.0F, 4.0F),
        PartPose.offset(-4.5F, 11.0F, 0.0F));
    root.addOrReplaceChild(
        "left_arm",
        CubeListBuilder.create().texOffs(14, 30).addBox(-0.5F, -1.0F, -2.0F, 3.0F, 9.0F, 4.0F),
        PartPose.offset(4.5F, 11.0F, 0.0F));
    root.addOrReplaceChild(
        "right_leg",
        CubeListBuilder.create().texOffs(28, 30).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F),
        PartPose.offset(-1.5F, 17.0F, 0.0F));
    root.addOrReplaceChild(
        "left_leg",
        CubeListBuilder.create().texOffs(42, 30).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F),
        PartPose.offset(1.5F, 17.0F, 0.0F));

    return LayerDefinition.create(mesh, 64, 64);
  }

  @Override
  public ModelPart root() {
    return root;
  }

  @Override
  public void setupAnim(LeagueMonster monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    root.getAllParts().forEach(ModelPart::resetPose);

    head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    head.xRot = headPitch * Mth.DEG_TO_RAD;

    float walk = Math.min(limbSwingAmount, 1.0F);
    rightLeg.xRot = Mth.cos(limbSwing * 0.8F) * 1.1F * walk;
    leftLeg.xRot = Mth.cos(limbSwing * 0.8F + Mth.PI) * 1.1F * walk;
    rightArm.xRot = Mth.cos(limbSwing * 0.8F + Mth.PI) * 0.75F * walk - 0.18F;
    leftArm.xRot = Mth.cos(limbSwing * 0.8F) * 0.75F * walk - 0.18F;
    root.y = Mth.sin(ageInTicks * 0.18F) * 0.35F;
  }
}
