package controllers;

import javafx.collections.ObservableList;
import models.Bugemon.TeamBugemon;
import views.SelectionView;

import java.io.IOException;

/**
 * Controller for the Bugémon selection screen.
 * Handles both new team creation and editing of existing teams.
 */
public class SelectionController extends Controller<SelectionView> {

    /** The shared list of saved teams. */
    private final ObservableList<TeamBugemon> teams;

    /** The team currently being edited, or null if creating a new team. */
    private TeamBugemon teamBeingEdited = null;

    /**
     * @param metaController the central controller
     * @param teams the shared observable list of saved teams
     * @throws IOException if the FXML fails to load
     */
    public SelectionController(MetaController metaController, ObservableList<TeamBugemon> teams) throws IOException {
        super(metaController, new SelectionView());
        this.teams = teams;
        this.view.setController(this);
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.metaController.switchScene(MetaController.Scenes.MENU);
    }

    /**
     * Enters edit mode for an existing team and pre-selects its Bugémons in the view.
     *
     * @param team the team to edit
     */
    public void loadTeamForEditing(TeamBugemon team) {
        this.teamBeingEdited = team;
        this.view.preSelectTeam(team);
    }

    /** Resets the selection screen to its initial empty state. */
    public void reset() {
        this.teamBeingEdited = null;
        this.view.reset();
    }

    /**
     * @return true if currently editing an existing team, false if creating a new one
     */
    public boolean isEditing() {
        return this.teamBeingEdited != null;
    }

    /**
     * Confirms the current selection. Updates the existing team if editing,
     * or adds a new named team to the shared list.
     *
     * @param playerTeam the team built by the player
     * @param teamName the name for the new team (ignored in edit mode)
     */
    public void confirmSelection(TeamBugemon playerTeam, String teamName) {
        if (this.teamBeingEdited != null) {
            this.teamBeingEdited.getTeam().clear();
            this.teamBeingEdited.getTeam().addAll(playerTeam.getTeam());
            this.teamBeingEdited = null;
        } else {
            playerTeam.setName(teamName);
            this.teams.add(playerTeam);
        }
        this.metaController.switchScene(MetaController.Scenes.PREBUILD_TEAM);
    }
}