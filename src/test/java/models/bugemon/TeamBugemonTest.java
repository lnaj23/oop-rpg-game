package models.bugemon;

import junit.framework.TestCase;
import org.junit.Test;
import exceptions.BugemonNotFoundException;
import exceptions.BugemonNotInTeamException;
import services.dataprovider.BugemonProvider;

import java.util.Arrays;
import java.util.List;

public class TeamBugemonTest extends TestCase {

    @Test
    public void testConstructorNotNull() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam", "commitide"));
        assertNotNull(team);
    }

    @Test
    public void testConstructorWithUnknownIdThrows() {
        try {
            new TeamBugemon("test", Arrays.asList("unknown_id"));
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testEmptyConstructorNotNull() {
        TeamBugemon team = new TeamBugemon();
        assertNotNull(team);
    }

    @Test
    public void testEmptyConstructorSizeIsZero() {
        TeamBugemon team = new TeamBugemon();
        assertEquals(0, team.getSize());
    }

    @Test
    public void testGetSizeCorrect() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam", "commitide"));
        assertEquals(3, team.getSize());
    }

    @Test
    public void testGetTeamNotNull() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam"));
        assertNotNull(team.getTeam());
    }

    @Test
    public void testGetTeamCorrectSize() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam"));
        assertEquals(2, team.getTeam().size());
    }

    @Test
    public void testGetTeamNoNullElements() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam", "commitide"));
        for (Bugemon bug : team.getTeam()) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testAddBugemonIncreasesSize() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu"));
        int sizeBefore = team.getSize();
        team.addBugemon(BugemonProvider.getListBugemon(Arrays.asList("exceflam")).get(0));
        assertEquals(sizeBefore + 1, team.getSize());
    }

    @Test
    public void testGetAllAttacksNotNull() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam"));
        assertNotNull(team.getAllAttacks());
    }

    @Test
    public void testGetAllAttacksNotEmpty() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam"));
        assertFalse(team.getAllAttacks().isEmpty());
    }

    @Test
    public void testGetAllAttacksTotalCount() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam", "commitide"));
        int expected = team.getTeam().stream().mapToInt(b -> b.getAttackNamesAsList().size()).sum();
        assertEquals(expected, team.getAllAttacks().size());
    }

    @Test
    public void testGetAllAttacksNoNullElement() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam", "commitide"));
        for (Attack attack : team.getAllAttacks()) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testHasNullAttacksReturnsFalse() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam", "commitide"));
        assertFalse(team.hasNullAttacks());
    }

    @Test
    public void testGetAttacksOfValidBugemon() throws BugemonNotFoundException, BugemonNotInTeamException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam"));
        List<Attack> attacks = team.getAttacksOf("florachu");
        assertNotNull(attacks);
        assertEquals(team.getTeam().get(0).getAttackNamesAsList().size(), attacks.size());
    }

    @Test
    public void testGetAttacksOfBugemonNoNullAttack() throws BugemonNotFoundException, BugemonNotInTeamException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam"));
        List<Attack> attacks = team.getAttacksOf("florachu");
        for (Attack attack : attacks) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testGetAttacksOfBugemonNotInTeamThrows() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu"));
        try {
            team.getAttacksOf("exceflam");
            fail("Expected BugemonNotInTeamException");
        } catch (BugemonNotInTeamException e) {
            assertTrue(e.getMessage().contains("exceflam"));
        }
    }

    @Test
    public void testGetAttacksOfUnknownIdThrows() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu"));
        try {
            team.getAttacksOf("unknown_id");
            fail("Expected BugemonNotInTeamException");
        } catch (BugemonNotInTeamException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testAllAttacksHaveNonNullId() throws BugemonNotFoundException {
        TeamBugemon team = new TeamBugemon("test", Arrays.asList("florachu", "exceflam", "commitide"));
        for (Attack attack : team.getAllAttacks()) {
            assertNotNull(attack.getId());
        }
    }
}