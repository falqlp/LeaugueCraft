package org.popolesama.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class RedMinion extends AbstractLaneMinion {
  public RedMinion(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.RED_MINION);
  }
}
