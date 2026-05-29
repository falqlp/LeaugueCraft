package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class BaronNashor extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 300;

  public BaronNashor(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.BARON_NASHOR);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
