package org.popolesama.leaguecraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LeagueGoldClientData {
  private static int gold;

  private LeagueGoldClientData() {
  }

  public static int gold() {
    return gold;
  }

  public static void setGold(int gold) {
    LeagueGoldClientData.gold = Math.max(0, gold);
  }
}
