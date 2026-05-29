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
public class RiftHeraldModel extends LeagueJungleModel {
  public RiftHeraldModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    root.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 20).addBox(-8.0F, -10.0F, -8.0F, 16.0F, 14.0F, 16.0F)
        .texOffs(0, 52).addBox(-7.0F, -13.0F, -6.0F, 14.0F, 5.0F, 12.0F, new CubeDeformation(0.25F)),
        PartPose.offset(0.0F, 14.0F, 2.0F));
    root.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-6.0F, -6.0F, -9.0F, 12.0F, 9.0F, 9.0F)
        .texOffs(42, 0).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 6.0F, 4.0F)
        .texOffs(58, 0).addBox(-7.0F, -8.0F, -6.0F, 3.0F, 4.0F, 3.0F)
        .texOffs(70, 0).addBox(4.0F, -8.0F, -6.0F, 3.0F, 4.0F, 3.0F),
        PartPose.offset(0.0F, 10.0F, -7.0F));
    root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(64, 20).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 10.0F, 6.0F), PartPose.offset(-6.0F, 14.0F, -4.0F));
    root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(64, 38).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 10.0F, 6.0F), PartPose.offset(6.0F, 14.0F, -4.0F));
    root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(40, 52).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 10.0F, 7.0F), PartPose.offset(-5.5F, 14.0F, 7.0F));
    root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(66, 54).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 10.0F, 7.0F), PartPose.offset(5.5F, 14.0F, 7.0F));
    root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(42, 8).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 13.0F), PartPose.offset(0.0F, 11.0F, 10.0F));

    return LayerDefinition.create(mesh, 96, 80);
  }
}
