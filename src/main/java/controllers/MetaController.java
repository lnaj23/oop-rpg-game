package controllers;

import controllers.fight.FightController;
import controllers.fight.ItemChoiceController;
import controllers.fight.SwitchBugemonController;
import controllers.teamchoice.PreBuildTeamController;
import controllers.teamchoice.SelectionController;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.bugemon.TeamBugemon;
import models.bugemon.dto.BugemonDTO;
import models.game.dto.ItemDTO;
import services.dataprovider.TeamService;
import views.FatalErrorView;
import views.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private final Stage stage;
    private final TitleScreenController titleScreenController;
    private final PreBuildTeamController preBuildTeamController;
    private final SelectionController selectionController;
    private final FightController fightController;
    /** Shared list of saved teams, passed to all controllers that need it. */
    private final List<TeamBugemon> teams = new ArrayList<>(TeamService.loadTeams());
    /**
     * Initializes all controllers and views.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if any FXML file fails to load
     */
    public MetaController(Stage stage) throws IOException {
        this.stage = stage;
        this.titleScreenController = new TitleScreenController(this);
        this.preBuildTeamController = new PreBuildTeamController(this, teams);
        this.selectionController = new SelectionController(this, teams);
        this.fightController = new FightController(this);
    }

    /**
     * Starts the application by showing the menu and maximizing the window.
     */
    public void start() {
        switchScene(Scenes.TITLE_SCREEN);
        stage.setMaximized(true);
    }

    /**
     * Switches the current scene while preserving window size and position.
     *
     * @param scene the scene to switch to
     */
    public void switchScene(Scenes scene) {
        double width = stage.getWidth();
        double height = stage.getHeight();
        double x = stage.getX();
        double y = stage.getY();

        switch (scene) {
            case TITLE_SCREEN -> this.titleScreenController.show(stage);
            case PREBUILD_TEAM -> {
                this.preBuildTeamController.show(stage);
                this.preBuildTeamController.refreshView();
            }
            case SELECTION -> this.selectionController.show(stage);
            case FIGHT -> this.fightController.show(stage);
            default -> throw new IllegalArgumentException("Invalid scene: " + scene);
        }
        if (stage.isShowing()) {
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setX(x);
            stage.setY(y);
        }
    }

    /**
     * Opens the given view in a secondary modal window.
     *
     * @param view the view to display in the sub-window
     * @param wait {@code true} to use {@code showAndWait()}, {@code false} to use {@code show()}
     */
    public void openSubScene(View<?> view, boolean wait) {
        Stage subStage = new Stage();
        subStage.initOwner(stage);
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
    public void openItemChoiceWindow(List<ItemDTO> items) {
        try {
            ItemChoiceController itemChoiceController =
                    new ItemChoiceController(this, items, fightController::onItemChosen);
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
    public void openSwitchWindow(List<BugemonDTO> team, boolean onPurpose, Object currentOnFieldId) {
        try {
            SwitchBugemonController switchController = new SwitchBugemonController(
                    this,
                    team,
                    onPurpose,
                    currentOnFieldId,
                    selectedId -> fightController.onSwitchBugemonChosen(selectedId, onPurpose),
                    fightController::onSwitchCancelled
            );
            openSubScene(switchController.getView(), !onPurpose);
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

    /**
     * Resets the selection view, pre-selects the given team's Bugémons, and navigates to it.
     *
     * @param team the team to edit
     */
    public void openSelectionForEdit(TeamBugemon team) {
        this.selectionController.reset();
        this.selectionController.loadTeamForEditing(team);
        this.switchScene(Scenes.SELECTION);
    }

    public void goToFight(TeamBugemon team) {
        this.fightController.setPlayerTeam(team);
        this.switchScene(Scenes.FIGHT);
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