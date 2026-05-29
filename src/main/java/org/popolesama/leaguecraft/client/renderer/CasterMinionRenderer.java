package org.popolesama.leaguecraft.client.renderer;

import org.popolesama.leaguecraft.LeagueCraft;
import org.popolesama.leaguecraft.client.model.CasterMinionModel;
import org.popolesama.leaguecraft.entity.LeagueMonster;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CasterMinionRenderer extends AbstractMinionRenderer<CasterMinionModel> {
  public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "caster_minion"), "main");
  private static final ResourceLocation NEUTRAL_TEXTURE = texture("neutral_caster_minion");
  private static final ResourceLocation BLUE_TEXTURE = texture("blue_caster_minion");
  private static final ResourceLocation RED_TEXTURE = texture("red_caster_minion");

  public CasterMinionRenderer(EntityRendererProvider.Context context) {
    super(context, new CasterMinionModel(context.bakeLayer(LAYER)), 0.4F);
  }

  @Override
  protected void scale(LeagueMonster monster, PoseStack poseStack, float partialTick) {
    poseStack.scale(0.9F, 0.9F, 0.9F);
  }

  @Override
  public ResourceLocation getTextureLocation(LeagueMonster monster) {
    return switch (monster.team()) {
      case BLUE -> BLUE_TEXTURE;
      case RED -> RED_TEXTURE;
      case NEUTRAL -> NEUTRAL_TEXTURE;
    };
  }
}
