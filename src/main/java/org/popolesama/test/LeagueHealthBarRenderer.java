package org.popolesama.test;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public final class LeagueHealthBarRenderer {
  private static final int BAR_WIDTH = 34;
  private static final int BAR_HEIGHT = 4;
  private static final int BORDER_COLOR = 0xCC111111;
  private static final int BACKGROUND_COLOR = 0xCC3A1212;
  private static final int HEALTH_COLOR = 0xFFE64B4B;

  private LeagueHealthBarRenderer() {
  }

  public static void render(PoseStack poseStack, MultiBufferSource buffer, Quaternionf cameraOrientation, Font font, float health, float maxHealth, String text, int packedLight) {
    float healthRatio = maxHealth <= 0.0F ? 0.0F : Mth.clamp(health / maxHealth, 0.0F, 1.0F);

    poseStack.pushPose();
    poseStack.mulPose(cameraOrientation);
    poseStack.scale(0.025F, -0.025F, 0.025F);

    Matrix4f matrix = poseStack.last().pose();
    int x = -BAR_WIDTH / 2;
    int y = text == null ? 0 : 8;
    int fillWidth = Math.round((BAR_WIDTH - 2) * healthRatio);

    fillQuad(matrix, buffer, x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, BORDER_COLOR);
    fillQuad(matrix, buffer, x, y, BAR_WIDTH, BAR_HEIGHT, BACKGROUND_COLOR);
    fillQuad(matrix, buffer, x + 1, y + 1, fillWidth, BAR_HEIGHT - 2, HEALTH_COLOR);

    if (text != null) {
      float textX = -font.width(text) / 2.0F;
      font.drawInBatch(text, textX, -4.0F, 0xFFFFFFFF, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
    }

    poseStack.popPose();
  }

  private static void fillQuad(Matrix4f matrix, MultiBufferSource buffer, int x, int y, int width, int height, int color) {
    VertexConsumer consumer = buffer.getBuffer(RenderType.gui());
    float x0 = x;
    float y0 = y;
    float x1 = x + width;
    float y1 = y + height;

    consumer.addVertex(matrix, x0, y1, 0.0F).setColor(color);
    consumer.addVertex(matrix, x1, y1, 0.0F).setColor(color);
    consumer.addVertex(matrix, x1, y0, 0.0F).setColor(color);
    consumer.addVertex(matrix, x0, y0, 0.0F).setColor(color);
  }
}
