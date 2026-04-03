package models.game;

import exceptions.BugemonNotInTeamException;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import models.bugemon.dto.BugemonDTO;
import services.fight.EffectManager;
import services.fight.TypeEffectivenessService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a fight between two Bugémon teams.
 */
public class Fight {

    private final TeamBugemon teamPlayer;
    private final TeamBugemon teamEnemy;
    private boolean isPlayerTurn;
    private String actionResulText;
    private List<String> predictedEffectiveness = new ArrayList<>();
    private final Random random = new Random();


    /**
     * @param teamPlayer the player's team
     * @param teamEnemy  the enemy team
     * @throws BugemonNotInTeamException if the first Bugémon cannot be placed on the field
     */
    public Fight(TeamBugemon teamPlayer, TeamBugemon teamEnemy) throws BugemonNotInTeamException {
        this.teamPlayer = new TeamBugemon(teamPlayer);
        this.teamEnemy  = new TeamBugemon(teamEnemy);
        this.teamPlayer.getTeam().forEach(b -> b.setIsPlayerToTrue());
        for(Bugemon bug : this.teamPlayer.getTeam()){
            bug.initPv(); // Heal all bugemon at the start, maybe we'll remove if we want to make a real roguelike game
        }
        for(Bugemon bug : this.teamPlayer.getTeam()){
            if (!bug.isKO()){
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
        actionResulText = attacker.attack(defender, attack) + "\n"
                + EffectManager.handleEffect(attacker, defender, attackerTeam, defenderTeam, attack);
        switchRole();
    }



    /**
     * @throws BugemonNotInTeamException if the Bugémon is not in the player's team
     */
    public void changePlayerOnField(String id) throws BugemonNotInTeamException {
        Bugemon bugemon = teamPlayer.getTeam().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BugemonNotInTeamException(id));
        teamPlayer.changeOnFieldBugemon(bugemon);
    }

    /**
     * @throws BugemonNotInTeamException if the Bugémon is not in the enemy's team
     */
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
    public TeamBugemon getTeamEnemy() { return teamEnemy; }
    public List<BugemonDTO> getTeamPlayerToDisplay() {
        return getTeamPlayer().getTeam().stream()
                .map(b -> (BugemonDTO) b)
                .collect(Collectors.toList());
    }
    public List<BugemonDTO> getTeamEnemyToDisplay() {
        return getTeamEnemy().getTeam().stream()
                .map(b -> (BugemonDTO) b)
                .collect(Collectors.toList());
    }
    public Bugemon getOnFieldPlayerBugemon() { return teamPlayer.getOnFieldBugemon(); }
    public String getOnFieldPlayerBugemonName() { return teamPlayer.getOnFieldBugemon().getName(); }
    public String getOnFieldPlayerBugemonId() { return teamPlayer.getOnFieldBugemon().getId(); }
    public Bugemon getOnFieldEnemyBugemon() { return teamEnemy.getOnFieldBugemon(); }
    public String getOnFieldEnemyBugemonName() { return teamEnemy.getOnFieldBugemon().getName(); }
    public List<Bugemon> getAvailablePlayerBugemons() { return teamPlayer.getAvailableBugemons(); }
    public List<Bugemon> getAvailableEnemyBugemons() { return teamEnemy.getAvailableBugemons(); }
    public String getActionResultText() { return actionResulText; }


    public void computePredictedEffectiveness() {
        predictedEffectiveness.clear();
        String targetType = teamEnemy.getOnFieldBugemonType();
        for (Attack attack : teamPlayer.getOnFieldBugemonAttacks()) {
            double m = TypeEffectivenessService.getTypeMultiplier(attack.getType(), targetType);
            if (m > 1.0) predictedEffectiveness.add("Super efficace !");
            else if (m < 1.0) predictedEffectiveness.add("Peu efficace...");
            else predictedEffectiveness.add("Rien de spécial");
        }
    }

    public List<String> getPredictedEffectiveness() {
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
}