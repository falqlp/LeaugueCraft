package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class RedCasterMinion extends AbstractCasterMinion {
  private static final int GOLD_REWARD = 14;

  public RedCasterMinion(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.RED_CASTER_MINION);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
