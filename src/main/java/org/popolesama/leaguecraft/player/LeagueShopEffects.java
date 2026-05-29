package org.popolesama.leaguecraft.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public final class LeagueShopEffects {
  private LeagueShopEffects() {
  }

  public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
    if (!(event.getEntity() instanceof LivingEntity target)) {
      return;
    }

    Entity directEntity = event.getSource().getDirectEntity();
    Entity attacker = event.getSource().getEntity();
    if (!(directEntity instanceof AbstractArrow) || !(attacker instanceof ServerPlayer player)) {
      return;
    }

    if (LeaguePlayerClass.hasRole(player, LeaguePlayerClass.Role.MARKSMAN)
        && LeagueShop.hasUpgrade(player, LeagueShopUpgrade.MARKSMAN_POISON_ARROWS)) {
      target.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 4, 0), player);
    }

    if (LeaguePlayerClass.hasRole(player, LeaguePlayerClass.Role.MARKSMAN)
        && LeagueShop.hasUpgrade(player, LeagueShopUpgrade.MARKSMAN_SLOW_ARROWS)) {
      target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 0), player);
    }
  }
}
