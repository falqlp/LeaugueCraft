package org.popolesama.test;

import java.util.Comparator;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class LeagueStructureBlockEntity extends BlockEntity {
  private final LeagueStructureKind kind;
  private final LeagueMonster.Team team;
  private float health;
  private int attackCooldown;

  public LeagueStructureBlockEntity(BlockPos pos, BlockState blockState) {
    this(
        pos,
        blockState,
        blockState.getBlock() instanceof LeagueStructureBlock structureBlock ? structureBlock.kind() : LeagueStructureKind.NEXUS,
        blockState.getBlock() instanceof LeagueStructureBlock structureBlock ? structureBlock.team() : LeagueMonster.Team.NEUTRAL);
  }

  public LeagueStructureBlockEntity(BlockPos pos, BlockState blockState, LeagueStructureKind kind, LeagueMonster.Team team) {
    super(Test.LEAGUE_STRUCTURE_BLOCK_ENTITY.get(), pos, blockState);
    this.kind = kind;
    this.team = team;
    this.health = kind.maxHealth();
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state, LeagueStructureBlockEntity blockEntity) {
    if (blockEntity.kind != LeagueStructureKind.TURRET || blockEntity.team == LeagueMonster.Team.NEUTRAL) {
      return;
    }

    if (blockEntity.attackCooldown > 0) {
      blockEntity.attackCooldown--;
      return;
    }

    Optional<LeagueMonster> target = blockEntity.findTurretTarget(level, pos);
    target.ifPresent(enemy -> {
      enemy.hurt(level.damageSources().magic(), blockEntity.kind.attackDamage());
      blockEntity.attackCooldown = 20;
    });
  }

  public boolean isEnemyOf(LeagueMonster monster) {
    return monster.isEnemyOfTeam(team);
  }

  public void hurtStructure(float damage) {
    health -= damage;
    setChanged();

    if (level != null) {
      level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
      if (health <= 0.0F) {
        level.destroyBlock(worldPosition, false);
      }
    }
  }

  public float health() {
    return health;
  }

  public float maxHealth() {
    return kind.maxHealth();
  }

  public LeagueStructureKind kind() {
    return kind;
  }

  public LeagueMonster.Team team() {
    return team;
  }

  private Optional<LeagueMonster> findTurretTarget(Level level, BlockPos pos) {
    double range = kind.attackRange();
    AABB area = new AABB(pos).inflate(range, 4.0D, range);
    return level.getEntitiesOfClass(
            LeagueMonster.class,
            area,
            monster -> monster.isAlive() && monster.isEnemyOfTeam(team))
        .stream()
        .min(Comparator.comparingDouble(monster -> monster.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)));
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putFloat("LeagueCraftHealth", health);
    tag.putInt("LeagueCraftAttackCooldown", attackCooldown);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    health = tag.contains("LeagueCraftHealth") ? tag.getFloat("LeagueCraftHealth") : kind.maxHealth();
    attackCooldown = tag.getInt("LeagueCraftAttackCooldown");
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    return saveWithoutMetadata(registries);
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
