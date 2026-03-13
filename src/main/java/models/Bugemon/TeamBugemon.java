package models.Bugemon;

import models.Bugemon.DTO.BugemonDTO;
import models.Bugemon.DTO.TeamBugemonDTO;
import exceptions.BugemonNotInTeamException;
import services.BugemonProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a named team of up to 6 Bugémons.
 * Implements {@link TeamBugemonDTO} to expose read-only data to views.
 */
public class TeamBugemon implements TeamBugemonDTO {

    private String name = null;
    private final List<Bugemon> team;

    @Override
    public String getName()          { return name; }
    public void setName(String name) { this.name = name; }

    public TeamBugemon() {
        team = new ArrayList<>();
    }

    public TeamBugemon(List<String> TeamId) {
        this.team = BugemonProvider.getListBugemon(TeamId);
    }

    public TeamBugemon(String name, List<String> TeamId) {
        this.name = name;
        this.team = BugemonProvider.getListBugemon(TeamId);
    }

    public void addBugemon(Bugemon bug) {
        team.add(bug);
    }

    @Override
    public void addBugemonDTO(BugemonDTO bug) {
        team.add((Bugemon) bug);
    }
    @Override
    public int getSize() {
        return team.size();
    }

    public List<Bugemon> getTeam() {
        return team;
    }
    @Override
    public List<BugemonDTO> getTeamDTO()   { return new ArrayList<>(team); }

    public List<Attack> getAllAttacks() {
        List<Attack> allAttacks = new ArrayList<>();
        for (Bugemon bug : team) {
            allAttacks.addAll(bug.getAttacks());
        }
        return allAttacks;
    }

    public List<Attack> getAttacksOf(String bugemonId) {
        for (Bugemon bug : team) {
            if (bug.getId().equals(bugemonId)) {
                return bug.getAttacks();
            }
        }
        throw new BugemonNotInTeamException(bugemonId);
    }

    public boolean hasNullAttacks() {
        for (Attack attack : getAllAttacks()) {
            if (attack == null) return true;
        }
        return false;
    }

    public void printTeam() {
        for (Bugemon bug : team) {
            System.out.println(bug.getId());
        }
    }
    private Bugemon onFieldBugemon = null;

    public int chooseRandomAttack() {
        Random rand = new Random();
        return rand.nextInt(3) + 1; // retourne 1, 2 ou 3
    }
    public void attackTeamEnnemi(TeamBugemon target) {
        if (onFieldBugemon == null) return;

        int attackNumber = chooseRandomAttack();
        System.out.println(onFieldBugemon.getId() + " utilise l'attaque #" + attackNumber);

        // infliger des dégâts au Bugemon ennemi sur le terrain
        if (target.getOnFieldBugemon() != null) {
            target.getOnFieldBugemon().takeDamage(attackNumber);
        }
    }
    //bugemon vivant ou non
    public boolean isStillStanding() {
        for (Bugemon bug : team) {
            if (bug.getCurrentPv() > 0) return true;
        }
        return false;
    }
    //les bugemons vivants
    public List<Bugemon> getAvailableBugemons() {
        return team.stream().filter(b -> b.getCurrentPv() > 0).toList();
    }
    //changer de bugemon en combat
    public void changeOnFieldBugemon(Bugemon bug) {
        if (bug != null && bug.getPv() > 0 && team.contains(bug)) {
            onFieldBugemon = bug;
            System.out.println("Nouveau Bugemon sur le terrain : " + bug.getNom());
        } else {
            System.out.println("Impossible de changer pour ce Bugemon !");
        }
    }
    //Le bugemon en combat
    public Bugemon getOnFieldBugemon() {
        return onFieldBugemon;
    }
    @Override
    public void removeBugemonDTO(BugemonDTO bug) {
        team.remove((Bugemon) bug);
    }
}