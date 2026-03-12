package models.bugemon;

import models.bugemon.dto.BugemonDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bugémon with its stats, attacks and sprite.
 * Implements {@link BugemonDTO} to expose read-only data to views.
 * Fields are final and populated via an explicit constructor used by Gson.
 */
public class Bugemon implements BugemonDTO {

    private String id;
    private String nom;
    private String type;
    private Stats stats;
    private List<String> attaques;
    private List<Attack> attacks = new ArrayList<>();
    private String sprite;
    private boolean starter;

    @Override public String getId()            { return id; }
    @Override public String getNom()           { return nom; }
    @Override public String getSprite()        { return sprite; }
    @Override public List<Attack> getAttacks() { return attacks; }
    @Override public int getPv()               { return stats.getPv(); }
    @Override public int getAttaque()          { return stats.getAttaque(); }
    @Override public int getDefense()          { return stats.getDefense(); }
    @Override public int getInitiative()       { return stats.getInitiative(); }

    public List<String> getAttackNamesAsList() { return attaques; }
    @Override public String getAttackNamesAsString() { return String.join(", ", attaques); }
    public void addAttacks(Attack attack)       { attacks.add(attack); }
}