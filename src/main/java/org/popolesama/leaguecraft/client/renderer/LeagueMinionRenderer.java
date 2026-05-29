package org.popolesama.leaguecraft.client.renderer;

import org.popolesama.leaguecraft.client.model.LeagueMinionModel;
import org.popolesama.leaguecraft.LeagueCraft;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LeagueMinionRenderer extends AbstractMinionRenderer<LeagueMinionModel> {
  public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "minion"), "main");

  public LeagueMinionRenderer(EntityRendererProvider.Context context) {
    super(context, new LeagueMinionModel(context.bakeLayer(LAYER)), 0.45F);
  }
}
