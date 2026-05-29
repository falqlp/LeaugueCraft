package org.popolesama.leaguecraft.block;

import org.popolesama.leaguecraft.LeagueCraft;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class TurretPartBlock extends Block {
  public static final EnumProperty<PartShape> SHAPE = EnumProperty.create("shape", PartShape.class);

  public TurretPartBlock(Properties properties) {
    super(properties);
    registerDefaultState(stateDefinition.any().setValue(SHAPE, PartShape.BASE));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(SHAPE);
  }

  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
      LeagueStructureBlock.findStructureController(level, pos)
          .ifPresent(controller -> serverLevel.destroyBlock(controller, false));
    }

    return super.playerWillDestroy(level, pos, state, player);
  }

  @Override
  public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
    return LeagueStructureBlock.findStructureController(level, pos)
        .map(controller -> level.getBlockState(controller).getDestroyProgress(player, level, controller))
        .orElse(super.getDestroyProgress(state, player, level, pos));
  }

  public boolean isPartFor(BlockState controllerState) {
    return this == LeagueCraft.structurePartFor(controllerState);
  }

  public enum PartShape implements StringRepresentable {
    BASE("base"),
    CORE("core"),
    TOP("top");

    private final String serializedName;

    PartShape(String serializedName) {
      this.serializedName = serializedName;
    }

    @Override
    public String getSerializedName() {
      return serializedName;
    }
  }
}
