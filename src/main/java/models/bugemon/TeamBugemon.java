package models.bugemon;

import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;
import exceptions.BugemonNotInTeamException;
import services.BugemonProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a named team of up to 6 Bugémons.
 * Implements {@link TeamBugemonDTO} to expose read-only data to views.
 */
public class TeamBugemon implements TeamBugemonDTO {

    private String name = null;
    private final List<Bugemon> team;
    private Bugemon onFieldBugemon = null;


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

    public TeamBugemon(TeamBugemon other) {
        this.name = other.name;
        this.team = other.team.stream().map(Bugemon::new).toList();
        this.onFieldBugemon = null; // pas de bugemon sur le terrain au début
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

    public void attackEnnemyTeam(TeamBugemon target) {
        if (onFieldBugemon == null) return;
        //
        Attack randomAttack = onFieldBugemon.chooseRandomAttack();
        System.out.println(onFieldBugemon.getId() + " utilise l'attaque #" + (onFieldBugemon.getAttacks().indexOf(randomAttack) + 1));

        // infliger des dégâts au Bugemon ennemi sur le terrain
        if (target.getOnFieldBugemon() != null) {
            target.getOnFieldBugemon().takeDamage(randomAttack.getPuissance());
        }
    }

    //les bugemons vivants
    public List<Bugemon> getAvailableBugemons() {
        List<Bugemon> availableBugemons = new ArrayList<>();
        for (Bugemon bug: team) {
            if (bug.isStillStanding()) {
                availableBugemons.add(bug);
            }
        }
        return availableBugemons;
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