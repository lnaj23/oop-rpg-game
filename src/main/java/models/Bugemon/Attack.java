package models.Bugemon;

import java.util.List;

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
}