package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class Dragon extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 125;

  public Dragon(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.DRAGON);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
