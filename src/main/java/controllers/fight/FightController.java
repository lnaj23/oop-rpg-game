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
import services.game.GameStateHolder;
import views.fight.FightView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import models.bugemon.TeamBugemon;

/**
 * Controller for the fight screen.
 */
public class FightController extends Controller<FightView> implements FightListener {

    private Fight fight;
    private final Random random = new Random();


    public FightController(ControllerManager metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setListener(this);
    }

    @Override
    public void onReturnToTitleRequested() {
        gotoTitleScreen();
    }

    /**
     * Initialises the fight from {@link GameStateHolder} against a hardcoded enemy team.
     */
    public void setFight() {
        try {
            view.resetDisplay();
            TeamBugemon team = GameStateHolder.getInstance().getCurrentTeamPlayer();
            fight = new Fight(team, new TeamBugemon(AppConfig.getEnemyTeam()));
            view.displayTeam(fight.getTeamPlayer().getTeamDTO());
            view.displayTeam(fight.getTeamEnemy().getTeamDTO());
            fight.computePredictedEffectiveness();
            view.updatePredictedEffectiveness(fight.getPredictedEffectiveness());
            view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
            view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
            String startFightMessage = "L'adversaire envoie " + fight.getOnFieldEnemyBugemonName() + " ! "
                    + (!fight.playerCanStart()
                    ? "L'adversaire a plus d'initiative, il lance les hostilités !"
                    : "L'adversaire a moins d'initiative, vous lancez les hostilités !");
            view.updateCombatMessage(startFightMessage);
            if (!fight.playerCanStart()) forceEnemyToAttack();
        } catch (BugemonNotFoundException | BugemonNotInTeamException e) {
            controllerManager.fatalError("Erreur lors de l'initialisation du combat : " + e.getMessage());
        }
    }

    public void gotoTitleScreen() {
        GameStateHolder.getInstance().clearCurrentTeamPlayer();
        this.controllerManager.goToTitleScreen();
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
        controllerManager.openItemChoiceWindow();
    }

    @Override
    public void onSwitchDisplaeyPlayerClicked() {
        view.setButtonsDisabled(true);
        showSwitchWindow(true);
    }

    @Override
    public void onForfeitClicked() {
        this.view.showMessageReturnTitle("Abandon", "Vous avez abandonné le combat.");
    }

    public void onItemChosen(Item item) {
        useItem(item);
    }

    public void onSwitchBugemonChosen(Object selectedId, boolean onPurpose) {
        try {
            Bugemon selectedBugemon = findAvailablePlayerBugemonById(selectedId);
            switchPlayerBugemon(selectedBugemon);
            if (onPurpose) {
                forceEnemyToAttack();
            } else {
                if (fight.playerGoesFirstAfterSwitch()) view.setButtonsDisabled(false);
                else forceEnemyToAttack();
            }
        } catch (BugemonNotInTeamException e) {
            controllerManager.fatalError("Erreur lors du changement de Bugémon : " + e.getMessage());
        }
    }

    public void onSwitchCancelled() {
        view.setButtonsDisabled(false);
    }

    private void useItem(Item item) {
        GameStateHolder.getInstance().consumeItem(item);
        view.setButtonsDisabled(true);
        view.updateCombatMessage("Vous utilisez " + item.getName() + " !");
        // TODO : appliquer l'effet de l'item sur le modèle fight
        forceEnemyToAttack();
    }

    private void playerAttackEnemy(int attackIndex) {
        fight.playerAttack(attackIndex);
        viewReactToAttack();
        if (fight.isOnFieldEnemyBugemonKO()) {
            view.markBugemonAsFainted(fight.getOnFieldEnemyBugemon());
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
            fight.enemyAttack();
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
            List<Bugemon> available = fight.getAvailableEnemyBugemons();
            if (available.isEmpty()) return;
            Bugemon randomBug = available.get(random.nextInt(available.size()));
            fight.changeEnemyOnField(randomBug);
            fight.recomputeTurnOrder();
            view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
            view.updateCombatMessage("L'adversaire envoie " + randomBug.getName() + " !");
        } catch (BugemonNotInTeamException e) {
            controllerManager.fatalError("Erreur lors du changement de Bugémon ennemi : " + e.getMessage());
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
        view.setButtonsDisabled(true);
        if (fight.playerWon()) view.showMessageReturnTitle("Victoire", "Vous avez gagné le combat !");
        else view.showMessageReturnTitle("Défaite", "Vous avez perdu le combat.");
    }

    private void switchPlayerBugemon(Bugemon selectedBugemon) throws BugemonNotInTeamException {
        String switchMessage = fight.getOnFieldPlayerBugemonName() + ", reviens !\n"
                + selectedBugemon.getName() + " entre en jeu !";
        fight.changePlayerOnField(selectedBugemon);
        fight.recomputeTurnOrder();
        fight.computePredictedEffectiveness();
        view.updatePredictedEffectiveness(fight.getPredictedEffectiveness());
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateCombatMessage(switchMessage);
    }

    private Bugemon findAvailablePlayerBugemonById(Object id) {
        for (Bugemon bugemon : fight.getAvailablePlayerBugemons()) {
            if (Objects.equals(bugemon.getId(), id)) return bugemon;
        }
        throw new IllegalArgumentException("Bugémon introuvable : " + id);
    }

    private void showSwitchWindow(boolean onPurpose) {
        GameStateHolder.getInstance().setCurrentPlayerTeamDTO(fight.getTeamPlayer().getTeamDTO());
        GameStateHolder.getInstance().setCurrentOnFieldId(fight.getOnFieldPlayerBugemonId());
        GameStateHolder.getInstance().setSwitchOnPurpose(onPurpose);
        controllerManager.openSwitchWindow();
    }
}