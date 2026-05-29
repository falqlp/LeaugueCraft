package org.popolesama.leaguecraft.item;

import org.popolesama.leaguecraft.player.LeaguePlayerClass;
import org.popolesama.leaguecraft.player.LeagueShop;
import org.popolesama.leaguecraft.player.LeagueShopUpgrade;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TankAbsorptionItem extends Item {
  private static final int COOLDOWN_TICKS = 20 * 28;
  private static final int DURATION_TICKS = 20 * 8;
  private static final int AMPLIFIER = 2;

  public TankAbsorptionItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!LeaguePlayerClass.hasRole(player, LeaguePlayerClass.Role.TANK)) {
      if (!level.isClientSide) {
        player.displayClientMessage(Component.literal("Tu dois etre Tank pour utiliser cet objet."), true);
      }
      return InteractionResultHolder.fail(stack);
    }

    if (player.getCooldowns().isOnCooldown(this)) {
      return InteractionResultHolder.fail(stack);
    }

    if (!level.isClientSide) {
      int amplifier = LeagueShop.hasUpgrade(player, LeagueShopUpgrade.TANK_ABSORPTION_POWER) ? AMPLIFIER + 1 : AMPLIFIER;
      int cooldown = LeagueShop.hasUpgrade(player, LeagueShopUpgrade.TANK_COOLDOWN) ? COOLDOWN_TICKS * 75 / 100 : COOLDOWN_TICKS;
      player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, DURATION_TICKS, amplifier, false, true, true));
      player.getCooldowns().addCooldown(this, cooldown);
      player.displayClientMessage(Component.literal("Bouclier d'absorption active."), true);
      level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 0.9F, 0.85F);
    }

    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }
}
