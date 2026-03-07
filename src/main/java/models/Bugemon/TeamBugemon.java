package models.Bugemon;

import models.Bugemon.DTO.BugemonDTO;
import models.Bugemon.DTO.TeamBugemonDTO;
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
    private List<Bugemon> team;

    @Override
    public String getName()          { return name; }
    public void setName(String name) { this.name = name; }

    public TeamBugemon() {
        team = new ArrayList<>();
    }

    public TeamBugemon(List<String> teamId) {
        this.team = BugemonProvider.getListBugemon(teamId);
    }

    public TeamBugemon(String name, List<String> teamId) {
        this.name = name;
        this.team = BugemonProvider.getListBugemon(teamId);
    }

    public void addBugemon(BugemonDTO bug) {
        team.add((Bugemon) bug);
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

    @Override
    public void removeBugemonDTO(BugemonDTO bug) {
        team.remove((Bugemon) bug);
    }
}