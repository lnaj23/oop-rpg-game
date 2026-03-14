package models.game;

import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;

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

    /** The player's team . */
    private final TeamBugemon teamPlayer;

    /** The enemy team . */
    private final TeamBugemon teamEnemy;

    /** {@code true} if it is currently the player's turn, {@code false} if it is the enemy's. */
    private boolean isPlayerTurn;

    /** Text describing the result of the last attack, to be displayed in the UI. */
    private String actionResulText;

    /**
     * Constructs a fight between two teams.
     * Initialises each Bugémon's HP, marks ownership, and places
     * the first Bugémon of the player's team and the last of the enemy's
     * team on the field.
     *
     * @param teamPlayer the player's team
     * @param teamCPU    the enemy team
     */
    public Fight(TeamBugemon teamPlayer, TeamBugemon teamCPU) {
        this.teamPlayer = new TeamBugemon(teamPlayer);
        this.teamEnemy  = new TeamBugemon(teamCPU);

        this.teamPlayer.getTeam().forEach(b -> { b.setIsPlayer(true);  b.initPv(); });
        this.teamEnemy.getTeam().forEach(b  -> { b.setIsPlayer(false); b.initPv(); });

        this.teamPlayer.changeOnFieldBugemon(this.teamPlayer.getTeam().getFirst());
        this.teamEnemy.changeOnFieldBugemon(this.teamEnemy.getTeam().getFirst());

        this.isPlayerTurn = true;
    }

    /**
     * Determines whether the player should go first by comparing
     * the total initiative of both teams.
     *
     * @return {@code true} if the player's total initiative is strictly greater
     */
    public boolean playerCanStart() {
        int initiativeTeamPlayer = 0;
        int initiativeTeamCPU    = 0;

        for (Bugemon bug : this.teamPlayer.getTeam()) initiativeTeamPlayer += bug.getInitiative();
        for (Bugemon bug : this.teamEnemy.getTeam())  initiativeTeamCPU    += bug.getInitiative();

        return initiativeTeamPlayer > initiativeTeamCPU;
    }

    /**
     * Flips the current turn.
     * Called automatically at the end of {@link #playingAttackWaiting(Attack)}.
     */
    private void switchRole() {
        isPlayerTurn = !isPlayerTurn;
    }

    /** Forces the turn to the enemy side (used when the player switches without attacking). */
    public void forceEnemyTurn() {
        isPlayerTurn = false;
    }
    /**
     * The active Bugémon of the current turn's side attacks
     * the active Bugémon of the opposing side.
     * The turn is automatically passed to the other side after the attack.
     *
     * @param attack the attack to use
     */
    public void playingAttackWaiting(Attack attack) {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        Bugemon defender = isPlayerTurn ? teamEnemy.getOnFieldBugemon()  : teamPlayer.getOnFieldBugemon();

        actionResulText = attacker.attack(attack, defender);

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
     * Returns whether the battle is over, i.e. all Bugémon
     * of at least one team are KO.
     *
     * @return {@code true} if the battle is over
     */
    public boolean isBattleOver() {
        return teamEnemy.getTeam().stream().noneMatch(Bugemon::isStillStanding)
                || teamPlayer.getTeam().stream().noneMatch(Bugemon::isStillStanding);
    }

    /**
     * Returns whether the player has won the battle.
     * Should be called after {@link #isBattleOver()}.
     *
     * @return {@code true} if all enemy Bugémon are KO
     */
    public boolean playerWon() {
        return teamEnemy.getTeam().stream().noneMatch(Bugemon::isStillStanding);
    }

    /**
     * Returns the attack at the given index of the current turn's active Bugémon.
     *
     * @param index the index of the attack in the Bugémon's attack list
     * @return the corresponding attack
     */
    public Attack getPlayingBugemonAttack(int index) {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.getAttacks().get(index);
    }

    /**
     * Returns a random attack from the current turn's active Bugémon.
     *
     * @return a randomly chosen attack
     */
    public Attack getPlayingBugemonRandomAttack() {
        Bugemon attacker = isPlayerTurn ? teamPlayer.getOnFieldBugemon() : teamEnemy.getOnFieldBugemon();
        return attacker.chooseRandomAttack();
    }

    /** @return {@code true} if it is the player's turn */
    public boolean isPlayerTurn()                     { return isPlayerTurn; }

    /** @return the player's team */
    public TeamBugemon getTeamPlayer()                { return teamPlayer; }

    /** @return the enemy team */
    public TeamBugemon getTeamEnemy()                 { return teamEnemy; }

    /** @return the player's currently active on-field Bugémon */
    public Bugemon getOnFieldPlayerBugemon()          { return teamPlayer.getOnFieldBugemon(); }

    /** @return the enemy's currently active on-field Bugémon */
    public Bugemon getOnFieldEnemyBugemon()           { return teamEnemy.getOnFieldBugemon(); }

    /** @return {@code true} if the player's on-field Bugémon is KO */
    public boolean isOnFieldPlayerBugemonKO()         { return teamPlayer.getOnFieldBugemon().isKO(); }

    /** @return {@code true} if the enemy's on-field Bugémon is KO */
    public boolean isOnFieldEnemyBugemonKO()          { return teamEnemy.getOnFieldBugemon().isKO(); }

    /** @return the text describing the result of the last attack */
    public String getActionResultText()               { return actionResulText; }

    /** @return the list of available (non-KO) Bugémon in the player's team */
    public List<Bugemon> getAvailablePlayerBugemons() { return teamPlayer.getAvailableBugemons(); }

    /** @return the list of available (non-KO) Bugémon in the enemy's team */
    public List<Bugemon> getAvailableEnemyBugemons()  { return teamEnemy.getAvailableBugemons(); }
}