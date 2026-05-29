package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class Minion extends AbstractLaneMinion {
  private static final int GOLD_REWARD = 21;

  public Minion(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.MINION);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
