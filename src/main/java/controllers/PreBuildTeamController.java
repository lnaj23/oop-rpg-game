package controllers;

import javafx.collections.ObservableList;
import models.Bugemon.TeamBugemon;
import views.PreBuildTeamView;

import java.io.IOException;

public class PreBuildTeamController extends Controller<PreBuildTeamView> {

    private final ObservableList<TeamBugemon> teams;

    public PreBuildTeamController(MetaController metaController, ObservableList<TeamBugemon> teams) throws IOException {
        super(metaController, new PreBuildTeamView(teams));
        this.teams = teams;
        this.view.setController(this);
    }

    public void goToMenu() {
        this.metaController.switchScene(MetaController.Scenes.MENU);
    }

    public void goToNewTeam() {
        this.metaController.openNewSelection();
    }

    public void modifyTeam(TeamBugemon team) {
        this.metaController.openSelectionForEdit(team);
    }

    public void deleteTeam(TeamBugemon team) {
        this.teams.remove(team);
    }

    public void renameTeam(TeamBugemon team, String newName) {
        team.setName(newName);
        this.view.refreshTeamList();
    }

    public void play(TeamBugemon team) {
        // Logique de jeu à implémenter
    }
}