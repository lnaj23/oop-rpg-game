package models.game;

import exceptions.BugemonNotFoundException;
import exceptions.BugemonNotInTeamException;
import junit.framework.TestCase;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import org.junit.Before;
import org.junit.Test;
import services.dataprovider.BugemonProvider;

import java.util.List;

public class FightTest extends TestCase {

    private Fight fightPlayerHighInitiative;
    private Fight fightEnemyHighInitiative;
    private BugemonProvider bugemonProvider = new BugemonProvider();

    @Before
    public void setUp() throws BugemonNotFoundException, BugemonNotInTeamException {
        TeamBugemon teamHighInit = new TeamBugemon(bugemonProvider.getListBugemon(List.of("pyricore", "loopine", "mergeau")));
        TeamBugemon teamLowInit  = new TeamBugemon(bugemonProvider.getListBugemon(List.of("bouldax", "granitron", "rockachu")));
        TeamBugemon teamEnemy    = new TeamBugemon(bugemonProvider.getListBugemon(List.of("pyricore", "moussil", "pebblit")));

        fightPlayerHighInitiative = new Fight(teamHighInit, teamEnemy);
        fightEnemyHighInitiative  = new Fight(teamLowInit,  teamEnemy);
    }

    @Test
    public void testInitialTurnIsPlayer() {
        assertTrue(fightPlayerHighInitiative.isPlayerTurn());
    }

    @Test
    public void testOnFieldBugemonsInitialisedAtConstruction() {
        assertNotNull(fightPlayerHighInitiative.getOnFieldPlayerBugemon());
        assertNotNull(fightPlayerHighInitiative.getOnFieldEnemyBugemon());
    }

    @Test
    public void testPlayerBugemonsMarkedAsPlayer() {
        fightPlayerHighInitiative.getTeamPlayer().getTeam()
                .forEach(b -> assertTrue(b.getIsPlayer()));
    }

    @Test
    public void testEnemyBugemonsMarkedAsNotPlayer() {
        fightPlayerHighInitiative.getTeamEnemy().getTeam()
                .forEach(b -> assertFalse(b.getIsPlayer()));
    }

    @Test
    public void testBugemonsHpInitialisedToMax() {
        fightPlayerHighInitiative.getTeamPlayer().getTeam()
                .forEach(b -> assertEquals(b.getPv(), b.getcurrenPv()));
        fightPlayerHighInitiative.getTeamEnemy().getTeam()
                .forEach(b -> assertEquals(b.getPv(), b.getcurrenPv()));
    }

    @Test
    public void testDeepCopyDoesNotMutateOriginalTeam() throws BugemonNotFoundException, BugemonNotInTeamException {
        TeamBugemon original = new TeamBugemon(bugemonProvider.getListBugemon(List.of("commitide", "moussil", "pebblit")));
        int hpBefore = original.getTeam().getFirst().getPv();
        Fight fight = new Fight(original, new TeamBugemon(bugemonProvider.getListBugemon(List.of("pyricore", "moussil", "pebblit"))));
        fight.playerAttack(0);
        assertEquals(hpBefore, original.getTeam().getFirst().getPv());
    }

    @Test
    public void testPlayerCanStartWhenHigherInitiative() {
        assertTrue(fightPlayerHighInitiative.playerCanStart());
    }

    @Test
    public void testPlayerCannotStartWhenLowerInitiative() {
        assertFalse(fightEnemyHighInitiative.playerCanStart());
    }

    @Test
    public void testTurnSwitchesAfterPlayerAttack() {
        assertTrue(fightPlayerHighInitiative.isPlayerTurn());
        fightPlayerHighInitiative.playerAttack(0);
        assertFalse(fightPlayerHighInitiative.isPlayerTurn());
    }

    @Test
    public void testTurnSwitchesBackAfterEnemyAttackFromIAChoice() {
        fightPlayerHighInitiative.playerAttack(0);
        assertFalse(fightPlayerHighInitiative.isPlayerTurn());
        fightPlayerHighInitiative.enemyAttackFromIAChoice();
        assertTrue(fightPlayerHighInitiative.isPlayerTurn());
    }

    @Test
    public void testForceEnemyTurnSetsTurnToEnemy() {
        assertTrue(fightPlayerHighInitiative.isPlayerTurn());
        fightPlayerHighInitiative.forceEnemyTurn();
        assertFalse(fightPlayerHighInitiative.isPlayerTurn());
    }

    @Test
    public void testPlayerAttackReducesEnemyHp() {
        Bugemon enemy = fightPlayerHighInitiative.getOnFieldEnemyBugemon();
        int hpBefore = enemy.getcurrenPv();
        fightPlayerHighInitiative.playerAttack(0);
        assertTrue(enemy.getcurrenPv() < hpBefore);
    }

    @Test
    public void testEnemyAttackFromIAChoiceReducesPlayerHp() {
        fightPlayerHighInitiative.forceEnemyTurn();
        Bugemon player = fightPlayerHighInitiative.getOnFieldPlayerBugemon();
        int hpBefore = player.getcurrenPv();
        fightPlayerHighInitiative.enemyAttackFromIAChoice();
        assertTrue(player.getcurrenPv() < hpBefore);
    }

    @Test
    public void testActionResultTextNotNullAfterAttack() {
        fightPlayerHighInitiative.playerAttack(0);
        assertNotNull(fightPlayerHighInitiative.getActionResultText());
    }


    @Test
    public void testChangeEnemyOnField() throws BugemonNotInTeamException {
        Bugemon newBugemon = fightPlayerHighInitiative.getTeamEnemy().getTeam().get(1);
        fightPlayerHighInitiative.changeEnemyOnField(newBugemon);
        assertEquals(newBugemon, fightPlayerHighInitiative.getOnFieldEnemyBugemon());
    }

    @Test
    public void testIsOnFieldEnemyBugemonKO() {
        Bugemon enemy = fightPlayerHighInitiative.getOnFieldEnemyBugemon();
        enemy.takeDamage(enemy.getcurrenPv());
        assertTrue(fightPlayerHighInitiative.isOnFieldEnemyBugemonKO());
    }

    @Test
    public void testIsOnFieldPlayerBugemonKO() {
        Bugemon player = fightPlayerHighInitiative.getOnFieldPlayerBugemon();
        player.takeDamage(player.getcurrenPv());
        assertTrue(fightPlayerHighInitiative.isOnFieldPlayerBugemonKO());
    }

    @Test
    public void testBattleNotOverAtStart() {
        assertFalse(fightPlayerHighInitiative.isBattleOver());
    }

    @Test
    public void testBattleOverWhenAllEnemyKO() {
        fightPlayerHighInitiative.getTeamEnemy().getTeam()
                .forEach(b -> b.takeDamage(b.getcurrenPv()));
        assertTrue(fightPlayerHighInitiative.isBattleOver());
    }

    @Test
    public void testBattleOverWhenAllPlayerKO() {
        fightPlayerHighInitiative.getTeamPlayer().getTeam()
                .forEach(b -> b.takeDamage(b.getcurrenPv()));
        assertTrue(fightPlayerHighInitiative.isBattleOver());
    }

    @Test
    public void testPlayerWonWhenAllEnemyKO() {
        fightPlayerHighInitiative.getTeamEnemy().getTeam()
                .forEach(b -> b.takeDamage(b.getcurrenPv()));
        assertTrue(fightPlayerHighInitiative.playerWon());
    }

    @Test
    public void testPlayerLostWhenAllPlayerKO() {
        fightPlayerHighInitiative.getTeamPlayer().getTeam()
                .forEach(b -> b.takeDamage(b.getcurrenPv()));
        assertFalse(fightPlayerHighInitiative.playerWon());
    }

    @Test
    public void testAvailablePlayerBugemonsAllAlive() {
        assertEquals(3, fightPlayerHighInitiative.getAvailablePlayerBugemons().size());
    }

    @Test
    public void testAvailablePlayerBugemonsDecreasesAfterKO() {
        fightPlayerHighInitiative.getTeamPlayer().getTeam().getFirst()
                .takeDamage(fightPlayerHighInitiative.getTeamPlayer().getTeam().getFirst().getcurrenPv());
        List<Bugemon> available = fightPlayerHighInitiative.getAvailablePlayerBugemons();
        assertEquals(2, available.size());
    }

    @Test
    public void testAvailableEnemyBugemonsAllAlive() {
        assertEquals(3, fightPlayerHighInitiative.getAvailableEnemyBugemons().size());
    }


}