package controllers;

import controllers.afterfight.DefeatController;
import controllers.afterfight.LevelUpController;
import controllers.afterfight.VictoryController;
import controllers.fight.*;
import controllers.teamchoice.EditTeamController;
import controllers.teamchoice.PreBuildTeamController;
import controllers.teamchoice.SelectionController;
import controllers.tower.ApplyBonusRewardController;
import controllers.tower.LearnNewAttackController;
import controllers.tower.RewardController;
import controllers.tower.SelectAttackController;
import exceptions.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.bugemon.Attack;
import models.game.LevelUpBonus;
import models.bugemon.Bugemon;
import models.game.Item;
import services.app.*;
import views.FatalErrorView;
import views.teamchoice.selection.SelectionView;

import java.io.IOException;
import java.util.List;

/**
 * Central controller that manages navigation between scenes and holds shared application state.
 * Creates and owns all sub-controllers and the shared team list.
 */
public class ControllerManager {

    private double width;
    private double height;
    private double x;
    private double y;

    private final Stage PrimaryStage;
    private final TitleScreenController titleScreenController;
    private final PreBuildTeamController preBuildTeamController;
    private final SelectionController selectionController;
    private final EditTeamController editTeamController;
    private final FightAgainstCPUController fightAgainstCPUController;
    private final VictoryController victoryController;
    private final DefeatController defeatController;
    private final RewardController rewardController;
    private final LevelUpController levelUpController;
    private final AddBugemonController addBugemonController;

    private final GameSessionService gameSessionService;
    private final TowerFlowService towerFlowService;
    private final GlobalMusicService globalMusicService;


    /** Enumeration of all available scenes in the application. */
    public enum Scenes {
        TITLE_SCREEN,
        PREBUILD_TEAM,
        SELECTION,
        EDIT_TEAM,
        FIGHT_AGAINST_CPU,
        VICTORY,
        DEFEAT,
        REWARD,
        LEVEL_UP,
        ADD_BUGEMON
    }

    /**
     * Initializes all controllers and views.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if any FXML file fails to load
     */
    public ControllerManager(Stage stage) throws IOException, AttackNotFoundException, JsonResourceNotFoundException {
        GameStateHolder.getInstance();
        this.PrimaryStage = stage;
        this.titleScreenController = new TitleScreenController(this);
        this.preBuildTeamController = new PreBuildTeamController(this);

        // Single SelectionView shared between creation and edition controllers.
        // Each controller registers itself as the listener in its onshow() method.
        SelectionView selectionView = new SelectionView();
        this.selectionController = new SelectionController(this, selectionView);
        this.editTeamController = new EditTeamController(this, selectionView);

        this.fightAgainstCPUController = new FightAgainstCPUController(this);
        this.victoryController = new VictoryController(this);
        this.defeatController = new DefeatController(this);
        this.rewardController = new RewardController(this);
        this.levelUpController = new LevelUpController(this);
        this.addBugemonController = new AddBugemonController(this);

        this.gameSessionService = new GameSessionService();
        this.towerFlowService = new TowerFlowService();
        this.globalMusicService = new GlobalMusicService();
    }

    /**
     * Starts the application by showing the menu and maximizing the window.
     */
    public void start() {
        switchScene(Scenes.TITLE_SCREEN);
        PrimaryStage.setMaximized(true);
    }

    /**
     * Switches the current scene while preserving window size and position.
     *
     * @param scene the scene to switch to
     */
    private void switchScene(Scenes scene) {
        globalMusicService.updateMusicForScene(scene);
        getPrimaryStageProperties();
        switch (scene) {
            case TITLE_SCREEN -> this.titleScreenController.show(PrimaryStage);
            case PREBUILD_TEAM -> this.preBuildTeamController.show(PrimaryStage);
            case SELECTION -> this.selectionController.show(PrimaryStage);
            case EDIT_TEAM -> this.editTeamController.show(PrimaryStage);
            case FIGHT_AGAINST_CPU -> this.fightAgainstCPUController.show(PrimaryStage);
            case VICTORY -> this.victoryController.show(PrimaryStage);
            case DEFEAT -> this.defeatController.show(PrimaryStage);
            case REWARD -> this.rewardController.show(PrimaryStage);
            case LEVEL_UP -> this.levelUpController.show(PrimaryStage);
            case ADD_BUGEMON -> this.addBugemonController.show(PrimaryStage);
            default -> throw new IllegalArgumentException("Invalid scene: " + scene);
        }
        ensureFullScreen();
    }

    private void getPrimaryStageProperties() {
        width = PrimaryStage.getWidth();
        height = PrimaryStage.getHeight();
        x = PrimaryStage.getX();
        y = PrimaryStage.getY();
    }

    private void ensureFullScreen() {
        if (PrimaryStage.isShowing()) {
            PrimaryStage.setWidth(width);
            PrimaryStage.setHeight(height);
            PrimaryStage.setX(x);
            PrimaryStage.setY(y);
        }
    }

    /**
     * Opens the view managed by the given controller in a secondary modal window.
     *
     * @param controller the controller responsible for the view to display
     */
    private void openSubScene(Controller<?> controller) {
        Stage subStage = new Stage();
        subStage.initOwner(PrimaryStage);
        subStage.initModality(Modality.APPLICATION_MODAL);
        subStage.initStyle(StageStyle.UNDECORATED);
        controller.attachTo(subStage);
        subStage.showAndWait();
    }

    /**
     * Opens the item selection sub-window for the fight scene.
     */
    public void openItemChoiceWindow() {
        try {
            ItemChoiceController itemChoiceController = new ItemChoiceController(this);
            openSubScene(itemChoiceController);
        } catch (IOException | IllegalStateException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre des objets.");
        }
    }

    /**
     * Opens the Bugemon switch sub-window for the fight scene.
     */
    public void openSwitchWindow(boolean onPurpose) {
        try {
            SwitchBugemonController switchController = new SwitchBugemonController(this, onPurpose);
            openSubScene(switchController);
        } catch (IOException | IllegalStateException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre de changement de Bugémon.");
        }
    }

    /**
     * Resets the selection view and navigates to it for creating a new team.
     */
    public void openNewSelection() {
        this.switchScene(Scenes.SELECTION);
    }

    public void goToPrebuildTeam() {
        this.switchScene(Scenes.PREBUILD_TEAM);
    }

    public void goToAddBugemon() {
        this.switchScene(Scenes.ADD_BUGEMON);
    }

    /**
     * Prepares the edit-team controller with the given team, then navigates to the edit scene.
     * The view is synchronized by {@link EditTeamController#onshow()}.
     */
    public void openSelectionForEdit(String teamId) {
        this.editTeamController.loadTeamForEditing(teamId);
        this.switchScene(Scenes.EDIT_TEAM);
    }

    public void goToTitleScreen() {
        try {
            gameSessionService.returnToMenu();
            this.switchScene(Scenes.TITLE_SCREEN);
        } catch (SavingTeamFailedException e) {
            this.fatalError("Sauvegarde échouée. Les équipes n'ont pas été sauvegardées.");
        }
    }


    public void goToVictoryScreen(int enemyCount) {
        this.victoryController.prepareVictory(enemyCount);
        this.switchScene(Scenes.VICTORY);
    }

    public void goToLevelUpScreen(List<Bugemon> bugemonToLevelUp) {
        this.levelUpController.startLevelUpList(bugemonToLevelUp);
        this.switchScene(Scenes.LEVEL_UP);
    }

    public void goToDefeatScreen() {
        this.switchScene(Scenes.DEFEAT);
    }

    public void goToRewardScreen() {
        this.switchScene(Scenes.REWARD);
    }

    public void startNewTower() {
        towerFlowService.startNewTower();
        goToNextFight();
    }

    private void goToNextFight() {
        this.switchScene(Scenes.FIGHT_AGAINST_CPU);
    }

    public void goToNextTowerStep() {
        try {
            TowerNextAction action = towerFlowService.advanceToNextAction();
            switch (action) {
                case FIGHT -> goToNextFight();
                case REWARD -> goToRewardScreen();
                case TITLE_SCREEN -> goToTitleScreen();
            }
        } catch (TowerNotInitializedException e) {
            fatalError("Une erreur est survenue lors de la progression dans la tour.");
        }
    }

    public void onSwitchBugemonChosen(String selectedId, boolean onPurpose) {
        try {
            fightAgainstCPUController.onSwitchBugemonChosen(selectedId, onPurpose);
        } catch (BugemonNotInTeamException | IOException e) {
            fatalError("Une erreur est survenue lors du changement de Bugemon Actif");
        }
    }

    public void onSwitchCancelled() {
        fightAgainstCPUController.onSwitchCancelled();
    }

    public void onItemChosen(Item item) {
        fightAgainstCPUController.onItemChosen(item);
    }

    /**
     * Displays a fatal error message and return to the Title screen.
     *
     * @param reason description of the error
     */
    public void fatalError(String reason) {
        FatalErrorView.show(reason);
        this.switchScene(Scenes.TITLE_SCREEN);
    }

    public void openBonusRewardWindow(LevelUpBonus bonus) {
        try {
            ApplyBonusRewardController bonusController = new ApplyBonusRewardController(this, bonus);
            openSubScene(bonusController);
        } catch (IOException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre de bonus.");
        }
    }

    public void openLearnAttackWindow(Attack newAttack) {
        try {
            LearnNewAttackController learnController = new LearnNewAttackController(this, newAttack);
            openSubScene(learnController);
        } catch (IOException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre d'apprentissage d'attaque.");
        }
    }

    public void openSelectAttackWindow(Bugemon target, Attack newAttack) {
        try {
            SelectAttackController selectAttack = new SelectAttackController(this, target, newAttack);
            openSubScene(selectAttack);
        } catch (IOException e) {
            fatalError("Une erreur est survenue lors du chargement de la sélection d'attaque.");
        }
    }
}