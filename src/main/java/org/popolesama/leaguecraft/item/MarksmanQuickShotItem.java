package org.popolesama.leaguecraft.item;

import org.popolesama.leaguecraft.player.LeaguePlayerClass;
import org.popolesama.leaguecraft.player.LeaguePlayerStats;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MarksmanQuickShotItem extends Item {
  private static final int COOLDOWN_TICKS = 50;
  private static final float VELOCITY = 3.2F;
  private static final double BASE_DAMAGE = 5.0D;
  private static final double AD_RATIO = 0.55D;

  public MarksmanQuickShotItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!LeaguePlayerClass.hasRole(player, LeaguePlayerClass.Role.MARKSMAN)) {
      if (!level.isClientSide) {
        player.displayClientMessage(Component.literal("Tu dois etre Tireur pour utiliser cet objet."), true);
      }
      return InteractionResultHolder.fail(stack);
    }

    if (player.getCooldowns().isOnCooldown(this)) {
      return InteractionResultHolder.fail(stack);
    }

    if (!level.isClientSide) {
      Arrow arrow = new Arrow(level, player, new ItemStack(Items.ARROW), stack);
      arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
      arrow.setBaseDamage(BASE_DAMAGE + LeaguePlayerStats.attackDamage(player) * AD_RATIO);
      arrow.setCritArrow(true);
      arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, VELOCITY, 0.0F);
      level.addFreshEntity(arrow);
      player.getCooldowns().addCooldown(this, LeaguePlayerStats.cooldownWithAttackSpeed(player, COOLDOWN_TICKS));
      level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.15F);
    }

    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }
}
