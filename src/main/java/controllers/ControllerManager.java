package controllers;

import controllers.fight.FightAgainstCPUController;
import controllers.fight.ItemChoiceController;
import controllers.fight.SwitchBugemonController;
import controllers.teamchoice.PreBuildTeamController;
import controllers.teamchoice.SelectionController;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.game.Item;
import services.game.GameStateHolder;
import views.FatalErrorView;
import views.View;

import java.io.IOException;

/**
 * Central controller that manages navigation between scenes and holds shared application state.
 * Creates and owns all sub-controllers and the shared team list.
 */
public class ControllerManager {

    private double width;
    private double height;
    private double x;
    private double y;

    /** Enumeration of all available scenes in the application. */
    private enum Scenes {
        TITLE_SCREEN,
        PREBUILD_TEAM,
        SELECTION,
        FIGHT_AGAINST_CPU
    }

    private final Stage PrimaryStage;
    private final TitleScreenController titleScreenController;
    private final PreBuildTeamController preBuildTeamController;
    private final SelectionController selectionController;
    private final FightAgainstCPUController fightAgainstCPUController;


    /**
     * Initializes all controllers and views.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if any FXML file fails to load
     */
    public ControllerManager(Stage stage) throws IOException {
        this.PrimaryStage = stage;
        this.titleScreenController = new TitleScreenController(this);
        this.preBuildTeamController = new PreBuildTeamController(this);
        this.selectionController = new SelectionController(this);
        this.fightAgainstCPUController = new FightAgainstCPUController(this);
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
        getPrimaryStageProperties();
        switch (scene) {
            case TITLE_SCREEN -> this.titleScreenController.show(PrimaryStage);
            case PREBUILD_TEAM -> this.preBuildTeamController.show(PrimaryStage);
            case SELECTION -> this.selectionController.show(PrimaryStage);
            case FIGHT_AGAINST_CPU -> this.fightAgainstCPUController.show(PrimaryStage);
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
        } catch (IOException e) {
            fatalError("couldn't load the view for the items : " + e.getMessage());
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
    public void openSwitchWindow() {
        try {
            SwitchBugemonController switchController = new SwitchBugemonController(this);
            openSubScene(switchController.getView(), !GameStateHolder.getInstance().isSwitchOnPurpose());
        } catch (IOException e) {
            fatalError("couldn't load the view for the bugemon of the team : " + e.getMessage());
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

    /**
     * Resets the selection view, pre-selects the given team's Bugémons, and navigates to it.
     *
     */
    public void openSelectionForEdit(int index) {
        this.selectionController.reset();
        this.selectionController.loadTeamForEditing(index);
        this.switchScene(Scenes.SELECTION);
    }

    public void goToFightAgainstCPU() {
        this.fightAgainstCPUController.setFight();
        this.switchScene(Scenes.FIGHT_AGAINST_CPU);
    }

    public void goToTitleScreen() {
        this.switchScene(Scenes.TITLE_SCREEN);
    }

    public void onSwitchBugemonChosen(String selectedId) {
        fightAgainstCPUController.onSwitchBugemonChosen(
                selectedId,
                GameStateHolder.getInstance().isSwitchOnPurpose()
        );
    }

    public void onSwitchCancelled() {
        fightAgainstCPUController.onSwitchCancelled();
    }

    public void onItemChosen(Item item) {
        fightAgainstCPUController.onItemChosen(item);
    }

    /**
     * Displays a fatal error message and exits the application.
     *
     * @param reason description of the error
     */
    public void fatalError(String reason) {
        FatalErrorView.show("Fatal error, closing the application.\nReason: " + reason);
        System.exit(1);
    }
}