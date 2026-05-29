package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class VoidGrub extends AbstractJungleMonster {
  private static final int GOLD_REWARD = 75;

  public VoidGrub(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.VOID_GRUB);
  }

  @Override
  protected int goldReward() {
    return GOLD_REWARD;
  }
}
