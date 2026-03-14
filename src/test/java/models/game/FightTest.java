package models.game;

import junit.framework.TestCase;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FightTest extends TestCase {

    private Fight fightPlayerHighInitiative;

    private Fight fightEnemyHighInitiative;

    @Before
    public void setUp() {
        TeamBugemon teamHighInit = new TeamBugemon(List.of("pyricore", "loopine", "mergeau"));
        TeamBugemon teamLowInit  = new TeamBugemon(List.of("bouldax", "granitron", "rockachu"));
        TeamBugemon teamEnemy    = new TeamBugemon(List.of("pyricore", "moussil", "pebblit"));

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
    public void testDeepCopyDoesNotMutateOriginalTeam() {
        TeamBugemon original = new TeamBugemon(List.of("commitide", "moussil", "pebblit"));
        int hpBefore = original.getTeam().getFirst().getPv();

        Fight fight = new Fight(original, new TeamBugemon(List.of("pyricore", "moussil", "pebblit")));
        fight.playingAttackWaiting(fight.getPlayingBugemonAttack(0));

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
        fightPlayerHighInitiative.playingAttackWaiting(fightPlayerHighInitiative.getPlayingBugemonAttack(0));
        assertFalse(fightPlayerHighInitiative.isPlayerTurn());
    }

    @Test
    public void testTurnSwitchesBackAfterEnemyAttack() {
        fightPlayerHighInitiative.playingAttackWaiting(fightPlayerHighInitiative.getPlayingBugemonAttack(0));
        assertFalse(fightPlayerHighInitiative.isPlayerTurn());
        fightPlayerHighInitiative.playingAttackWaiting(fightPlayerHighInitiative.getPlayingBugemonRandomAttack());
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
        fightPlayerHighInitiative.playingAttackWaiting(fightPlayerHighInitiative.getPlayingBugemonAttack(0));
        assertTrue(enemy.getcurrenPv() < hpBefore);
    }

    @Test
    public void testEnemyAttackReducesPlayerHp() {
        fightPlayerHighInitiative.forceEnemyTurn();
        Bugemon player = fightPlayerHighInitiative.getOnFieldPlayerBugemon();
        int hpBefore = player.getcurrenPv();
        fightPlayerHighInitiative.playingAttackWaiting(fightPlayerHighInitiative.getPlayingBugemonRandomAttack());
        assertTrue(player.getcurrenPv() < hpBefore);
    }

    @Test
    public void testActionResultTextNotNullAfterAttack() {
        fightPlayerHighInitiative.playingAttackWaiting(fightPlayerHighInitiative.getPlayingBugemonAttack(0));
        assertNotNull(fightPlayerHighInitiative.getActionResultText());
    }

    @Test
    public void testGetPlayingBugemonAttackReturnsCorrectAttack() {
        Bugemon onField = fightPlayerHighInitiative.getOnFieldPlayerBugemon();
        assertEquals(onField.getAttacks().get(0), fightPlayerHighInitiative.getPlayingBugemonAttack(0));
        assertEquals(onField.getAttacks().get(1), fightPlayerHighInitiative.getPlayingBugemonAttack(1));
        assertEquals(onField.getAttacks().get(2), fightPlayerHighInitiative.getPlayingBugemonAttack(2));
    }


    @Test
    public void testChangePlayerOnField() {
        Bugemon newBugemon = fightPlayerHighInitiative.getTeamPlayer().getTeam().get(1);
        fightPlayerHighInitiative.changePlayerOnField(newBugemon);
        assertEquals(newBugemon, fightPlayerHighInitiative.getOnFieldPlayerBugemon());
    }

    @Test
    public void testChangeEnemyOnField() {
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