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
 *
 * <p>
 * Mutation methods ({@link #addTeam}, {@link #updateTeam}, {@link #deleteTeam},
 * {@link #renameTeam}) load the current list internally, mutate it, then persist
 * it back. Callers therefore never have to manipulate the full list — they only
 * pass the team (or id) they want to act on.
 * </p>
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

    /**
     * Loads all teams from the persistence file.
     */
    public List<TeamBugemon> loadTeams() throws LoadingTeamFailedException {
        try {
            return readPersistenceData(AppConfig.TEAMS_FILE, JsonTeams.class);
        } catch (JsonPersistenceLoadingException e) {
            throw new LoadingTeamFailedException("Error loading teams", e);
        }
    }

    /**
     * Appends the given team to the existing list and persists the new list.
     */
    public void addTeam(TeamBugemon team) throws LoadingTeamFailedException, SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeams();
        teams.add(team);
        saveTeams(teams);
    }

    /**
     * Replaces the team with the same id as {@code team} in the persisted list.
     *
     * @throws SavingTeamFailedException if no team with that id is found
     */
    public void updateTeam(TeamBugemon team) throws LoadingTeamFailedException, SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeams();
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId().equals(team.getId())) {
                teams.set(i, team);
                saveTeams(teams);
                return;
            }
        }
        throw new SavingTeamFailedException("Team not found: " + team.getId(), null);
    }

    /**
     * Removes the team with the given id from the persisted list.
     * Silently no-op if no such team exists.
     */
    public void deleteTeam(String teamId) throws LoadingTeamFailedException, SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeams();
        teams.removeIf(team -> team.getId().equals(teamId));
        saveTeams(teams);
    }

    /**
     * Renames the team with the given id and persists the updated list.
     *
     * @throws SavingTeamFailedException if no team with that id is found
     */
    public void renameTeam(String teamId, String newName) throws LoadingTeamFailedException, SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeams();
        for (TeamBugemon team : teams) {
            if (team.getId().equals(teamId)) {
                team.setName(newName);
                saveTeams(teams);
                return;
            }
        }
        throw new SavingTeamFailedException("Team not found: " + teamId, null);
    }

    /**
     * Persists the given list of teams, replacing whatever was previously on disk.
     * Kept private — callers must use the per-team mutation methods above.
     */
    private void saveTeams(List<TeamBugemon> teams) throws SavingTeamFailedException {
        JsonTeams wrapper = new JsonTeams();
        wrapper.teams = teams;
        try {
            updatePersistenceData(AppConfig.TEAMS_FILE, wrapper);
        } catch (JsonPersistenceSavingException e) {
            throw new SavingTeamFailedException("Error saving teams", e);
        }
    }
}