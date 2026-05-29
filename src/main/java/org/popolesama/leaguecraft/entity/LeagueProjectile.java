package org.popolesama.leaguecraft.entity;

import org.popolesama.leaguecraft.LeagueCraft;
import org.popolesama.leaguecraft.block.LeagueStructureBlockEntity;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeagueProjectile extends Entity {
  private static final EntityDataAccessor<Integer> PROJECTILE_KIND = SynchedEntityData.defineId(LeagueProjectile.class, EntityDataSerializers.INT);
  private static final double HIT_DISTANCE_SQR = 0.35D * 0.35D;
  private static final int MAX_LIFE_TICKS = 80;

  private int ownerId = -1;
  private int targetEntityId = -1;
  private BlockPos targetBlockPos;
  private float damage;
  private int lifeTicks;
  private double targetX;
  private double targetY;
  private double targetZ;

  public LeagueProjectile(EntityType<? extends LeagueProjectile> entityType, Level level) {
    super(entityType, level);
    setNoGravity(true);
  }

  public static void shootAtEntity(Level level, Entity owner, LeagueMonster target, float damage, Kind kind) {
    Vec3 start = owner.position().add(0.0D, owner.getBbHeight() * 0.65D, 0.0D);
    Vec3 end = target.position().add(0.0D, target.getBbHeight() * 0.55D, 0.0D);
    LeagueProjectile projectile = create(level, owner, damage, kind, start, end, 0.62D);
    projectile.targetEntityId = target.getId();
    level.addFreshEntity(projectile);
  }

  public static void shootAtStructure(Level level, Entity owner, BlockPos target, float damage, Kind kind) {
    Vec3 start = owner.position().add(0.0D, owner.getBbHeight() * 0.65D, 0.0D);
    Vec3 end = Vec3.atCenterOf(target);
    LeagueProjectile projectile = create(level, owner, damage, kind, start, end, 0.62D);
    projectile.targetBlockPos = target.immutable();
    level.addFreshEntity(projectile);
  }

  public static void shootFromStructure(Level level, BlockPos source, LivingEntity target, float damage) {
    Vec3 start = Vec3.atCenterOf(source).add(0.0D, 4.25D, 0.0D);
    Vec3 end = target.position().add(0.0D, target.getBbHeight() * 0.55D, 0.0D);
    LeagueProjectile projectile = create(level, null, damage, Kind.TURRET, start, end, 0.8D);
    projectile.targetEntityId = target.getId();
    level.addFreshEntity(projectile);
  }

  private static LeagueProjectile create(Level level, Entity owner, float damage, Kind kind, Vec3 start, Vec3 end, double speed) {
    LeagueProjectile projectile = new LeagueProjectile(LeagueCraft.LEAGUE_PROJECTILE.get(), level);
    projectile.setPos(start.x, start.y, start.z);
    projectile.ownerId = owner == null ? -1 : owner.getId();
    projectile.damage = damage;
    projectile.setKind(kind);
    projectile.setTarget(end);
    projectile.setDeltaMovement(end.subtract(start).normalize().scale(speed));
    return projectile;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    builder.define(PROJECTILE_KIND, Kind.CASTER.ordinal());
  }

  @Override
  public void tick() {
    super.tick();

    lifeTicks++;
    if (lifeTicks > MAX_LIFE_TICKS) {
      discard();
      return;
    }

    updateTrackedTarget();
    Vec3 target = new Vec3(targetX, targetY, targetZ);
    Vec3 toTarget = target.subtract(position());
    if (toTarget.lengthSqr() <= HIT_DISTANCE_SQR) {
      hitTarget();
      return;
    }

    setDeltaMovement(toTarget.normalize().scale(getDeltaMovement().length()));
    move(net.minecraft.world.entity.MoverType.SELF, getDeltaMovement());
    spawnTrailParticle();
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    ownerId = compound.getInt("LeagueCraftOwnerId");
    targetEntityId = compound.getInt("LeagueCraftTargetEntityId");
    damage = compound.getFloat("LeagueCraftDamage");
    lifeTicks = compound.getInt("LeagueCraftLifeTicks");
    targetX = compound.getDouble("LeagueCraftTargetX");
    targetY = compound.getDouble("LeagueCraftTargetY");
    targetZ = compound.getDouble("LeagueCraftTargetZ");
    setKind(Kind.byId(compound.getInt("LeagueCraftProjectileKind")));
    if (compound.contains("LeagueCraftTargetBlockX")) {
      targetBlockPos = new BlockPos(
          compound.getInt("LeagueCraftTargetBlockX"),
          compound.getInt("LeagueCraftTargetBlockY"),
          compound.getInt("LeagueCraftTargetBlockZ"));
    }
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    compound.putInt("LeagueCraftOwnerId", ownerId);
    compound.putInt("LeagueCraftTargetEntityId", targetEntityId);
    compound.putFloat("LeagueCraftDamage", damage);
    compound.putInt("LeagueCraftLifeTicks", lifeTicks);
    compound.putDouble("LeagueCraftTargetX", targetX);
    compound.putDouble("LeagueCraftTargetY", targetY);
    compound.putDouble("LeagueCraftTargetZ", targetZ);
    compound.putInt("LeagueCraftProjectileKind", kind().ordinal());
    if (targetBlockPos != null) {
      compound.putInt("LeagueCraftTargetBlockX", targetBlockPos.getX());
      compound.putInt("LeagueCraftTargetBlockY", targetBlockPos.getY());
      compound.putInt("LeagueCraftTargetBlockZ", targetBlockPos.getZ());
    }
  }

  private void setKind(Kind kind) {
    entityData.set(PROJECTILE_KIND, kind.ordinal());
  }

  private Kind kind() {
    return Kind.byId(entityData.get(PROJECTILE_KIND));
  }

  private void setTarget(Vec3 target) {
    targetX = target.x;
    targetY = target.y;
    targetZ = target.z;
  }

  private void updateTrackedTarget() {
    if (targetEntityId == -1) {
      return;
    }

    Entity target = level().getEntity(targetEntityId);
    if (target instanceof LivingEntity livingTarget && livingTarget.isAlive()) {
      setTarget(livingTarget.position().add(0.0D, livingTarget.getBbHeight() * 0.55D, 0.0D));
    }
  }

  private void hitTarget() {
    if (!level().isClientSide) {
      if (targetEntityId != -1 && level().getEntity(targetEntityId) instanceof LivingEntity livingTarget && livingTarget.isAlive()) {
        livingTarget.hurt(damageSource(), damage);
      } else if (targetBlockPos != null && level().getBlockEntity(targetBlockPos) instanceof LeagueStructureBlockEntity structure) {
        structure.hurtStructure(damage);
      }
    }

    discard();
  }

  private DamageSource damageSource() {
    Entity owner = ownerId == -1 ? null : level().getEntity(ownerId);
    if (owner instanceof LivingEntity livingOwner) {
      return level().damageSources().mobAttack(livingOwner);
    }

    return level().damageSources().magic();
  }

  private void spawnTrailParticle() {
    ParticleOptions particle = switch (kind()) {
      case CASTER -> ParticleTypes.ENCHANT;
      case CANNON -> ParticleTypes.SMOKE;
      case TURRET -> ParticleTypes.CRIT;
    };

    level().addParticle(particle, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
  }

  public enum Kind {
    CASTER,
    CANNON,
    TURRET;

    private static Kind byId(int id) {
      Kind[] values = values();
      if (id < 0 || id >= values.length) {
        return CASTER;
      }

      return values[id];
    }
  }
}
