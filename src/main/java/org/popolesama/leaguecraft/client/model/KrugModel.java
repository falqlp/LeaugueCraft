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
public class KrugModel extends LeagueJungleModel {
  public KrugModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    root.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 17).addBox(-6.0F, -9.0F, -5.0F, 12.0F, 13.0F, 11.0F)
        .texOffs(0, 42).addBox(-7.0F, -11.0F, -3.0F, 14.0F, 4.0F, 8.0F, new CubeDeformation(0.2F)),
        PartPose.offset(0.0F, 15.0F, 1.0F));
    root.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 8.0F, 7.0F)
        .texOffs(34, 0).addBox(-6.0F, -7.0F, -5.0F, 12.0F, 3.0F, 5.0F),
        PartPose.offset(0.0F, 10.5F, -6.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(44, 17).addBox(-3.0F, 0.0F, -3.0F, 5.0F, 9.0F, 6.0F), PartPose.offset(-5.0F, 15.0F, -3.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(44, 32).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 9.0F, 6.0F), PartPose.offset(5.0F, 15.0F, -3.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(24, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offset(-4.5F, 16.0F, 5.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(42, 47).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offset(4.5F, 16.0F, 5.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 13.0F, 7.0F));
    return LayerDefinition.create(mesh, 64, 64);
  }
}
