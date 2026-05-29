package org.popolesama.leaguecraft.player;

import org.popolesama.leaguecraft.network.LeagueStatsSyncPayload;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class LeaguePlayerStats {
  private static final String AD_TAG = "LeagueCraftAttackDamage";
  private static final String AP_TAG = "LeagueCraftAbilityPower";
  private static final String ATTACK_SPEED_TAG = "LeagueCraftAttackSpeed";
  private static final int DEFAULT_AD = 0;
  private static final int DEFAULT_AP = 0;
  private static final int DEFAULT_ATTACK_SPEED = 0;

  private LeaguePlayerStats() {
  }

  public static int attackDamage(Player player) {
    return Math.max(0, player.getPersistentData().contains(AD_TAG) ? player.getPersistentData().getInt(AD_TAG) : DEFAULT_AD);
  }

  public static int abilityPower(Player player) {
    return Math.max(0, player.getPersistentData().contains(AP_TAG) ? player.getPersistentData().getInt(AP_TAG) : DEFAULT_AP);
  }

  public static int attackSpeed(Player player) {
    return Math.max(0, player.getPersistentData().contains(ATTACK_SPEED_TAG) ? player.getPersistentData().getInt(ATTACK_SPEED_TAG) : DEFAULT_ATTACK_SPEED);
  }

  public static void setAttackDamage(ServerPlayer player, int value) {
    player.getPersistentData().putInt(AD_TAG, Math.max(0, value));
    sync(player);
  }

  public static void addAttackDamage(ServerPlayer player, int value) {
    setAttackDamage(player, attackDamage(player) + value);
  }

  public static void setAbilityPower(ServerPlayer player, int value) {
    player.getPersistentData().putInt(AP_TAG, Math.max(0, value));
    sync(player);
  }

  public static void addAbilityPower(ServerPlayer player, int value) {
    setAbilityPower(player, abilityPower(player) + value);
  }

  public static void setAttackSpeed(ServerPlayer player, int value) {
    player.getPersistentData().putInt(ATTACK_SPEED_TAG, Math.max(0, value));
    sync(player);
  }

  public static void addAttackSpeed(ServerPlayer player, int value) {
    setAttackSpeed(player, attackSpeed(player) + value);
  }

  public static int cooldownWithAttackSpeed(Player player, int baseCooldownTicks) {
    return Math.max(5, baseCooldownTicks * 100 / (100 + attackSpeed(player)));
  }

  public static void sync(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, new LeagueStatsSyncPayload(attackDamage(player), abilityPower(player)));
  }

  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      sync(player);
    }
  }

  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      sync(player);
    }
  }

  public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      sync(player);
    }
  }

  public static void onPlayerClone(PlayerEvent.Clone event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      CompoundTag oldData = event.getOriginal().getPersistentData();
      player.getPersistentData().putInt(AD_TAG, Math.max(0, oldData.getInt(AD_TAG)));
      player.getPersistentData().putInt(AP_TAG, Math.max(0, oldData.getInt(AP_TAG)));
      player.getPersistentData().putInt(ATTACK_SPEED_TAG, Math.max(0, oldData.getInt(ATTACK_SPEED_TAG)));
      sync(player);
    }
  }
}
