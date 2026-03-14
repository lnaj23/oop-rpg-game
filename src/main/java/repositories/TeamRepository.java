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
 * Handles saving and loading teams from a JSON file in the user's data directory.
 */
public class TeamRepository {

    private static final String TEAMS_FILE = "persistence/savedteam/team.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Wrapper class for JSON serialization of the teams list.
     */
    private static class JsonTeams {
        public List<TeamData> teams;

        JsonTeams(List<TeamData> teams) {
            this.teams = teams;
        }
    }

    /**
     * Data transfer object for serializing team info.
     */
    private static class TeamData {
        public String name;
        public List<String> bugemonIds;

        TeamData(String name, List<String> bugemonIds) {
            this.name = name;
            this.bugemonIds = bugemonIds;
        }
    }

    /**
     * Loads all saved teams from the JSON file.
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
                for (TeamData data : jsonTeams.teams) {
                    teams.add(new TeamBugemon(data.name, data.bugemonIds));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading teams: " + e.getMessage());
        }

        return teams;
    }

    /**
     * Saves all teams to the JSON file.
     *
     * @param teams the list of teams to save
     */
    public static void saveTeams(List<TeamBugemon> teams) {
        List<TeamData> teamDataList = new ArrayList<>();

        for (TeamBugemon team : teams) {
            List<String> bugemonIds = new ArrayList<>();
            for (var bugemon : team.getTeam()) {
                bugemonIds.add(bugemon.getId());
            }
            teamDataList.add(new TeamData(team.getName(), bugemonIds));
        }

        JsonTeams jsonTeams = new JsonTeams(teamDataList);

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
     * Saves a single team to the persistent list.
     * Adds the team to the list and persists it to disk.
     *
     * @param team the team to save
     * @param allTeams the full list of teams to persist
     */
    public static void saveTeam(TeamBugemon team, List<TeamBugemon> allTeams) {
        saveTeams(allTeams);
    }
}
