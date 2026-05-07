package services.models.dataprovider;

import exceptions.*;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import repositories.TeamRepository;

import java.util.List;

public class TeamProvider {

    BugemonProvider bugemonProvider = new BugemonProvider();

    public TeamProvider() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    public List<TeamBugemon> loadTeams() throws LoadingTeamFailedException, AttackNotFoundException, JsonResourceNotFoundException {
        List<TeamBugemon> teams = TeamRepository.getInstance().loadTeams();
        for (TeamBugemon t : teams) {
            for (Bugemon bugemon : t.getTeam()) {
                bugemonProvider.initBugemonFromSavedTeam(bugemon);
            }
        }
        return teams;
    }

    public void saveTeams(List<TeamBugemon> teams) throws SavingTeamFailedException {
        TeamRepository.getInstance().saveTeams(teams);
    }
}