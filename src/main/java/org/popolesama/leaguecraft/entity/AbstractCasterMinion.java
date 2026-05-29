package org.popolesama.leaguecraft.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public abstract class AbstractCasterMinion extends AbstractRangedLaneMinion {
  private static final double ATTACK_RANGE_SQR = 8.0D * 8.0D;

  protected AbstractCasterMinion(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
    super(entityType, level, profile);
  }

  @Override
  protected double rangedAttackRangeSqr() {
    return ATTACK_RANGE_SQR;
  }

  @Override
  protected int rangedAttackCooldownTicks() {
    return 24;
  }

  @Override
  protected ParticleOptions rangedAttackParticle() {
    return ParticleTypes.ENCHANT;
  }

  @Override
  protected LeagueProjectile.Kind projectileKind() {
    return LeagueProjectile.Kind.CASTER;
  }
}
