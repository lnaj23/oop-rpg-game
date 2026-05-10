package models.bugemon;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.BugemonNotInTeamException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.fight.Fight;
import org.junit.Before;
import org.junit.Test;
import services.controller.dataprovider.BugemonProvider;

import java.util.List;


public class StatsTest extends TestCase {

    // florachu: PV=90, Atk=55, Def=40
    private Fight fight;
    private Bugemon florachu;
    private Stats stats;

    private BugemonProvider bugemonProvider = new BugemonProvider();

    public StatsTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    @Before
    public void setUp() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        TeamBugemon teamPlayer = new TeamBugemon(bugemonProvider.getListBugemon(List.of("florachu")));
        TeamBugemon teamEnemy  = new TeamBugemon(bugemonProvider.getListBugemon(List.of("loopine")));
        fight = new Fight(teamPlayer, teamEnemy);
        florachu = fight.getOnFieldPlayerBugemon();
        stats = florachu.getStats();
    }

    // Initial state

    @Test
    public void testInitialLevelIsOne() {
        assertEquals(1, stats.getLevel());
    }

    @Test
    public void testInitialXpIsZero() {
        assertEquals(0, stats.getXp());
    }

    @Test
    public void testXpThresholdLevel1() {
        // level 1 => 50 + 100 * 0 = 50
        assertEquals(50, stats.getXpThreshold());
    }

    @Test
    public void testXpThresholdLevel2() {
        stats.addXp(50);
        stats.levelUp();
        // level 2 => 50 + 100 * 1 = 150
        assertEquals(150, stats.getXpThreshold());
    }

    @Test
    public void testXpThresholdLevel3() {
        stats.addXp(50);
        stats.levelUp();
        stats.addXp(150);
        stats.levelUp();
        // level 3 => 50 + 100 * 2 = 250
        assertEquals(250, stats.getXpThreshold());
    }

    // Adding XP

    @Test
    public void testAddXp() {
        stats.addXp(30);
        assertEquals(30, stats.getXp());
        stats.addXp(20);
        assertEquals(50, stats.getXp());
    }

    // Level up

    @Test
    public void testLevelUpIncrementsLevel() {
        stats.addXp(50);
        stats.levelUp();
        assertEquals(2, stats.getLevel());
    }

    @Test
    public void testLevelUpConservesExcessXp() {
        stats.addXp(70); // threshold = 50, excess = 20
        stats.levelUp();
        assertEquals(20, stats.getXp());
    }

    @Test
    public void testLevelUpRestoresHpToMax() {
        stats.reducePv(50); // take some damage
        int hpAfterDamage = stats.getCurrentPv();
        stats.addXp(50);
        stats.levelUp();
        assertEquals(stats.getPv(), stats.getCurrentPv());
        assertTrue(stats.getCurrentPv() > hpAfterDamage);
    }

    // Permanent stat bonuses

    @Test
    public void testAddPermanentAttack() {
        int before = florachu.getAttaque();
        stats.addPermanentAttack(10);
        assertEquals(before + 10, florachu.getAttaque());
    }

    @Test
    public void testAddPermanentDefense() {
        int before = florachu.getDefense();
        stats.addPermanentDefense(10);
        assertEquals(before + 10, florachu.getDefense());
    }

    @Test
    public void testAddPermanentInitiative() {
        int before = florachu.getInitiative();
        stats.addPermanentInitiative(20);
        assertEquals(before + 20, florachu.getInitiative());
    }

    @Test
    public void testAddPermanentHp() {
        int pvBefore = florachu.getPv();
        stats.addPermanentHp(20);
        assertEquals(pvBefore + 20, florachu.getPv());
        assertEquals(florachu.getPv(), florachu.getCurrenPv());
    }
}