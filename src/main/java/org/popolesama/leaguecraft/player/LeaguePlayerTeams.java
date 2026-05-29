package org.popolesama.leaguecraft.player;

import java.util.Locale;

import org.popolesama.leaguecraft.entity.LeagueMonster;

import net.minecraft.world.entity.player.Player;

public final class LeaguePlayerTeams {
  private static final String TEAM_TAG = "LeagueCraftTeam";

  private LeaguePlayerTeams() {
  }

  public static LeagueMonster.Team getTeam(Player player) {
    String teamName = player.getPersistentData().getString(TEAM_TAG);
    if (teamName == null || teamName.isBlank()) {
      return LeagueMonster.Team.NEUTRAL;
    }

    try {
      return LeagueMonster.Team.valueOf(teamName.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException ignored) {
      return LeagueMonster.Team.NEUTRAL;
    }
  }

  public static void setTeam(Player player, LeagueMonster.Team team) {
    if (team == LeagueMonster.Team.NEUTRAL) {
      clearTeam(player);
      return;
    }

    player.getPersistentData().putString(TEAM_TAG, team.name());
  }

  public static void clearTeam(Player player) {
    player.getPersistentData().remove(TEAM_TAG);
  }

  public static boolean isEnemyOfTeam(Player player, LeagueMonster.Team team) {
    LeagueMonster.Team playerTeam = getTeam(player);
    return playerTeam != LeagueMonster.Team.NEUTRAL && team != LeagueMonster.Team.NEUTRAL && playerTeam != team;
  }

  public static String displayName(LeagueMonster.Team team) {
    return switch (team) {
      case BLUE -> "bleu";
      case RED -> "rouge";
      case NEUTRAL -> "neutre";
    };
  }
}
