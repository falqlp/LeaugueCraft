package org.popolesama.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class Gromp extends AbstractJungleMonster {
  public Gromp(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.GROMP);
  }
}
