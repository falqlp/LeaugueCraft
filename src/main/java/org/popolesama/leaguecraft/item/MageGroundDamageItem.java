package org.popolesama.leaguecraft.item;

import org.popolesama.leaguecraft.player.LeaguePlayerClass;
import org.popolesama.leaguecraft.player.LeaguePlayerStats;
import org.popolesama.leaguecraft.player.LeagueShop;
import org.popolesama.leaguecraft.player.LeagueShopUpgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public class MageGroundDamageItem extends Item {
  private static final int COOLDOWN_TICKS = 20 * 24;
  private static final int COLOR = 0x5F2CA8;

  public MageGroundDamageItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!LeaguePlayerClass.hasRole(player, LeaguePlayerClass.Role.MAGE)) {
      if (!level.isClientSide) {
        player.displayClientMessage(Component.literal("Tu dois etre Mage pour utiliser cet objet."), true);
      }
      return InteractionResultHolder.fail(stack);
    }

    if (player.getCooldowns().isOnCooldown(this)) {
      return InteractionResultHolder.fail(stack);
    }

    if (!level.isClientSide) {
      ThrownPotion potion = new ThrownPotion(level, player);
      potion.setItem(createPotionStack(player));
      potion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.65F, 1.0F);
      level.addFreshEntity(potion);
      player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
      player.displayClientMessage(Component.literal("Potion de zone lancee."), true);
      level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.PLAYERS, 0.8F, 1.0F);
    }

    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }

  private ItemStack createPotionStack(Player player) {
    int abilityPower = LeaguePlayerStats.abilityPower(player);
    ItemStack stack = new ItemStack(Items.LINGERING_POTION);
    List<MobEffectInstance> effects = new ArrayList<>();
    effects.add(new MobEffectInstance(MobEffects.HARM, 1, Math.min(2, abilityPower / 60)));
    if (LeagueShop.hasUpgrade(player, LeagueShopUpgrade.MAGE_LINGERING_POISON)) {
      effects.add(new MobEffectInstance(MobEffects.POISON, 20 * 4, 0));
    }
    if (LeagueShop.hasUpgrade(player, LeagueShopUpgrade.MAGE_LINGERING_SLOW)) {
      effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 0));
    }
    stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(COLOR), effects));
    return stack;
  }
}
