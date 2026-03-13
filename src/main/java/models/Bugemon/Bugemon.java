package models.Bugemon;

import com.google.gson.annotations.SerializedName;
import models.Bugemon.DTO.BugemonDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private boolean isPlayer = false;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    @Override
    public List<Attack> getAttacks() {
        return attacks;
    }

    @Override
    public int getPv() {
        return stats.getPv();
    }

    @Override
    public int getAttaque() {
        return stats.getAttaque();
    }

    @Override
    public int getDefense() {
        return stats.getDefense();
    }

    @Override
    public int getInitiative() {
        return stats.getInitiative();
    }

    public List<String> getAttackNamesAsList() {
        return attaques;
    }

    @Override
    public String getAttackNamesAsString() {
        return String.join(", ", attaques);
    }

    public void addAttacks(Attack attack) {
        attacks.add(attack);
    }

    public int getCurrentPv() {
        return stats.getCurrentPv();
    }

    public void takeDamage(int damage) {
        stats.reducePv(damage);
    }

    public boolean isKO() {
        return stats.getCurrentPv() == 0;
    }

    public String attack(Attack attack, Bugemon target) {

        int damage = attack.getPuissance();

        target.takeDamage(damage);

        return this.nom + " utilise " + attack.getNom() + " sur " + target.getNom();
    }

    public Attack chooseRandomAttack() {
        Random rand = new Random();
        return this.attacks.get(rand.nextInt(3));
    }

    public boolean isStillStanding() {
        if (this.getCurrentPv() <= 0) {
            return false;
        }
        return true;
    }

    public boolean getIsPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(boolean a) {
        isPlayer = a;
    }
}