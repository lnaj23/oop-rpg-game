package controllers.fight;

import configuration.AppConfig;
import controllers.Controller;
import controllers.ControllerManager;
import exceptions.BugemonNotFoundException;
import exceptions.BugemonNotInTeamException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import models.bugemon.Bugemon;
import models.game.Fight;
import models.game.Item;
import listener.FightListener;
import services.fight.EnemyTeamGenerator;
import services.game.GameStateHolder;
import views.fight.FightView;

import java.io.IOException;
import java.util.Objects;

import models.bugemon.TeamBugemon;

import static services.fight.ItemEffectApplier.ApplyItems;

/**
 * Controller for the fight screen.
 */
public class FightAgainstCPUController extends Controller<FightView> implements FightListener {

    private Fight fight;

    private boolean useItemToChange = false;

    public FightAgainstCPUController(ControllerManager metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setListener(this);
    }

    /**
     * Initialises the fight from {@link GameStateHolder} against a randomly generated enemy team
     * with the same number of Bugemons as the player's team.
     */
    public void setFight() {
        try {
            view.resetDisplay();
            TeamBugemon team = GameStateHolder.getInstance().getCurrentTeamPlayer();
            fight = new Fight(team, EnemyTeamGenerator.generateEnemyTeam());
            viewDisplayTeams();
            viewDisplayPredictedEffectiveness();
            viewInitDisplayedBugemon();
            viewInitStartMessage();
            if (!fight.playerCanStart()) forceEnemyToAttack();
            view.updateFloorInfo(GameStateHolder.getInstance().getCurrentLevelTower().getProgressAsString());
        } catch (BugemonNotInTeamException e) {
            controllerManager.fatalError("couldn't load the fight : " + e.getMessage());
        }
    }

    private void viewInitStartMessage() {
        String startFightMessage = "L'adversaire envoie " + fight.getOnFieldEnemyBugemonName() + " ! "
                + (!fight.playerCanStart()
                ? "L'adversaire a plus d'initiative, il lance les hostilités !"
                : "L'adversaire a moins d'initiative, vous lancez les hostilités !");
        view.updateCombatMessage(startFightMessage);
    }

    private void viewInitDisplayedBugemon() {
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
    }

    private void viewDisplayPredictedEffectiveness() {
        fight.computePredictedEffectiveness();
        view.updatePredictedEffectiveness(fight.getPredictedEffectiveness());
    }

    private void viewDisplayTeams() {
        view.displayTeam(fight.getTeamPlayerToDisplay());
        view.displayTeam(fight.getTeamEnemyToDisplay());
    }


    @Override
    public void onAttackSelected(int attackIndex) {
        if (!fight.isPlayerTurn()) return;
        view.setButtonsDisabled(true);
        view.showAttackandActionPanels(false);
        playerAttackEnemy(attackIndex);
    }

    @Override
    public void onItemClicked() {
        view.updateCombatMessage("");
        controllerManager.openItemChoiceWindow();
    }

    @Override
    public void onSwitchDisplaeyPlayerClicked() {
        view.setButtonsDisabled(true);
        showSwitchWindow(true);
    }

    @Override
    public void onForfeitClicked() {
        this.controllerManager.goToDefeatScreen();
    }

    public void onItemChosen(Item item) {
            useItem(item);
    }

    public void onSwitchBugemonChosen(String selectedId, boolean onPurpose) {
        try {
            switchPlayerBugemon(selectedId);
            if (onPurpose) {
                forceEnemyToAttack();
            } else {
                if (fight.playerGoesFirstAfterSwitch() || useItemToChange ) view.setButtonsDisabled(false);
                else forceEnemyToAttack();
            }
        } catch (BugemonNotInTeamException e) {
            controllerManager.fatalError("couldn't load the Bugemon : " + e.getMessage());
        }
    }

    public void onSwitchCancelled() {
        view.setButtonsDisabled(false);
    }

    private void useItem(Item item) {
        GameStateHolder.getInstance().consumeItem(item);
        if(item.getName().equals("Pierre de Retraite")){
            if(GameStateHolder.getInstance().getCurrentTeamPlayer().getSize() == 1){
                return;
            }
            showSwitchWindow(false);
            useItemToChange = true;
            return;
        }
        useItemToChange = false;
        view.setButtonsDisabled(true);
        ApplyItems(fight.getOnFieldPlayerBugemon(), item);
        viewReactToAttack();
        view.updateCombatMessage("Vous utilisez " + item.getName() + " !");

        PauseTransition pause = new PauseTransition(Duration.seconds(AppConfig.ENEMY_REACT_TIME));
        pause.setOnFinished(e -> forceEnemyToAttack());
        pause.play();
    }

    private void playerAttackEnemy(int attackIndex) {
        fight.playerAttack(attackIndex);
        viewReactToAttack();
        if (fight.isOnFieldEnemyBugemonKO()) {
            view.markBugemonAsFainted(fight.getOnFieldEnemyBugemon());
            fight.removeOnFieldEnemyFromTeam();
            PauseTransition pauseKO = new PauseTransition(Duration.seconds(AppConfig.DEATH_DISPLAY_TIME));
            pauseKO.setOnFinished(e -> {
                if (fight.isBattleOver()) {
                    endIfBattleOver();
                } else {
                    switchDisplayEnemyBugemon();
                    if (fight.playerGoesFirstAfterSwitch()) view.setButtonsDisabled(false);
                    else enemyAttackPlayer();
                }
            });
            pauseKO.play();
        } else {
            if (fight.isBattleOver()) endIfBattleOver();
            else enemyAttackPlayer();
        }
    }

    private void enemyAttackPlayer() {
        PauseTransition pause = new PauseTransition(Duration.seconds(AppConfig.ENEMY_REACT_TIME));
        pause.setOnFinished(e -> {
            Bugemon target = fight.getOnFieldPlayerBugemon();
            fight.enemyAttackFromIAChoice();
            viewReactToAttack();
            if (fight.isOnFieldPlayerBugemonKO()) {
                view.markBugemonAsFainted(target);
                PauseTransition pauseKO = new PauseTransition(Duration.seconds(AppConfig.DEATH_DISPLAY_TIME));
                pauseKO.setOnFinished(e2 -> {
                    if (fight.isBattleOver()) endIfBattleOver();
                    else Platform.runLater(() -> showSwitchWindow(false));
                });
                pauseKO.play();
            } else {
                view.setButtonsDisabled(false);
            }
        });
        pause.play();
    }

    private void switchDisplayEnemyBugemon() {
        try {
            fight.changeOnFieldEnemyBugemonRandomly();
            fight.recomputeTurnOrder();
            view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
            view.updateCombatMessage("L'adversaire envoie " + fight.getOnFieldEnemyBugemonName() + " !");
        } catch (BugemonNotInTeamException e) {
            controllerManager.fatalError("Couldnt load bugemon : " + e.getMessage());
        }
    }

    private void forceEnemyToAttack() {
        view.setButtonsDisabled(true);
        fight.forceEnemyTurn();
        enemyAttackPlayer();
    }

    private void viewReactToAttack() {
        view.updateBugemonHPAndEffectDisplay(fight.getOnFieldEnemyBugemon());
        view.updateBugemonHPAndEffectDisplay(fight.getOnFieldPlayerBugemon());
        view.updateCombatMessage(fight.getActionResultText());
    }

    private void endIfBattleOver() {
        if (!fight.isBattleOver()) return;
        GameStateHolder.getInstance().setCurrentTeamPlayer(fight.getTeamPlayer());
        view.setButtonsDisabled(true);
        if (fight.playerWon()) controllerManager.goToVictoryScreen();
        else controllerManager.goToDefeatScreen();
    }

    private void switchPlayerBugemon(String selectedId) throws BugemonNotInTeamException {
        String switchMessage = fight.getOnFieldPlayerBugemonName() + ", reviens !\n";
        fight.changePlayerOnField(selectedId);
        switchMessage += fight.getOnFieldPlayerBugemonName() + " entre en jeu !";
        fight.recomputeTurnOrder();
        viewDisplayPredictedEffectiveness();
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateCombatMessage(switchMessage);
    }

    private void showSwitchWindow(boolean onPurpose) {
        GameStateHolder.getInstance().setCurrentPlayerTeamDTO(fight.getTeamPlayerToDisplay());
        GameStateHolder.getInstance().setCurrentOnFieldId(fight.getOnFieldPlayerBugemonId());
        GameStateHolder.getInstance().setSwitchOnPurpose(onPurpose);
        controllerManager.openSwitchWindow();
    }
}