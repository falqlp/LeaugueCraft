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
public class CannonMinionModel extends HierarchicalModel<LeagueMonster> {
  private final ModelPart root;
  private final ModelPart chassis;
  private final ModelPart turret;
  private final ModelPart barrel;
  private final ModelPart leftWheel;
  private final ModelPart rightWheel;

  public CannonMinionModel(ModelPart root) {
    this.root = root;
    this.chassis = root.getChild("chassis");
    this.turret = root.getChild("turret");
    this.barrel = root.getChild("barrel");
    this.leftWheel = root.getChild("left_wheel");
    this.rightWheel = root.getChild("right_wheel");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    root.addOrReplaceChild(
        "chassis",
        CubeListBuilder.create()
            .texOffs(0, 0).addBox(-6.0F, -3.0F, -5.0F, 12.0F, 6.0F, 10.0F, new CubeDeformation(0.1F))
            .texOffs(0, 16).addBox(-4.5F, -5.0F, -3.5F, 9.0F, 3.0F, 7.0F),
        PartPose.offset(0.0F, 16.0F, 0.0F));
    root.addOrReplaceChild(
        "turret",
        CubeListBuilder.create()
            .texOffs(34, 16).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.2F)),
        PartPose.offset(0.0F, 11.5F, -1.0F));
    root.addOrReplaceChild(
        "barrel",
        CubeListBuilder.create()
            .texOffs(0, 28).addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 11.0F)
            .texOffs(28, 28).addBox(-2.5F, -2.5F, -11.5F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.05F)),
        PartPose.offset(0.0F, 11.5F, -4.5F));
    root.addOrReplaceChild(
        "left_wheel",
        CubeListBuilder.create().texOffs(0, 42).addBox(-1.0F, -3.0F, -4.5F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.1F)),
        PartPose.offset(6.5F, 19.0F, 0.0F));
    root.addOrReplaceChild(
        "right_wheel",
        CubeListBuilder.create().texOffs(22, 42).addBox(-1.0F, -3.0F, -4.5F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.1F)),
        PartPose.offset(-6.5F, 19.0F, 0.0F));

    return LayerDefinition.create(mesh, 64, 64);
  }

  @Override
  public ModelPart root() {
    return root;
  }

  @Override
  public void setupAnim(LeagueMonster monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    root.getAllParts().forEach(ModelPart::resetPose);

    turret.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.5F;
    barrel.yRot = turret.yRot;
    barrel.xRot = headPitch * Mth.DEG_TO_RAD * 0.35F;
    chassis.y = Mth.sin(ageInTicks * 0.18F) * 0.12F;

    float roll = limbSwing * 0.9F;
    leftWheel.xRot = roll;
    rightWheel.xRot = roll;
  }
}
