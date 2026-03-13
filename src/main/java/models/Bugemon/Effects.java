package models.Bugemon;

/**
 * Represents a secondary effect that can be applied by an attack.
 * Effects may modify a stat, apply a status or have a duration.
 * This class is mainly used when loading attack data from JSON.
 */
class Effects {
    private String type;
    private String target;
    private String stat;
    private String modifier;
    private String duration;

    public Effects(Effects other) {
        this.type = other.type;
        this.target = other.target;
        this.stat = other.stat;
        this.modifier = other.modifier;
        this.duration = other.duration;
    }
}