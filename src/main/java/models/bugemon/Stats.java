package models.bugemon;

/**
 * Represents the statistics of a Bugémon.
 * Stores its maximum health points, current health points,
 * attack, defense and initiative.
 * Provides methods to modify or reduce health during battles.
 */
public class Stats {

    /** Maximum health points of this Bugémon. */
    private int pv;

    /** Current health points of this Bugémon during a battle. */
    private int currentpv;

    /** Attack stat used to compute damage dealt. */
    private int attaque;

    /** Defense stat used to compute damage received. */
    private int defense;

    /** Initiative stat determining turn order in battle. */
    private int initiative;

    /**
     * Copy constructor. Creates a deep copy of the given stats.
     *
     * @param other the Stats to copy
     */
    public Stats(Stats other) {
        this.pv         = other.pv;
        this.currentpv  = other.currentpv;
        this.attaque    = other.attaque;
        this.defense    = other.defense;
        this.initiative = other.initiative;
    }

    /**
     * Returns the maximum HP.
     *
     * @return maximum HP
     */
    public int getPv() {
        return pv;
    }

    /**
     * Returns the current HP.
     *
     * @return current HP
     */
    public int getCurrentPv() {
        return currentpv;
    }

    /**
     * Returns the attack stat.
     *
     * @return attack stat
     */
    public int getAttaque() {
        return attaque;
    }

    /**
     * Returns the defense stat.
     *
     * @return defense stat
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Returns the initiative stat.
     *
     * @return initiative stat
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * Sets the current HP to the given value.
     *
     * @param value the new current HP
     */
    public void setCurrentPv(int value) {
        this.currentpv = value;
    }

    /**
     * Reduces the current HP by the specified damage amount.
     * HP cannot go below zero.
     *
     * @param damage the amount of damage to apply
     */
    public void reducePv(int damage) {
        this.currentpv -= damage;
        if (this.currentpv < 0) {
            this.currentpv = 0;
        }
    }

    /**
     * Resets the current HP to the maximum HP.
     * Should be called at the start of each fight.
     */
    //Pv au début = Pv max
    public void initCurrentPv() {
        this.currentpv = this.pv;
    }
}