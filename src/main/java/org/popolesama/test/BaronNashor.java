package org.popolesama.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class BaronNashor extends AbstractJungleMonster {
  public BaronNashor(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.BARON_NASHOR);
  }
}
