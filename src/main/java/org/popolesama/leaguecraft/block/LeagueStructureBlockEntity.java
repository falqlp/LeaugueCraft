package org.popolesama.leaguecraft.block;

import org.popolesama.leaguecraft.entity.LeagueMonster;
import org.popolesama.leaguecraft.entity.LeagueProjectile;
import org.popolesama.leaguecraft.player.LeaguePlayerTeams;

import org.popolesama.leaguecraft.LeagueCraft;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class LeagueStructureBlockEntity extends BlockEntity {
  private static final int PLAYER_FOCUS_TICKS = 120;

  private final LeagueStructureKind kind;
  private final LeagueMonster.Team team;
  @Setter(AccessLevel.PRIVATE)
  private float health;
  @Setter(AccessLevel.PRIVATE)
  private int attackCooldown;
  private UUID focusedPlayerId;
  private int focusedPlayerTicks;

  public LeagueStructureBlockEntity(BlockPos pos, BlockState blockState) {
    this(
        pos,
        blockState,
        blockState.getBlock() instanceof LeagueStructureBlock structureBlock ? structureBlock.kind() : LeagueStructureKind.NEXUS,
        blockState.getBlock() instanceof LeagueStructureBlock structureBlock ? structureBlock.team() : LeagueMonster.Team.NEUTRAL);
  }

  public LeagueStructureBlockEntity(BlockPos pos, BlockState blockState, LeagueStructureKind kind, LeagueMonster.Team team) {
    super(LeagueCraft.LEAGUE_STRUCTURE_BLOCK_ENTITY.get(), pos, blockState);
    this.kind = kind;
    this.team = team;
    this.health = kind.maxHealth();
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state, LeagueStructureBlockEntity blockEntity) {
    if (blockEntity.kind != LeagueStructureKind.TURRET || blockEntity.team == LeagueMonster.Team.NEUTRAL) {
      return;
    }

    blockEntity.tickFocusedPlayer();

    if (blockEntity.attackCooldown > 0) {
      blockEntity.attackCooldown--;
      return;
    }

    Optional<? extends LivingEntity> target = blockEntity.findTurretTarget(level, pos);
    target.ifPresent(enemy -> {
      LeagueProjectile.shootFromStructure(level, pos, enemy, blockEntity.kind.attackDamage());
      blockEntity.attackCooldown = 20;
    });
  }

  public boolean isEnemyOf(LeagueMonster monster) {
    return monster.isEnemyOfTeam(team);
  }

  public boolean canFocusPlayer(ServerPlayer player) {
    return kind == LeagueStructureKind.TURRET
        && LeaguePlayerTeams.isEnemyOfTeam(player, team)
        && isInAttackRange(player);
  }

  public void focusPlayer(ServerPlayer player) {
    focusedPlayerId = player.getUUID();
    focusedPlayerTicks = PLAYER_FOCUS_TICKS;
    setChanged();
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

  public float maxHealth() {
    return kind.maxHealth();
  }

  private Optional<? extends LivingEntity> findTurretTarget(Level level, BlockPos pos) {
    Optional<ServerPlayer> focusedPlayer = findFocusedPlayer(level);
    if (focusedPlayer.isPresent()) {
      return focusedPlayer;
    }

    double range = kind.attackRange();
    AABB area = new AABB(pos).inflate(range, 4.0D, range);
    return level.getEntitiesOfClass(
            LeagueMonster.class,
            area,
            monster -> monster.isAlive() && monster.isEnemyOfTeam(team))
        .stream()
        .min(Comparator.comparingDouble(monster -> monster.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)));
  }

  private Optional<ServerPlayer> findFocusedPlayer(Level level) {
    if (focusedPlayerId == null || focusedPlayerTicks <= 0 || !(level instanceof ServerLevel serverLevel)) {
      return Optional.empty();
    }

    ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(focusedPlayerId);
    if (player == null || !player.isAlive() || !canFocusPlayer(player)) {
      clearFocusedPlayer();
      return Optional.empty();
    }

    return Optional.of(player);
  }

  private boolean isInAttackRange(LivingEntity target) {
    double range = kind.attackRange();
    return target.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= range * range;
  }

  private void tickFocusedPlayer() {
    if (focusedPlayerTicks > 0) {
      focusedPlayerTicks--;
      if (focusedPlayerTicks == 0) {
        focusedPlayerId = null;
      }
    }
  }

  private void clearFocusedPlayer() {
    focusedPlayerId = null;
    focusedPlayerTicks = 0;
    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putFloat("LeagueCraftHealth", health);
    tag.putInt("LeagueCraftAttackCooldown", attackCooldown);
    tag.putInt("LeagueCraftFocusedPlayerTicks", focusedPlayerTicks);
    if (focusedPlayerId != null) {
      tag.putUUID("LeagueCraftFocusedPlayerId", focusedPlayerId);
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    health = tag.contains("LeagueCraftHealth") ? tag.getFloat("LeagueCraftHealth") : kind.maxHealth();
    attackCooldown = tag.getInt("LeagueCraftAttackCooldown");
    focusedPlayerTicks = tag.getInt("LeagueCraftFocusedPlayerTicks");
    focusedPlayerId = tag.hasUUID("LeagueCraftFocusedPlayerId") ? tag.getUUID("LeagueCraftFocusedPlayerId") : null;
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
