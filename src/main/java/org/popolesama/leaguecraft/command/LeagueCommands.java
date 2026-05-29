package org.popolesama.leaguecraft.command;

import org.popolesama.leaguecraft.entity.LeagueMonster;
import org.popolesama.leaguecraft.player.LeagueGold;
import org.popolesama.leaguecraft.player.LeaguePlayerClass;
import org.popolesama.leaguecraft.player.LeaguePlayerStats;
import org.popolesama.leaguecraft.player.LeaguePlayerTeams;
import org.popolesama.leaguecraft.player.LeagueShop;
import org.popolesama.leaguecraft.player.LeagueShopUpgrade;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class LeagueCommands {
  private LeagueCommands() {
  }

  public static void register(RegisterCommandsEvent event) {
    CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
    dispatcher.register(Commands.literal("leaguecraft")
        .then(Commands.literal("team")
            .then(Commands.literal("get")
                .executes(context -> getTeam(context.getSource().getPlayerOrException()))
                .then(Commands.argument("joueur", EntityArgument.player())
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> getTeam(EntityArgument.getPlayer(context, "joueur")))))
            .then(teamCommand("blue", LeagueMonster.Team.BLUE))
            .then(teamCommand("red", LeagueMonster.Team.RED))
            .then(teamCommand("neutral", LeagueMonster.Team.NEUTRAL))
            .then(teamCommand("clear", LeagueMonster.Team.NEUTRAL)))
        .then(Commands.literal("gold")
            .then(Commands.literal("get")
                .executes(context -> getGold(context.getSource().getPlayerOrException()))
                .then(Commands.argument("joueur", EntityArgument.player())
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> getGold(EntityArgument.getPlayer(context, "joueur")))))
            .then(Commands.literal("set")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("montant", IntegerArgumentType.integer(0))
                    .executes(context -> setGold(
                        context.getSource().getPlayerOrException(),
                        IntegerArgumentType.getInteger(context, "montant")))
                    .then(Commands.argument("joueur", EntityArgument.player())
                        .executes(context -> setGold(
                            EntityArgument.getPlayer(context, "joueur"),
                            IntegerArgumentType.getInteger(context, "montant"))))))
            .then(Commands.literal("add")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("montant", IntegerArgumentType.integer(1))
                    .executes(context -> addGold(
                        context.getSource().getPlayerOrException(),
                        IntegerArgumentType.getInteger(context, "montant")))
                    .then(Commands.argument("joueur", EntityArgument.player())
                        .executes(context -> addGold(
                            EntityArgument.getPlayer(context, "joueur"),
                            IntegerArgumentType.getInteger(context, "montant")))))))
        .then(Commands.literal("class")
            .then(Commands.literal("get")
                .executes(context -> getPlayerClass(context.getSource().getPlayerOrException()))
                .then(Commands.argument("joueur", EntityArgument.player())
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> getPlayerClass(EntityArgument.getPlayer(context, "joueur")))))
            .then(classCommand("mage", LeaguePlayerClass.Role.MAGE))
            .then(classCommand("support", LeaguePlayerClass.Role.SUPPORT))
            .then(classCommand("tireur", LeaguePlayerClass.Role.MARKSMAN))
            .then(classCommand("marksman", LeaguePlayerClass.Role.MARKSMAN))
            .then(classCommand("tank", LeaguePlayerClass.Role.TANK))
            .then(classCommand("assassin", LeaguePlayerClass.Role.ASSASSIN))
            .then(Commands.literal("clear")
                .executes(context -> clearPlayerClass(context.getSource().getPlayerOrException()))
                .then(Commands.argument("joueur", EntityArgument.player())
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> clearPlayerClass(EntityArgument.getPlayer(context, "joueur"))))))
        .then(Commands.literal("stats")
            .then(Commands.literal("get")
                .executes(context -> getStats(context.getSource().getPlayerOrException()))
                .then(Commands.argument("joueur", EntityArgument.player())
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> getStats(EntityArgument.getPlayer(context, "joueur")))))
            .then(statCommand("ad", true))
            .then(statCommand("ap", false)))
        .then(Commands.literal("shop")
            .then(Commands.literal("list")
                .executes(context -> LeagueShop.list(context.getSource().getPlayerOrException())))
            .then(Commands.literal("buy")
                .then(Commands.argument("upgrade", StringArgumentType.word())
                    .executes(context -> buyUpgrade(
                        context.getSource().getPlayerOrException(),
                        StringArgumentType.getString(context, "upgrade")))))));
  }

  private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> teamCommand(String name, LeagueMonster.Team team) {
    return Commands.literal(name)
        .executes(context -> setTeam(context.getSource().getPlayerOrException(), team))
        .then(Commands.argument("joueur", EntityArgument.player())
            .requires(source -> source.hasPermission(2))
            .executes(context -> setTeam(EntityArgument.getPlayer(context, "joueur"), team)));
  }

  private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> classCommand(String name, LeaguePlayerClass.Role role) {
    return Commands.literal(name)
        .executes(context -> setPlayerClass(context.getSource().getPlayerOrException(), role))
        .then(Commands.argument("joueur", EntityArgument.player())
            .requires(source -> source.hasPermission(2))
            .executes(context -> setPlayerClass(EntityArgument.getPlayer(context, "joueur"), role)));
  }

  private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> statCommand(String name, boolean attackDamage) {
    return Commands.literal(name)
        .requires(source -> source.hasPermission(2))
        .then(Commands.literal("set")
            .then(Commands.argument("valeur", IntegerArgumentType.integer(0))
                .executes(context -> setStat(
                    context.getSource().getPlayerOrException(),
                    attackDamage,
                    IntegerArgumentType.getInteger(context, "valeur")))
                .then(Commands.argument("joueur", EntityArgument.player())
                    .executes(context -> setStat(
                        EntityArgument.getPlayer(context, "joueur"),
                        attackDamage,
                        IntegerArgumentType.getInteger(context, "valeur"))))))
        .then(Commands.literal("add")
            .then(Commands.argument("valeur", IntegerArgumentType.integer())
                .executes(context -> addStat(
                    context.getSource().getPlayerOrException(),
                    attackDamage,
                    IntegerArgumentType.getInteger(context, "valeur")))
                .then(Commands.argument("joueur", EntityArgument.player())
                    .executes(context -> addStat(
                        EntityArgument.getPlayer(context, "joueur"),
                        attackDamage,
                        IntegerArgumentType.getInteger(context, "valeur"))))));
  }

  private static int setTeam(ServerPlayer player, LeagueMonster.Team team) throws CommandSyntaxException {
    LeaguePlayerTeams.setTeam(player, team);
    String teamName = LeaguePlayerTeams.displayName(team);
    player.sendSystemMessage(Component.literal("Tu es maintenant dans le camp " + teamName + "."));
    return 1;
  }

  private static int getTeam(ServerPlayer player) {
    LeagueMonster.Team team = LeaguePlayerTeams.getTeam(player);
    player.sendSystemMessage(Component.literal("Camp LeagueCraft actuel : " + LeaguePlayerTeams.displayName(team) + "."));
    return 1;
  }

  private static int getGold(ServerPlayer player) {
    player.sendSystemMessage(Component.literal("Or LeagueCraft actuel : " + LeagueGold.getGold(player) + "."));
    LeagueGold.sync(player);
    return 1;
  }

  private static int setGold(ServerPlayer player, int gold) {
    LeagueGold.setGold(player, gold);
    player.sendSystemMessage(Component.literal("Or LeagueCraft defini a " + LeagueGold.getGold(player) + "."));
    return 1;
  }

  private static int addGold(ServerPlayer player, int gold) {
    LeagueGold.addGold(player, gold);
    player.sendSystemMessage(Component.literal("Or LeagueCraft ajoute : +" + gold + ". Total : " + LeagueGold.getGold(player) + "."));
    return 1;
  }

  private static int setPlayerClass(ServerPlayer player, LeaguePlayerClass.Role role) {
    LeaguePlayerClass.setRole(player, role);
    return 1;
  }

  private static int clearPlayerClass(ServerPlayer player) {
    LeaguePlayerClass.clearRole(player);
    return 1;
  }

  private static int getPlayerClass(ServerPlayer player) {
    String className = LeaguePlayerClass.getRole(player)
        .map(LeaguePlayerClass.Role::displayName)
        .orElse("Aucune");
    player.sendSystemMessage(Component.literal("Classe LeagueCraft actuelle : " + className + "."));
    return 1;
  }

  private static int getStats(ServerPlayer player) {
    player.sendSystemMessage(Component.literal("Stats LeagueCraft : AD " + LeaguePlayerStats.attackDamage(player) + " / AP " + LeaguePlayerStats.abilityPower(player) + " / Vitesse d'attaque " + LeaguePlayerStats.attackSpeed(player) + "."));
    LeaguePlayerStats.sync(player);
    return 1;
  }

  private static int setStat(ServerPlayer player, boolean attackDamage, int value) {
    if (attackDamage) {
      LeaguePlayerStats.setAttackDamage(player, value);
    } else {
      LeaguePlayerStats.setAbilityPower(player, value);
    }
    return getStats(player);
  }

  private static int addStat(ServerPlayer player, boolean attackDamage, int value) {
    if (attackDamage) {
      LeaguePlayerStats.addAttackDamage(player, value);
    } else {
      LeaguePlayerStats.addAbilityPower(player, value);
    }
    return getStats(player);
  }

  private static int buyUpgrade(ServerPlayer player, String upgradeId) {
    return LeagueShop.findUpgrade(upgradeId)
        .map(upgrade -> LeagueShop.buy(player, upgrade))
        .orElseGet(() -> {
          player.sendSystemMessage(Component.literal("Upgrade inconnue : " + upgradeId + ". Utilise /leaguecraft shop list."));
          return 0;
        });
  }
}
