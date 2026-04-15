package controllers.fight;

import configuration.AppConfig;
import controllers.Controller;
import controllers.ControllerManager;
import exceptions.AttackNotFoundException;
import exceptions.BugemonNotInTeamException;
import exceptions.JsonResourceNotFoundException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import models.game.Fight;
import models.game.Item;
import models.game.LevelTower;
import views.listener.FightListener;
import services.game.EnemyTeamGenerator;
import services.game.GameStateHolder;
import views.fight.FightView;

import java.io.IOException;

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
    public void setFight() throws BugemonNotInTeamException, AttackNotFoundException, JsonResourceNotFoundException {
        view.resetDisplay();
        TeamBugemon team = GameStateHolder.getInstance().getCurrentTeamPlayer();
        fight = new Fight(team, EnemyTeamGenerator.generateEnemyTeam());
        viewDisplayTeams();
        viewDisplayPredictedEffectiveness();
        viewInitDisplayedBugemon();
        view.createStartMessage(fight.getOnFieldEnemyBugemonName(), fight.playerCanStart());
        if (!fight.playerCanStart()) forceEnemyToAttack();
        LevelTower tower = GameStateHolder.getInstance().getCurrentLevelTower();
        view.updateFloorInfo(tower.getCurrentFloor(), tower.getCurrentStep(), tower.getCurrentStepType(), tower.isFinished());
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
        view.animateAttack(fight.getOnFieldPlayerBugemon(), () -> playerAttackEnemy(attackIndex));
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

    public void onSwitchBugemonChosen(String selectedId, boolean onPurpose) throws BugemonNotInTeamException {
        switchPlayerBugemon(selectedId);

        if (onPurpose) {
            forceEnemyToAttack();
        } else {
            if (fight.playerGoesFirstAfterSwitch() || useItemToChange) {
                view.setButtonsDisabled(false);
            } else {
                forceEnemyToAttack();
            }
            useItemToChange = false;
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

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> forceEnemyToAttack());
        pause.play();
    }

    private void playerAttackEnemy(int attackIndex) {
        fight.playerAttack(attackIndex);
        viewReactToAttack();
        view.animateHit(fight.getOnFieldEnemyBugemon(), this::handleEnemyGettingHit);
    }

    private void handleEnemyGettingHit() {
        if (fight.isOnFieldEnemyBugemonKO()) {
            view.markBugemonAsFainted(fight.getOnFieldEnemyBugemon());
            view.animateKO(fight.getOnFieldEnemyBugemon(), this::handleEnemyBeingKO);
        } else {
            if (fight.isBattleOver()) endIfBattleOver();
            else enemyAttackPlayer();
        }
    }

    private void handleEnemyBeingKO() {
        if (fight.isBattleOver()) endIfBattleOver();
        else {
            switchDisplayEnemyBugemon();
            if (fight.playerGoesFirstAfterSwitch()) view.setButtonsDisabled(false);
            else enemyAttackPlayer();
        }
    }

    private void enemyAttackPlayer() {
        PauseTransition pause = new PauseTransition(Duration.seconds(AppConfig.ENEMY_REACT_TIME));
        pause.setOnFinished(e -> {
            fight.enemyAttackFromIAChoice();
            view.animateAttack(fight.getOnFieldEnemyBugemon(), () -> {
                viewReactToAttack();
                view.animateHit(fight.getOnFieldPlayerBugemon(), this::handlePlayerAfterHit);
            });
        });
        pause.play();
    }

    private void handlePlayerAfterHit() {
        if (fight.isOnFieldPlayerBugemonKO()) {
            view.markBugemonAsFainted(fight.getOnFieldPlayerBugemon());
            view.animateKO(fight.getOnFieldPlayerBugemon(), this::handlePlayerBeingKO);
        } else {
            view.setButtonsDisabled(false);
        }
    }

    private void handlePlayerBeingKO() {
        if (fight.isBattleOver()) endIfBattleOver();
        else Platform.runLater(() -> showSwitchWindow(false));
    }

    private void switchDisplayEnemyBugemon() {
        try {
            fight.changeOnFieldEnemyBugemonRandomly();
            fight.recomputeTurnOrder();
            view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
            view.updateCombatMessage("L'adversaire envoie " + fight.getOnFieldEnemyBugemonName() + " !");
        } catch (BugemonNotInTeamException e) {
            controllerManager.fatalError("Une erreur est survenue lors du changement de Bugemon Actif Enemi");
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
        if (fight.playerWon()) controllerManager.goToVictoryScreen(fight.getEnemyTeamSize());
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