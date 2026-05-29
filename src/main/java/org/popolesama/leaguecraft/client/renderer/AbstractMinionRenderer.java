package org.popolesama.leaguecraft.client.renderer;

import org.popolesama.leaguecraft.LeagueCraft;
import org.popolesama.leaguecraft.entity.LeagueMonster;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractMinionRenderer<M extends EntityModel<LeagueMonster>> extends MobRenderer<LeagueMonster, M> {
  private static final ResourceLocation NEUTRAL_TEXTURE = texture("neutral_minion");
  private static final ResourceLocation BLUE_TEXTURE = texture("blue_minion");
  private static final ResourceLocation RED_TEXTURE = texture("red_minion");

  protected AbstractMinionRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
    super(context, model, shadowRadius);
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
    poseStack.translate(0.0D, monster.getBbHeight() + healthBarYOffset(), 0.0D);
    LeagueHealthBarRenderer.render(poseStack, buffer, entityRenderDispatcher.cameraOrientation(), getFont(), monster.getHealth(), monster.getMaxHealth(), null, packedLight);
    poseStack.popPose();
  }

  protected double healthBarYOffset() {
    return 0.35D;
  }

  protected static ResourceLocation texture(String name) {
    return ResourceLocation.fromNamespaceAndPath(LeagueCraft.MODID, "textures/entity/minion/" + name + ".png");
  }
}
