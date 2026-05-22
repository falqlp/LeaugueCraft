package org.popolesama.test;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LeagueStructureBlock extends BaseEntityBlock {
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

  public LeagueStructureKind kind() {
    return kind;
  }

  public LeagueMonster.Team team() {
    return team;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return level.isClientSide ? null : createTickerHelper(blockEntityType, Test.LEAGUE_STRUCTURE_BLOCK_ENTITY.get(), LeagueStructureBlockEntity::serverTick);
  }
}
