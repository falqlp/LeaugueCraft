package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class RiftHerald extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 100;

  public RiftHerald(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.RIFT_HERALD);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
