package org.popolesama.leaguecraft.client.model;

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
public class VoidGrubModel extends LeagueJungleModel {
  public VoidGrubModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    root.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 16).addBox(-5.0F, -5.0F, -6.0F, 10.0F, 8.0F, 13.0F, new CubeDeformation(0.25F))
        .texOffs(0, 38).addBox(-4.0F, -7.0F, -3.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.1F)),
        PartPose.offset(0.0F, 17.0F, 1.0F));
    root.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-4.0F, -4.0F, -7.0F, 8.0F, 6.0F, 7.0F)
        .texOffs(30, 0).addBox(-5.0F, -6.0F, -5.0F, 3.0F, 3.0F, 3.0F)
        .texOffs(42, 0).addBox(2.0F, -6.0F, -5.0F, 3.0F, 3.0F, 3.0F),
        PartPose.offset(0.0F, 15.0F, -6.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(46, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F), PartPose.offset(-4.0F, 19.0F, -3.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(46, 26).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F), PartPose.offset(4.0F, 19.0F, -3.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(0, 50).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F), PartPose.offset(-3.5F, 19.0F, 5.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(12, 50).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F), PartPose.offset(3.5F, 19.0F, 5.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 50).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 7.0F), PartPose.offset(0.0F, 16.0F, 8.0F));

    return LayerDefinition.create(mesh, 64, 64);
  }
}
