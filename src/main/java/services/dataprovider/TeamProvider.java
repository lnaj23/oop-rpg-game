package services.dataprovider;

import models.bugemon.TeamBugemon;
import repositories.TeamRepository;

import java.util.List;

/**
 * Provides access to team data with persistence management.
 * Acts as the single entry point for loading and saving teams.
 * This class is not intended to be instantiated.
 */
public abstract class TeamProvider {

    /**
     * Loads all saved teams from persistent storage.
     *
     * @return the list of all saved teams
     */
    public static List<TeamBugemon> loadTeams() {
        return TeamRepository.loadTeams();
    }

    /**
     * Saves all teams to persistent storage.
     *
     * @param teams the list of teams to save
     */
    public static void saveTeams(List<TeamBugemon> teams) {
        TeamRepository.saveTeams(teams);
    }
}
