package controllers;

import javafx.stage.Stage;
import models.Bugemon.TeamBugemon;
import views.FatalErrorView;

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
    private final List<TeamBugemon> teams = new ArrayList<>();

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