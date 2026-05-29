package org.popolesama.leaguecraft.network;

import org.popolesama.leaguecraft.client.LeagueGoldClientData;
import org.popolesama.leaguecraft.client.LeagueShopClient;
import org.popolesama.leaguecraft.client.LeagueStatsClientData;
import org.popolesama.leaguecraft.player.LeagueShop;
import org.popolesama.leaguecraft.player.LeagueShopUpgrade;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class LeagueCraftNetwork {
  private LeagueCraftNetwork() {
  }

  public static void register(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar("1");
    registrar.playToClient(
        LeagueGoldSyncPayload.TYPE,
        LeagueGoldSyncPayload.STREAM_CODEC,
        (payload, context) -> context.enqueueWork(() -> LeagueGoldClientData.setGold(payload.gold())));
    registrar.playToClient(
        LeagueStatsSyncPayload.TYPE,
        LeagueStatsSyncPayload.STREAM_CODEC,
        (payload, context) -> context.enqueueWork(() -> LeagueStatsClientData.setStats(payload.attackDamage(), payload.abilityPower())));
    registrar.playToClient(
        LeagueShopOpenPayload.TYPE,
        LeagueShopOpenPayload.STREAM_CODEC,
        (payload, context) -> context.enqueueWork(LeagueShopClient::openShopScreen));
    registrar.playToServer(
        LeagueShopBuyPayload.TYPE,
        LeagueShopBuyPayload.STREAM_CODEC,
        (payload, context) -> context.enqueueWork(() -> {
          LeagueShopUpgrade[] upgrades = LeagueShopUpgrade.values();
          if (payload.upgradeOrdinal() >= 0 && payload.upgradeOrdinal() < upgrades.length && context.player() instanceof ServerPlayer player) {
            LeagueShop.buy(player, upgrades[payload.upgradeOrdinal()]);
          }
        }));
  }
}
