package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class BlueSuperMinion extends AbstractSuperMinion {
  private static final int GOLD_REWARD = 90;

  public BlueSuperMinion(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.BLUE_SUPER_MINION);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
