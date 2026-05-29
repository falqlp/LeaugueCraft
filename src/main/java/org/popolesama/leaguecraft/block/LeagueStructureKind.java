package org.popolesama.leaguecraft.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum LeagueStructureKind {
  NEXUS(300.0F, 0.0F, 0),
  INHIBITOR(180.0F, 0.0F, 0),
  TURRET(140.0F, 5.0F, 10);

  private final float maxHealth;
  private final float attackDamage;
  private final int attackRange;
}
