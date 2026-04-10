package services.dataprovider;

import models.bugemon.TeamBugemon;
import repositories.TeamRepository;

import java.util.List;

public abstract class TeamProvider {

    static BugemonProvider bugemonProvider = new BugemonProvider();

    public static List<TeamBugemon> loadTeams() {
        List<TeamBugemon> teams = TeamRepository.loadTeams();
        teams.forEach(t -> t.getTeam().forEach(bugemonProvider::initBugemonFromSavedTeam));
        return teams;
    }

    public static void saveTeams(List<TeamBugemon> teams) {
        TeamRepository.saveTeams(teams);
    }
}