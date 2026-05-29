package org.popolesama.leaguecraft.client;

import org.popolesama.leaguecraft.network.LeagueShopBuyPayload;
import org.popolesama.leaguecraft.player.LeaguePlayerClass;
import org.popolesama.leaguecraft.player.LeagueShopUpgrade;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class LeagueShopScreen extends Screen {
  private static final int PANEL_WIDTH = 430;
  private static final int PANEL_HEIGHT = 246;
  private static final int ROW_HEIGHT = 46;
  private static final int TAB_WIDTH = 78;
  private static final int BUY_BUTTON_WIDTH = 72;
  private LeaguePlayerClass.Role selectedRole = LeaguePlayerClass.Role.MAGE;

  public LeagueShopScreen() {
    super(Component.literal("Shop LeagueCraft"));
  }

  @Override
  protected void init() {
    rebuildShopWidgets();
  }

  private void rebuildShopWidgets() {
    clearWidgets();
    int left = (width - PANEL_WIDTH) / 2;
    int top = (height - PANEL_HEIGHT) / 2;
    int tabX = left + 12;

    for (LeaguePlayerClass.Role role : LeaguePlayerClass.Role.values()) {
      addRenderableWidget(Button.builder(Component.literal(role.displayName()), button -> {
        selectedRole = role;
        rebuildShopWidgets();
      }).bounds(tabX, top + 22, TAB_WIDTH - 6, 20).build());
      tabX += TAB_WIDTH;
    }

    List<LeagueShopUpgrade> upgrades = Arrays.stream(LeagueShopUpgrade.values())
        .filter(upgrade -> upgrade.role() == selectedRole)
        .toList();
    int rowY = top + 54;
    for (LeagueShopUpgrade upgrade : upgrades) {
      addRenderableWidget(Button.builder(Component.literal("Acheter"), button -> PacketDistributor.sendToServer(new LeagueShopBuyPayload(upgrade.ordinal())))
          .bounds(left + PANEL_WIDTH - BUY_BUTTON_WIDTH - 14, rowY + 11, BUY_BUTTON_WIDTH, 20)
          .build());
      rowY += ROW_HEIGHT;
    }
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    int left = (width - PANEL_WIDTH) / 2;
    int top = (height - PANEL_HEIGHT) / 2;
    drawVanillaPanel(guiGraphics, left, top);

    int rowY = top + 54;
    for (LeagueShopUpgrade upgrade : LeagueShopUpgrade.values()) {
      if (upgrade.role() != selectedRole) {
        continue;
      }

      guiGraphics.fill(left + 10, rowY - 3, left + PANEL_WIDTH - 10, rowY + ROW_HEIGHT - 5, 0xFF8B8B8B);
      guiGraphics.fill(left + 11, rowY - 2, left + PANEL_WIDTH - 11, rowY + ROW_HEIGHT - 6, 0xFFC6C6C6);
      rowY += ROW_HEIGHT;
    }

    super.render(guiGraphics, mouseX, mouseY, partialTick);

    guiGraphics.drawString(font, title, left + 12, top + 8, 0x404040, false);

    rowY = top + 56;
    for (LeagueShopUpgrade upgrade : LeagueShopUpgrade.values()) {
      if (upgrade.role() != selectedRole) {
        continue;
      }

      guiGraphics.drawString(font, Component.literal(upgrade.id() + " - " + upgrade.price() + " or"), left + 18, rowY, 0x404040, false);
      drawWrappedDescription(guiGraphics, upgrade.description(), left + 18, rowY + 12, left + PANEL_WIDTH - BUY_BUTTON_WIDTH - 30);
      rowY += ROW_HEIGHT;
    }
  }

  private void drawVanillaPanel(GuiGraphics guiGraphics, int left, int top) {
    guiGraphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xFFC6C6C6);
    guiGraphics.fill(left, top, left + PANEL_WIDTH, top + 2, 0xFFFFFFFF);
    guiGraphics.fill(left, top, left + 2, top + PANEL_HEIGHT, 0xFFFFFFFF);
    guiGraphics.fill(left, top + PANEL_HEIGHT - 2, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xFF555555);
    guiGraphics.fill(left + PANEL_WIDTH - 2, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xFF555555);
    guiGraphics.fill(left + 4, top + 48, left + PANEL_WIDTH - 4, top + 50, 0xFF777777);
  }

  private void drawWrappedDescription(GuiGraphics guiGraphics, String description, int x, int y, int maxX) {
    int maxWidth = maxX - x;
    int lineY = y;
    StringBuilder line = new StringBuilder();
    for (String word : description.split(" ")) {
      String candidate = line.isEmpty() ? word : line + " " + word;
      if (font.width(candidate) > maxWidth) {
        guiGraphics.drawString(font, Component.literal(line.toString()), x, lineY, 0x404040, false);
        line = new StringBuilder(word);
        lineY += 10;
      } else {
        line = new StringBuilder(candidate);
      }
    }

    if (!line.isEmpty()) {
      guiGraphics.drawString(font, Component.literal(line.toString()), x, lineY, 0x404040, false);
    }
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
