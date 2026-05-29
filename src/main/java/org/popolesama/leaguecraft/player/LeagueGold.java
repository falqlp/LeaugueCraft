package org.popolesama.leaguecraft.player;

import org.popolesama.leaguecraft.network.LeagueGoldSyncPayload;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class LeagueGold {
  private static final String GOLD_TAG = "LeagueCraftGold";

  private LeagueGold() {
  }

  public static int getGold(Player player) {
    return Math.max(0, player.getPersistentData().getInt(GOLD_TAG));
  }

  public static void setGold(ServerPlayer player, int gold) {
    player.getPersistentData().putInt(GOLD_TAG, Math.max(0, gold));
    sync(player);
  }

  public static void addGold(ServerPlayer player, int amount) {
    if (amount <= 0) {
      return;
    }

    int nextGold = getGold(player) + amount;
    player.getPersistentData().putInt(GOLD_TAG, nextGold);
    sync(player);
    player.displayClientMessage(Component.literal("+" + amount + " or"), true);
  }

  public static void sync(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, new LeagueGoldSyncPayload(getGold(player)));
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
      player.getPersistentData().putInt(GOLD_TAG, Math.max(0, oldData.getInt(GOLD_TAG)));
      sync(player);
    }
  }
}
