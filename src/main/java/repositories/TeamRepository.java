package repositories;

import com.google.gson.*;
import configuration.AppConfig;
import exceptions.LoadingTeamFailedException;
import exceptions.SavingTeamFailedException;
import models.bugemon.TeamBugemon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton repository for team persistence.
 * Handles saving and loading {@link TeamBugemon} objects to/from {@code teams.json}.
 */
public class TeamRepository {

    private static TeamRepository instance;

    private static final Gson SAVE_GSON = new GsonBuilder().setPrettyPrinting().create();

    private static class JsonTeams {
        List<TeamBugemon> teams;
    }

    private TeamRepository() {}

    /**
     * Returns the unique instance of {@link TeamRepository},
     * creating it on first call.
     *
     * @return the singleton instance
     */
    public static TeamRepository getInstance() {
        if (instance == null) instance = new TeamRepository();
        return instance;
    }

    /**
     * Loads all teams from {@code teams.json}.
     * Returns an empty list if the file does not exist or is malformed.
     *
     * @return the list of loaded teams
     */
    public List<TeamBugemon> loadTeams() throws LoadingTeamFailedException {
        File file = new File(AppConfig.TEAMS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            JsonTeams wrapper = new Gson().fromJson(reader, JsonTeams.class);
            if (wrapper == null || wrapper.teams == null) return new ArrayList<>();
            return wrapper.teams;
        } catch (IOException e) {
            throw new LoadingTeamFailedException("Error loading teams: " + e.getMessage(), e);
        }
    }

    /**
     * Saves the given list of teams to {@code teams.json}.
     * Creates parent directories if they do not exist.
     *
     * @param teams the list of teams to save
     */
    public void saveTeams(List<TeamBugemon> teams) throws SavingTeamFailedException {
        File file = new File(AppConfig.TEAMS_FILE);
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();
        JsonTeams wrapper = new JsonTeams();
        wrapper.teams = teams;
        try (Writer writer = new FileWriter(file)) {
            SAVE_GSON.toJson(wrapper, writer);
        } catch (IOException e) {
            throw new SavingTeamFailedException("Error saving teams: " + e.getMessage(), e);
        }
    }
}