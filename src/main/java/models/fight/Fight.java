package models.fight;

import exceptions.BugemonNotInTeamException;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import models.fight.AttackActionResult;
import models.fight.EffectEvent;
import models.fight.Effectiveness;
import models.game.Item;
import services.models.fight.EffectManager;
import services.models.fight.ItemEffectApplier;
import services.models.fight.TypeEffectivenessService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a fight between two Bugémon teams.
 */
public class Fight {

    private final TeamBugemon teamPlayer;
    private final TeamBugemon teamEnemy;
    private boolean isPlayerTurn;
    private AttackActionResult lastActionResult;
    private final List<Effectiveness> predictedEffectiveness = new ArrayList<>();
    private final Random random = new Random();

    /**
     * @param teamPlayer the player's team
     * @param teamEnemy  the enemy team
     * @throws BugemonNotInTeamException if the first Bugémon cannot be placed on the field
     */
    public Fight(TeamBugemon teamPlayer, TeamBugemon teamEnemy) throws BugemonNotInTeamException {
        this.teamPlayer = teamPlayer;
        this.teamEnemy  = teamEnemy;
        this.teamPlayer.getTeam().forEach(Bugemon::setIsPlayerToTrue);
        this.teamEnemy.getTeam().forEach(Bugemon::setIsPlayerToFalse);
        this.teamPlayer.changeOnFieldBugemon(this.teamPlayer.getTeam().getFirst());
        for (Bugemon bug : this.teamPlayer.getTeam()) {
            bug.initPv();
            bug.resetAllModifiers();
        }
        for (Bugemon bug : this.teamPlayer.getTeam()) {
            if (!bug.isKO()) {
                this.teamPlayer.changeOnFieldBugemon(bug);
                break;
            }
        }
        this.teamEnemy.changeOnFieldBugemon(this.teamEnemy.getTeam().getFirst());
        this.isPlayerTurn = true;
    }

    /** Executes the player's attack at the given index. */
    public void playerAttack(int attackIndex) {
        attackerAttackDefender(getAttack(attackIndex));
    }

    /** Executes a random enemy attack. */
    public void enemyAttackFromIAChoice() {
        attackerAttackDefender(getRandomAttack());
    }

    private void attackerAttackDefender(Attack attack) {
        Bugemon attacker         = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        Bugemon defender         = isPlayerTurn ? teamEnemy.getOnFieldBugemon()  : teamPlayer.getOnFieldBugemon();
        TeamBugemon attackerTeam = isPlayerTurn ? teamPlayer : teamEnemy;
        TeamBugemon defenderTeam = isPlayerTurn ? teamEnemy  : teamPlayer;

        int damage = attacker.attack(defender, attack);
        List<EffectEvent> effects = EffectManager.handleEffect(attacker, defender, attackerTeam, defenderTeam, attack);

        lastActionResult = new AttackActionResult(
                attacker.getName(),
                attack.getName(),
                defender.getName(),
                damage,
                defender.isKO(),
                TypeEffectivenessService.getEffectiveness(attack.getType(), defender.getType()),
                effects
        );

        switchRole();
    }

    /** @throws BugemonNotInTeamException if the Bugémon is not in the player's team */
    public void changePlayerOnField(String id) throws BugemonNotInTeamException {
        Bugemon bugemon = teamPlayer.getTeam().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BugemonNotInTeamException(id));
        teamPlayer.changeOnFieldBugemon(bugemon);
    }

    /** @throws BugemonNotInTeamException if the Bugémon is not in the enemy's team */
    public void changeEnemyOnField(Bugemon bugemon) throws BugemonNotInTeamException {
        teamEnemy.changeOnFieldBugemon(bugemon);
    }

    /** @return {@code true} if the player's total initiative is strictly greater */
    public boolean playerCanStart() {
        int playerInit = 0, enemyInit = 0;
        for (Bugemon bug : teamPlayer.getTeam()) playerInit += bug.getInitiative();
        for (Bugemon bug : teamEnemy.getTeam())  enemyInit  += bug.getInitiative();
        return playerInit > enemyInit;
    }

    /** Recomputes turn order based on current on-field Bugémons' initiative. */
    public void recomputeTurnOrder() {
        this.isPlayerTurn = teamPlayer.getOnFieldBugemon().getInitiative()
                >= teamEnemy.getOnFieldBugemon().getInitiative();
    }

    /** @return {@code true} if the player's on-field Bugémon has equal or greater initiative */
    public boolean playerGoesFirstAfterSwitch() {
        return teamPlayer.getOnFieldBugemon().getInitiative()
                >= teamEnemy.getOnFieldBugemon().getInitiative();
    }

    /** Forces the turn to the enemy side. */
    public void forceEnemyTurn() { isPlayerTurn = false; }

    /** @return {@code true} if it is the player's turn */
    public boolean isPlayerTurn() { return isPlayerTurn; }

    public boolean isBattleOver() {
        return !teamEnemy.isTeamStillStanding() || !teamPlayer.isTeamStillStanding();
    }

    public boolean playerWon() { return !teamEnemy.isTeamStillStanding(); }

    public boolean isOnFieldPlayerBugemonKO() { return teamPlayer.isOnFieldBugemonKO(); }

    public boolean isOnFieldEnemyBugemonKO() { return teamEnemy.isOnFieldBugemonKO(); }

    public TeamBugemon getTeamPlayer() { return teamPlayer; }
    public TeamBugemon getTeamEnemy()  { return teamEnemy; }
    public Bugemon getOnFieldPlayerBugemon() { return teamPlayer.getOnFieldBugemon(); }
    public Bugemon getOnFieldEnemyBugemon() { return teamEnemy.getOnFieldBugemon(); }
    public List<Bugemon> getAvailablePlayerBugemons() { return teamPlayer.getAvailableBugemons(); }
    public List<Bugemon> getAvailableEnemyBugemons()  { return teamEnemy.getAvailableBugemons(); }

    public AttackActionResult getLastActionResult() { return lastActionResult; }

    /**
     * @return the number of Bugemons in the enemy team for XP computation enemy_count factor
     */
    public int getEnemyTeamSize() {
        return teamEnemy.getTeam().size();
    }

    public void computePredictedEffectiveness() {
        predictedEffectiveness.clear();
        String targetType = teamEnemy.getOnFieldBugemonType();
        for (Attack attack : teamPlayer.getOnFieldBugemonAttacks()) {
            predictedEffectiveness.add(
                    TypeEffectivenessService.getEffectiveness(attack.getType(), targetType)
            );
        }
    }

    public List<Effectiveness> getPredictedEffectiveness() {
        return Collections.unmodifiableList(predictedEffectiveness);
    }

    private Attack getAttack(int index) {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.getAttackFromIndex(index);
    }

    private Attack getRandomAttack() {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.chooseRandomAttack();
    }

    private void switchRole() { isPlayerTurn = !isPlayerTurn; }

    public void changeOnFieldEnemyBugemonRandomly() throws BugemonNotInTeamException {
        List<Bugemon> available = getAvailableEnemyBugemons();
        if (available.isEmpty()) return;
        Bugemon randomBug = available.get(random.nextInt(available.size()));
        changeEnemyOnField(randomBug);
    }

    /**
     * Applies the effect of an item to the player's on-field Bugémon.
     */
    public void applyItem(Item item) {
        ItemEffectApplier.ApplyItems(teamPlayer.getOnFieldBugemon(), item);
    }
}