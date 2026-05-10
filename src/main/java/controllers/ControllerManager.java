package controllers;

import controllers.afterfight.DefeatController;
import controllers.afterfight.LevelUpController;
import controllers.afterfight.VictoryController;
import controllers.fight.*;
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
import models.game.TowerNodeType;
import models.bugemon.Bugemon;
import models.game.Item;
import models.game.LevelTower;
import services.app.MusicService;
import services.controller.dataprovider.TeamProvider;
import services.models.game.ExperienceService;
import services.app.GameStateHolder;
import views.FatalErrorView;
import views.View;

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
    private final FightAgainstCPUController fightAgainstCPUController;
    private final VictoryController victoryController;
    private final DefeatController defeatController;
    private final RewardController rewardController;
    private final LevelUpController levelUpController;
    private final AddBugemonController addBugemonController;

    private TeamProvider teamProvider = new TeamProvider();

    /** Enumeration of all available scenes in the application. */
    public enum Scenes {
        TITLE_SCREEN,
        PREBUILD_TEAM,
        SELECTION,
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
        this.selectionController = new SelectionController(this);
        this.fightAgainstCPUController = new FightAgainstCPUController(this);
        this.victoryController = new VictoryController(this);
        this.defeatController = new DefeatController(this);
        this.rewardController = new RewardController(this);
        this.levelUpController = new LevelUpController(this);
        this.addBugemonController = new AddBugemonController(this);
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
        MusicService.updateMusicForScene(scene);
        getPrimaryStageProperties();
        switch (scene) {
            case TITLE_SCREEN -> this.titleScreenController.show(PrimaryStage);
            case PREBUILD_TEAM -> this.preBuildTeamController.show(PrimaryStage);
            case SELECTION -> this.selectionController.show(PrimaryStage);
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
     * Opens the given view in a secondary modal window.
     *
     * @param view the view to display in the sub-window
     * @param wait {@code true} to use {@code showAndWait()}, {@code false} to use {@code show()}
     */
    private void openSubScene(View<?> view, boolean wait) {
        Stage subStage = new Stage();
        subStage.initOwner(PrimaryStage);
        subStage.initModality(Modality.APPLICATION_MODAL);
        subStage.initStyle(StageStyle.UNDECORATED);

        view.attachTo(subStage);

        if (wait) {
            subStage.showAndWait();
        } else {
            subStage.show();
        }
    }

    /**
     * Opens the item selection sub-window for the fight scene.
     * <p>
     * The corresponding sub-controller is created here and the selected item
     * is sent back to the fight controller through the meta-controller.
     * </p>
     *
     */
    public void openItemChoiceWindow() {
        try {
            ItemChoiceController itemChoiceController = new ItemChoiceController(this);
            openSubScene(itemChoiceController.getView(), true);
        } catch (IOException | IllegalStateException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre des objets.");
        }
    }

    /**
     * Opens the Bugemon switch sub-window for the fight scene.
     * <p>
     * The corresponding sub-controller is created here and the selected Bugemon
     * is sent back to the fight controller through the meta-controller.
     * </p>
     *
     */
    public void openSwitchWindow(boolean onPurpose) {
        try {
            SwitchBugemonController switchController = new SwitchBugemonController(this, onPurpose);
            openSubScene(switchController.getView(), !onPurpose);
        } catch (IOException | IllegalStateException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre de changement de Bugémon.");
        }
    }

    /**
     * Resets the selection view and navigates to it for creating a new team.
     */
    public void openNewSelection() {
        this.selectionController.reset();
        this.switchScene(Scenes.SELECTION);
    }

    public void goToPrebuildTeam() {
        this.preBuildTeamController.refreshView();
        this.switchScene(Scenes.PREBUILD_TEAM);
    }

    public void goToAddBugemon() {
        this.addBugemonController.initAddBugemonMenu();
        this.switchScene(Scenes.ADD_BUGEMON);
    }

    /**
     * Resets the selection view, pre-selects the given team's Bugémons, and navigates to it.
     *
     */
    public void openSelectionForEdit(int index) {
        this.selectionController.reset();
        this.selectionController.loadTeamForEditing(index);
        this.switchScene(Scenes.SELECTION);
    }

    public void goToTitleScreen() {
        if (GameStateHolder.getInstance().getCurrentTeamPlayer() != null) {
            try {
                teamProvider.saveTeams(GameStateHolder.getInstance().getSavedTeams());
            } catch (SavingTeamFailedException e ) {
                this.fatalError("Sauvegarde échouée. Les équipes n'ont pas été sauvegardées.");
                return;
            }
        }
        GameStateHolder.getInstance().clearCurrentTeamPlayer();
        GameStateHolder.getInstance().clearCurrentLevelTower();
        this.switchScene(Scenes.TITLE_SCREEN);
    }


    public void goToVictoryScreen(int enemyCount) {
        LevelTower tower = GameStateHolder.getInstance().getCurrentLevelTower();
        int floorNumber = tower.getCurrentFloor();
        boolean isBossFight = tower.getCurrentStepType() == TowerNodeType.BOSS;

        int totalXp = ExperienceService.computeCombatXp(floorNumber, isBossFight, enemyCount);

        this.victoryController.updateExp(totalXp);
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
        try {
            this.rewardController.setReward();
            this.switchScene(Scenes.REWARD);
        } catch (JsonResourceNotFoundException e){
            this.fatalError("Une erreur est survenue lors du chargement des récompenses");
        }
    }

    public void startNewTower() {
        GameStateHolder.getInstance().setCurrentLevelTower(new LevelTower());
        this.goToNextFight();
    }

    private void goToNextFight() {
        try {
            this.fightAgainstCPUController.setFight();
        } catch (BugemonNotInTeamException | AttackNotFoundException | JsonResourceNotFoundException | IOException e){
            this.fatalError("Une erreur est survenue lors du chargement du combat.");
            return;
        }
        this.switchScene(Scenes.FIGHT_AGAINST_CPU);
    }

    public void goToNextTowerStep(){
        GameStateHolder.getInstance().advanceStep();
        if(GameStateHolder.getInstance().getCurrentLevelTower().isFinished()) {
            this.goToTitleScreen();
            return;
        }
        try {
            TowerNodeType nextStep = GameStateHolder.getInstance().getCurrentStepType();
            switch (nextStep) {
                case COMBAT, BOSS -> this.goToNextFight();
                case REWARD -> this.goToRewardScreen();
            }
        } catch (TowerNotInitializedException e){
            this.fatalError("Une erreur est survenue lors de la progression dans la tour.");
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
            openSubScene(bonusController.getView(), true);
        } catch (IOException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre de bonus.");
        }
    }

    public void openLearnAttackWindow(Attack newAttack) {
        try {
            LearnNewAttackController learnController = new LearnNewAttackController(this, newAttack);
            openSubScene(learnController.getView(), true);
        } catch (IOException e) {
            fatalError("Une erreur est survenue lors du chargement de la fenêtre d'apprentissage d'attaque.");
        }
    }

    public void openSelectAttackWindow(Bugemon target, Attack newAttack) {
        try {
            SelectAttackController selectAttack = new SelectAttackController(this, target, newAttack);
            openSubScene(selectAttack.getView(), true);
        } catch (IOException e) {
            fatalError("Une erreur est survenue lors du chargement de la sélection d'attaque.");
        }
    }
}