package models.bugemon;

import com.google.gson.annotations.SerializedName;
import models.bugemon.dto.BugemonDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import services.fight.TypeEffectivenessService;


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
    private boolean starter;

    /** Whether this Bugémon belongs to the player's team. */
    private boolean isPlayer = false;

    /** No-arg constructor used by Gson for deserialization. */
    public Bugemon() {}

    private String Color;

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
     * @param attack the attack to use
     * @param target the Bugémon receiving the damage
     * @return a message describing the attack result
     */
    public String attack(Attack attack, Bugemon target) {
        int damage = attack.getPower();
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
        if (this.type == null) { this.Color = "#AAAAAA"; return; }
        this.Color = switch (this.type.toLowerCase()) {
            case "pyro"  -> "#FF4500";
            case "aqua"  -> "#1E90FF";
            case "flora" -> "#32CD32";
            case "litho" -> "#A0522D";
            default      -> "#AAAAAA";
        };
    }
}