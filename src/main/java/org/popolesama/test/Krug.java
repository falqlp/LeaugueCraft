package org.popolesama.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class Krug extends AbstractJungleMonster {
  public Krug(EntityType<? extends Zombie> entityType, Level level) {
    super(entityType, level, Profile.KRUG);
  }
}
