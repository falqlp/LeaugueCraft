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
public class CasterMinionModel extends HierarchicalModel<LeagueMonster> {
  private final ModelPart root;
  private final ModelPart hood;
  private final ModelPart robe;
  private final ModelPart staff;
  private final ModelPart rightSleeve;
  private final ModelPart leftSleeve;

  public CasterMinionModel(ModelPart root) {
    this.root = root;
    this.hood = root.getChild("hood");
    this.robe = root.getChild("robe");
    this.staff = root.getChild("staff");
    this.rightSleeve = root.getChild("right_sleeve");
    this.leftSleeve = root.getChild("left_sleeve");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    root.addOrReplaceChild(
        "hood",
        CubeListBuilder.create()
            .texOffs(0, 0).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F)
            .texOffs(28, 0).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.15F)),
        PartPose.offset(0.0F, 9.0F, 0.0F));
    root.addOrReplaceChild(
        "robe",
        CubeListBuilder.create()
            .texOffs(0, 16).addBox(-4.0F, -1.0F, -3.0F, 8.0F, 11.0F, 6.0F)
            .texOffs(28, 16).addBox(-5.0F, 5.0F, -3.5F, 10.0F, 5.0F, 7.0F, new CubeDeformation(0.1F)),
        PartPose.offset(0.0F, 10.0F, 0.0F));
    root.addOrReplaceChild(
        "right_sleeve",
        CubeListBuilder.create().texOffs(0, 33).addBox(-2.5F, -1.0F, -2.0F, 3.0F, 8.0F, 4.0F),
        PartPose.offset(-4.25F, 11.0F, 0.0F));
    root.addOrReplaceChild(
        "left_sleeve",
        CubeListBuilder.create().texOffs(14, 33).addBox(-0.5F, -1.0F, -2.0F, 3.0F, 8.0F, 4.0F),
        PartPose.offset(4.25F, 11.0F, 0.0F));
    root.addOrReplaceChild(
        "staff",
        CubeListBuilder.create()
            .texOffs(28, 33).addBox(-0.5F, -10.0F, -0.5F, 1.0F, 18.0F, 1.0F)
            .texOffs(32, 33).addBox(-2.0F, -12.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)),
        PartPose.offset(5.5F, 15.0F, -1.5F));

    return LayerDefinition.create(mesh, 64, 64);
  }

  @Override
  public ModelPart root() {
    return root;
  }

  @Override
  public void setupAnim(LeagueMonster monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    root.getAllParts().forEach(ModelPart::resetPose);

    hood.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    hood.xRot = headPitch * Mth.DEG_TO_RAD;

    float walk = Math.min(limbSwingAmount, 1.0F);
    robe.y = Mth.sin(ageInTicks * 0.16F) * 0.25F;
    rightSleeve.xRot = Mth.cos(limbSwing * 0.8F + Mth.PI) * 0.35F * walk - 0.35F;
    leftSleeve.xRot = Mth.cos(limbSwing * 0.8F) * 0.35F * walk - 0.35F;
    staff.xRot = leftSleeve.xRot * 0.4F;
    staff.zRot = Mth.sin(ageInTicks * 0.12F) * 0.04F;
  }
}
