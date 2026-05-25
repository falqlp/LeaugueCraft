package org.popolesama.test;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
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
    root.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 18).addBox(-8.0F, -9.0F, -8.0F, 16.0F, 13.0F, 16.0F)
        .texOffs(0, 48).addBox(-7.0F, -12.0F, -5.0F, 14.0F, 5.0F, 12.0F, new CubeDeformation(0.2F)),
        PartPose.offset(0.0F, 15.0F, 1.0F));
    root.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-7.0F, -6.0F, -9.0F, 14.0F, 9.0F, 9.0F)
        .texOffs(46, 0).addBox(-9.0F, -8.0F, -7.0F, 3.0F, 7.0F, 3.0F)
        .texOffs(58, 0).addBox(6.0F, -8.0F, -7.0F, 3.0F, 7.0F, 3.0F),
        PartPose.offset(0.0F, 9.0F, -8.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(48, 18).addBox(-3.0F, 0.0F, -3.0F, 5.0F, 9.0F, 6.0F), PartPose.offset(-6.0F, 15.0F, -4.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(48, 33).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 9.0F, 6.0F), PartPose.offset(6.0F, 15.0F, -4.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(40, 49).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 7.0F), PartPose.offset(-5.5F, 15.0F, 7.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(60, 49).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 7.0F), PartPose.offset(5.5F, 15.0F, 7.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(40, 8).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 18.0F), PartPose.offset(0.0F, 12.0F, 9.0F));
    root.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 66).addBox(-8.0F, -2.0F, -1.0F, 16.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 1.5F, -13.0F));
    return LayerDefinition.create(mesh, 96, 96);
  }
}
