package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.Bugemon.TeamBugemon;
import views.FatalErrorView;

import java.io.IOException;

public class MetaController {

    public enum Scenes {
        MENU,
        PREBUILD_TEAM,
        SELECTION
    }

    private boolean isMaximized = false;
    private final Stage stage;
    private final MenuController menuController;
    private final PreBuildTeamController preBuildTeamController;
    private final SelectionController selectionController;

    // Liste partagée des équipes — injectée dans les controllers qui en ont besoin
    private final ObservableList<TeamBugemon> teams = FXCollections.observableArrayList();

    public MetaController(Stage stage) throws IOException {
        this.stage = stage;
        this.menuController = new MenuController(this);
        this.preBuildTeamController = new PreBuildTeamController(this, teams);
        this.selectionController = new SelectionController(this, teams);
    }

    public void start() {
        switchScene(Scenes.MENU);
        stage.setMaximized(true);
    }

    public void switchScene(Scenes scene) {
        double width = stage.getWidth();
        double height = stage.getHeight();
        double x = stage.getX();
        double y = stage.getY();

        switch (scene) {
            case MENU -> this.menuController.show(stage);
            case PREBUILD_TEAM -> this.preBuildTeamController.show(stage);
            case SELECTION -> this.selectionController.show(stage);
            default -> throw new IllegalArgumentException("Invalid scene: " + scene);
        }

        if (stage.isShowing()) {
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setX(x);
            stage.setY(y);
        }
    }

    public void openNewSelection() {
        this.selectionController.reset();
        this.switchScene(Scenes.SELECTION);
    }

    public void openSelectionForEdit(TeamBugemon team) {
        this.selectionController.reset();
        this.selectionController.loadTeamForEditing(team);
        this.switchScene(Scenes.SELECTION);
    }

    public void fatalError(String reason) {
        FatalErrorView.show("Fatal error, closing the application.\nReason: " + reason);
        System.exit(1);
    }
}