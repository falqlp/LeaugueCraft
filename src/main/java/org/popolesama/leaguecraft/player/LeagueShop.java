package org.popolesama.leaguecraft.player;

import java.util.Arrays;
import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class LeagueShop {
  private static final String SHOP_TAG_PREFIX = "LeagueCraftShop.";

  private LeagueShop() {
  }

  public static Optional<LeagueShopUpgrade> findUpgrade(String id) {
    return Arrays.stream(LeagueShopUpgrade.values())
        .filter(upgrade -> upgrade.id().equalsIgnoreCase(id))
        .findFirst();
  }

  public static boolean hasUpgrade(ServerPlayer player, LeagueShopUpgrade upgrade) {
    return player.getPersistentData().getBoolean(tag(upgrade));
  }

  public static boolean hasUpgrade(net.minecraft.world.entity.player.Player player, LeagueShopUpgrade upgrade) {
    return player.getPersistentData().getBoolean(tag(upgrade));
  }

  public static int buy(ServerPlayer player, LeagueShopUpgrade upgrade) {
    Optional<LeaguePlayerClass.Role> role = LeaguePlayerClass.getRole(player);
    if (role.filter(current -> current == upgrade.role()).isEmpty()) {
      player.sendSystemMessage(Component.literal("Cet achat est reserve au role " + upgrade.role().displayName() + "."));
      return 0;
    }

    if (hasUpgrade(player, upgrade)) {
      player.sendSystemMessage(Component.literal("Tu as deja achete : " + upgrade.id() + "."));
      return 0;
    }

    int gold = LeagueGold.getGold(player);
    if (gold < upgrade.price()) {
      player.sendSystemMessage(Component.literal("Il te manque " + (upgrade.price() - gold) + " or pour acheter " + upgrade.id() + "."));
      return 0;
    }

    LeagueGold.setGold(player, gold - upgrade.price());
    player.getPersistentData().putBoolean(tag(upgrade), true);
    upgrade.apply(player);
    player.sendSystemMessage(Component.literal("Achat reussi : " + upgrade.id() + " (" + upgrade.description() + ")."));
    return 1;
  }

  public static int list(ServerPlayer player) {
    Optional<LeaguePlayerClass.Role> role = LeaguePlayerClass.getRole(player);
    if (role.isEmpty()) {
      player.sendSystemMessage(Component.literal("Choisis d'abord une classe avec /leaguecraft class."));
      return 0;
    }

    player.sendSystemMessage(Component.literal("Shop " + role.get().displayName() + " :"));
    Arrays.stream(LeagueShopUpgrade.values())
        .filter(upgrade -> upgrade.role() == role.get())
        .forEach(upgrade -> player.sendSystemMessage(Component.literal(
            "- " + upgrade.id() + " : " + upgrade.price() + " or - " + upgrade.description()
                + (hasUpgrade(player, upgrade) ? " [achete]" : ""))));
    return 1;
  }

  public static void onPlayerClone(PlayerEvent.Clone event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }

    for (LeagueShopUpgrade upgrade : LeagueShopUpgrade.values()) {
      if (event.getOriginal().getPersistentData().getBoolean(tag(upgrade))) {
        player.getPersistentData().putBoolean(tag(upgrade), true);
      }
    }
  }

  private static String tag(LeagueShopUpgrade upgrade) {
    return SHOP_TAG_PREFIX + upgrade.id();
  }
}
