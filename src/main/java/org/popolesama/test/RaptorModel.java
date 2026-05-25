package org.popolesama.test;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RaptorModel extends LeagueJungleModel {
  public RaptorModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 10.0F, 10.0F), PartPose.offset(0.0F, 13.0F, 1.0F));
    root.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-3.5F, -4.0F, -7.0F, 7.0F, 6.0F, 7.0F)
        .texOffs(28, 0).addBox(-2.0F, -1.0F, -11.0F, 4.0F, 3.0F, 5.0F),
        PartPose.offset(0.0F, 9.5F, -5.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(37, 18).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offset(-4.0F, 13.0F, -2.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(45, 18).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offset(4.0F, 13.0F, -2.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 11.0F, 4.0F), PartPose.offset(-2.5F, 13.0F, 4.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(14, 40).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 11.0F, 4.0F), PartPose.offset(2.5F, 13.0F, 4.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(30, 38).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 13.0F), PartPose.offset(0.0F, 12.0F, 6.0F));
    return LayerDefinition.create(mesh, 64, 64);
  }
}
