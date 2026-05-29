package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class BlueBuff extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 100;

  public BlueBuff(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.BLUE_BUFF);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
