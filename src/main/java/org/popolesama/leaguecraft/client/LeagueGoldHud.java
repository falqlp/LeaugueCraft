package org.popolesama.leaguecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LeagueGoldHud {
  private static final int PANEL_COLOR = 0x99000000;
  private static final int BORDER_COLOR = 0xAAE0B94A;
  private static final int TEXT_COLOR = 0xFFFFD966;

  private LeagueGoldHud() {
  }

  public static void render(GuiGraphics guiGraphics, net.minecraft.client.DeltaTracker deltaTracker) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player == null || minecraft.options.hideGui) {
      return;
    }

    Font font = minecraft.font;
    Component goldText = Component.literal(LeagueGoldClientData.gold() + " or");
    Component attackDamageText = Component.literal("AD " + LeagueStatsClientData.attackDamage());
    Component abilityPowerText = Component.literal("AP " + LeagueStatsClientData.abilityPower());
    int width = Math.max(74, Math.max(font.width(goldText), Math.max(font.width(attackDamageText), font.width(abilityPowerText))) + 22);
    int x = guiGraphics.guiWidth() - width - 8;
    int y = 8;

    guiGraphics.fill(x, y, x + width, y + 38, PANEL_COLOR);
    guiGraphics.renderOutline(x, y, width, 38, BORDER_COLOR);
    guiGraphics.fill(x + 6, y + 5, x + 12, y + 11, 0xFFFFC83D);
    guiGraphics.fill(x + 8, y + 3, x + 14, y + 9, 0xFFE3A928);
    guiGraphics.fill(x + 6, y + 17, x + 13, y + 20, 0xFFE05757);
    guiGraphics.fill(x + 8, y + 15, x + 10, y + 24, 0xFFE05757);
    guiGraphics.fill(x + 6, y + 29, x + 13, y + 32, 0xFF7D9CFF);
    guiGraphics.fill(x + 8, y + 27, x + 10, y + 36, 0xFF7D9CFF);
    guiGraphics.drawString(font, goldText, x + 18, y + 5, TEXT_COLOR, true);
    guiGraphics.drawString(font, attackDamageText, x + 18, y + 17, 0xFFFF8C8C, true);
    guiGraphics.drawString(font, abilityPowerText, x + 18, y + 29, 0xFFAEC2FF, true);
  }
}
