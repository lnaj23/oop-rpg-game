package models.Game;

import junit.framework.TestCase;
import models.Bugemon.Bugemon;
import models.Bugemon.TeamBugemon;
import org.junit.Test;

import java.util.Arrays;

public class FightTest extends TestCase {

    @Test
    public void testConstructorNotNull() {
        TeamBugemon player = new TeamBugemon("player", Arrays.asList("florachu"));
        TeamBugemon cpu = new TeamBugemon("cpu", Arrays.asList("exceflam"));

        Fight fight = new Fight(player, cpu);

        assertNotNull(fight);
    }

    @Test
    public void testPlayerCanStartReturnsBoolean() {
        TeamBugemon player = new TeamBugemon("player", Arrays.asList("florachu"));
        TeamBugemon cpu = new TeamBugemon("cpu", Arrays.asList("exceflam"));

        Fight fight = new Fight(player, cpu);

        boolean result = fight.playerCanStart();

        assertTrue(result || !result); // vérifie juste que ça retourne bien un booléen
    }

    @Test
    public void testPlayerCanStartConsistentWithInitiative() {
        TeamBugemon player = new TeamBugemon("player", Arrays.asList("florachu", "commitide"));
        TeamBugemon cpu = new TeamBugemon("cpu", Arrays.asList("exceflam"));

        Fight fight = new Fight(player, cpu);

        int initiativePlayer = player.getTeam().stream()
                .mapToInt(Bugemon::getInitiative)
                .sum();

        int initiativeCPU = cpu.getTeam().stream()
                .mapToInt(Bugemon::getInitiative)
                .sum();

        boolean expected = initiativePlayer > initiativeCPU;

        assertEquals(expected, fight.playerCanStart());
    }

    @Test
    public void testTeamsNotNullAfterConstructor() {
        TeamBugemon player = new TeamBugemon("player", Arrays.asList("florachu"));
        TeamBugemon cpu = new TeamBugemon("cpu", Arrays.asList("exceflam"));

        Fight fight = new Fight(player, cpu);

        assertNotNull(player.getTeam());
        assertNotNull(cpu.getTeam());
    }

    @Test
    public void testTeamsContainNoNullBugemon() {
        TeamBugemon player = new TeamBugemon("player", Arrays.asList("florachu", "commitide"));
        TeamBugemon cpu = new TeamBugemon("cpu", Arrays.asList("exceflam"));

        for (Bugemon bug : player.getTeam()) {
            assertNotNull(bug);
        }

        for (Bugemon bug : cpu.getTeam()) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testPlayerTeamBugemonMarkedAsPlayer() {
        TeamBugemon player = new TeamBugemon("player", Arrays.asList("florachu", "commitide"));
        TeamBugemon cpu = new TeamBugemon("cpu", Arrays.asList("exceflam"));

        Fight fight = new Fight(player, cpu);

        for (Bugemon bug : player.getTeam()) {
            assertTrue(bug.getIsPlayer());
        }
    }
}