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
public class DragonModel extends LeagueJungleModel {
  public DragonModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    root.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 18).addBox(-5.0F, -6.0F, -8.0F, 10.0F, 9.0F, 17.0F)
        .texOffs(0, 45).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 3.0F, 12.0F, new CubeDeformation(0.1F)),
        PartPose.offset(0.0F, 14.0F, 1.0F));
    root.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 7.0F, 8.0F)
        .texOffs(32, 0).addBox(-5.0F, -7.0F, -6.0F, 2.0F, 4.0F, 2.0F)
        .texOffs(40, 0).addBox(3.0F, -7.0F, -6.0F, 2.0F, 4.0F, 2.0F),
        PartPose.offset(0.0F, 10.0F, -8.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(44, 18).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 9.0F, 4.0F), PartPose.offset(-4.0F, 15.0F, -4.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(44, 31).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 9.0F, 4.0F), PartPose.offset(4.0F, 15.0F, -4.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(28, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 5.0F), PartPose.offset(-4.0F, 15.0F, 6.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(46, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 5.0F), PartPose.offset(4.0F, 15.0F, 6.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(36, 8).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 16.0F), PartPose.offset(0.0F, 12.0F, 9.0F));
    root.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 60).addBox(-16.0F, 0.0F, 0.0F, 16.0F, 1.0F, 9.0F), PartPose.offset(-4.0F, 8.0F, -2.0F));
    root.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 60).addBox(0.0F, 0.0F, 0.0F, 16.0F, 1.0F, 9.0F), PartPose.offset(4.0F, 8.0F, -2.0F));
    return LayerDefinition.create(mesh, 80, 80);
  }
}
