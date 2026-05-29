package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class RedSuperMinion extends AbstractSuperMinion {
  private static final int GOLD_REWARD = 90;

  public RedSuperMinion(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.RED_SUPER_MINION);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
