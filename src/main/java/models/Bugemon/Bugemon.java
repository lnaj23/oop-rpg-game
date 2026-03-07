package models.Bugemon;

import models.Bugemon.DTO.BugemonDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bugémon with its stats, attacks and sprite.
 * Implements {@link BugemonDTO} to expose read-only data to views.
 */
public class Bugemon implements BugemonDTO {

    private String id;
    private String nom;
    private String type;
    private Stats stats;
    private List<String> attaques;
    private List<Attack> Attacks = new ArrayList<>();
    private String sprite;
    private boolean starter;

    @Override public String getId()            { return id; }
    @Override public String getNom()           { return nom; }
    @Override public String getSprite()        { return sprite; }
    @Override public List<Attack> getAttacks() { return Attacks; }
    @Override public int getPv()               { return stats.getPv(); }
    @Override public int getAttaque()          { return stats.getAttaque(); }
    @Override public int getDefense()          { return stats.getDefense(); }
    @Override public int getInitiative()       { return stats.getInitiative(); }

    public Stats getStats()              { return stats; }
    public List<String> getAttackNames() { return attaques; }
    public void addAttacks(Attack attack)       { Attacks.add(attack); }
}