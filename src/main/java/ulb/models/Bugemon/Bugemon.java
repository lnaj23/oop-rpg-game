package ulb.models.Bugemon;

import java.util.ArrayList;
import java.util.List;

class Bugemon {
    private String id;
    private String nom;
    private String type;

    private Stats stats;

    private List<String> attaques;
    private List<Attack> Attacks = new ArrayList<>();

    private String sprite;
    private boolean starter;

    String getId()              { return id; }
    String getNom()             { return nom; }
    Stats getStats()            { return stats; }
    List<String> getAttackNames() { return attaques; }
    List<Attack> getAttacks()   { return Attacks; }

    void addAttacks(Attack attack) {
        Attacks.add(attack);
    }
}