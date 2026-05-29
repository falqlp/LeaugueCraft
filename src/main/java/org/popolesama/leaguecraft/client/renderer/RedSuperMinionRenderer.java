package org.popolesama.leaguecraft.client.renderer;

import org.popolesama.leaguecraft.LeagueCraft;
import org.popolesama.leaguecraft.client.model.RedSuperMinionModel;
import org.popolesama.leaguecraft.entity.LeagueMonster;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RedSuperMinionRenderer extends AbstractMinionRenderer<RedSuperMinionModel> {
  public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "red_super_minion"), "main");
  private static final ResourceLocation TEXTURE = texture("red_super_minion");

  public RedSuperMinionRenderer(EntityRendererProvider.Context context) {
    super(context, new RedSuperMinionModel(context.bakeLayer(LAYER)), 0.65F);
  }

  @Override
  protected void scale(LeagueMonster monster, PoseStack poseStack, float partialTick) {
    poseStack.scale(0.95F, 0.95F, 0.95F);
  }

  @Override
  protected double healthBarYOffset() {
    return 0.75D;
  }

  @Override
  public ResourceLocation getTextureLocation(LeagueMonster monster) {
    return TEXTURE;
  }
}
