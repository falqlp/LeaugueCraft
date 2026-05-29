package org.popolesama.leaguecraft.player;

import org.popolesama.leaguecraft.block.LeagueStructureBlockEntity;
import org.popolesama.leaguecraft.block.LeagueStructureKind;
import org.popolesama.leaguecraft.entity.LeagueMonster;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public final class LeagueTurretAggro {
  private LeagueTurretAggro() {
  }

  public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer victim)) {
      return;
    }

    Entity attackerEntity = event.getSource().getEntity();
    if (!(attackerEntity instanceof ServerPlayer attacker)) {
      return;
    }

    LeagueMonster.Team victimTeam = LeaguePlayerTeams.getTeam(victim);
    LeagueMonster.Team attackerTeam = LeaguePlayerTeams.getTeam(attacker);
    if (victimTeam == LeagueMonster.Team.NEUTRAL || attackerTeam == LeagueMonster.Team.NEUTRAL || victimTeam == attackerTeam) {
      return;
    }

    focusNearbyAlliedTurrets(victim.level(), attacker, victimTeam);
  }

  private static void focusNearbyAlliedTurrets(Level level, ServerPlayer attacker, LeagueMonster.Team turretTeam) {
    BlockPos center = attacker.blockPosition();
    int maxRange = LeagueStructureKind.TURRET.attackRange();

    for (BlockPos pos : BlockPos.betweenClosed(center.offset(-maxRange, -5, -maxRange), center.offset(maxRange, 6, maxRange))) {
      if (level.getBlockEntity(pos) instanceof LeagueStructureBlockEntity structure
          && structure.kind() == LeagueStructureKind.TURRET
          && structure.team() == turretTeam
          && structure.canFocusPlayer(attacker)) {
        structure.focusPlayer(attacker);
      }
    }
  }
}
