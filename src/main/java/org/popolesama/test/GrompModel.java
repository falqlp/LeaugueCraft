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
public class GrompModel extends LeagueJungleModel {
  public GrompModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    root.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 18).addBox(-7.0F, -8.0F, -6.0F, 14.0F, 11.0F, 12.0F, new CubeDeformation(0.4F)),
        PartPose.offset(0.0F, 16.0F, 1.0F));
    root.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-6.0F, -5.0F, -8.0F, 12.0F, 8.0F, 8.0F)
        .texOffs(40, 0).addBox(-7.0F, -7.0F, -5.0F, 3.0F, 4.0F, 3.0F)
        .texOffs(52, 0).addBox(4.0F, -7.0F, -5.0F, 3.0F, 4.0F, 3.0F),
        PartPose.offset(0.0F, 12.0F, -6.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(44, 18).addBox(-2.5F, 0.0F, -2.5F, 4.0F, 7.0F, 5.0F), PartPose.offset(-5.0F, 17.0F, -3.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(44, 30).addBox(-1.5F, 0.0F, -2.5F, 4.0F, 7.0F, 5.0F), PartPose.offset(5.0F, 17.0F, -3.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(28, 43).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 7.0F, 5.0F), PartPose.offset(-4.5F, 17.0F, 5.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(46, 43).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 7.0F, 5.0F), PartPose.offset(4.5F, 17.0F, 5.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(39, 8).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 14.0F, 7.0F));
    return LayerDefinition.create(mesh, 64, 64);
  }
}
