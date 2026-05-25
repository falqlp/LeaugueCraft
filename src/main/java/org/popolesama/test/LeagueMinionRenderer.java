package org.popolesama.test;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LeagueMinionRenderer extends MobRenderer<LeagueMonster, LeagueMinionModel> {
  public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Test.MODID, "minion"), "main");

  private static final ResourceLocation NEUTRAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Test.MODID, "textures/entity/minion/neutral_minion.png");
  private static final ResourceLocation BLUE_TEXTURE = ResourceLocation.fromNamespaceAndPath(Test.MODID, "textures/entity/minion/blue_minion.png");
  private static final ResourceLocation RED_TEXTURE = ResourceLocation.fromNamespaceAndPath(Test.MODID, "textures/entity/minion/red_minion.png");

  public LeagueMinionRenderer(EntityRendererProvider.Context context) {
    super(context, new LeagueMinionModel(context.bakeLayer(LAYER)), 0.45F);
  }

  @Override
  public ResourceLocation getTextureLocation(LeagueMonster monster) {
    return switch (monster.team()) {
      case BLUE -> BLUE_TEXTURE;
      case RED -> RED_TEXTURE;
      case NEUTRAL -> NEUTRAL_TEXTURE;
    };
  }

  @Override
  public void render(LeagueMonster monster, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    super.render(monster, entityYaw, partialTick, poseStack, buffer, packedLight);

    poseStack.pushPose();
    poseStack.translate(0.0D, monster.getBbHeight() + 0.35D, 0.0D);
    LeagueHealthBarRenderer.render(poseStack, buffer, entityRenderDispatcher.cameraOrientation(), getFont(), monster.getHealth(), monster.getMaxHealth(), null, packedLight);
    poseStack.popPose();
  }
}
