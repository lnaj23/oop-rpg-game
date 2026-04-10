package services.dataprovider;

import models.bugemon.TeamBugemon;
import repositories.TeamRepository;

import java.util.List;

public class TeamProvider {

    BugemonProvider bugemonProvider = new BugemonProvider();

    public List<TeamBugemon> loadTeams() {
        List<TeamBugemon> teams = TeamRepository.getInstance().loadTeams();
        teams.forEach(t -> t.getTeam().forEach(bugemonProvider::initBugemonFromSavedTeam));
        return teams;
    }

    public void saveTeams(List<TeamBugemon> teams) {
        TeamRepository.getInstance().saveTeams(teams);
    }
}