package models.game;

import models.bugemon.Bugemon;

/**
 * Represents a bonus option proposed to the player when a Bugemon levels up.
 *
 * Each bonus is composed of stat modifications
 * (HP, attack, defense, initiative) with a total of 10 points.
 * Conversion: 1 point = 2 HP or 2 Initiative or 1 Attack or 1 Defense.
 *
 * Examples of pure bonuses:
 * - +20 HP (10 points * 2 HP/point)
 * - +10 Attack (10 points * 1 Atk/point)
 * - +10 Defense (10 points * 1 Def/point)
 * - +20 Initiative (10 points * 2 Init/point)
 */
public class LevelUpBonus {

    /** HP maximum bonus to apply. */
    private final int hpBonus;

    /** Attack bonus to apply. */
    private final int attackBonus;

    /** Defense bonus to apply. */
    private final int defenseBonus;

    /** Initiative bonus to apply. */
    private final int initiativeBonus;

    /**
     * Builds a level-up bonus with the specified values.
     *
     * @param hpBonus          HP maximum bonus
     * @param attackBonus      attack bonus
     * @param defenseBonus     defense bonus
     * @param initiativeBonus  initiative bonus
     */
    public LevelUpBonus(int hpBonus, int attackBonus, int defenseBonus, int initiativeBonus) {
        this.hpBonus = hpBonus;
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.initiativeBonus = initiativeBonus;
    }

    /**
     * Permanently applies this bonus to the given Bugemon.
     * Directly modifies the Bugemon's base stats.
     *
     * @param bugemon the Bugemon receiving the bonus
     */
    public void applyTo(Bugemon bugemon) {
        if (hpBonus > 0)         bugemon.getStats().addPermanentHp(hpBonus);
        if (attackBonus > 0)     bugemon.getStats().addPermanentAttack(attackBonus);
        if (defenseBonus > 0)    bugemon.getStats().addPermanentDefense(defenseBonus);
        if (initiativeBonus > 0) bugemon.getStats().addPermanentInitiative(initiativeBonus);
    }

    /**
     * Returns a human-readable description of the bonus
     * for display on the LevelUp view choice buttons.
     *
     * @return the bonus description "+20 HP" or "+5 Attaque, +5 Défense"
     */
    @Override
    public String toString() {
        // StringBuilder builds the description
        // by adding each non-zero stat separated by ", "
        StringBuilder sb = new StringBuilder();
        if (hpBonus > 0)         appendStat(sb, "+" + hpBonus + " HP");
        if (attackBonus > 0)     appendStat(sb, "+" + attackBonus + " Attaque");
        if (defenseBonus > 0)    appendStat(sb, "+" + defenseBonus + " Défense");
        if (initiativeBonus > 0) appendStat(sb, "+" + initiativeBonus + " Initiative");
        return sb.toString();
    }

    /**
     * Helper to append a stat to the description with a separator.
     * @param sb   the StringBuilder being built
     * @param stat the stat string to append
     */
    private void appendStat(StringBuilder sb, String stat) {
        if (sb.length() > 0) sb.append(", ");
        sb.append(stat);
    }

    /**
     * @return the HP bonus value
     */
    public int getHpBonus() { return hpBonus; }

    /**
     * @return the attack bonus value
     */
    public int getAttackBonus() { return attackBonus; }

    /**
     * @return the defense bonus value
     */
    public int getDefenseBonus() { return defenseBonus; }

    /**
     * @return the initiative bonus value
     */
    public int getInitiativeBonus() { return initiativeBonus; }

}