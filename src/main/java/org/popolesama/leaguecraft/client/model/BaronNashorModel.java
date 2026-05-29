package org.popolesama.leaguecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaronNashorModel extends LeagueJungleModel {
  public BaronNashorModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
    body.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -14.0F, -1.0F, 14.0F, 14.0F, 14.0F), PartPose.offset(-6.0F, -2.0F, -6.0F));
    body.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(56, 30).addBox(-1.0F, -16.0F, -1.0F, 12.0F, 16.0F, 12.0F), PartPose.offsetAndRotation(-5.0F, -16.0F, -4.0F, -0.1309F, 0.0F, 0.0F));
    body.addOrReplaceChild("under_body", CubeListBuilder.create().texOffs(0, 58).addBox(-1.0F, -2.0F, -1.0F, 14.0F, 2.0F, 14.0F), PartPose.offset(-6.0F, 0.0F, -4.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(56, 82).addBox(-1.0F, -4.0F, -1.0F, 4.0F, 4.0F, 6.0F), PartPose.offset(8.0F, 24.0F, -9.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(76, 82).addBox(-1.0F, -4.0F, -1.0F, 4.0F, 4.0F, 6.0F), PartPose.offset(-10.0F, 24.0F, -9.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -2.0F, -1.0F, 6.0F, 2.0F, 8.0F), PartPose.offset(-12.0F, 24.0F, 4.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(28, 74).addBox(-1.0F, -2.0F, -1.0F, 6.0F, 2.0F, 8.0F), PartPose.offset(8.0F, 24.0F, 4.0F));
    PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -13.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
    head.addOrReplaceChild("main_head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -1.0F, 10.0F, 10.0F, 20.0F), PartPose.offset(-4.0F, 5.0F, -10.0F));
    head.addOrReplaceChild("left_head", CubeListBuilder.create().texOffs(56, 58).addBox(-1.0F, -3.0F, -1.0F, 6.0F, 6.0F, 18.0F), PartPose.offsetAndRotation(-11.0F, 3.0F, -8.0F, 0.0F, 0.0873F, 0.0F));
    head.addOrReplaceChild("right_head", CubeListBuilder.create().texOffs(60, 0).addBox(-1.0F, -3.0F, -1.0F, 6.0F, 6.0F, 18.0F), PartPose.offsetAndRotation(7.0F, 3.0F, -8.0F, 0.0F, -0.0873F, 0.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.ZERO);
    return LayerDefinition.create(mesh, 128, 128);
  }
}
