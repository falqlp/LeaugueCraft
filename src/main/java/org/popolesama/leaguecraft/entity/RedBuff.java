package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class RedBuff extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 100;

  public RedBuff(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.RED_BUFF);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
