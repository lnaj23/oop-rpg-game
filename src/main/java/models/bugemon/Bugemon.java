package models.bugemon;

import com.google.gson.annotations.SerializedName;
import models.bugemon.dto.BugemonDTO;
import models.game.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static services.models.fight.DamageCalculator.computeDamage;

/**
 * Represents a Bugémon with its stats, attacks and sprite.
 * Implements {@link BugemonDTO} to expose read-only data to views.
 * Fields are populated via Gson deserialization or one of the constructors.
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

    /** Path or identifier of the sprite image for this Bugémon. */
    private String sprite;

    /** Whether this Bugémon is a starter Bugémon. */
    private boolean starter;

    /** {@code true} for a built-in Bugémon, {@code false} for a user-added one. */
    private boolean isDefault = true;

    /** Resolved list of {@link Attack} objects populated after deserialization. */
    private transient List<Attack> attacks = new ArrayList<>();

    /** Whether this Bugémon belongs to the player's team. */
    private transient boolean isPlayer = false;

    /** Whether this Bugémon took part in the current fight (used for XP sharing). */
    private transient boolean hasParticiped = false;

    /** Effects currently affecting this Bugémon during a fight. */
    private transient List<ActiveEffect> ongoingEffects = new ArrayList<>();

    /** No-arg constructor used by Gson for deserialization. */
    public Bugemon() {}

    /**
     * Full constructor used to create a user-defined Bugémon.
     *
     * @param name        the display name (also used to derive the id)
     * @param type        the elemental type
     * @param pv          the maximum HP
     * @param attack      the base attack stat
     * @param defense     the base defense stat
     * @param initiative  the base initiative stat
     * @param starter     whether this Bugémon is a starter
     * @param sprite      the sprite path or identifier
     * @param attacks     the resolved list of attacks
     * @param attackNames the raw attack names
     */
    public Bugemon(String name, String type, int pv, int attack, int defense, int initiative,
                   boolean starter, String sprite, List<Attack> attacks, List<String> attackNames) {
        this.id = name.toLowerCase();
        this.name = name;
        this.type = type;
        this.stats = new Stats(pv, attack, defense, initiative);
        this.starter = starter;
        this.sprite = sprite;
        this.attacks = attacks;
        this.attackNames = attackNames;
        this.isDefault = false;
    }

    /**
     * Copy constructor. Creates a deep copy of the given Bugémon.
     * Used by the BugemonProvider so two teams don't share the same Bugémon in memory.
     *
     * @param other the Bugémon to copy
     */
    public Bugemon(Bugemon other) {
        this.id            = other.id;
        this.name          = other.name;
        this.type          = other.type;
        this.stats         = new Stats(other.stats);
        this.attackNames   = other.attackNames;
        this.attacks       = new ArrayList<>(other.attacks);
        this.sprite        = other.sprite;
        this.starter       = other.starter;
        this.isPlayer      = other.isPlayer;
        this.hasParticiped = other.hasParticiped;
        this.isDefault     = other.isDefault;
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

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public boolean isPlayer() {
        return isPlayer;
    }

    @Override
    public int getPv() {
        return stats.getPv();
    }

    @Override
    public int getCurrenPv() {
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
        return stats.getAttackWithModifier();
    }

    @Override
    public int getDefense() {
        return stats.getDefenseWithModifier();
    }

    @Override
    public int getInitiative() {
        return stats.getInitiativeWithModifier();
    }

    public Stats getStats() {
        return stats;
    }

    @Override
    public int getAttackModifier() {
        return stats.getTotalAttackModifier();
    }

    @Override
    public int getDefenseModifier() {
        return stats.getTotalDefenseModifier();
    }

    @Override
    public int getInitiativeModifier() {
        return stats.getTotalInitiativeModifier();
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public Attack getAttackFromIndex(int attackIndex) {
        return attacks.get(attackIndex);
    }

    @Override
    public List<String> getAttackNamesAsList() {
        return attackNames;
    }

    @Override
    public List<String> getAttackTypesAsList() {
        return attacks.stream().map(Attack::getType).toList();
    }

    @Override
    public String getAttackNamesAsString() {
        return String.join(", ", attackNames);
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
     * Returns whether this Bugémon already knows the given attack.
     * Used for Testing purpose
     * @param attackId the id of the attack to look for
     * @return {@code true} if the attack is already known
     */
    public boolean hasAttack(String attackId) {
        for (Attack attack : attacks) {
            if (attack.getId().equals(attackId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces a known attack with a new one, keeping the names list in sync.
     *
     * @param newAttack       the attack to learn
     * @param attackToReplace the attack to forget
     */
    public void replaceAttack(Attack newAttack, Attack attackToReplace) {
        for (int i = 0; i < attacks.size(); i++) {
            if (attacks.get(i).equals(attackToReplace)) {
                attacks.set(i, newAttack);
                attackNames.set(i, newAttack.getId());
                return;
            }
        }
    }

    /**
     * Chooses a random attack from the first three available attacks.
     *
     * @return a randomly selected attack
     */
    public Attack chooseRandomAttack() {
        return this.attacks.get(new Random().nextInt(3));
    }

    /**
     * Executes an attack against a target Bugémon and applies the damage.
     *
     * @param target the Bugémon receiving the damage
     * @param attack the attack to use
     * @return the amount of damage dealt to the target
     */
    public int attack(Bugemon target, Attack attack) {
        int damage = computeDamage(this, target, attack);
        target.takeDamage(damage);
        return damage;
    }

    /**
     * Reduces this Bugémon's current HP by the given amount.
     *
     * @param damage the amount of damage to apply
     */
    public void takeDamage(int damage) {
        stats.reducePv(damage);
    }

    public void heal(int healAmount) {
        this.stats.heal(healAmount);
    }

    /**
     * Returns whether this Bugémon is knocked out.
     *
     * @return true if current HP is 0
     */
    @Override
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
     * Initializes the current HP to the maximum HP.
     * Should be called at the start of each fight.
     */
    public void initPv() {
        this.stats.initCurrentPv();
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
        stats.resetItemModifiers();
    }

    /**
     * Applies an item's stat modifier to the matching stat.
     *
     * @param item the item whose modifier should be applied
     */
    public void updatestatModifierFromItem(Item item) {
        int amount = item.getModifier();
        switch (item.getStatToModify()) {
            case "attaque":    this.stats.updateAttackModifierFromItem(amount);     break;
            case "defense":    this.stats.updateDefenseModifierFromItem(amount);    break;
            case "initiative": this.stats.updateInitiativeModifierFromItem(amount); break;
        }
    }

    public void cancelDebuff() {
        this.stats.cancelDebuff();
    }

    /** Marks this Bugémon as belonging to the player's team. */
    public void setIsPlayerToTrue() {
        this.isPlayer = true;
    }

    /** Marks this Bugémon as not belonging to the player's team. */
    public void setIsPlayerToFalse() {
        this.isPlayer = false;
    }

    public void setHasParticipedToTrue() {
        this.hasParticiped = true;
    }

    public void setHasParticipedToFalse() {
        this.hasParticiped = false;
    }

    @Override
    public boolean hasParticipated() {
        return this.hasParticiped;
    }

    /**
     * Returns the current level of this Bugémon.
     *
     * @return the current level
     */
    public int getLevel() {
        return stats.getLevel();
    }

    /**
     * Returns the current XP of this Bugémon.
     *
     * @return the current experience points
     */
    public int getXp() {
        return stats.getXp();
    }

    /**
     * Adds XP to this Bugémon.
     *
     * @param amount the amount of XP to add
     */
    public void addXp(int amount) {
        stats.addXp(amount);
    }

    /**
     * Checks whether this Bugémon has enough XP to level up.
     *
     * @return true if accumulated XP exceeds the required threshold
     */
    public boolean canLevelUp() {
        return stats.getXp() >= stats.getXpThreshold();
    }

    /** Levels up this Bugémon. */
    public void levelUp() {
        stats.levelUp();
    }
}