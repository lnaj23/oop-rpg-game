package repositories;

import com.google.gson.*;
import configuration.AppConfig;
import models.bugemon.TeamBugemon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TeamRepository {

    private static class JsonTeams {
        List<TeamBugemon> teams;
    }

    private static final Gson SAVE_GSON = new GsonBuilder().setPrettyPrinting().create();

    public static List<TeamBugemon> loadTeams() {
        File file = new File(AppConfig.TEAMS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            JsonTeams wrapper = new Gson().fromJson(reader, JsonTeams.class);
            if (wrapper == null || wrapper.teams == null) return new ArrayList<>();
            return wrapper.teams;
        } catch (IOException e) {
            System.err.println("Error loading teams: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveTeams(List<TeamBugemon> teams) {
        File file = new File(AppConfig.TEAMS_FILE);
        file.getParentFile().mkdirs();
        JsonTeams wrapper = new JsonTeams();
        wrapper.teams = teams;
        try (Writer writer = new FileWriter(file)) {
            SAVE_GSON.toJson(wrapper, writer);
        } catch (IOException e) {
            System.err.println("Error saving teams: " + e.getMessage());
        }
    }
}