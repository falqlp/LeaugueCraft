package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class Krug extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 70;

  public Krug(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.KRUG);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
