package org.popolesama.leaguecraft.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public abstract class AbstractCannonMinion extends AbstractRangedLaneMinion {
  private static final double ATTACK_RANGE_SQR = 10.0D * 10.0D;

  protected AbstractCannonMinion(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
    super(entityType, level, profile);
  }

  @Override
  protected double rangedAttackRangeSqr() {
    return ATTACK_RANGE_SQR;
  }

  @Override
  protected int rangedAttackCooldownTicks() {
    return 36;
  }

  @Override
  protected ParticleOptions rangedAttackParticle() {
    return ParticleTypes.SMOKE;
  }

  @Override
  protected LeagueProjectile.Kind projectileKind() {
    return LeagueProjectile.Kind.CANNON;
  }
}
