package controllers;

import javafx.collections.ObservableList;
import models.Bugemon.TeamBugemon;
import views.SelectionView;

import java.io.IOException;

public class SelectionController extends Controller<SelectionView> {

    private final ObservableList<TeamBugemon> teams;
    private TeamBugemon teamBeingEdited = null;

    public SelectionController(MetaController metaController, ObservableList<TeamBugemon> teams) throws IOException {
        super(metaController, new SelectionView());
        this.teams = teams;
        this.view.setController(this);
    }

    public void goToMenu() {
        this.metaController.switchScene(MetaController.Scenes.MENU);
    }

    public void loadTeamForEditing(TeamBugemon team) {
        this.teamBeingEdited = team;
        this.view.preSelectTeam(team);
    }

    public void reset() {
        this.teamBeingEdited = null;
        this.view.reset();
    }

    public boolean isEditing() {
        return this.teamBeingEdited != null;
    }

    public void confirmSelection(TeamBugemon playerTeam, String teamName) {
        if (this.teamBeingEdited != null) {
            // Mode modification : remplace le contenu de l'équipe existante
            this.teamBeingEdited.getTeam().clear();
            this.teamBeingEdited.getTeam().addAll(playerTeam.getTeam());
            this.teamBeingEdited = null;
        } else {
            // Mode création : nomme l'équipe et l'ajoute à la liste
            playerTeam.setName(teamName);
            this.teams.add(playerTeam);
        }
        this.metaController.switchScene(MetaController.Scenes.PREBUILD_TEAM);
    }
}