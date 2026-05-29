package org.popolesama.leaguecraft.block;

import org.popolesama.leaguecraft.entity.LeagueMonster;

import org.popolesama.leaguecraft.LeagueCraft;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Optional;

@Getter
@Accessors(fluent = true)
public class LeagueStructureBlock extends BaseEntityBlock {
  private static final int TURRET_RADIUS = 1;
  private static final int TURRET_BODY_HEIGHT = 4;
  private static final int TURRET_TOP_Y = 4;
  private static final int INHIBITOR_RADIUS = 1;
  private static final int INHIBITOR_BODY_HEIGHT = 2;
  private static final int INHIBITOR_TOP_Y = 2;
  private static final int NEXUS_BASE_RADIUS = 2;
  private static final int NEXUS_CORE_RADIUS = 1;
  private static final int NEXUS_TOP_Y = 3;

  private final MapCodec<LeagueStructureBlock> codec;
  private final LeagueStructureKind kind;
  private final LeagueMonster.Team team;

  public LeagueStructureBlock(Properties properties, LeagueStructureKind kind, LeagueMonster.Team team) {
    super(properties);
    this.kind = kind;
    this.team = team;
    this.codec = simpleCodec(newProperties -> new LeagueStructureBlock(newProperties, kind, team));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return codec;
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new LeagueStructureBlockEntity(pos, state, kind, team);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState state = super.getStateForPlacement(context);
    if (state == null || canPlaceStructureAt(context, context.getClickedPos(), kind)) {
      return state;
    }

    return null;
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(level, pos, state, placer, stack);

    if (!level.isClientSide) {
      placeStructureParts(level, pos, state, kind);
    }
  }

  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    super.onRemove(state, level, pos, newState, movedByPiston);

    if (!level.isClientSide && !state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel) {
      removeStructureParts(serverLevel, pos, state, kind);
    }
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return level.isClientSide ? null : createTickerHelper(blockEntityType, LeagueCraft.LEAGUE_STRUCTURE_BLOCK_ENTITY.get(), LeagueStructureBlockEntity::serverTick);
  }

  public static Optional<BlockPos> findStructureController(BlockGetter level, BlockPos partPos) {
    BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

    for (int y = -NEXUS_TOP_Y - 1; y <= 0; y++) {
      for (int x = -NEXUS_BASE_RADIUS; x <= NEXUS_BASE_RADIUS; x++) {
        for (int z = -NEXUS_BASE_RADIUS; z <= NEXUS_BASE_RADIUS; z++) {
          cursor.set(partPos.getX() + x, partPos.getY() + y, partPos.getZ() + z);
          BlockState candidate = level.getBlockState(cursor);
          if (candidate.getBlock() instanceof LeagueStructureBlock structureBlock
              && LeagueCraft.structurePartFor(candidate) instanceof TurretPartBlock partBlock
              && partBlock.isPartFor(candidate)
              && isStructurePartPosition(cursor, partPos, structureBlock.kind)) {
            return Optional.of(cursor.immutable());
          }
        }
      }
    }

    return Optional.empty();
  }

  private static boolean isStructurePartPosition(BlockPos controller, BlockPos part, LeagueStructureKind kind) {
    int dx = part.getX() - controller.getX();
    int dy = part.getY() - controller.getY();
    int dz = part.getZ() - controller.getZ();

    if (dx == 0 && dy == 0 && dz == 0) {
      return false;
    }

    return switch (kind) {
      case TURRET -> (dy >= 0 && dy < TURRET_BODY_HEIGHT && Math.abs(dx) <= TURRET_RADIUS && Math.abs(dz) <= TURRET_RADIUS)
          || (dy == TURRET_TOP_Y && dx == 0 && dz == 0);
      case INHIBITOR -> (dy >= 0 && dy < INHIBITOR_BODY_HEIGHT && Math.abs(dx) <= INHIBITOR_RADIUS && Math.abs(dz) <= INHIBITOR_RADIUS)
          || (dy == INHIBITOR_TOP_Y && dx == 0 && dz == 0);
      case NEXUS -> (dy >= 0 && dy < 2 && Math.abs(dx) <= NEXUS_BASE_RADIUS && Math.abs(dz) <= NEXUS_BASE_RADIUS)
          || (dy == 2 && Math.abs(dx) <= NEXUS_CORE_RADIUS && Math.abs(dz) <= NEXUS_CORE_RADIUS)
          || (dy == NEXUS_TOP_Y && dx == 0 && dz == 0);
    };
  }

  private static boolean canPlaceStructureAt(BlockPlaceContext context, BlockPos controller, LeagueStructureKind kind) {
    Level level = context.getLevel();
    for (BlockPos partPos : structurePartPositions(controller, kind)) {
      if (!level.getBlockState(partPos).canBeReplaced(context)) {
        return false;
      }
    }

    return true;
  }

  private static Iterable<BlockPos> structurePartPositions(BlockPos controller, LeagueStructureKind kind) {
    java.util.List<BlockPos> positions = new java.util.ArrayList<>();

    for (int y = 0; y <= maxStructureY(kind); y++) {
      int radius = structureRadiusAt(kind, y);
      for (int x = -radius; x <= radius; x++) {
        for (int z = -radius; z <= radius; z++) {
          BlockPos partPos = controller.offset(x, y, z);
          if (!partPos.equals(controller) && isStructurePartPosition(controller, partPos, kind)) {
            positions.add(partPos);
          }
        }
      }
    }

    return positions;
  }

  private static int maxStructureY(LeagueStructureKind kind) {
    return switch (kind) {
      case TURRET -> TURRET_TOP_Y;
      case INHIBITOR -> INHIBITOR_TOP_Y;
      case NEXUS -> NEXUS_TOP_Y;
    };
  }

  private static int structureRadiusAt(LeagueStructureKind kind, int y) {
    return switch (kind) {
      case TURRET -> y == TURRET_TOP_Y ? 0 : TURRET_RADIUS;
      case INHIBITOR -> y == INHIBITOR_TOP_Y ? 0 : INHIBITOR_RADIUS;
      case NEXUS -> y < 2 ? NEXUS_BASE_RADIUS : y == 2 ? NEXUS_CORE_RADIUS : 0;
    };
  }

  private static void placeStructureParts(Level level, BlockPos controller, BlockState controllerState, LeagueStructureKind kind) {
    Block partBlock = LeagueCraft.structurePartFor(controllerState);
    if (partBlock == null) {
      return;
    }

    for (BlockPos partPos : structurePartPositions(controller, kind)) {
      BlockState partState = partBlock.defaultBlockState();
      if (partBlock instanceof TurretPartBlock) {
        partState = partState.setValue(TurretPartBlock.SHAPE, partShapeFor(controller, partPos, kind));
      }
      level.setBlock(partPos, partState, Block.UPDATE_ALL);
    }
  }

  private static void removeStructureParts(ServerLevel level, BlockPos controller, BlockState controllerState, LeagueStructureKind kind) {
    Block partBlock = LeagueCraft.structurePartFor(controllerState);
    if (partBlock == null) {
      return;
    }

    for (BlockPos partPos : structurePartPositions(controller, kind)) {
      if (level.getBlockState(partPos).is(partBlock)) {
        level.removeBlock(partPos, false);
      }
    }
  }

  private static TurretPartBlock.PartShape partShapeFor(BlockPos controller, BlockPos part, LeagueStructureKind kind) {
    int dy = part.getY() - controller.getY();
    return switch (kind) {
      case TURRET -> dy == TURRET_TOP_Y ? TurretPartBlock.PartShape.TOP : dy >= 2 ? TurretPartBlock.PartShape.CORE : TurretPartBlock.PartShape.BASE;
      case INHIBITOR -> dy == INHIBITOR_TOP_Y ? TurretPartBlock.PartShape.TOP : dy == 1 ? TurretPartBlock.PartShape.CORE : TurretPartBlock.PartShape.BASE;
      case NEXUS -> dy == NEXUS_TOP_Y ? TurretPartBlock.PartShape.TOP : dy >= 2 ? TurretPartBlock.PartShape.CORE : TurretPartBlock.PartShape.BASE;
    };
  }
}
