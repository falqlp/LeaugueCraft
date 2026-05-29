package org.popolesama.leaguecraft.client.renderer;

import org.popolesama.leaguecraft.LeagueCraft;
import org.popolesama.leaguecraft.client.model.CannonMinionModel;
import org.popolesama.leaguecraft.entity.LeagueMonster;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CannonMinionRenderer extends AbstractMinionRenderer<CannonMinionModel> {
  public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "cannon_minion"), "main");
  private static final ResourceLocation NEUTRAL_TEXTURE = texture("neutral_cannon_minion");
  private static final ResourceLocation BLUE_TEXTURE = texture("blue_cannon_minion");
  private static final ResourceLocation RED_TEXTURE = texture("red_cannon_minion");

  public CannonMinionRenderer(EntityRendererProvider.Context context) {
    super(context, new CannonMinionModel(context.bakeLayer(LAYER)), 0.65F);
  }

  @Override
  protected void scale(LeagueMonster monster, PoseStack poseStack, float partialTick) {
    poseStack.scale(0.9F, 0.85F, 0.9F);
  }

  @Override
  protected double healthBarYOffset() {
    return 0.85D;
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
