package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class RedCannonMinion extends AbstractCannonMinion {
  private static final int GOLD_REWARD = 60;

  public RedCannonMinion(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.RED_CANNON_MINION);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
