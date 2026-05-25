package org.popolesama.test;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LeagueStructureRenderer implements BlockEntityRenderer<LeagueStructureBlockEntity> {
  private final BlockEntityRendererProvider.Context context;

  public LeagueStructureRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
  }

  @Override
  public void render(LeagueStructureBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    if (blockEntity.kind() != LeagueStructureKind.TURRET) {
      return;
    }

    poseStack.pushPose();
    poseStack.translate(0.5D, 1.35D, 0.5D);
    LeagueHealthBarRenderer.render(
        poseStack,
        buffer,
        context.getEntityRenderer().cameraOrientation(),
        context.getFont(),
        blockEntity.health(),
        blockEntity.maxHealth(),
        null,
        packedLight);
    poseStack.popPose();
  }
}
