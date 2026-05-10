package services.fight;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.BugemonNotInTeamException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import models.fight.Fight;
import org.junit.Before;
import org.junit.Test;
import services.controller.dataprovider.BugemonProvider;

import java.util.List;

public class EffectManagerTest extends TestCase {
    // mergeau a : [0] courant_croise  (adversaire, initiative, -8,  permanent)
    //             [1] vague_rapide    (lanceur,    initiative, +10, 1_tour)
    //             [2] maree_stable    (lanceur,    defense,    +6,  permanent)
    // loopine a : [0] racines_vives   (lanceur,    defense,    +5,  permanent)
    //             [1] pollen_sournois (adversaire, initiative, -10, 1_tour)
    //             [2] feuille_tranchante (aucun effet)
    private Fight fightMergeauVsLoopine;
    private BugemonProvider bugemonProvider = new BugemonProvider();

    public EffectManagerTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    @Before
    public void setUp() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        TeamBugemon teamPlayer = new TeamBugemon(bugemonProvider.getListBugemon(List.of("mergeau")));
        TeamBugemon teamEnemy  = new TeamBugemon(bugemonProvider.getListBugemon(List.of("loopine")));
        fightMergeauVsLoopine  = new Fight(teamPlayer, teamEnemy);
    }

    // Soin

    @Test
    public void testHealRestoresPlayerHp() throws BugemonNotFoundException, BugemonNotInTeamException, JsonResourceNotFoundException {
        // commitide a pluie_calme (index 2) = soin lanceur +15 PV
        TeamBugemon teamPlayer = new TeamBugemon(bugemonProvider.getListBugemon(List.of("commitide")));
        TeamBugemon teamEnemy  = new TeamBugemon(bugemonProvider.getListBugemon(List.of("loopine")));
        Fight fight = new Fight(teamPlayer, teamEnemy);

        Bugemon player = fight.getOnFieldPlayerBugemon();
        player.takeDamage(20);
        int hpAfterDamage = player.getCurrenPv();

        fight.playerAttack(2); // pluie_calme → soin +15

        assertEquals(hpAfterDamage + 15, player.getCurrenPv());
    }

    @Test
    public void testHealDoesNotExceedMaxHp() {
        Bugemon player = fightMergeauVsLoopine.getOnFieldPlayerBugemon();
        int maxHp = player.getPv();
        player.getStats().heal(9999);
        assertEquals(maxHp, player.getCurrenPv());
    }

    // Stat modifier permanement

    @Test
    public void testPermanentDefenseModifierOnAttacker() {
        // maree_stable (index 2) = lanceur defense +6 permanent
        Bugemon player = fightMergeauVsLoopine.getOnFieldPlayerBugemon();
        int defenseBefore = player.getDefense();
        fightMergeauVsLoopine.playerAttack(2); // maree_stable
        assertEquals(defenseBefore + 6, player.getDefense());
    }

    @Test
    public void testPermanentInitiativeModifierOnTarget() {
        // courant_croise (index 0) = adversaire initiative -8 permanent
        Bugemon enemy = fightMergeauVsLoopine.getOnFieldEnemyBugemon();
        int initiativeBefore = enemy.getInitiative();
        fightMergeauVsLoopine.playerAttack(0); // courant_croise
        assertEquals(initiativeBefore - 8, enemy.getInitiative());
    }

    @Test
    public void testPermanentModifierPersistsAfterMultipleTurns() {
        // courant_croise = adversaire initiative -8 permanent
        Bugemon enemy = fightMergeauVsLoopine.getOnFieldEnemyBugemon();
        int initiativeBefore = enemy.getInitiative();
        fightMergeauVsLoopine.playerAttack(0); // courant_croise
        fightMergeauVsLoopine.enemyAttackFromIAChoice();   // tour ennemi
        // le modificateur doit toujours être là
        assertEquals(initiativeBefore - 8, enemy.getInitiative());
    }

    // Stat modifier pour 1 tour

    @Test
    public void testTemporaryModifierAppliedAfterAttack() {
        // vague_rapide (index 1) = lanceur initiative +10 pendant 1 tour
        Bugemon player = fightMergeauVsLoopine.getOnFieldPlayerBugemon();
        int initiativeBefore = player.getInitiative();
        fightMergeauVsLoopine.playerAttack(1); // vague_rapide
        assertEquals(initiativeBefore + 10, player.getInitiative());
    }

    @Test
    public void testTemporaryModifierRemovedFromOngoingEffects() {
        // vague_rapide (index 1) = lanceur initiative +10 pendant 1 tour
        Bugemon player = fightMergeauVsLoopine.getOnFieldPlayerBugemon();
        fightMergeauVsLoopine.playerAttack(1); // vague_rapide
        fightMergeauVsLoopine.enemyAttackFromIAChoice();
        fightMergeauVsLoopine.playerAttack(0); // autre attaque pour tick
        // l'effet 1_tour doit avoir été retiré de la liste
        long temporaryEffects = player.getOngoingEffects().stream()
                .filter(ae -> ae.getTurnsLeft() != null)
                .count();
        assertEquals(0, temporaryEffects);
    }

    // Reset des modficiateurs

    @Test
    public void testResetAllModifiersClearsStats() {
        Bugemon player = fightMergeauVsLoopine.getOnFieldPlayerBugemon();
        int defenseBefore = player.getDefense();
        fightMergeauVsLoopine.playerAttack(2); // maree_stable defense +6
        player.resetAllModifiers();
        assertEquals(defenseBefore, player.getDefense());
    }

    @Test
    public void testResetAllModifiersClearsOngoingEffects() {
        Bugemon player = fightMergeauVsLoopine.getOnFieldPlayerBugemon();
        fightMergeauVsLoopine.playerAttack(2); // maree_stable
        player.resetAllModifiers();
        assertTrue(player.getOngoingEffects().isEmpty());
    }
}