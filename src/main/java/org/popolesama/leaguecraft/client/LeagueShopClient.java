package org.popolesama.leaguecraft.client;

import net.minecraft.client.Minecraft;

public final class LeagueShopClient {
  private LeagueShopClient() {
  }

  public static void openShopScreen() {
    Minecraft.getInstance().setScreen(new LeagueShopScreen());
  }
}
