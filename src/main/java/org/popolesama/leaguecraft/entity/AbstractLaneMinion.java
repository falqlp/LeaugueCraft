package org.popolesama.leaguecraft.entity;

import org.popolesama.leaguecraft.block.LeagueStructureBlockEntity;

import org.popolesama.leaguecraft.block.LaneBlock;

import org.popolesama.leaguecraft.LeagueCraft;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractLaneMinion extends LeagueMonster {
  private static final int LANE_SEARCH_RADIUS = 10;
  private static final int LANE_STEP_SEARCH = 6;
  private static final double LANE_RAIL_DISTANCE_SQR = 2.5D * 2.5D;
  private static final double LANE_FORWARD_SPEED = 0.18D;
  private static final double LANE_CENTERING_SPEED = 0.12D;

  private int structureAttackCooldown;

  protected AbstractLaneMinion(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
    super(entityType, level, profile);
  }

  @Override
  public void tick() {
    super.tick();

    if (!level().isClientSide) {
      tickLaneMinion();
    }
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("LeagueCraftStructureAttackCooldown", structureAttackCooldown);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    structureAttackCooldown = compound.getInt("LeagueCraftStructureAttackCooldown");
  }

  protected void tickLaneMinion() {
    if (structureAttackCooldown > 0) {
      structureAttackCooldown--;
    }

    LivingEntity currentTarget = getTarget();
    if (currentTarget instanceof LeagueMonster otherMinion && profile().isEnemyOf(otherMinion.profile())) {
      if (trySpecialAttack(otherMinion)) {
        setTarget(null);
      }
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

    Optional<LeagueMonster> enemyMinion = findEnemyLaneMinion();
    if (enemyMinion.isPresent()) {
      LeagueMonster enemy = enemyMinion.get();
      if (!trySpecialAttack(enemy)) {
        setTarget(enemy);
      }
      return;
    }

    followLane();
  }

  protected Optional<LeagueMonster> findEnemyLaneMinion() {
    AABB searchArea = getBoundingBox().inflate(12.0D, 4.0D, 12.0D);
    List<LeagueMonster> enemies = level().getEntitiesOfClass(
        LeagueMonster.class,
        searchArea,
        candidate -> candidate != this
            && candidate.isAlive()
            && profile().isEnemyOf(candidate.profile())
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

  protected Optional<LeagueStructureBlockEntity> findEnemyStructure() {
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

  protected void attackStructure(LeagueStructureBlockEntity structure) {
    BlockPos pos = structure.getBlockPos();
    double targetX = pos.getX() + 0.5D;
    double targetY = pos.getY() + 0.5D;
    double targetZ = pos.getZ() + 0.5D;
    double attackRangeSqr = structureAttackRangeSqr();

    if (distanceToSqr(targetX, targetY, targetZ) > attackRangeSqr) {
      getNavigation().moveTo(targetX, targetY, targetZ, 1.0D);
      return;
    }

    getNavigation().stop();
    if (structureAttackCooldown == 0) {
      structure.hurtStructure((float) profile().attackDamage());
      structureAttackCooldown = 20;
    }
  }

  protected boolean trySpecialAttack(LeagueMonster enemy) {
    return false;
  }

  protected double structureAttackRangeSqr() {
    return 4.0D;
  }

  protected void followLane() {
    Optional<BlockPos> lanePos = findNearestLaneBlock();
    if (lanePos.isEmpty()) {
      getNavigation().stop();
      return;
    }

    BlockState laneState = level().getBlockState(lanePos.get());
    Direction direction = laneState.getValue(LaneBlock.FACING);
    if (team() == Team.RED) {
      direction = direction.getOpposite();
    }

    BlockPos destination = findNextLaneBlock(lanePos.get(), direction).orElse(lanePos.get().relative(direction, 4));
    Vec3 destinationCenter = Vec3.atBottomCenterOf(destination.above());

    if (distanceToSqr(Vec3.atBottomCenterOf(lanePos.get().above())) > LANE_RAIL_DISTANCE_SQR) {
      getNavigation().moveTo(destinationCenter.x, destinationCenter.y, destinationCenter.z, 1.0D);
      return;
    }

    moveStraightAlongLane(lanePos.get(), direction, destination);
  }

  protected void moveStraightAlongLane(BlockPos lanePos, Direction direction, BlockPos destination) {
    getNavigation().stop();

    double targetX = destination.getX() + 0.5D;
    double targetY = destination.getY() + 1.0D;
    double targetZ = destination.getZ() + 0.5D;
    double centerCorrectionX = 0.0D;
    double centerCorrectionZ = 0.0D;

    if (direction.getAxis() == Direction.Axis.X) {
      targetZ = lanePos.getZ() + 0.5D;
      centerCorrectionZ = clamp((targetZ - getZ()) * LANE_CENTERING_SPEED, -LANE_CENTERING_SPEED, LANE_CENTERING_SPEED);
    } else {
      targetX = lanePos.getX() + 0.5D;
      centerCorrectionX = clamp((targetX - getX()) * LANE_CENTERING_SPEED, -LANE_CENTERING_SPEED, LANE_CENTERING_SPEED);
    }

    getMoveControl().setWantedPosition(targetX, targetY, targetZ, 1.0D);
    setDeltaMovement(
        direction.getStepX() * LANE_FORWARD_SPEED + centerCorrectionX,
        getDeltaMovement().y,
        direction.getStepZ() * LANE_FORWARD_SPEED + centerCorrectionZ);

    setYRot(directionToYRot(direction));
    yHeadRot = getYRot();
    yBodyRot = getYRot();
  }

  protected static double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  private static float directionToYRot(Direction direction) {
    return switch (direction) {
      case SOUTH -> 0.0F;
      case WEST -> 90.0F;
      case NORTH -> 180.0F;
      case EAST -> 270.0F;
      default -> 0.0F;
    };
  }

  protected Optional<BlockPos> findNearestLaneBlock() {
    BlockPos center = blockPosition();
    BlockPos closest = null;
    double closestDistance = Double.MAX_VALUE;

    for (BlockPos pos : BlockPos.betweenClosed(
        center.offset(-LANE_SEARCH_RADIUS, -2, -LANE_SEARCH_RADIUS),
        center.offset(LANE_SEARCH_RADIUS, 2, LANE_SEARCH_RADIUS))) {
      if (level().getBlockState(pos).is(LeagueCraft.LANE_BLOCK.get())) {
        double distance = center.distSqr(pos);
        if (distance < closestDistance) {
          closest = pos.immutable();
          closestDistance = distance;
        }
      }
    }

    return Optional.ofNullable(closest);
  }

  protected Optional<BlockPos> findNextLaneBlock(BlockPos current, Direction direction) {
    for (int distance = 1; distance <= LANE_STEP_SEARCH; distance++) {
      BlockPos candidate = current.relative(direction, distance);
      if (level().getBlockState(candidate).is(LeagueCraft.LANE_BLOCK.get())) {
        return Optional.of(candidate);
      }
    }

    return Optional.empty();
  }
}
