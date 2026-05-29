package org.popolesama.leaguecraft.player;

import org.popolesama.leaguecraft.entity.LeagueMonster;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

public final class LeagueSpellFriendlyFire {
  private LeagueSpellFriendlyFire() {
  }

  public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
    MobEffectInstance effect = event.getEffectInstance();
    if (effect.getEffect().value().getCategory() != MobEffectCategory.HARMFUL) {
      return;
    }

    Entity source = sourceOwner(event.getEffectSource());
    if (!(source instanceof Player caster)) {
      return;
    }

    LivingEntity target = event.getEntity();
    if (target == caster || isSameLeagueTeam(caster, target)) {
      event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }
  }

  private static Entity sourceOwner(Entity source) {
    if (source instanceof AreaEffectCloud cloud && cloud.getOwner() != null) {
      return cloud.getOwner();
    }
    return source;
  }

  private static boolean isSameLeagueTeam(Player caster, LivingEntity target) {
    LeagueMonster.Team casterTeam = LeaguePlayerTeams.getTeam(caster);
    if (casterTeam == LeagueMonster.Team.NEUTRAL) {
      return false;
    }

    LeagueMonster.Team targetTeam = teamOf(target);
    return targetTeam != LeagueMonster.Team.NEUTRAL && targetTeam == casterTeam;
  }

  private static LeagueMonster.Team teamOf(LivingEntity entity) {
    if (entity instanceof Player player) {
      return LeaguePlayerTeams.getTeam(player);
    }
    if (entity instanceof LeagueMonster monster) {
      return monster.team();
    }
    return LeagueMonster.Team.NEUTRAL;
  }
}
