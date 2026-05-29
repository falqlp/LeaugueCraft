package org.popolesama.leaguecraft.entity;

import org.popolesama.leaguecraft.block.LeagueStructureBlockEntity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public abstract class AbstractRangedLaneMinion extends AbstractLaneMinion {
  private int rangedAttackCooldown;

  protected AbstractRangedLaneMinion(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
    super(entityType, level, profile);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("LeagueCraftRangedAttackCooldown", rangedAttackCooldown);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    rangedAttackCooldown = compound.getInt("LeagueCraftRangedAttackCooldown");
  }

  @Override
  protected void tickLaneMinion() {
    if (rangedAttackCooldown > 0) {
      rangedAttackCooldown--;
    }

    super.tickLaneMinion();
  }

  @Override
  protected boolean trySpecialAttack(LeagueMonster enemy) {
    if (distanceToSqr(enemy) > rangedAttackRangeSqr() || !hasLineOfSight(enemy)) {
      return false;
    }

    getNavigation().stop();
    lookAt(enemy, 30.0F, 30.0F);

    if (rangedAttackCooldown == 0) {
      LeagueProjectile.shootAtEntity(level(), this, enemy, (float) profile().attackDamage(), projectileKind());
      rangedAttackCooldown = rangedAttackCooldownTicks();
    }

    return true;
  }

  @Override
  protected void attackStructure(LeagueStructureBlockEntity structure) {
    BlockPos pos = structure.getBlockPos();
    double targetX = pos.getX() + 0.5D;
    double targetY = pos.getY() + 0.5D;
    double targetZ = pos.getZ() + 0.5D;

    if (distanceToSqr(targetX, targetY, targetZ) > structureAttackRangeSqr()) {
      getNavigation().moveTo(targetX, targetY, targetZ, 1.0D);
      return;
    }

    getNavigation().stop();
    if (rangedAttackCooldown == 0) {
      LeagueProjectile.shootAtStructure(level(), this, pos, (float) profile().attackDamage(), projectileKind());
      rangedAttackCooldown = rangedAttackCooldownTicks();
    }
  }

  @Override
  protected double structureAttackRangeSqr() {
    return rangedAttackRangeSqr();
  }

  protected abstract double rangedAttackRangeSqr();

  protected abstract int rangedAttackCooldownTicks();

  protected abstract ParticleOptions rangedAttackParticle();

  protected abstract LeagueProjectile.Kind projectileKind();
}
