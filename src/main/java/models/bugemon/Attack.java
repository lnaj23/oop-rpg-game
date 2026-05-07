package models.bugemon;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents an attack that a Bugémon can use during a fight.
 * An attack has a name, type, description, power and optional effects.
 * Instances of this class are loaded from the game data (JSON) and
 * are used by Bugémon to inflict damage during battles.
 */
public class Attack{

    /** Unique identifier of this attack. */
    private String id;

    /** Display name of this attack. */
    @SerializedName("nom")
    private String name;

    /** Type of this attack (e.g. fire, water). */
    private String type;

    /** Short description of this attack. */
    private String description;

    /** Base damage dealt by this attack. */
    @SerializedName("puissance")
    private int power;

    /** Optional list of secondary effects applied by this attack. */
    @SerializedName("effets")
    private List<Effect> effects;

    /**
     * Copy constructor. Creates a deep copy of the given attack.
     *
     * @param other the attack to copy
     */
    public Attack(Attack other) {
        this.id          = other.id;
        this.name = other.name;
        this.type        = other.type;
        this.description = other.description;
        this.power = other.power;
        this.effects = other.effects == null ? null : other.effects.stream().map(Effect::new).toList();
    }



    /**
     * Returns the unique identifier of this attack.
     *
     * @return the attack ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the display name of this attack.
     *
     * @return the attack name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this attack.
     *
     * @return the attack type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the description of this attack.
     *
     * @return the attack description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the base damage dealt by this attack.
     *
     * @return the attack power
     */
    public int getPower() {
        return power;
    }

    /**
     * Returns the list of secondary effects of this attack.
     *
     * @return the effects list, or null if none
     */
    public List<Effect> getEffects() {
        return effects;
    }


    @Override
    public String toString() {
        return name;
    }


}