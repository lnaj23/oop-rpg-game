package models.bugemon;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import org.junit.Test;
import exceptions.BugemonNotFoundException;
import exceptions.BugemonNotInTeamException;
import services.dataprovider.BugemonProvider;

import java.util.Arrays;
import java.util.List;

public class TeamBugemonTest extends TestCase {

    private final BugemonProvider bugemonProvider = new BugemonProvider();

    public TeamBugemonTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    @Test
    public void testConstructorNotNull() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide")));
        assertNotNull(team);
    }

    @Test
    public void testConstructorWithUnknownIdThrows() throws JsonResourceNotFoundException{
        try {
            bugemonProvider.getListBugemon(Arrays.asList("unknown_id"));
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetSizeCorrect() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide")));
        assertEquals(3, team.getSize());
    }

    @Test
    public void testGetTeamNotNull() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam")));
        assertNotNull(team.getTeam());
    }

    @Test
    public void testGetTeamCorrectSize() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam")));
        assertEquals(2, team.getTeam().size());
    }

    @Test
    public void testGetTeamNoNullElements() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide")));
        for (Bugemon bug : team.getTeam()) assertNotNull(bug);
    }

    @Test
    public void testAddBugemonIncreasesSize() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu")));
        int sizeBefore = team.getSize();
        team.addBugemon(bugemonProvider.getListBugemon(Arrays.asList("exceflam")).get(0));
        assertEquals(sizeBefore + 1, team.getSize());
    }

    @Test
    public void testGetAllAttacksNotNull() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam")));
        assertNotNull(team.getAllAttacks());
    }

    @Test
    public void testGetAllAttacksNotEmpty() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam")));
        assertFalse(team.getAllAttacks().isEmpty());
    }

    @Test
    public void testGetAllAttacksTotalCount() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide")));
        int expected = team.getTeam().stream().mapToInt(b -> b.getAttackNamesAsList().size()).sum();
        assertEquals(expected, team.getAllAttacks().size());
    }

    @Test
    public void testGetAllAttacksNoNullElement() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide")));
        for (Attack attack : team.getAllAttacks()) assertNotNull(attack);
    }

    @Test
    public void testHasNullAttacksReturnsFalse() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide")));
        assertFalse(team.hasNullAttacks());
    }

    @Test
    public void testGetAttacksOfValidBugemon() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam")));
        List<Attack> attacks = team.getAttacksOf("florachu");
        assertNotNull(attacks);
        assertEquals(team.getTeam().get(0).getAttackNamesAsList().size(), attacks.size());
    }

    @Test
    public void testGetAttacksOfBugemonNoNullAttack() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam")));
        for (Attack attack : team.getAttacksOf("florachu")) assertNotNull(attack);
    }

    @Test
    public void testGetAttacksOfBugemonNotInTeamThrows() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu")));
        try {
            team.getAttacksOf("exceflam");
            fail("Expected BugemonNotInTeamException");
        } catch (BugemonNotInTeamException e) {
            assertTrue(e.getMessage().contains("exceflam"));
        }
    }

    @Test
    public void testAllAttacksHaveNonNullId() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon team = new TeamBugemon("test", bugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide")));
        for (Attack attack : team.getAllAttacks()) assertNotNull(attack.getId());
    }
}
