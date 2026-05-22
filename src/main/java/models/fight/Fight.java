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
 * Core model of a single fight between the player's team and an enemy team.
 * <p>
 * A {@code Fight} owns both {@link TeamBugemon}s (already deep-copied by the
 * caller, so mutating their Bugémons here never affects the saved teams) and
 * drives the turn-based combat loop. It exposes the queries the controller needs
 * to orchestrate the fight but holds no UI logic: outcomes are returned as
 * structured data ({@link AttackActionResult}, {@link EffectEvent}), never as
 * display strings.
 * </p>
 * <p>
 * Turn ownership is the single source of truth of this class: the
 * {@code isPlayerTurn} flag alone decides who is attacking. The attacker and
 * defender are derived from each team's current on-field Bugémon at call time
 * rather than kept in swapped "playing"/"waiting" references — this avoids the
 * desynchronisation bugs that role-swapping references used to cause.
 * </p>
 */
public class Fight {

    private final TeamBugemon teamPlayer;
    private final TeamBugemon teamEnemy;
    private boolean isPlayerTurn;
    private AttackActionResult lastActionResult;
    private final List<Effectiveness> predictedEffectiveness = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Sets up the fight: tags every Bugémon as player- or enemy-owned, restores
     * full HP and clears all stat modifiers on the player's team, places the
     * first non-KO player Bugémon and the enemy's first Bugémon on the field,
     * and gives the opening turn to the player.
     *
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

    /**
     * Executes the on-field player Bugémon's attack at the given index against
     * the enemy, records the outcome and hands the turn over to the enemy.
     *
     * @param attackIndex zero-based index of the attack to use
     */
    public void playerAttack(int attackIndex) {
        attackerAttackDefender(getAttack(attackIndex));
    }

    /**
     * Executes a randomly chosen attack of the on-field enemy Bugémon against
     * the player, records the outcome and hands the turn back to the player.
     */
    public void enemyAttackFromIAChoice() {
        attackerAttackDefender(getRandomAttack());
    }

    /**
     * Runs one attack from the current attacker against the current defender:
     * applies damage and effects, stores the resulting {@link AttackActionResult},
     * then hands the turn to the other side.
     */
    private void attackerAttackDefender(Attack attack) {
        Bugemon attacker         = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        Bugemon defender         = isPlayerTurn ? teamEnemy.getOnFieldBugemon()  : teamPlayer.getOnFieldBugemon();
        TeamBugemon attackerTeam = isPlayerTurn ? teamPlayer : teamEnemy;

        int damage = attacker.attack(defender, attack);
        List<EffectEvent> effects = EffectManager.handleEffect(attacker, defender, attackerTeam, attack);

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

    /**
     * Brings the player's Bugémon with the given id onto the field.
     *
     * @param id the id of the Bugémon to send out
     * @throws BugemonNotInTeamException if no such Bugémon is in the player's team
     */
    public void changePlayerOnField(String id) throws BugemonNotInTeamException {
        Bugemon bugemon = teamPlayer.getTeam().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BugemonNotInTeamException(id));
        teamPlayer.changeOnFieldBugemon(bugemon);
    }

    /**
     * Brings the given enemy Bugémon onto the field.
     *
     * @param bugemon the enemy Bugémon to send out
     * @throws BugemonNotInTeamException if the Bugémon is not in the enemy's team
     */
    public void changeEnemyOnField(Bugemon bugemon) throws BugemonNotInTeamException {
        teamEnemy.changeOnFieldBugemon(bugemon);
    }

    /**
     * Determines who acts first at the very start of the fight, based on the
     * cumulated initiative of both full teams.
     *
     * @return {@code true} if the player's total team initiative is strictly greater
     */
    public boolean playerCanStart() {
        int playerInit = 0, enemyInit = 0;
        for (Bugemon bug : teamPlayer.getTeam()) playerInit += bug.getInitiative();
        for (Bugemon bug : teamEnemy.getTeam())  enemyInit  += bug.getInitiative();
        return playerInit > enemyInit;
    }

    /** Recomputes whose turn it is from the current on-field Bugémons' initiative. */
    public void recomputeTurnOrder() {
        this.isPlayerTurn = teamPlayer.getOnFieldBugemon().getInitiative()
                >= teamEnemy.getOnFieldBugemon().getInitiative();
    }

    /**
     * Tells whether the player keeps the initiative after a Bugémon switch.
     *
     * @return {@code true} if the player's on-field Bugémon has equal or greater initiative
     */
    public boolean playerGoesFirstAfterSwitch() {
        return teamPlayer.getOnFieldBugemon().getInitiative()
                >= teamEnemy.getOnFieldBugemon().getInitiative();
    }

    /** Forces the next turn to the enemy side. */
    public void forceEnemyTurn() { isPlayerTurn = false; }

    /** @return {@code true} if it is currently the player's turn */
    public boolean isPlayerTurn() { return isPlayerTurn; }

    /** @return {@code true} once either team has no Bugémon left standing */
    public boolean isBattleOver() {
        return !teamEnemy.isTeamStillStanding() || !teamPlayer.isTeamStillStanding();
    }

    /** @return {@code true} if the enemy team has been fully defeated */
    public boolean playerWon() { return !teamEnemy.isTeamStillStanding(); }

    /** @return {@code true} if the player's on-field Bugémon is knocked out */
    public boolean isOnFieldPlayerBugemonKO() { return teamPlayer.isOnFieldBugemonKO(); }

    /** @return {@code true} if the enemy's on-field Bugémon is knocked out */
    public boolean isOnFieldEnemyBugemonKO() { return teamEnemy.isOnFieldBugemonKO(); }

    /** @return the player's team */
    public TeamBugemon getTeamPlayer() { return teamPlayer; }
    /** @return the enemy team */
    public TeamBugemon getTeamEnemy()  { return teamEnemy; }
    /** @return the player's Bugémon currently on the field */
    public Bugemon getOnFieldPlayerBugemon() { return teamPlayer.getOnFieldBugemon(); }
    /** @return the enemy's Bugémon currently on the field */
    public Bugemon getOnFieldEnemyBugemon() { return teamEnemy.getOnFieldBugemon(); }
    /** @return the player's Bugémons still able to be sent onto the field */
    public List<Bugemon> getAvailablePlayerBugemons() { return teamPlayer.getAvailableBugemons(); }
    /** @return the enemy's Bugémons still able to be sent onto the field */
    public List<Bugemon> getAvailableEnemyBugemons()  { return teamEnemy.getAvailableBugemons(); }

    /** @return the result of the most recent attack, or {@code null} if none has happened yet */
    public AttackActionResult getLastActionResult() { return lastActionResult; }

    /**
     * @return the size of the enemy team, used as the enemy-count factor when
     *         computing combat XP
     */
    public int getEnemyTeamSize() {
        return teamEnemy.getTeam().size();
    }

    /**
     * Pre-computes, for each attack of the player's on-field Bugémon, how
     * effective it would be against the enemy's on-field Bugémon. The result is
     * cached and read back through {@link #getPredictedEffectiveness()}.
     */
    public void computePredictedEffectiveness() {
        predictedEffectiveness.clear();
        String targetType = teamEnemy.getOnFieldBugemonType();
        for (Attack attack : teamPlayer.getOnFieldBugemonAttacks()) {
            predictedEffectiveness.add(
                    TypeEffectivenessService.getEffectiveness(attack.getType(), targetType)
            );
        }
    }

    /** @return an unmodifiable view of the last predicted effectiveness, one entry per player attack */
    public List<Effectiveness> getPredictedEffectiveness() {
        return Collections.unmodifiableList(predictedEffectiveness);
    }

    /** @return the current attacker's attack at the given index. */
    private Attack getAttack(int index) {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.getAttackFromIndex(index);
    }

    /** @return a random attack of the current attacker. */
    private Attack getRandomAttack() {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.chooseRandomAttack();
    }

    private void switchRole() { isPlayerTurn = !isPlayerTurn; }

    /**
     * Sends a random still-available enemy Bugémon onto the field. Does nothing
     * if the enemy has no available Bugémon left.
     *
     * @throws BugemonNotInTeamException if the chosen Bugémon is not in the enemy's team
     */
    public void changeOnFieldEnemyBugemonRandomly() throws BugemonNotInTeamException {
        List<Bugemon> available = getAvailableEnemyBugemons();
        if (available.isEmpty()) return;
        Bugemon randomBug = available.get(random.nextInt(available.size()));
        changeEnemyOnField(randomBug);
    }

    /**
     * Applies the effect of the given item to the player's on-field Bugémon
     * (healing, stat boost or debuff removal, depending on the item).
     *
     * @param item the item to apply
     */
    public void applyItem(Item item) {
        ItemEffectApplier.ApplyItems(teamPlayer.getOnFieldBugemon(), item);
    }
}