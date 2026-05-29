package org.popolesama.leaguecraft.entity;

import org.popolesama.leaguecraft.player.LeagueGold;

import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public abstract class LeagueMonster extends Zombie {
  @Getter
  @Accessors(fluent = true)
  private final Profile profile;

  protected LeagueMonster(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
    super(entityType, level);
    this.profile = profile;
    setCanBreakDoors(false);
  }

  @Override
  public boolean isBaby() {
    return false;
  }

  @Override
  protected boolean isSunSensitive() {
    return false;
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return null;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.GENERIC_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.GENERIC_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
  }

  @Override
  public void die(DamageSource damageSource) {
    if (!level().isClientSide) {
      Entity killer = damageSource.getEntity();
      if (killer instanceof ServerPlayer player) {
        LeagueGold.addGold(player, goldReward());
      }
    }

    super.die(damageSource);
  }

  protected int goldReward() {
    return 0;
  }

  @Override
  public Component getTypeName() {
    return Component.translatable(profile.translationKey());
  }

  public boolean isEnemyOfTeam(Team otherTeam) {
    return profile.isLaneMinion() && profile.team() != Team.NEUTRAL && otherTeam != Team.NEUTRAL && profile.team() != otherTeam;
  }

  public Team team() {
    return profile.team();
  }

  @Getter
  @Accessors(fluent = true)
  @RequiredArgsConstructor
  public enum Profile {
    MINION("entity.leaguecraft.minion", 20.0, 3.0, 0.24, 24.0, true, Team.NEUTRAL),
    BLUE_MINION("entity.leaguecraft.blue_minion", 20.0, 3.0, 0.24, 24.0, true, Team.BLUE),
    RED_MINION("entity.leaguecraft.red_minion", 20.0, 3.0, 0.24, 24.0, true, Team.RED),
    BLUE_CASTER_MINION("entity.leaguecraft.blue_caster_minion", 16.0, 2.0, 0.25, 30.0, true, Team.BLUE),
    RED_CASTER_MINION("entity.leaguecraft.red_caster_minion", 16.0, 2.0, 0.25, 30.0, true, Team.RED),
    BLUE_CANNON_MINION("entity.leaguecraft.blue_cannon_minion", 42.0, 5.0, 0.20, 26.0, true, Team.BLUE),
    RED_CANNON_MINION("entity.leaguecraft.red_cannon_minion", 42.0, 5.0, 0.20, 26.0, true, Team.RED),
    BLUE_SUPER_MINION("entity.leaguecraft.blue_super_minion", 80.0, 8.0, 0.18, 28.0, true, Team.BLUE),
    RED_SUPER_MINION("entity.leaguecraft.red_super_minion", 80.0, 8.0, 0.18, 28.0, true, Team.RED),
    BLUE_BUFF("entity.leaguecraft.blue_buff", 92.0, 8.0, 0.18, 26.0, false, Team.NEUTRAL),
    RED_BUFF("entity.leaguecraft.red_buff", 92.0, 8.0, 0.18, 26.0, false, Team.NEUTRAL),
    VOID_GRUB("entity.leaguecraft.void_grub", 48.0, 6.0, 0.22, 24.0, false, Team.NEUTRAL),
    RIFT_HERALD("entity.leaguecraft.rift_herald", 180.0, 13.0, 0.20, 36.0, false, Team.NEUTRAL),
    GROMP("entity.leaguecraft.gromp", 34.0, 5.0, 0.20, 20.0, false, Team.NEUTRAL),
    KRUG("entity.leaguecraft.krug", 42.0, 6.0, 0.18, 18.0, false, Team.NEUTRAL),
    RAPTOR("entity.leaguecraft.raptor", 26.0, 4.0, 0.30, 24.0, false, Team.NEUTRAL),
    DRAGON("entity.leaguecraft.dragon", 120.0, 10.0, 0.26, 32.0, false, Team.NEUTRAL),
    BARON_NASHOR("entity.leaguecraft.baron_nashor", 220.0, 14.0, 0.22, 40.0, false, Team.NEUTRAL);

    private final String translationKey;
    private final double maxHealth;
    private final double attackDamage;
    private final double movementSpeed;
    private final double followRange;
    private final boolean laneMinion;
    private final Team team;

    public boolean isEnemyOf(Profile other) {
      return isLaneMinion() && other.isLaneMinion() && team != Team.NEUTRAL && other.team != Team.NEUTRAL && team != other.team;
    }

    public boolean isLaneMinion() {
      return laneMinion;
    }
  }

  public enum Team {
    NEUTRAL,
    BLUE,
    RED
  }
}
