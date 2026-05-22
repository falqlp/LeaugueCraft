package org.popolesama.test;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LeagueMonster extends Zombie {
  private static final double LEASH_RADIUS = 14.0;
  private static final double LEASH_RADIUS_SQR = LEASH_RADIUS * LEASH_RADIUS;
  private static final double HOME_RADIUS_SQR = 2.0 * 2.0;
  private static final int LANE_SEARCH_RADIUS = 10;
  private static final int LANE_STEP_SEARCH = 6;

  private final Profile profile;
  private double campX;
  private double campY;
  private double campZ;
  private boolean hasCamp;
  private boolean provoked;
  private boolean resetting;
  private int structureAttackCooldown;

  public LeagueMonster(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
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
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
    SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    markCampPosition();
    applyJungleIdleState();
    return data;
  }

  @Override
  public void tick() {
    super.tick();

    if (!level().isClientSide && profile.isJungleMonster()) {
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

    if (!level().isClientSide && profile.isLaneMinion()) {
      tickLaneMinion();
    }
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    boolean hurt = super.hurt(source, amount);

    if (hurt && !level().isClientSide && profile.isJungleMonster() && !resetting) {
      Entity attacker = source.getEntity();
      if (attacker instanceof LivingEntity livingAttacker) {
        provoke(livingAttacker);
      }
    }

    return hurt;
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (profile.isJungleMonster() && resetting) {
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
    compound.putInt("LeagueCraftStructureAttackCooldown", structureAttackCooldown);
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
    structureAttackCooldown = compound.getInt("LeagueCraftStructureAttackCooldown");
    applyJungleIdleState();
  }

  @Override
  public Component getTypeName() {
    return Component.translatable(profile.translationKey());
  }

  private void markCampPosition() {
    campX = getX();
    campY = getY();
    campZ = getZ();
    hasCamp = true;
  }

  private void applyJungleIdleState() {
    if (!profile.isJungleMonster()) {
      return;
    }

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

  private void tickLaneMinion() {
    if (structureAttackCooldown > 0) {
      structureAttackCooldown--;
    }

    LivingEntity currentTarget = getTarget();
    if (currentTarget instanceof LeagueMonster otherMinion && profile.isEnemyOf(otherMinion.profile)) {
      return;
    }

    if (currentTarget != null) {
      setTarget(null);
    }

    Optional<LeagueStructureBlockEntity> enemyStructure = findEnemyStructure();
    if (enemyStructure.isPresent()) {
      attackStructure(enemyStructure.get());
      return;
    }

    findEnemyLaneMinion().ifPresentOrElse(this::setTarget, this::followLane);
  }

  private Optional<LeagueMonster> findEnemyLaneMinion() {
    AABB searchArea = getBoundingBox().inflate(12.0D, 4.0D, 12.0D);
    List<LeagueMonster> enemies = level().getEntitiesOfClass(
        LeagueMonster.class,
        searchArea,
        candidate -> candidate != this
            && candidate.isAlive()
            && profile.isEnemyOf(candidate.profile)
            && hasLineOfSight(candidate));

    LeagueMonster closest = null;
    double closestDistance = Double.MAX_VALUE;
    for (LeagueMonster enemy : enemies) {
      double distance = distanceToSqr(enemy);
      if (distance < closestDistance) {
        closest = enemy;
        closestDistance = distance;
      }
    }

    return Optional.ofNullable(closest);
  }

  private Optional<LeagueStructureBlockEntity> findEnemyStructure() {
    BlockPos center = blockPosition();
    LeagueStructureBlockEntity closest = null;
    double closestDistance = Double.MAX_VALUE;

    for (BlockPos pos : BlockPos.betweenClosed(center.offset(-12, -2, -12), center.offset(12, 3, 12))) {
      if (level().getBlockEntity(pos) instanceof LeagueStructureBlockEntity structure && structure.isEnemyOf(this)) {
        double distance = center.distSqr(pos);
        if (distance < closestDistance) {
          closest = structure;
          closestDistance = distance;
        }
      }
    }

    return Optional.ofNullable(closest);
  }

  private void attackStructure(LeagueStructureBlockEntity structure) {
    BlockPos pos = structure.getBlockPos();
    double targetX = pos.getX() + 0.5D;
    double targetY = pos.getY() + 0.5D;
    double targetZ = pos.getZ() + 0.5D;

    if (distanceToSqr(targetX, targetY, targetZ) > 4.0D) {
      getNavigation().moveTo(targetX, targetY, targetZ, 1.0D);
      return;
    }

    getNavigation().stop();
    if (structureAttackCooldown == 0) {
      structure.hurtStructure((float) profile.attackDamage());
      structureAttackCooldown = 20;
    }
  }

  private void followLane() {
    Optional<BlockPos> lanePos = findNearestLaneBlock();
    if (lanePos.isEmpty()) {
      getNavigation().stop();
      return;
    }

    BlockState laneState = level().getBlockState(lanePos.get());
    Direction direction = laneState.getValue(LaneBlock.FACING);
    if (profile.team() == Team.RED) {
      direction = direction.getOpposite();
    }

    BlockPos destination = findNextLaneBlock(lanePos.get(), direction).orElse(lanePos.get().relative(direction, 4));
    Vec3 destinationCenter = Vec3.atBottomCenterOf(destination).add(0.0D, 0.1D, 0.0D);

    if (getNavigation().isDone() || distanceToSqr(destinationCenter) > 4.0D) {
      getNavigation().moveTo(destinationCenter.x, destinationCenter.y, destinationCenter.z, 1.0D);
    }
  }

  private Optional<BlockPos> findNearestLaneBlock() {
    BlockPos center = blockPosition();
    BlockPos closest = null;
    double closestDistance = Double.MAX_VALUE;

    for (BlockPos pos : BlockPos.betweenClosed(
        center.offset(-LANE_SEARCH_RADIUS, -2, -LANE_SEARCH_RADIUS),
        center.offset(LANE_SEARCH_RADIUS, 2, LANE_SEARCH_RADIUS))) {
      if (level().getBlockState(pos).is(Test.LANE_BLOCK.get())) {
        double distance = center.distSqr(pos);
        if (distance < closestDistance) {
          closest = pos.immutable();
          closestDistance = distance;
        }
      }
    }

    return Optional.ofNullable(closest);
  }

  private Optional<BlockPos> findNextLaneBlock(BlockPos current, Direction direction) {
    for (int distance = 1; distance <= LANE_STEP_SEARCH; distance++) {
      BlockPos candidate = current.relative(direction, distance);
      if (level().getBlockState(candidate).is(Test.LANE_BLOCK.get())) {
        return Optional.of(candidate);
      }
    }

    return Optional.empty();
  }

  public enum Profile {
    MINION("entity.leaguecraft.minion", 20.0, 3.0, 0.24, 24.0, false, Team.NEUTRAL),
    BLUE_MINION("entity.leaguecraft.blue_minion", 20.0, 3.0, 0.24, 24.0, false, Team.BLUE),
    RED_MINION("entity.leaguecraft.red_minion", 20.0, 3.0, 0.24, 24.0, false, Team.RED),
    GROMP("entity.leaguecraft.gromp", 34.0, 5.0, 0.20, 20.0, true, Team.NEUTRAL),
    KRUG("entity.leaguecraft.krug", 42.0, 6.0, 0.18, 18.0, true, Team.NEUTRAL),
    RAPTOR("entity.leaguecraft.raptor", 26.0, 4.0, 0.30, 24.0, true, Team.NEUTRAL),
    DRAGON("entity.leaguecraft.dragon", 120.0, 10.0, 0.26, 32.0, true, Team.NEUTRAL),
    BARON_NASHOR("entity.leaguecraft.baron_nashor", 220.0, 14.0, 0.22, 40.0, true, Team.NEUTRAL);

    private final String translationKey;
    private final double maxHealth;
    private final double attackDamage;
    private final double movementSpeed;
    private final double followRange;
    private final boolean jungleMonster;
    private final Team team;

    Profile(String translationKey, double maxHealth, double attackDamage, double movementSpeed, double followRange, boolean jungleMonster, Team team) {
      this.translationKey = translationKey;
      this.maxHealth = maxHealth;
      this.attackDamage = attackDamage;
      this.movementSpeed = movementSpeed;
      this.followRange = followRange;
      this.jungleMonster = jungleMonster;
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

    public boolean isJungleMonster() {
      return jungleMonster;
    }

    public boolean isLaneMinion() {
      return team == Team.BLUE || team == Team.RED;
    }

    public boolean isEnemyOf(Profile other) {
      return isLaneMinion() && other.isLaneMinion() && team != other.team;
    }

    public Team team() {
      return team;
    }
  }

  public boolean isEnemyOfTeam(Team otherTeam) {
    return profile.isLaneMinion() && otherTeam != Team.NEUTRAL && profile.team() != otherTeam;
  }

  public enum Team {
    NEUTRAL,
    BLUE,
    RED
  }
}
