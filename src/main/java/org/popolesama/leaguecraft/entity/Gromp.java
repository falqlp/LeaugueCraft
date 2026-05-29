package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class Gromp extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 80;

  public Gromp(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.GROMP);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
