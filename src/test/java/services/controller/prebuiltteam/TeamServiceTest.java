package services.controller.prebuiltteam;

import configuration.AppConfig;
import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.LoadingTeamFailedException;
import exceptions.SavingTeamFailedException;
import models.bugemon.TeamBugemon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.controller.dataprovider.BugemonProvider;
import services.controller.prebuiltteam.TeamService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class TeamServiceTest {

    private final BugemonProvider bugemonProvider = new BugemonProvider();
    private final TeamService teamService = new TeamService();

    private byte[] jsonBackup;

    public TeamServiceTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    @Before
    public void backupAndCleanup() throws IOException {
        File jsonFile = new File(AppConfig.TEAMS_FILE);
        jsonBackup = jsonFile.exists() ? Files.readAllBytes(jsonFile.toPath()) : null;
        if (jsonFile.exists()) jsonFile.delete();
    }

    @After
    public void restore() throws IOException {
        File jsonFile = new File(AppConfig.TEAMS_FILE);
        if (jsonFile.exists()) jsonFile.delete();
        if (jsonBackup != null) Files.write(jsonFile.toPath(), jsonBackup);
    }

    private TeamBugemon makeTeam(String name, String... bugemonIds)
            throws BugemonNotFoundException, JsonResourceNotFoundException {
        return new TeamBugemon(name, bugemonProvider.getListBugemon(List.of(bugemonIds)));
    }

    private TeamBugemon findById(List<TeamBugemon> teams, String id) {
        for (TeamBugemon t : teams) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }

    @Test
    public void testFindTeamByIdReturnsMatchingTeam()
            throws BugemonNotFoundException, JsonResourceNotFoundException,
            SavingTeamFailedException, LoadingTeamFailedException {

        TeamBugemon team = makeTeam("Trouve-moi", "florachu");
        teamService.addTeam(team);

        TeamBugemon found = teamService.findTeamById(team.getId());
        assertEquals(team.getId(), found.getId());
        assertEquals("Trouve-moi", found.getName());
    }

    @Test
    public void testFindTeamByIdReturnsDefensiveCopy()
            throws BugemonNotFoundException, JsonResourceNotFoundException,
            SavingTeamFailedException, LoadingTeamFailedException {

        TeamBugemon team = makeTeam("Original", "florachu");
        teamService.addTeam(team);

        TeamBugemon found1 = teamService.findTeamById(team.getId());
        TeamBugemon found2 = teamService.findTeamById(team.getId());
        assertNotSame("findTeamById should return a fresh copy", found1, found2);
    }

    @Test(expected = LoadingTeamFailedException.class)
    public void testFindTeamByIdThrowsWhenNotFound() throws LoadingTeamFailedException {
        teamService.findTeamById("inexistant");
    }

    @Test(expected = SavingTeamFailedException.class)
    public void testUpdateTeamByIdThrowsWhenIdUnknown()
            throws BugemonNotFoundException, JsonResourceNotFoundException, SavingTeamFailedException {

        TeamBugemon updated = makeTeam("Whatever", "florachu");
        teamService.updateTeamById("id_inexistant", updated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRenameTeamRejectsNameUsedByOtherTeam()
            throws BugemonNotFoundException, JsonResourceNotFoundException, SavingTeamFailedException {

        TeamBugemon team1 = makeTeam("Alpha", "florachu");
        TeamBugemon team2 = makeTeam("Beta",  "exceflam");
        teamService.addTeam(team1);
        teamService.addTeam(team2);

        teamService.renameTeam(team2.getId(), "Alpha");
    }

    @Test
    public void testRenameTeamAllowsKeepingOwnName()
            throws BugemonNotFoundException, JsonResourceNotFoundException,
            SavingTeamFailedException, LoadingTeamFailedException, AttackNotFoundException {

        TeamBugemon team = makeTeam("Mon nom", "florachu");
        teamService.addTeam(team);

        teamService.renameTeam(team.getId(), "Mon nom");

        TeamBugemon reloaded = findById(teamService.loadTeams(), team.getId());
        assertNotNull(reloaded);
        assertEquals("Mon nom", reloaded.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRenameTeamIsCaseInsensitive()
            throws BugemonNotFoundException, JsonResourceNotFoundException, SavingTeamFailedException {

        TeamBugemon team1 = makeTeam("Alpha", "florachu");
        TeamBugemon team2 = makeTeam("Beta",  "exceflam");
        teamService.addTeam(team1);
        teamService.addTeam(team2);

        teamService.renameTeam(team2.getId(), "ALPHA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTeamRejectsDuplicateNameWithDifferentWhitespace()
            throws BugemonNotFoundException, JsonResourceNotFoundException, SavingTeamFailedException {

        teamService.addTeam(makeTeam("Alpha", "florachu"));
        teamService.addTeam(makeTeam("  Alpha  ", "exceflam"));
    }

    @Test
    public void testAddTeamAcceptsNullName()
            throws BugemonNotFoundException, JsonResourceNotFoundException,
            SavingTeamFailedException, LoadingTeamFailedException, AttackNotFoundException {

        TeamBugemon team = makeTeam(null, "florachu");
        teamService.addTeam(team);

        assertNotNull(findById(teamService.loadTeams(), team.getId()));
    }
}