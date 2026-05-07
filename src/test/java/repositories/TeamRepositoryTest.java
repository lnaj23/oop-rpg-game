package repositories;

import configuration.AppConfig;
import exceptions.*;
import models.bugemon.TeamBugemon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.dataprovider.BugemonProvider;
import services.dataprovider.TeamProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TeamRepositoryTest {

    private final BugemonProvider bugemonProvider = new BugemonProvider();
    private final TeamProvider teamProvider = new TeamProvider();

    /** In-memory backup of the real teams file, taken before each test. */
    private byte[] jsonBackup;

    public TeamRepositoryTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    @Before
    public void backupAndCleanup() throws IOException {
        // 1) Back up the real file if it already exists
        File jsonFile = new File(AppConfig.TEAMS_FILE);
        jsonBackup = jsonFile.exists() ? Files.readAllBytes(jsonFile.toPath()) : null;

        // 2) Start the test from a clean state
        if (jsonFile.exists()) jsonFile.delete();
    }

    @Test
    public void testLoadTeamsEmptyWhenFileDoesNotExist()
            throws LoadingTeamFailedException, AttackNotFoundException, JsonResourceNotFoundException {

        List<TeamBugemon> teams = teamProvider.loadTeams();
        assertNotNull("Teams list should not be null", teams);
        assertTrue("Teams list should be empty", teams.isEmpty());
    }

    @Test
    public void testSaveAndLoadTeams()
            throws BugemonNotFoundException, SavingTeamFailedException, LoadingTeamFailedException,
            JsonResourceNotFoundException, AttackNotFoundException {

        List<TeamBugemon> originalTeams = new ArrayList<>();

        TeamBugemon team1 = new TeamBugemon("Mon équipe 1", bugemonProvider.getListBugemon(List.of("florachu", "exceflam")));
        TeamBugemon team2 = new TeamBugemon("Mon équipe 2", bugemonProvider.getListBugemon(List.of("moussil")));

        originalTeams.add(team1);
        originalTeams.add(team2);

        teamProvider.saveTeams(originalTeams);

        List<TeamBugemon> loadedTeams = teamProvider.loadTeams();

        assertNotNull("Loaded teams should not be null", loadedTeams);
        assertEquals("Should have 2 teams", 2, loadedTeams.size());

        assertEquals("First team name should match", "Mon équipe 1", loadedTeams.get(0).getName());
        assertEquals("First team size should be 2", 2, loadedTeams.get(0).getSize());

        assertEquals("Second team name should match", "Mon équipe 2", loadedTeams.get(1).getName());
        assertEquals("Second team size should be 1", 1, loadedTeams.get(1).getSize());
    }

    @After
    public void restore() throws IOException {
        // 1) Clean what the test wrote
        File jsonFile = new File(AppConfig.TEAMS_FILE);
        if (jsonFile.exists()) jsonFile.delete();

        // 2) Restore the real file exactly as it was before the test
        if (jsonBackup != null) {
            Files.write(jsonFile.toPath(), jsonBackup);
        }
    }
}