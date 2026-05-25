package org.popolesama.test;

import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LeagueMonster extends Zombie {
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
  public Component getTypeName() {
    return Component.translatable(profile.translationKey());
  }

  protected Profile profile() {
    return profile;
  }

  public boolean isEnemyOfTeam(Team otherTeam) {
    return profile.isLaneMinion() && profile.team() != Team.NEUTRAL && otherTeam != Team.NEUTRAL && profile.team() != otherTeam;
  }

  public boolean isLaneMinion() {
    return profile.isLaneMinion();
  }

  public Team team() {
    return profile.team();
  }

  public enum Profile {
    MINION("entity.leaguecraft.minion", 20.0, 3.0, 0.24, 24.0, true, Team.NEUTRAL),
    BLUE_MINION("entity.leaguecraft.blue_minion", 20.0, 3.0, 0.24, 24.0, true, Team.BLUE),
    RED_MINION("entity.leaguecraft.red_minion", 20.0, 3.0, 0.24, 24.0, true, Team.RED),
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

    Profile(String translationKey, double maxHealth, double attackDamage, double movementSpeed, double followRange, boolean laneMinion, Team team) {
      this.translationKey = translationKey;
      this.maxHealth = maxHealth;
      this.attackDamage = attackDamage;
      this.movementSpeed = movementSpeed;
      this.followRange = followRange;
      this.laneMinion = laneMinion;
      this.team = team;
    }

    public String translationKey() {
      return translationKey;
    }

    public double maxHealth() {
      return maxHealth;
    }

    public double attackDamage() {
      return attackDamage;
    }

    public double movementSpeed() {
      return movementSpeed;
    }

    public double followRange() {
      return followRange;
    }

    public boolean isLaneMinion() {
      return laneMinion;
    }

    public boolean isEnemyOf(Profile other) {
      return isLaneMinion() && other.isLaneMinion() && team != Team.NEUTRAL && other.team != Team.NEUTRAL && team != other.team;
    }

    public Team team() {
      return team;
    }
  }

  public enum Team {
    NEUTRAL,
    BLUE,
    RED
  }
}
