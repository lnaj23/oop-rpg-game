package ulb.models.Bugemon;

import junit.framework.TestCase;
import org.junit.Test;
import ulb.exceptions.BugemonNotFoundException;
import ulb.exceptions.BugemonNotInTeamException;

import java.util.Arrays;
import java.util.List;

public class TeamBugemonTest extends TestCase {

    @Test
    public void testConstructorNotNull() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        assertNotNull(team);
    }

    @Test
    public void testConstructorWithUnknownIdThrows() {
        try {
            new TeamBugemon(Arrays.asList("unknown_id"));
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
    public void testGetSizeCorrect() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        assertEquals(3, team.getSize());
    }

    @Test
    public void testGetTeamNotNull() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam"));
        assertNotNull(team.getTeam());
    }

    @Test
    public void testGetTeamCorrectSize() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam"));
        assertEquals(2, team.getTeam().size());
    }

    @Test
    public void testGetTeamNoNullElements() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        for (Bugemon bug : team.getTeam()) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testAddBugemonIncreasesSize() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu"));
        int sizeBefore = team.getSize();
        team.addBugemon(BugemonProvider.getListBugemon(Arrays.asList("exceflam")).get(0));
        assertEquals(sizeBefore + 1, team.getSize());
    }

    @Test
    public void testGetAllAttacksNotNull() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam"));
        assertNotNull(team.getAllAttacks());
    }

    @Test
    public void testGetAllAttacksNotEmpty() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam"));
        assertFalse(team.getAllAttacks().isEmpty());
    }

    @Test
    public void testGetAllAttacksTotalCount() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        int expected = team.getTeam().stream().mapToInt(b -> b.getAttackNames().size()).sum();
        assertEquals(expected, team.getAllAttacks().size());
    }

    @Test
    public void testGetAllAttacksNoNullElement() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        for (Attack attack : team.getAllAttacks()) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testHasNullAttacksReturnsFalse() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        assertFalse(team.hasNullAttacks());
    }

    @Test
    public void testGetAttacksOfValidBugemon() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam"));
        List<Attack> attacks = team.getAttacksOf("florachu");
        assertNotNull(attacks);
        assertEquals(team.getTeam().get(0).getAttackNames().size(), attacks.size());
    }

    @Test
    public void testGetAttacksOfBugemonNoNullAttack() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam"));
        List<Attack> attacks = team.getAttacksOf("florachu");
        for (Attack attack : attacks) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testGetAttacksOfBugemonNotInTeamThrows() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu"));
        try {
            team.getAttacksOf("exceflam");
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotInTeamException e) {
            assertTrue(e.getMessage().contains("exceflam"));
        }
    }

    @Test
    public void testGetAttacksOfUnknownIdThrows() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu"));
        try {
            team.getAttacksOf("unknown_id");
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotInTeamException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testPrintTeamDoesNotThrow() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam"));
        team.printTeam();
    }

    @Test
    public void testAllAttacksHaveNonNullId() {
        TeamBugemon team = new TeamBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        for (Attack attack : team.getAllAttacks()) {
            assertNotNull(attack.getId());
        }
    }
}