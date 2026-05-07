package models.game;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.BugemonNotInTeamException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import org.junit.Test;
import services.models.dataprovider.BugemonProvider;

import java.util.List;


public class LevelUpBonusTest extends TestCase {

    private BugemonProvider bugemonProvider = new BugemonProvider();

    public LevelUpBonusTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    /**
     * Helper to get a fresh Bugemon from a Fight,
     * following the same pattern as EffectManagerTest.
     */
    private Bugemon createBugemon(String id) throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        TeamBugemon teamPlayer = new TeamBugemon(bugemonProvider.getListBugemon(List.of(id)));
        TeamBugemon teamEnemy  = new TeamBugemon(bugemonProvider.getListBugemon(List.of("loopine")));
        Fight fight = new Fight(teamPlayer, teamEnemy);
        return fight.getOnFieldPlayerBugemon();
    }

    /**
     * Verifies that a pure HP bonus correctly increases max HP.
     */
    @Test
    public void testApplyHpBonus() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        Bugemon bugemon = createBugemon("florachu");
        int pvBefore = bugemon.getPv();

        LevelUpBonus bonus = new LevelUpBonus(20, 0, 0, 0);
        bonus.applyTo(bugemon);

        assertEquals(pvBefore + 20, bugemon.getPv());
    }

    /**
     * Verifies that a pure attack bonus correctly increases attack.
     */
    @Test
    public void testApplyAttackBonus() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        Bugemon bugemon = createBugemon("florachu");
        int atkBefore = bugemon.getAttaque();

        LevelUpBonus bonus = new LevelUpBonus(0, 10, 0, 0);
        bonus.applyTo(bugemon);

        assertEquals(atkBefore + 10, bugemon.getAttaque());
    }

    /**
     * Verifies that a pure defense bonus correctly increases defense.
     */
    @Test
    public void testApplyDefenseBonus() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        Bugemon bugemon = createBugemon("exceflam");
        int defBefore = bugemon.getDefense();

        LevelUpBonus bonus = new LevelUpBonus(0, 0, 10, 0);
        bonus.applyTo(bugemon);

        assertEquals(defBefore + 10, bugemon.getDefense());
    }

    /**
     * Verifies that a mixed bonus (attack + defense) applies correctly.
     */
    @Test
    public void testApplyMixedBonus() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        Bugemon bugemon = createBugemon("exceflam");
        int atkBefore = bugemon.getAttaque();
        int defBefore = bugemon.getDefense();

        LevelUpBonus bonus = new LevelUpBonus(0, 5, 5, 0);
        bonus.applyTo(bugemon);

        assertEquals(atkBefore + 5, bugemon.getAttaque());
        assertEquals(defBefore + 5, bugemon.getDefense());
    }

    /**
     * Verifies that toString() produces readable text for pure HP.
     */
    @Test
    public void testToStringPureHp() {
        LevelUpBonus bonus = new LevelUpBonus(20, 0, 0, 0);
        assertEquals("+20 HP", bonus.toString());
    }

    /**
     * Verifies that toString() with a mixed bonus displays both stats.
     */
    @Test
    public void testToStringMixed() {
        LevelUpBonus bonus = new LevelUpBonus(0, 5, 5, 0);
        assertEquals("+5 Attaque, +5 Défense", bonus.toString());
    }
}