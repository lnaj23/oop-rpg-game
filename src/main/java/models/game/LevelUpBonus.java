package models.game;

import models.bugemon.Bugemon;

/**
 * Represents a bonus option proposed to the player when a Bugemon levels up.
 * <p>
 * Each bonus is composed of stat modifications (HP, attack, defense, initiative)
 * with a total of 10 points. Conversion: 1 point = 2 HP or 2 Initiative or 1 Attack or 1 Defense.
 * <p>
 * Pure data + apply behavior — UI formatting is handled by
 * {@code services.view.formatter.LevelUpBonusFormatter}.
 */
public class LevelUpBonus {

    private final int hpBonus;
    private final int attackBonus;
    private final int defenseBonus;
    private final int initiativeBonus;

    public LevelUpBonus(int hpBonus, int attackBonus, int defenseBonus, int initiativeBonus) {
        this.hpBonus = hpBonus;
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.initiativeBonus = initiativeBonus;
    }

    /** Permanently applies this bonus to the given Bugemon. */
    public void applyTo(Bugemon bugemon) {
        if (hpBonus > 0)         bugemon.getStats().addPermanentHp(hpBonus);
        if (attackBonus > 0)     bugemon.getStats().addPermanentAttack(attackBonus);
        if (defenseBonus > 0)    bugemon.getStats().addPermanentDefense(defenseBonus);
        if (initiativeBonus > 0) bugemon.getStats().addPermanentInitiative(initiativeBonus);
    }

    public int getHpBonus()         { return hpBonus; }
    public int getAttackBonus()     { return attackBonus; }
    public int getDefenseBonus()    { return defenseBonus; }
    public int getInitiativeBonus() { return initiativeBonus; }
}