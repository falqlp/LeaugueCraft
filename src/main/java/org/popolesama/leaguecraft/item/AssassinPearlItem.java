package org.popolesama.leaguecraft.item;

import org.popolesama.leaguecraft.player.LeaguePlayerClass;
import org.popolesama.leaguecraft.player.LeagueShop;
import org.popolesama.leaguecraft.player.LeagueShopUpgrade;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class AssassinPearlItem extends Item {
  private static final int COOLDOWN_TICKS = 20 * 12;
  private static final float VELOCITY = 1.7F;

  public AssassinPearlItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!LeaguePlayerClass.hasRole(player, LeaguePlayerClass.Role.ASSASSIN)) {
      if (!level.isClientSide) {
        player.displayClientMessage(Component.literal("Tu dois etre Assassin pour utiliser cet objet."), true);
      }
      return InteractionResultHolder.fail(stack);
    }

    if (player.getCooldowns().isOnCooldown(this)) {
      return InteractionResultHolder.fail(stack);
    }

    if (!level.isClientSide) {
      ThrownEnderpearl pearl = new ThrownEnderpearl(level, player);
      pearl.setItem(new ItemStack(Items.ENDER_PEARL));
      float velocity = LeagueShop.hasUpgrade(player, LeagueShopUpgrade.ASSASSIN_PEARL_RANGE) ? VELOCITY + 0.45F : VELOCITY;
      int cooldown = LeagueShop.hasUpgrade(player, LeagueShopUpgrade.ASSASSIN_COOLDOWN) ? COOLDOWN_TICKS * 70 / 100 : COOLDOWN_TICKS;
      pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 1.0F);
      level.addFreshEntity(pearl);
      player.getCooldowns().addCooldown(this, cooldown);
      level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.9F, 1.0F);
    }

    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }
}
