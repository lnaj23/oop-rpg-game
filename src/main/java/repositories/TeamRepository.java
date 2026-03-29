package repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.bugemon.TeamBugemon;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing saved teams persistence.
 * Loads team templates from bugemon.json (complete Bugémon objects, no reconstruction).
 * Persists user-created teams to the user's data directory.
 */
public abstract class TeamRepository extends JsonRepository {

    /**
     * Mirrors the structure of bugemon.json, which contains a list of TeamBugemon objects
     * (with complete Bugémon data, not just IDs) under the key "teams".
     * Used exclusively by Gson to deserialize the JSON array.
     */
    private static class JsonTeams {
        public List<TeamBugemon> teams;
    }

    private static final String TEAMS_FILE = "persistence/savedteam/team.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String PATH = JSON_PREFIX + "bugemon.json";

    /** All team templates loaded from bugemon.json at startup. */
    private static final List<TeamBugemon> teamTemplates;

    static {
        teamTemplates = readJSON(PATH, JsonTeams.class, w -> w.teams);
    }

    /**
     * Returns the team templates loaded from bugemon.json (read-only).
     * These teams contain complete Bugémon objects.
     *
     * @return the list of team templates
     */
    public static List<TeamBugemon> getTeamTemplates() {
        return teamTemplates;
    }

    /**
     * Saves all teams to the user's JSON file.
     *
     * @param teams the list of teams to save
     */
    public static void saveTeams(List<TeamBugemon> teams) {
        JsonTeams jsonTeams = new JsonTeams();
        jsonTeams.teams = teams;

        try {
            File file = new File(TEAMS_FILE);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(jsonTeams, writer);
            }
        } catch (IOException e) {
            System.err.println("Error saving teams: " + e.getMessage());
        }
    }

    /**
     * Loads all saved teams from the user's JSON file.
     *
     * @return list of saved teams, empty list if file doesn't exist
     */
    public static List<TeamBugemon> loadTeams() {
        List<TeamBugemon> teams = new ArrayList<>();
        File file = new File(TEAMS_FILE);

        if (!file.exists()) {
            return teams;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonTeams jsonTeams = gson.fromJson(reader, JsonTeams.class);
            if (jsonTeams != null && jsonTeams.teams != null) {
                teams.addAll(jsonTeams.teams);
            }
        } catch (IOException e) {
            System.err.println("Error loading teams: " + e.getMessage());
        }

        return teams;
    }

    /**
     * Saves a single team to the persistent list.
     *
     * @param team the team to save
     * @param allTeams the full list of teams to persist
     */
    public static void saveTeam(TeamBugemon team, List<TeamBugemon> allTeams) {
        saveTeams(allTeams);
    }
}
