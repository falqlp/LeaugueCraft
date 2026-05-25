package org.popolesama.test;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public abstract class AbstractJungleMonster extends LeagueMonster {
  private static final double LEASH_RADIUS = 14.0;
  private static final double LEASH_RADIUS_SQR = LEASH_RADIUS * LEASH_RADIUS;
  private static final double HOME_RADIUS_SQR = 2.0 * 2.0;

  private double campX;
  private double campY;
  private double campZ;
  private boolean hasCamp;
  private boolean provoked;
  private boolean resetting;

  protected AbstractJungleMonster(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
    super(entityType, level, profile);
  }

  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
    SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    markCampPosition();
    applyJungleIdleState();
    return data;
  }

  @Override
  public void tick() {
    super.tick();

    if (!level().isClientSide) {
      if (!hasCamp) {
        markCampPosition();
      }

      if (resetting) {
        tickResetToCamp();
      } else if (provoked && distanceToCampSqr() > LEASH_RADIUS_SQR) {
        startResetToCamp();
      } else if (!provoked) {
        applyJungleIdleState();
      }
    }
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    boolean hurt = super.hurt(source, amount);

    if (hurt && !level().isClientSide && !resetting) {
      Entity attacker = source.getEntity();
      if (attacker instanceof LivingEntity livingAttacker) {
        provoke(livingAttacker);
      }
    }

    return hurt;
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (resetting) {
      return false;
    }

    return super.doHurtTarget(target);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putDouble("LeagueCraftCampX", campX);
    compound.putDouble("LeagueCraftCampY", campY);
    compound.putDouble("LeagueCraftCampZ", campZ);
    compound.putBoolean("LeagueCraftHasCamp", hasCamp);
    compound.putBoolean("LeagueCraftProvoked", provoked);
    compound.putBoolean("LeagueCraftResetting", resetting);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    campX = compound.getDouble("LeagueCraftCampX");
    campY = compound.getDouble("LeagueCraftCampY");
    campZ = compound.getDouble("LeagueCraftCampZ");
    hasCamp = compound.getBoolean("LeagueCraftHasCamp");
    provoked = compound.getBoolean("LeagueCraftProvoked");
    resetting = compound.getBoolean("LeagueCraftResetting");
    applyJungleIdleState();
  }

  private void markCampPosition() {
    campX = getX();
    campY = getY();
    campZ = getZ();
    hasCamp = true;
  }

  private void applyJungleIdleState() {
    if (!provoked && !resetting) {
      setTarget(null);
      getNavigation().stop();
      setDeltaMovement(0.0, getDeltaMovement().y, 0.0);
      setNoAi(true);
    }
  }

  private void provoke(LivingEntity attacker) {
    provoked = true;
    resetting = false;
    setNoAi(false);
    setTarget(attacker);
  }

  private void startResetToCamp() {
    provoked = false;
    resetting = true;
    setTarget(null);
    setNoAi(false);
    heal(getMaxHealth());
    getNavigation().moveTo(campX, campY, campZ, 1.15D);
  }

  private void tickResetToCamp() {
    setTarget(null);

    if (distanceToCampSqr() > LEASH_RADIUS_SQR * 4.0) {
      teleportTo(campX, campY, campZ);
    } else if (getNavigation().isDone()) {
      getNavigation().moveTo(campX, campY, campZ, 1.15D);
    }

    if (distanceToCampSqr() <= HOME_RADIUS_SQR) {
      resetting = false;
      setPos(campX, campY, campZ);
      applyJungleIdleState();
    }
  }

  private double distanceToCampSqr() {
    double dx = getX() - campX;
    double dy = getY() - campY;
    double dz = getZ() - campZ;
    return dx * dx + dy * dy + dz * dz;
  }
}
