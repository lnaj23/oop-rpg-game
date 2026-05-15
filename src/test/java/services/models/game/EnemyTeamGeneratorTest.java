package services.models.game;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.app.GameStateHolder;
import services.controller.dataprovider.BugemonProvider;

import java.util.List;

import java.util.Random;

public class EnemyTeamGeneratorTest extends TestCase {

    private final BugemonProvider bugemonProvider = new BugemonProvider();

    public EnemyTeamGeneratorTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    @Before
    public void setUp() throws BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon playerTeam = new TeamBugemon(
                bugemonProvider.getListBugemon(List.of("florachu", "exceflam", "commitide")));
        GameStateHolder.getInstance().setCurrentTeamPlayer(playerTeam);
        EnemyTeamGenerator.setRandom(new Random(42));
    }

    @After
    public void tearDown() {
        EnemyTeamGenerator.setRandom(new Random());
    }

    @Test
    public void testGenerateEnemyTeamMatchesPlayerTeamSize()
            throws AttackNotFoundException, JsonResourceNotFoundException {
        TeamBugemon enemy = EnemyTeamGenerator.generateEnemyTeam();
        assertEquals(GameStateHolder.getInstance().getCurrentTeamPlayer().getSize(),
                enemy.getSize());
    }

    @Test
    public void testGenerateEnemyTeamRespectsCustomSize()
            throws AttackNotFoundException, BugemonNotFoundException, JsonResourceNotFoundException {
        TeamBugemon playerTeam = new TeamBugemon(
                bugemonProvider.getListBugemon(List.of("florachu", "exceflam")));
        GameStateHolder.getInstance().setCurrentTeamPlayer(playerTeam);

        TeamBugemon enemy = EnemyTeamGenerator.generateEnemyTeam();
        assertEquals(2, enemy.getSize());
    }

    @Test
    public void testGenerateEnemyTeamReturnsNonNullBugemons()
            throws AttackNotFoundException, JsonResourceNotFoundException {
        TeamBugemon enemy = EnemyTeamGenerator.generateEnemyTeam();
        for (Bugemon b : enemy.getTeam()) {
            assertNotNull(b);
            assertNotNull(b.getId());
        }
    }

    @Test
    public void testGenerateEnemyTeamInitializesPv()
            throws AttackNotFoundException, JsonResourceNotFoundException {
        TeamBugemon enemy = EnemyTeamGenerator.generateEnemyTeam();
        for (Bugemon b : enemy.getTeam()) {
            assertEquals("current pv not initialized", b.getPv(), b.getCurrentPv());
        }
    }

    @Test
    public void testGenerateEnemyTeamIsDeterministicForSameSeed()
            throws AttackNotFoundException, JsonResourceNotFoundException {
        EnemyTeamGenerator.setRandom(new Random(42));
        TeamBugemon team1 = EnemyTeamGenerator.generateEnemyTeam();
        EnemyTeamGenerator.setRandom(new Random(42));
        TeamBugemon team2 = EnemyTeamGenerator.generateEnemyTeam();

        assertEquals(team1.getSize(), team2.getSize());
        for (int i = 0; i < team1.getSize(); i++) {
            assertEquals("same seed should pick same bugemon",
                    team1.getTeam().get(i).getId(), team2.getTeam().get(i).getId());
        }
    }

    @Test
    public void testGenerateEnemyTeamReturnsFreshCopies()
            throws AttackNotFoundException, JsonResourceNotFoundException {
        TeamBugemon enemy1 = EnemyTeamGenerator.generateEnemyTeam();
        Bugemon b = enemy1.getTeam().get(0);
        b.takeDamage(5);

        TeamBugemon enemy2 = EnemyTeamGenerator.generateEnemyTeam();
        for (Bugemon b2 : enemy2.getTeam()) {
            assertEquals("state leaked between generations", b2.getPv(), b2.getCurrentPv());
        }
    }
}