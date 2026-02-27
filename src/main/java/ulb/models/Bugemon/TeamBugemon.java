package ulb.models.Bugemon;

import ulb.exceptions.AttackNotFoundException;
import ulb.exceptions.BugemonNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class TeamBugemon {
    private List<Bugemon> team;

    public TeamBugemon() {
        team = new ArrayList<>();
    }

    public TeamBugemon(List<String> TeamId) {
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
        throw new BugemonNotFoundException(bugemonId);
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