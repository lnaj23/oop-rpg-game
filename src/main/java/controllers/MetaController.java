package controllers;

import controllers.fight.FightController;
import controllers.fight.ItemChoiceController;
import controllers.fight.SwitchBugemonController;
import controllers.teamchoice.PreBuildTeamController;
import controllers.teamchoice.SelectionController;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.game.GameStateHolder;
import views.FatalErrorView;
import views.View;

import java.io.IOException;

/**
 * Central controller that manages navigation between scenes and holds shared application state.
 * Creates and owns all sub-controllers and the shared team list.
 */
public class MetaController {

    /** Enumeration of all available scenes in the application. */
    public enum Scenes {
        TITLE_SCREEN,
        PREBUILD_TEAM,
        SELECTION,
        FIGHT
    }

    private final Stage PrimaryStage;
    private final TitleScreenController titleScreenController;
    private final PreBuildTeamController preBuildTeamController;
    private final SelectionController selectionController;
    private final FightController fightController;


    /**
     * Initializes all controllers and views.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if any FXML file fails to load
     */
    public MetaController(Stage stage) throws IOException {
        this.PrimaryStage = stage;
        this.titleScreenController = new TitleScreenController(this);
        this.preBuildTeamController = new PreBuildTeamController(this);
        this.selectionController = new SelectionController(this);
        this.fightController = new FightController(this);
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
        double width = PrimaryStage.getWidth();
        double height = PrimaryStage.getHeight();
        double x = PrimaryStage.getX();
        double y = PrimaryStage.getY();

        switch (scene) {
            case TITLE_SCREEN -> this.titleScreenController.show(PrimaryStage);
            case PREBUILD_TEAM -> {
                this.preBuildTeamController.show(PrimaryStage);
                this.preBuildTeamController.refreshView();
            }
            case SELECTION -> this.selectionController.show(PrimaryStage);
            case FIGHT -> this.fightController.show(PrimaryStage);
            default -> throw new IllegalArgumentException("Invalid scene: " + scene);
        }
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
     * @param items the list of available items to display
     */
    public void openItemChoiceWindow() {
        try {
            ItemChoiceController itemChoiceController = new ItemChoiceController(this, fightController::onItemChosen);
            openSubScene(itemChoiceController.getView(), true);
        } catch (IOException e) {
            fatalError("Impossible d'ouvrir la vue des objets : " + e.getMessage());
        }
    }

    /**
     * Opens the Bugemon switch sub-window for the fight scene.
     * <p>
     * The corresponding sub-controller is created here and the selected Bugemon
     * is sent back to the fight controller through the meta-controller.
     * </p>
     *
     * @param team the player's team to display in the switch window
     * @param onPurpose {@code true} if the player opened the switch window voluntarily,
     *                  {@code false} if the switch is forced
     * @param currentOnFieldId the identifier of the Bugemon currently on the field
     */
    public void openSwitchWindow() {
        try {
            SwitchBugemonController switchController = new SwitchBugemonController(this);
            openSubScene(switchController.getView(), !GameStateHolder.getInstance().isSwitchOnPurpose());
        } catch (IOException e) {
            fatalError("Impossible d'ouvrir la vue de changement : " + e.getMessage());
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
        this.switchScene(Scenes.PREBUILD_TEAM);
    }

    /**
     * Resets the selection view, pre-selects the given team's Bugémons, and navigates to it.
     *
     * @param team the team to edit
     */
    public void openSelectionForEdit(int index) {
        this.selectionController.reset();
        this.selectionController.loadTeamForEditing(index);
        this.switchScene(Scenes.SELECTION);
    }

    public void goToFight() {
        this.fightController.setFight();
        this.switchScene(Scenes.FIGHT);
    }

    public void goToTitleScreen() {
        this.switchScene(Scenes.TITLE_SCREEN);
    }

    public void onSwitchBugemonChosen(Object selectedId) {
        fightController.onSwitchBugemonChosen(
                selectedId,
                GameStateHolder.getInstance().isSwitchOnPurpose()
        );
    }

    public void onSwitchCancelled() {
        fightController.onSwitchCancelled();
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