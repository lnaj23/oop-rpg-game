package models.Bugemon;

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
}
