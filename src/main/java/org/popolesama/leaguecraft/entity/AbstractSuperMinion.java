package org.popolesama.leaguecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public abstract class AbstractSuperMinion extends AbstractLaneMinion {
  protected AbstractSuperMinion(EntityType<? extends Zombie> entityType, Level level, Profile profile) {
    super(entityType, level, profile);
  }

  @Override
  protected double structureAttackRangeSqr() {
    return 5.0D * 5.0D;
  }
}
