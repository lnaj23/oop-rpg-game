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

    /** Cumulative modifier applied to the attack stat during battle. */
    private int attaqueModifier = 0;

    /** Cumulative modifier applied to the defense stat during battle. */
    private int defenseModifier = 0;

    /** Cumulative modifier applied to the initiative stat during battle. */
    private int initiativeModifier = 0;

    /** Bugemon level start at 1 */
    private int level = 1;

    /** XP gained by the Bugemon */
    private int xp = 0;

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
        this.attaqueModifier = other.attaqueModifier;
        this.defenseModifier = other.defenseModifier;
        this.initiativeModifier = other.initiativeModifier;
        this.level = other.level;
        this.xp = other.xp;
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
        return attaque + attaqueModifier;
    }

    /**
     * Returns the defense stat.
     *
     * @return defense stat
     */
    public int getDefense() {
        return defense + defenseModifier;
    }

    /**
     * Returns the initiative stat.
     *
     * @return initiative stat
     */
    public int getInitiative() {
        return initiative + initiativeModifier;
    }

    /**
     * Return Bugemon level
     *
     * @return level start at 1
     */
    public int getLevel() { return level < 1 ? 1 : level; }

    /**
     * Return XP already gained by the Bugemon.
     *
     * @return actual XP
     */
    public int getXp() { return xp; }

    /**
     * Computes the XP threshold required to reach the next level.
     * Formula from the specification (Section 3.2.4.1):
     * Level n -> (n+1) : 50 + 100 * (n - 1) XP
     * Examples: level 1->2 = 50, level 2->3 = 150, level 3->4 = 250
     *
     * @return the amount of XP needed to level up
     */
    public int getXpThreshold() {
        int effectiveLevel = level < 1 ? 1 : level;
        return 50 + 100 * (effectiveLevel - 1);
    }

    /**
     * Adds XP to the Bugémon
     * @param amount the amount of XP to add (must be positive)
     */
    public void addXp(int amount) {
        this.xp += amount;
    }

    /**
     * Levels up the Bugémon by one level, subtracts the XP threshold
     * (any excess XP is carried over to the next level),
     * and restores HP to its maximum value.
     */
    public void levelUp() {
        if (level < 1) level = 1;
        this.xp -= getXpThreshold();  // keeps the excess
        this.level++;
        this.currentpv = this.pv;     // HP restored to max on level up
    }

    /**
     * Permanently increases the maximum HP.
     * Also updates current HP to reflect the new maximum.
     *
     * @param amount the HP bonus to add
     */
    public void addPermanentHp(int amount) {
        this.pv += amount;
        this.currentpv = this.pv;  // refill HP to new max
    }

    /**
     * Permanently increases the base attack stat.
     *
     * @param amount the attack bonus to add
     */
    public void addPermanentAttack(int amount) {
        this.attaque += amount;
    }

    /**
     * Permanently increases the base defense stat.
     *
     * @param amount the defense bonus to add
     */
    public void addPermanentDefense(int amount) {
        this.defense += amount;
    }

    /**
     * Permanently increases the base initiative stat.
     *
     * @param amount the initiative bonus to add
     */
    public void addPermanentInitiative(int amount) {
        this.initiative += amount;
    }

    /**
     * Modifies the attack stat by the specified delta.
     * The modifier is cumulative and persists during the battle.
     *
     * @param delta the amount to add to or subtract from the attack modifier
     */
    public void modifyAttaque(int delta) {this.attaqueModifier += delta;}

    /**
     * Modifies the defense stat by the specified delta.
     * The modifier is cumulative and persists during the battle.
     *
     * @param delta the amount to add to or subtract from the defense modifier
     */
    public void modifyDefense(int delta) {this.defenseModifier += delta;}

    /**
     * Modifies the initiative stat by the specified delta.
     * The modifier is cumulative and persists during the battle.
     *
     * @param delta the amount to add to or subtract from the initiative modifier
     */
    public void modifyInitiative(int delta) {this.initiativeModifier += delta;}

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
     * Increases the current HP by the specified heal amount.
     * HP cannot exceed the maximum HP (pv).
     *
     * @param healAmount the amount of health to restore
     */
    public void heal(int healAmount){
        this.currentpv = Math.min(this.currentpv + healAmount, this.pv);
    }

    /**
     * Resets all the modifiers to zero.
     * Should be called at the end of each fight.
     */
    public void resetModifiers() {
        attaqueModifier = 0;
        defenseModifier = 0;
        initiativeModifier = 0;
    }

    /**
     * Resets the current HP to the maximum HP.
     * Should be called at the start of each fight.
     */
    //Pv au début = Pv max
    public void initCurrentPv() {
        this.currentpv = this.pv;
    }

    String getActifModifierAsStringToDisplay() {
        return "Effets :" + "\n attaque : " + this.attaqueModifier + "\n defense : " + this.defenseModifier
                + "\n initiative : " + this.initiativeModifier;
    }

}