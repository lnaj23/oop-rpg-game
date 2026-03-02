package ulb.models.Bugemon;

import ulb.exceptions.BugemonNotInTeamException;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents a Bugemon team, identified by a name and the Bugemon that compose it.
 * In the game, teams are the core unit — individual Bugemon are never manipulated directly during gameplay.
 * Uses BugemonProvider to instantiate Bugemon objects from a list of their ids.
 */
public class TeamBugemon {

    private String name = null;
    private List<Bugemon> team;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

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

    public int getSize() {
        return team.size();
    }

    public List<Bugemon> getTeam() {
        return team;
    }

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
}