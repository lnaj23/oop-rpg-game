package models.game;

import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import services.fight.EffectManager;
import services.fight.TypeEffectivenessService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a fight between two Bugémon teams.
 * <p>
 * Manages turn order via a boolean {@code isPlayerTurn},
 * attacks, on-field Bugémon switches, and determines
 * whether the battle is over and who won.
 * </p>
 * <p>
 * Both teams are deep-copied at construction to avoid
 * mutating the player's original teams.
 * </p>
 */
public class Fight {

    /** The player's team. */
    private final TeamBugemon teamPlayer;

    /** The enemy team. */
    private final TeamBugemon teamEnemy;

    /** {@code true} if it is currently the player's turn, {@code false} if it is the enemy's. */
    private boolean isPlayerTurn;

    /** Text describing the result of the last attack, to be displayed in the UI. */
    private String actionResulText;

    /**
     * List of predicted effectiveness messages for each attack of the player's
     * on-field Bugémon against the enemy's on-field Bugémon.
     */
    private List<String> predictedEffectiveness = new ArrayList<>();

    /**
     * Constructs a fight between two teams.
     * Deep-copies both teams, marks ownership, and places the first Bugémon
     * of each team on the field.
     *
     * @param teamPlayer the player's team
     * @param teamEnemy  the enemy team
     */
    public Fight(TeamBugemon teamPlayer, TeamBugemon teamEnemy) {
        this.teamPlayer = new TeamBugemon(teamPlayer);
        this.teamEnemy  = new TeamBugemon(teamEnemy);

        this.teamPlayer.getTeam().forEach(b -> b.setIsPlayer(true));

        this.teamPlayer.changeOnFieldBugemon(this.teamPlayer.getTeam().getFirst());
        this.teamEnemy.changeOnFieldBugemon(this.teamEnemy.getTeam().getFirst());

        this.isPlayerTurn = true;
    }

    /**
     * Executes the player's attack at the given index against the enemy's on-field Bugémon.
     *
     * @param attackIndex the index of the attack in the player's on-field Bugémon's attack list
     */
    public void playerAttack(int attackIndex) {
        attackerAttackDefender(getAttack(attackIndex));
    }

    /**
     * Executes a random attack from the enemy's on-field Bugémon against the player's.
     */
    public void enemyAttack() {
        attackerAttackDefender(getRandomAttack());
    }

    /**
     * Executes an attack from the active Bugémon of the current turn's side
     * against the active Bugémon of the opposing side.
     * Turn is automatically passed to the other side afterwards.
     *
     * @param attack the attack to use
     */
    private void attackerAttackDefender(Attack attack) {
        Bugemon attacker     = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        Bugemon defender     = isPlayerTurn ? teamEnemy.getOnFieldBugemon()  : teamPlayer.getOnFieldBugemon();
        TeamBugemon attackerTeam = isPlayerTurn ? teamPlayer : teamEnemy;
        TeamBugemon defenderTeam = isPlayerTurn ? teamEnemy  : teamPlayer;

        actionResulText = attacker.attack(defender, attack) + "\n"
                + EffectManager.handleEffect(attacker, defender, attackerTeam, defenderTeam, attack);

        switchRole();
    }

    /**
     * Replaces the player's active on-field Bugémon.
     *
     * @param bugemon the new Bugémon to put on the field
     */
    public void changePlayerOnField(Bugemon bugemon) {
        teamPlayer.changeOnFieldBugemon(bugemon);
    }

    /**
     * Replaces the enemy's active on-field Bugémon.
     *
     * @param bugemon the new Bugémon to put on the field
     */
    public void changeEnemyOnField(Bugemon bugemon) {
        teamEnemy.changeOnFieldBugemon(bugemon);
    }

    /**
     * Determines whether the player should go first at the start of the fight,
     * comparing the total initiative of both teams.
     *
     * @return {@code true} if the player's total initiative is strictly greater
     */
    public boolean playerCanStart() {
        int playerInit = 0;
        int enemyInit  = 0;
        for (Bugemon bug : this.teamPlayer.getTeam()) playerInit += bug.getInitiative();
        for (Bugemon bug : this.teamEnemy.getTeam())  enemyInit  += bug.getInitiative();
        return playerInit > enemyInit;
    }

    /**
     * Recomputes turn order based on the initiative of the two currently on-field Bugémons.
     * Called after a Bugémon faints and a new one enters the field.
     * If initiatives are equal, the player goes first.
     */
    public void recomputeTurnOrder() {
        int playerInit = teamPlayer.getOnFieldBugemon().getInitiative();
        int enemyInit  = teamEnemy.getOnFieldBugemon().getInitiative();
        this.isPlayerTurn = (playerInit >= enemyInit);
    }

    /**
     * Returns whether the player's on-field Bugémon goes first after a switch,
     * based on individual initiative comparison.
     *
     * @return {@code true} if the player's Bugémon has equal or greater initiative
     */
    public boolean playerGoesFirstAfterSwitch() {
        return teamPlayer.getOnFieldBugemon().getInitiative()
                >= teamEnemy.getOnFieldBugemon().getInitiative();
    }

    /**
     * Forces the turn to the enemy side.
     * Used when the player switches voluntarily without attacking.
     */
    public void forceEnemyTurn() {
        isPlayerTurn = false;
    }

    /**
     * Returns whether it is currently the player's turn.
     *
     * @return {@code true} if it is the player's turn
     */
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Returns whether the battle is over, i.e. all Bugémon of at least one team are KO.
     *
     * @return {@code true} if the battle is over
     */
    public boolean isBattleOver() {
        return !teamEnemy.isTeamStillStanding() || !teamPlayer.isTeamStillStanding();
    }

    /**
     * Returns whether the player has won.
     * Should be called after {@link #isBattleOver()}.
     *
     * @return {@code true} if all enemy Bugémon are KO
     */
    public boolean playerWon() {
        return !teamEnemy.isTeamStillStanding();
    }

    /**
     * Returns whether the player's on-field Bugémon is KO.
     *
     * @return {@code true} if the player's on-field Bugémon has no HP left
     */
    public boolean isOnFieldPlayerBugemonKO() {
        return teamPlayer.isOnFieldBugemonKO();
    }

    /**
     * Returns whether the enemy's on-field Bugémon is KO.
     *
     * @return {@code true} if the enemy's on-field Bugémon has no HP left
     */
    public boolean isOnFieldEnemyBugemonKO() {
        return teamEnemy.isOnFieldBugemonKO();
    }

    /**
     * Returns the player's team.
     *
     * @return the player's {@link TeamBugemon}
     */
    public TeamBugemon getTeamPlayer() {
        return teamPlayer;
    }

    /**
     * Returns the enemy team.
     *
     * @return the enemy {@link TeamBugemon}
     */
    public TeamBugemon getTeamEnemy() {
        return teamEnemy;
    }

    /**
     * Returns the player's currently active on-field Bugémon.
     *
     * @return the player's on-field Bugémon
     */
    public Bugemon getOnFieldPlayerBugemon() {
        return teamPlayer.getOnFieldBugemon();
    }

    /**
     * Returns the name of the player's currently active on-field Bugémon.
     *
     * @return the name of the player's on-field Bugémon
     */
    public String getOnFieldPlayerBugemonName() {
        return teamPlayer.getOnFieldBugemon().getName();
    }

    /**
     * Returns the id of the player's currently active on-field Bugémon.
     *
     * @return the id of the player's on-field Bugémon
     */
    public String getOnFieldPlayerBugemonId() {
        return teamPlayer.getOnFieldBugemon().getId();
    }

    /**
     * Returns the enemy's currently active on-field Bugémon.
     *
     * @return the enemy's on-field Bugémon
     */
    public Bugemon getOnFieldEnemyBugemon() {
        return teamEnemy.getOnFieldBugemon();
    }

    /**
     * Returns the name of the enemy's currently active on-field Bugémon.
     *
     * @return the name of the enemy's on-field Bugémon
     */
    public String getOnFieldEnemyBugemonName() {
        return teamEnemy.getOnFieldBugemon().getName();
    }

    /**
     * Returns the list of available (non-KO) Bugémons in the player's team.
     *
     * @return list of available player Bugémons
     */
    public List<Bugemon> getAvailablePlayerBugemons() {
        return teamPlayer.getAvailableBugemons();
    }

    /**
     * Returns the list of available (non-KO) Bugémons in the enemy's team.
     *
     * @return list of available enemy Bugémons
     */
    public List<Bugemon> getAvailableEnemyBugemons() {
        return teamEnemy.getAvailableBugemons();
    }

    /**
     * Returns the text describing the result of the last attack.
     *
     * @return the action result text
     */
    public String getActionResultText() {
        return actionResulText;
    }

    /**
     * Rebuilds the predicted type-effectiveness messages for each attack of the
     * player's on-field Bugémon against the enemy's on-field Bugémon.
     * Called on every on-field change to stay up to date.
     */
    public void computePredictedEffectiveness() {
        predictedEffectiveness.clear();
        String targetType = teamEnemy.getOnFieldBugemon().getType();

        for (Attack attack : teamPlayer.getOnFieldBugemon().getAttacks()) {
            double multiplier = TypeEffectivenessService.getTypeMultiplier(attack.getType(), targetType);
            if (multiplier > 1.0) {
                predictedEffectiveness.add("Super efficace !");
            } else if (multiplier < 1.0) {
                predictedEffectiveness.add("Peu efficace...");
            } else {
                predictedEffectiveness.add("Rien de spécial");
            }
        }
    }

    /**
     * Returns an unmodifiable view of the predicted effectiveness messages.
     *
     * @return the list of effectiveness messages, one per attack
     */
    public List<String> getPredictedEffectiveness() {
        return Collections.unmodifiableList(predictedEffectiveness);
    }

    /**
     * Returns the attack at the given index for the current attacker.
     *
     * @param index the attack index
     * @return the corresponding {@link Attack}
     */
    private Attack getAttack(int index) {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.getAttackFromIndex(index);
    }

    /**
     * Returns a random attack from the current attacker.
     *
     * @return a randomly chosen {@link Attack}
     */
    private Attack getRandomAttack() {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.chooseRandomAttack();
    }

    /**
     * Flips the current turn.
     * Called automatically at the end of {@link #attackerAttackDefender(Attack)}.
     */
    private void switchRole() {
        isPlayerTurn = !isPlayerTurn;
    }
}