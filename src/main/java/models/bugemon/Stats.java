package models.bugemon;

import com.google.gson.annotations.SerializedName;

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
    private transient int currentpv;

    /** Attack stat used to compute damage dealt. */
    @SerializedName("attaque")
    private int attack;

    /** Defense stat used to compute damage received. */
    private int defense;

    /** Initiative stat determining turn order in battle. */
    private int initiative;

    /** Cumulative modifier applied to the attack stat during battle. */
    private transient int attackModifier = 0;

    /** Cumulative modifier applied to the defense stat during battle. */
    private transient int defenseModifier = 0;

    /** Cumulative modifier applied to the initiative stat during battle. */
    private transient int initiativeModifier = 0;

    /**
     * Copy constructor. Creates a deep copy of the given stats.
     *
     * @param other the Stats to copy
     */
    public Stats(Stats other) {
        this.pv         = other.pv;
        this.currentpv  = other.currentpv;
        this.attack = other.attack;
        this.defense    = other.defense;
        this.initiative = other.initiative;
        this.attackModifier = other.attackModifier;
        this.defenseModifier = other.defenseModifier;
        this.initiativeModifier = other.initiativeModifier;
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
    public int getAttackWithModifier() {
        return attack + attackModifier;
    }

    /**
     * Returns the defense stat.
     *
     * @return defense stat
     */
    public int getDefenseWithModifier() {
        return defense + defenseModifier;
    }

    /**
     * Returns the initiative stat.
     *
     * @return initiative stat
     */
    public int getInitiativeWithModifier() {
        return initiative + initiativeModifier;
    }


    /**
     * Modifies the attack stat by the specified delta.
     * The modifier is cumulative and persists during the battle.
     *
     * @param delta the amount to add to or subtract from the attack modifier
     */
    public void modifyAttack(int delta) {this.attackModifier += delta;}

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
        attackModifier = 0;
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
        return "Effets :" + "\n attaque : " + this.attackModifier + "\n defense : " + this.defenseModifier
                + "\n initiative : " + this.initiativeModifier;
    }
}