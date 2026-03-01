package ulb.models.Bugemon;

import java.util.ArrayList;
import java.util.List;

public class Bugemon {
    private String id;
    private String nom;
    private String type;

    private Stats stats;

    private List<String> attaques;
    private List<Attack> Attacks = new ArrayList<>();

    private String sprite;
    private boolean starter;

    public String getId()              { return id; }
    public String getNom()             { return nom; }
    public String getSprite() {return sprite;}
    Stats getStats()            { return stats; }
    List<String> getAttackNames() { return attaques; }
    List<Attack> getAttacks()   { return Attacks; }

    void addAttacks(Attack attack) {
        Attacks.add(attack);
    }
}