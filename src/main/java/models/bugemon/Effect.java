package models.bugemon;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a secondary effect that can be applied by an attack.
 * Effects may modify a stat, apply a status or have a duration.
 * This class is mainly used when loading attack data from JSON.
 */
class Effect {

    /** Category of this effect (e.g. status, stat-modifier). */
    private String type;

    /** Target of this effect (e.g. self, opponent). */
    @SerializedName("cible")
    private String target;

    /** Stat affected by this effect (e.g. attack, defense). */
    private String stat;

    /** Modifier applied to the stat (e.g. +1, -2). */
    @SerializedName("modificateur")
    private String modifier;

    /** Number of turns this effect lasts. */
    @SerializedName("duree")
    private String duration;

    /**
     * Copy constructor. Creates a deep copy of the given effect.
     *
     * @param other the effect to copy
     */
    public Effect(Effect other) {
        this.type     = other.type;
        this.target   = other.target;
        this.stat     = other.stat;
        this.modifier = other.modifier;
        this.duration = other.duration;
    }

    /**
     * Returns the category of this effect.
     *
     * @return the effect type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the target of this effect.
     *
     * @return the effect target
     */
    public String getTarget() {
        return target;
    }

    /**
     * Returns the stat affected by this effect.
     *
     * @return the affected stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * Returns the modifier applied to the stat.
     *
     * @return the stat modifier
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * Returns the duration of this effect in turns.
     *
     * @return the effect duration
     */
    public String getDuration() {
        return duration;
    }
}