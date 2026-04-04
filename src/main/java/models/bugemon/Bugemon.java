package models.bugemon;

import com.google.gson.annotations.SerializedName;
import models.bugemon.dto.BugemonDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import services.fight.TypeEffectivenessService;
import services.color.TypeColorService;


import static services.fight.DamageCalculator.computeDamage;
import static services.color.TypeColorService.getTypeColorOfBugemon;

/**
 * Represents a Bugémon with its stats, attacks and sprite.
 * Implements {@link BugemonDTO} to expose read-only data to views.
 * Fields are populated via Gson deserialization or the copy constructor.
 */
public class Bugemon implements BugemonDTO {

    /** Unique identifier of this Bugémon. */
    private String id;

    /** Display name of this Bugémon. */
    @SerializedName("nom")
    private String name;

    /** Type of this Bugémon (e.g. fire, water). */
    private String type;

    /** Stats bundle holding HP, attack, defense and initiative values. */
    private Stats stats;

    /** Raw list of attack names used for deserialization. */
    @SerializedName("attaques")
    private List<String> attackNames;

    /** Resolved list of {@link Attack} objects populated after deserialization. */
    private List<Attack> attacks = new ArrayList<>();

    /** Path or identifier of the sprite image for this Bugémon. */
    private String sprite;

    /** Whether this Bugémon is a starter Bugémon. */
    public boolean starter;


    /** Whether this Bugémon belongs to the player's team. */
    private boolean isPlayer = false;

    /** No-arg constructor used by Gson for deserialization. */
    public Bugemon() {}

    /** Color to display in the selectionView */
    private String Color;

    private List<ActiveEffect> ongoingEffects = new ArrayList<>();

    /**
     * Copy constructor. Creates a deep copy of the given Bugémon.
     * Used by the BugemonProvider so two teams don't share the same Bugémon in memory.
     *
     * @param other the Bugémon to copy
     */
    public Bugemon(Bugemon other) {
        this.id       = other.id;
        this.name = other.name;
        this.type     = other.type;
        this.stats    = new Stats(other.stats);
        this.attackNames = other.attackNames;
        this.attacks  = new ArrayList<>(other.attacks);
        this.sprite   = other.sprite;
        this.starter  = other.starter;
        this.isPlayer = other.isPlayer;
        this.Color = other.Color;
        initAttackColors();
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }


    public Attack getAttackFromIndex(int attackIndex){
        return attacks.get(attackIndex);
    }

    @Override
    public int getPv() {
        return stats.getPv();
    }

    @Override
    public int getcurrenPv() {
        return stats.getCurrentPv();
    }

    /**
     * Returns the current HP of this Bugémon.
     *
     * @return current HP
     */
    public int getCurrentPv() {
        return stats.getCurrentPv();
    }

    @Override
    public int getAttaque() {
        return stats.getAttaque();
    }

    @Override
    public int getDefense() {
        return stats.getDefense();
    }

    @Override
    public int getInitiative() {
        return stats.getInitiative();
    }

    public Stats getStats(){
        return stats;
    }

    @Override
    public List<String> getAttackNamesAsList() {
        return attackNames;
    }

    @Override
    public String getAttackNamesAsString() {
        return String.join(", ", attackNames);
    }

    @Override
    public boolean getIsPlayer() {
        return isPlayer;
    }

    @Override
    public String getColor() { return Color; }

    @Override
    public String getAttackColor(int index) {
        return attacks.get(index).getColor();
    }

    /**
     * Returns a list of strings representing the names and values
     * of all currently active effects on this Bugémon.
     *
     * @return a list of active effect descriptions
     */
    @Override
    public String getActifModifierAsStringToDisplay() {
        return this.stats.getActifModifierAsStringToDisplay();
    }

    /**
     * Adds a new active effect to this Bugémon's ongoing effects list.
     *
     * @param effect the effect to be added
     */
    public void addOngoingEffect(Effect effect) {
        this.ongoingEffects.add(new ActiveEffect(effect));
    }

    /**
     * Returns the list of all ongoing effects currently affecting this Bugémon.
     *
     * @return the list of active effects
     */
    public List<ActiveEffect> getOngoingEffects() {
        return ongoingEffects;
    }

    /**
     * Resets all stat modifiers and clears the list of ongoing effects.
     * Typically used at the end of a battle or when applying a recovery item.
     */
    public void resetAllModifiers() {
        stats.resetModifiers();
        ongoingEffects.clear();
    }

    /**
     * Marks whether this Bugémon belongs to the player's team.
     *
     * @param isPlayer true if this Bugémon belongs to the player
     */
    public void setIsPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    /**
     * Adds an attack to this Bugémon's attack list.
     *
     * @param attack the attack to add
     */
    public void addAttacks(Attack attack) {
        attacks.add(attack);
    }

    /**
     * Reduces this Bugémon's current HP by the given amount.
     *
     * @param damage the amount of damage to apply
     */
    public void takeDamage(int damage) {
        stats.reducePv(damage);
    }

    /**
     * Returns whether this Bugémon is knocked out.
     *
     * @return true if current HP is 0
     */
    public boolean isKO() {
        return stats.getCurrentPv() == 0;
    }

    /**
     * Returns whether this Bugémon is still able to fight.
     *
     * @return true if current HP is greater than 0
     */
    public boolean isStillStanding() {
        return this.getCurrentPv() > 0;
    }

    /**
     * Initialises the current HP to the maximum HP.
     * Should be called at the start of each fight.
     */
    public void initPv() {
        this.stats.initCurrentPv();
    }

    /**
     * Executes an attack against a target Bugémon and returns a description of the action.
     *
     * @param target the Bugémon receiving the damage
     * @param attack the attack to use
     * @return a message describing the attack result
     */
    public String attack(Bugemon target, Attack attack) {
        int damage = computeDamage(this, target, attack);
        target.takeDamage(damage);
        return this.name + " utilise " + attack.getName() + "! "
                + TypeEffectivenessService.getEffectivenessMessage(
                attack.getType(),
                target.getType()) + "\n"
                + target.getName() + " perd " + damage + " PV ! "
                + (target.isKO() ? target.getName() + " est mit KO" : "");
    }
    /**
     * Chooses a random attack from the first three available attacks.
     *
     * @return a randomly selected attack
     */
    public Attack chooseRandomAttack() {
        return this.attacks.get(new Random().nextInt(3));
    }

    public void initColor() {
        this.Color = TypeColorService.getTypeColorOfBugemon(this);
    }

    /** Associates each attack with its type colour. */
    private void initAttackColors() {
        for (Attack attack : this.attacks) {
            attack.setColor(getTypeColorOfBugemon(attack));
        }
    }


    // XP

    /**
     * Returns the current level of this Bugemon
     *
     * @return the current level
     */
    public int getlevel() {
        return stats.getLevel();
    }

    /**
     * Returns the current XP of the Bugemon
     *
     * @return the current experience points
     */
    public int getXp() {
        return stats.getXp();
    }

    /**
     * Adds XP to the Bugemon
     *
     * @param amount the amount of XP to add
     */
    public void addXp(int amount) {
        stats.addXp(amount);
    }

    /**
     * Checks whether this Bugemon has enough XP to level up.
     *
     * @return true if accumulated XP exceeds the required threshold
     */
    public boolean canLevelUp() {
        return stats.getXp() >= stats.getXpThreshold();
    }

    /**
     * Levels up this Bugemon
     */
    public void levelUp() {
        stats.levelUp();
    }
}