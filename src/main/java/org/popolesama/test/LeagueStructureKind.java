package org.popolesama.test;

public enum LeagueStructureKind {
  NEXUS(300.0F, 0.0F, 0),
  INHIBITOR(180.0F, 0.0F, 0),
  TURRET(140.0F, 5.0F, 10);

  private final float maxHealth;
  private final float attackDamage;
  private final int attackRange;

  LeagueStructureKind(float maxHealth, float attackDamage, int attackRange) {
    this.maxHealth = maxHealth;
    this.attackDamage = attackDamage;
    this.attackRange = attackRange;
  }

  public float maxHealth() {
    return maxHealth;
  }

  public float attackDamage() {
    return attackDamage;
  }

  public int attackRange() {
    return attackRange;
  }
}
