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

public class ClassPotionItem extends Item {
  private final LeaguePlayerClass.Role role;
  private final SpellEffect spellEffect;
  private final int cooldownTicks;
  private final String message;

  public ClassPotionItem(Properties properties, LeaguePlayerClass.Role role, SpellEffect spellEffect, int cooldownTicks, String message) {
    super(properties.stacksTo(1));
    this.role = role;
    this.spellEffect = spellEffect;
    this.cooldownTicks = cooldownTicks;
    this.message = message;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!LeaguePlayerClass.hasRole(player, role)) {
      if (!level.isClientSide) {
        player.displayClientMessage(Component.literal("Tu dois etre " + role.displayName() + " pour utiliser cet objet."), true);
      }
      return InteractionResultHolder.fail(stack);
    }

    if (player.getCooldowns().isOnCooldown(this)) {
      return InteractionResultHolder.fail(stack);
    }

    if (!level.isClientSide) {
      ItemStack potionStack = createPotionStack(player);
      ThrownPotion thrownPotion = new ThrownPotion(level, player);
      thrownPotion.setItem(potionStack);
      thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.75F, 1.0F);
      level.addFreshEntity(thrownPotion);
      player.getCooldowns().addCooldown(this, effectiveCooldown(player));
      player.displayClientMessage(Component.literal(message), true);
      level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.8F, 1.0F);
    }

    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }

  private ItemStack createPotionStack(Player player) {
    int abilityPower = LeaguePlayerStats.abilityPower(player);
    ItemStack stack = new ItemStack(Items.SPLASH_POTION);
    MobEffectInstance effect = switch (spellEffect) {
      case DAMAGE -> new MobEffectInstance(MobEffects.HARM, 1, Math.min(2, abilityPower / 60));
      case HEAL -> new MobEffectInstance(MobEffects.HEAL, 1, Math.min(2, abilityPower / 60));
      case SLOW -> new MobEffectInstance(
          MobEffects.MOVEMENT_SLOWDOWN,
          slowDuration(player, abilityPower),
          abilityPower >= 80 ? 1 : 0);
    };
    List<MobEffectInstance> effects = new ArrayList<>();
    effects.add(effect);
    if (role == LeaguePlayerClass.Role.MAGE && spellEffect == SpellEffect.DAMAGE) {
      if (LeagueShop.hasUpgrade(player, LeagueShopUpgrade.MAGE_LINGERING_POISON)) {
        effects.add(new MobEffectInstance(MobEffects.POISON, 20 * 4, 0));
      }
      if (LeagueShop.hasUpgrade(player, LeagueShopUpgrade.MAGE_LINGERING_SLOW)) {
        effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 0));
      }
    }
    int color = switch (spellEffect) {
      case DAMAGE -> 0x7D2AC8;
      case HEAL -> 0xF25FCC;
      case SLOW -> 0x55B7D8;
    };
    stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), effects));
    return stack;
  }

  private int slowDuration(Player player, int abilityPower) {
    int duration = Math.min(20 * 8, 20 * 3 + abilityPower);
    if (role == LeaguePlayerClass.Role.SUPPORT && LeagueShop.hasUpgrade(player, LeagueShopUpgrade.SUPPORT_SLOW_MASTERY)) {
      duration += 20 * 3;
    }
    return duration;
  }

  private int effectiveCooldown(Player player) {
    if (role == LeaguePlayerClass.Role.SUPPORT && LeagueShop.hasUpgrade(player, LeagueShopUpgrade.SUPPORT_COOLDOWN)) {
      return cooldownTicks * 75 / 100;
    }
    return cooldownTicks;
  }

  public enum SpellEffect {
    DAMAGE,
    HEAL,
    SLOW
  }
}
