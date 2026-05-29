package org.popolesama.leaguecraft.client.renderer;

import org.popolesama.leaguecraft.block.LeagueStructureKind;

import org.popolesama.leaguecraft.block.LeagueStructureBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import lombok.RequiredArgsConstructor;

@OnlyIn(Dist.CLIENT)
@RequiredArgsConstructor
public class LeagueStructureRenderer implements BlockEntityRenderer<LeagueStructureBlockEntity> {
  private final BlockEntityRendererProvider.Context context;

  @Override
  public void render(LeagueStructureBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    poseStack.pushPose();
    poseStack.translate(0.5D, healthBarHeight(blockEntity.kind()), 0.5D);
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

  private static double healthBarHeight(LeagueStructureKind kind) {
    return switch (kind) {
      case NEXUS -> 4.35D;
      case INHIBITOR -> 3.35D;
      case TURRET -> 5.35D;
    };
  }
}
