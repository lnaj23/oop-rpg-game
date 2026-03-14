package controllers;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import models.Bugemon.Bugemon;
import models.Game.Fight;
import views.FightView;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import models.Bugemon.TeamBugemon;

/**
 * Controller for the fight screen.
 * <p>
 * Orchestrates the turn-based combat loop between the player and the enemy:
 * handles attack selection, enemy AI reaction, on-field Bugémon switches,
 * and delegates all visual updates to {@link FightView}.
 * </p>
 */
public class FightController extends Controller<FightView> {

    /** The current fight instance holding all battle state. */
    private Fight fight;

    /** Random number generator used to pick enemy and switch targets. */
    private final Random random = new Random();

    /** Delay in seconds before the enemy reacts after the player's attack. */
    private final int ENEMY_REACT_TIME = 2;

    private final double DISPLAY_PLAYER_DEATH_NOTE_TIME = 1.5;

    /**
     * Constructs the controller and initialises the associated {@link FightView}.
     *
     * @param metaController the meta-controller used to switch scenes
     * @throws IOException if the FXML file for the view cannot be loaded
     */
    public FightController(MetaController metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setController(this);
    }

    /**
     * Initialises the fight with the given player team and a hardcoded enemy team,
     * then updates the view to reflect the initial battle state.
     *
     * @param team the player's team
     */
    public void setPlayerTeam(TeamBugemon team) {
        fight = new Fight(team, new TeamBugemon(List.of("pyricore", "moussil", "pebblit")));

        view.displayTeam(fight.getTeamPlayer().getTeamDTO());
        view.displayTeam(fight.getTeamEnemy().getTeamDTO());
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
        String startFightMessage = "Un " + fight.getOnFieldEnemyBugemon().getNom() + " sauvage apparaît ! " + (!fight.playerCanStart() ? "L'adversaire a plus d'initiative, il lance les hostilités !": "L'adversaire a moins d'initiative, vous lancez les hostilités !" );
        view.updateCombatMessage(startFightMessage);
        if (!fight.playerCanStart()){
            forceEnemyToAttack();
        }
    }

    /**
     * Navigates back to the title screen.
     */
    public void goToTitleScreen() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }

    /**
     * Called when the player selects an attack.
     * Triggers the player's attack, then schedules the enemy's counterattack
     * after {@link #ENEMY_REACT_TIME} seconds via {@link #enemyAttackPlayer()}.
     * Checks if the battle is over after the player's attack.
     *
     * @param attackIndex the index of the selected attack in the player's attack list
     */
    public void onAttackSelected(int attackIndex) {
        if (!fight.isPlayerTurn()) return;

        view.setButtonsDisabled(true);
        view.showAttackandActionPanels(false);

        playerAttackEnemy(attackIndex);
    }

    /**
     * Called when the player clicks the item button.
     * Not yet implemented.
     */
    public void onItemClicked() {
        // choisir objet
    }

    /**
     * Called when the player clicks the switch button.
     * Switches the player's on-field Bugémon to a random available one
     * and updates the view accordingly.
     */
    public void onSwitchDisplaeyPlayerClicked() {
        switchPlayerBugemon();
        forceEnemyToAttack();
    }

    /**
     * Switches the player's on-field Bugémon to a random available one (forced, no turn cost).
     */
    private void switchPlayerBugemon() {
        String switchmessage = fight.getOnFieldPlayerBugemon().getNom() + ", reviens !\n";
        List<Bugemon> available = fight.getAvailablePlayerBugemons();
        if (available.isEmpty()) return;
        Bugemon randomBug = available.get(random.nextInt(available.size()));
        fight.changePlayerOnField(randomBug);
        view.updateBugemonHPDisplay(randomBug);
        switchmessage = switchmessage + randomBug.getNom() + " entre en jeu !";
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateCombatMessage(switchmessage);
    }

    /**
     * Switches the enemy's on-field Bugémon to a random available one
     * and updates the view accordingly.
     */
    private void switchDisplayEnemyBugemon() {
        List<Bugemon> available = fight.getAvailableEnemyBugemons();
        if (available.isEmpty()) return;
        Bugemon randomBug = available.get(random.nextInt(available.size()));
        fight.changeEnemyOnField(randomBug);
        view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
        view.updateCombatMessage("L'adversaire envoie " + randomBug.getNom() + " !");
    }

    /**
     * Called when the player clicks the forfeit button.
     * Immediately navigates back to the title screen.
     */
    public void onForfeitClicked() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }

    /**
     * Executes the player's attack against the enemy and updates the view.
     * If the enemy's on-field Bugémon is KO after the attack, switches it out.
     *
     * @param attackIndex the index of the attack to use
     */
    private void playerAttackEnemy(int attackIndex) {
        fight.playingAttackWaiting(fight.getPlayingBugemonAttack(attackIndex));
        viewReactToAttack(fight.getOnFieldEnemyBugemon());
        if (fight.isOnFieldEnemyBugemonKO()) {
            view.markBugemonAsFainted(fight.getOnFieldEnemyBugemon());
            PauseTransition pauseKO = new PauseTransition(Duration.seconds(DISPLAY_PLAYER_DEATH_NOTE_TIME));
            pauseKO.setOnFinished(e -> {
                switchDisplayEnemyBugemon();
                if (!fight.isBattleOver()) enemyAttackPlayer();
                else endIfBattleOver();
            });
            pauseKO.play();
        } else {
            if (!fight.isBattleOver()) enemyAttackPlayer();
            else endIfBattleOver();
        }
    }

    /**
     * Schedules and executes the enemy's attack against the player after a delay
     * of {@link #ENEMY_REACT_TIME} seconds. Uses a random attack.
     * If the player's on-field Bugémon is KO after the attack, marks it as
     * fainted and switches it out. Re-enables buttons once the attack is resolved.
     */
    private void enemyAttackPlayer() {
        PauseTransition pause = new PauseTransition(Duration.seconds(ENEMY_REACT_TIME));
        pause.setOnFinished(e -> {
            Bugemon target = fight.getOnFieldPlayerBugemon();
            fight.playingAttackWaiting(fight.getPlayingBugemonRandomAttack());
            viewReactToAttack(target);
            if (fight.isOnFieldPlayerBugemonKO()) {
                view.markBugemonAsFainted(target);
                PauseTransition pauseKO = new PauseTransition(Duration.seconds(DISPLAY_PLAYER_DEATH_NOTE_TIME));
                pauseKO.setOnFinished(e2 -> {
                    switchPlayerBugemon();
                    endIfBattleOver();
                    view.setButtonsDisabled(false);
                });
                pauseKO.play();
            } else {
                view.setButtonsDisabled(false);
            }
        });
        pause.play();
    }


    private void forceEnemyToAttack() {
        view.setButtonsDisabled(true);
        fight.forceEnemyTurn();
        enemyAttackPlayer();
        endIfBattleOver();
    }

    private void viewReactToAttack(Bugemon bug) {
        view.updateBugemonHPDisplay(bug);
        view.updateCombatMessage(fight.getActionResultText());
    }
    /**
     * Checks if the battle is over and, if so, displays the outcome message
     * then navigates to the title screen after a short delay.
     */
    private void endIfBattleOver() {
        if (fight.isBattleOver()) {
            view.setButtonsDisabled(true);
            if (fight.playerWon()) { view.updateCombatMessage("VICTORY"); }
            else                   { view.updateCombatMessage("DEFEAT");  }
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            if (fight.isBattleOver()) {
                goToTitleScreen();
            }
        });
        pause.play();
    }
}