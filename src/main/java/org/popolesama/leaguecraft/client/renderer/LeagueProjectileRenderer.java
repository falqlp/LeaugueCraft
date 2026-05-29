package org.popolesama.leaguecraft.client.renderer;

import org.popolesama.leaguecraft.LeagueCraft;
import org.popolesama.leaguecraft.entity.LeagueProjectile;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LeagueProjectileRenderer extends EntityRenderer<LeagueProjectile> {
  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "textures/entity/projectile/league_projectile.png");

  public LeagueProjectileRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public ResourceLocation getTextureLocation(LeagueProjectile entity) {
    return TEXTURE;
  }
}
