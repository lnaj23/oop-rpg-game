package models.Bugemon;

import java.util.List;

/**
 * Represents an attack that a Bugémon can use during a fight.
 * An attack has a name, type, description, power and optional effects.
 * Instances of this class are loaded from the game data (JSON) and
 * are used by Bugémon to inflict damage during battles.
 */
public class Attack {

    private String id;
    private String nom;
    private String type;
    private String description;
    private int puissance;
    private List<Effects> effets;

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getType() { return type; }
    public int getPuissance() { return puissance; }
    public String getDescription() { return description; }

    public Attack(Attack other) {
        this.id = other.id;
        this.nom = other.nom;
        this.type = other.type;
        this.description = other.description;
        this.puissance = other.puissance;
        this.effets = other.effets == null ? null : other.effets.stream().map(Effects::new).toList();
    }
}