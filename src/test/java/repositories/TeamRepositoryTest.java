package repositories;

import models.bugemon.TeamBugemon;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TeamRepositoryTest {

    @Before
    public void cleanup() {
        File f = new File(System.getProperty("user.home") + "/.bugemon/bugemon.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void testLoadTeamsEmptyWhenFileDoesNotExist() {
        List<TeamBugemon> teams = TeamRepository.loadTeams();
        assertNotNull("Teams list should not be null", teams);
        assertTrue("Teams list should be empty", teams.isEmpty());
    }

    @Test
    public void testSaveAndLoadTeams() {
        List<TeamBugemon> originalTeams = new ArrayList<>();
        
        TeamBugemon team1 = new TeamBugemon("Mon équipe 1", List.of("florachu", "exceflam"));
        TeamBugemon team2 = new TeamBugemon("Mon équipe 2", List.of("moussil"));
        
        originalTeams.add(team1);
        originalTeams.add(team2);
        
        TeamRepository.saveTeams(originalTeams);
        
        List<TeamBugemon> loadedTeams = TeamRepository.loadTeams();
        
        assertNotNull("Loaded teams should not be null", loadedTeams);
        assertEquals("Should have 2 teams", 2, loadedTeams.size());
        
        assertEquals("First team name should match", "Mon équipe 1", loadedTeams.get(0).getName());
        assertEquals("First team size should be 2", 2, loadedTeams.get(0).getSize());
        
        assertEquals("Second team name should match", "Mon équipe 2", loadedTeams.get(1).getName());
        assertEquals("Second team size should be 1", 1, loadedTeams.get(1).getSize());
    }
}
