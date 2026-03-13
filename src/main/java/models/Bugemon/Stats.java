package models.Bugemon;

/**
 * Represents the statistics of a Bugémon.
 * Stores its maximum health points, current health points,
 * attack, defense and initiative.
 * Provides methods to modify or reduce health during battles.
 */
public class Stats {
    private int pv;
    private int currentpv;
    private int attaque;
    private int defense;
    private int initiative;

    public int getPv()         { return pv; }
    public int getCurrentPv()  { return currentpv; }
    public int getAttaque()    { return attaque; }
    public int getDefense()    { return defense; }
    public int getInitiative() { return initiative; }

    public void setCurrentPv(int value) {
        this.currentpv = value;
    }

    /**
     * Reduces the current HP by the specified damage amount.
     * HP cannot go below zero.
     */
    public void reducePv(int damage) {
        this.currentpv -= damage;

        if (this.currentpv < 0) {
            this.currentpv = 0;
        }
    }
    //Pv au début = Pv max
    public void initCurrentPv() {
        this.currentpv = this.pv;
    }

    public Stats(Stats other) {
        this.pv = other.pv;
        this.currentpv = other.currentpv;
        this.attaque = other.attaque;
        this.defense = other.defense;
        this.initiative = other.initiative;
    }
}
