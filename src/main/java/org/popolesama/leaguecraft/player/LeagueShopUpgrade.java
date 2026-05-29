package org.popolesama.leaguecraft.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.level.ServerPlayer;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum LeagueShopUpgrade {
  MARKSMAN_AD("tireur_ad", LeaguePlayerClass.Role.MARKSMAN, 180, "+12 AD"),
  MARKSMAN_ATTACK_SPEED("tireur_vitesse_attaque", LeaguePlayerClass.Role.MARKSMAN, 220, "+20 vitesse d'attaque"),
  MARKSMAN_POISON_ARROWS("tireur_fleches_poison", LeaguePlayerClass.Role.MARKSMAN, 260, "Les fleches empoisonnent la cible"),
  MARKSMAN_SLOW_ARROWS("tireur_fleches_slow", LeaguePlayerClass.Role.MARKSMAN, 260, "Les fleches ralentissent la cible"),

  MAGE_AP("mage_ap", LeaguePlayerClass.Role.MAGE, 180, "+18 AP"),
  MAGE_BIG_AP("mage_baton", LeaguePlayerClass.Role.MAGE, 320, "+35 AP"),
  MAGE_LINGERING_POISON("mage_zone_poison", LeaguePlayerClass.Role.MAGE, 260, "Les potions de degats ajoutent du poison"),
  MAGE_LINGERING_SLOW("mage_zone_slow", LeaguePlayerClass.Role.MAGE, 260, "Les potions de degats ajoutent du ralentissement"),

  SUPPORT_AP("support_ap", LeaguePlayerClass.Role.SUPPORT, 160, "+14 AP"),
  SUPPORT_HEAL_POWER("support_soin", LeaguePlayerClass.Role.SUPPORT, 240, "+20 AP pour renforcer les soins"),
  SUPPORT_SLOW_MASTERY("support_slow", LeaguePlayerClass.Role.SUPPORT, 240, "Le ralentissement dure plus longtemps"),
  SUPPORT_COOLDOWN("support_cooldown", LeaguePlayerClass.Role.SUPPORT, 220, "Cooldowns des sorts de support reduits"),

  TANK_AD("tank_ad", LeaguePlayerClass.Role.TANK, 160, "+8 AD"),
  TANK_AP("tank_ap", LeaguePlayerClass.Role.TANK, 160, "+8 AP"),
  TANK_ABSORPTION_POWER("tank_absorption", LeaguePlayerClass.Role.TANK, 260, "Le bouclier d'absorption est plus fort"),
  TANK_COOLDOWN("tank_cooldown", LeaguePlayerClass.Role.TANK, 220, "Cooldown du rempart reduit"),

  ASSASSIN_AD("assassin_ad", LeaguePlayerClass.Role.ASSASSIN, 180, "+14 AD"),
  ASSASSIN_AP("assassin_ap", LeaguePlayerClass.Role.ASSASSIN, 180, "+10 AP"),
  ASSASSIN_PEARL_RANGE("assassin_range", LeaguePlayerClass.Role.ASSASSIN, 260, "La perle est lancee plus vite"),
  ASSASSIN_COOLDOWN("assassin_cooldown", LeaguePlayerClass.Role.ASSASSIN, 220, "Cooldown de la perle reduit");

  private final String id;
  private final LeaguePlayerClass.Role role;
  private final int price;
  private final String description;

  public void apply(ServerPlayer player) {
    switch (this) {
      case MARKSMAN_AD -> LeaguePlayerStats.addAttackDamage(player, 12);
      case MARKSMAN_ATTACK_SPEED -> LeaguePlayerStats.addAttackSpeed(player, 20);
      case MAGE_AP -> LeaguePlayerStats.addAbilityPower(player, 18);
      case MAGE_BIG_AP -> LeaguePlayerStats.addAbilityPower(player, 35);
      case SUPPORT_AP -> LeaguePlayerStats.addAbilityPower(player, 14);
      case SUPPORT_HEAL_POWER -> LeaguePlayerStats.addAbilityPower(player, 20);
      case TANK_AD -> LeaguePlayerStats.addAttackDamage(player, 8);
      case TANK_AP -> LeaguePlayerStats.addAbilityPower(player, 8);
      case ASSASSIN_AD -> LeaguePlayerStats.addAttackDamage(player, 14);
      case ASSASSIN_AP -> LeaguePlayerStats.addAbilityPower(player, 10);
      default -> {
      }
    }
  }
}
