package org.popolesama.leaguecraft.player;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.popolesama.leaguecraft.LeagueCraft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class LeaguePlayerClass {
  private static final String CLASS_TAG = "LeagueCraftClass";

  private LeaguePlayerClass() {
  }

  public static Optional<Role> getRole(Player player) {
    String roleName = player.getPersistentData().getString(CLASS_TAG);
    if (roleName.isBlank()) {
      return Optional.empty();
    }

    try {
      return Optional.of(Role.valueOf(roleName.toUpperCase(Locale.ROOT)));
    } catch (IllegalArgumentException ignored) {
      return Optional.empty();
    }
  }

  public static boolean hasRole(Player player, Role role) {
    return getRole(player).filter(current -> current == role).isPresent();
  }

  public static void setRole(ServerPlayer player, Role role) {
    player.getPersistentData().putString(CLASS_TAG, role.name());
    removeClassItems(player);
    giveClassItems(player, role);
    player.sendSystemMessage(Component.literal("Classe LeagueCraft choisie : " + role.displayName() + "."));
  }

  public static void clearRole(ServerPlayer player) {
    player.getPersistentData().remove(CLASS_TAG);
    removeClassItems(player);
    player.sendSystemMessage(Component.literal("Classe LeagueCraft retiree."));
  }

  public static void syncClassItem(ServerPlayer player) {
    getRole(player).ifPresent(role -> {
      for (Item item : role.items()) {
        if (!player.getInventory().contains(stack -> stack.is(item))) {
          giveClassItem(player, item);
        }
      }
    });
  }

  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      syncClassItem(player);
    }
  }

  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      syncClassItem(player);
    }
  }

  public static void onPlayerClone(PlayerEvent.Clone event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      player.getPersistentData().putString(CLASS_TAG, event.getOriginal().getPersistentData().getString(CLASS_TAG));
      syncClassItem(player);
    }
  }

  private static void giveClassItems(ServerPlayer player, Role role) {
    for (Item item : role.items()) {
      giveClassItem(player, item);
    }
  }

  private static void giveClassItem(ServerPlayer player, Item item) {
    ItemStack stack = new ItemStack(item);
    if (!player.getInventory().add(stack)) {
      player.drop(stack, false);
    }
  }

  private static void removeClassItems(ServerPlayer player) {
    removeItem(player, LeagueCraft.MAGE_CLASS_ITEM.get());
    removeItem(player, LeagueCraft.MAGE_GROUND_DAMAGE_ITEM.get());
    removeItem(player, LeagueCraft.SUPPORT_CLASS_ITEM.get());
    removeItem(player, LeagueCraft.SUPPORT_SLOW_ITEM.get());
    removeItem(player, LeagueCraft.MARKSMAN_CLASS_ITEM.get());
    removeItem(player, LeagueCraft.MARKSMAN_VOLLEY_ITEM.get());
    removeItem(player, LeagueCraft.TANK_CLASS_ITEM.get());
    removeItem(player, LeagueCraft.ASSASSIN_CLASS_ITEM.get());
    player.getInventory().setChanged();
  }

  private static void removeItem(ServerPlayer player, Item item) {
    for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
      ItemStack stack = player.getInventory().getItem(slot);
      if (stack.is(item)) {
        player.getInventory().setItem(slot, ItemStack.EMPTY);
      }
    }
  }

  @Getter
  @Accessors(fluent = true)
  @RequiredArgsConstructor
  public enum Role {
    MAGE("Mage"),
    SUPPORT("Support"),
    MARKSMAN("Tireur"),
    TANK("Tank"),
    ASSASSIN("Assassin");

    private final String displayName;

    public List<Item> items() {
      return switch (this) {
        case MAGE -> List.of(LeagueCraft.MAGE_CLASS_ITEM.get(), LeagueCraft.MAGE_GROUND_DAMAGE_ITEM.get());
        case SUPPORT -> List.of(LeagueCraft.SUPPORT_CLASS_ITEM.get(), LeagueCraft.SUPPORT_SLOW_ITEM.get());
        case MARKSMAN -> List.of(LeagueCraft.MARKSMAN_CLASS_ITEM.get(), LeagueCraft.MARKSMAN_VOLLEY_ITEM.get());
        case TANK -> List.of(LeagueCraft.TANK_CLASS_ITEM.get());
        case ASSASSIN -> List.of(LeagueCraft.ASSASSIN_CLASS_ITEM.get());
      };
    }
  }
}
