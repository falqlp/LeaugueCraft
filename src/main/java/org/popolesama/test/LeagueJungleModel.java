package org.popolesama.test;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class LeagueJungleModel extends HierarchicalModel<LeagueMonster> {
  protected final ModelPart root;
  protected final ModelPart head;
  protected final ModelPart rightFrontLeg;
  protected final ModelPart leftFrontLeg;
  protected final ModelPart rightBackLeg;
  protected final ModelPart leftBackLeg;
  protected final ModelPart tail;

  protected LeagueJungleModel(ModelPart root) {
    this.root = root;
    this.head = root.getChild("head");
    this.rightFrontLeg = root.getChild("right_front_leg");
    this.leftFrontLeg = root.getChild("left_front_leg");
    this.rightBackLeg = root.getChild("right_back_leg");
    this.leftBackLeg = root.getChild("left_back_leg");
    this.tail = root.getChild("tail");
  }

  @Override
  public ModelPart root() {
    return root;
  }

  @Override
  public void setupAnim(LeagueMonster monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    root.getAllParts().forEach(ModelPart::resetPose);

    head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    head.xRot = headPitch * Mth.DEG_TO_RAD * 0.65F;

    float walk = Math.min(limbSwingAmount, 1.0F);
    rightFrontLeg.xRot = Mth.cos(limbSwing * 0.75F) * 0.9F * walk;
    leftBackLeg.xRot = rightFrontLeg.xRot;
    leftFrontLeg.xRot = Mth.cos(limbSwing * 0.75F + Mth.PI) * 0.9F * walk;
    rightBackLeg.xRot = leftFrontLeg.xRot;
    tail.yRot = Mth.sin(ageInTicks * 0.16F) * 0.18F;
    root.y = Mth.sin(ageInTicks * 0.12F) * 0.25F;
  }
}
