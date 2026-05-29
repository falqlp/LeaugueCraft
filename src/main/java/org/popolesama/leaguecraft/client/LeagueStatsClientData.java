package org.popolesama.leaguecraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LeagueStatsClientData {
  private static int attackDamage;
  private static int abilityPower;

  private LeagueStatsClientData() {
  }

  public static int attackDamage() {
    return attackDamage;
  }

  public static int abilityPower() {
    return abilityPower;
  }

  public static void setStats(int attackDamage, int abilityPower) {
    LeagueStatsClientData.attackDamage = Math.max(0, attackDamage);
    LeagueStatsClientData.abilityPower = Math.max(0, abilityPower);
  }
}
