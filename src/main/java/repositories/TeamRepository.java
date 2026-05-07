package repositories;

import configuration.AppConfig;
import exceptions.JsonPersistenceLoadingException;
import exceptions.JsonPersistenceSavingException;
import exceptions.LoadingTeamFailedException;
import exceptions.SavingTeamFailedException;
import models.bugemon.TeamBugemon;

import java.util.List;

/**
 * Singleton repository for team persistence.
 * Handles saving and loading {@link TeamBugemon} objects to/from {@code teams.json}.
 */
public class TeamRepository extends JsonRepository {

    private static TeamRepository instance;

    /** JSON wrapper mirroring the {teams: [...]} structure on disk. */
    private static class JsonTeams implements JsonWrapper<TeamBugemon> {
        List<TeamBugemon> teams;
        @Override
        public List<TeamBugemon> getJsonData() { return teams; }
    }

    private TeamRepository() {}

    public static TeamRepository getInstance() {
        if (instance == null) instance = new TeamRepository();
        return instance;
    }

    public List<TeamBugemon> loadTeams() throws LoadingTeamFailedException {
        try {
            return readPersistenceData(AppConfig.TEAMS_FILE, JsonTeams.class);
        } catch (JsonPersistenceLoadingException e) {
            throw new LoadingTeamFailedException("Error loading teams", e);
        }
    }

    public void saveTeams(List<TeamBugemon> teams) throws SavingTeamFailedException {
        JsonTeams wrapper = new JsonTeams();
        wrapper.teams = teams;
        try {
            updatePersistenceData(AppConfig.TEAMS_FILE, wrapper);
        } catch (JsonPersistenceSavingException e) {
            throw new SavingTeamFailedException("Error saving teams", e);
        }
    }
}