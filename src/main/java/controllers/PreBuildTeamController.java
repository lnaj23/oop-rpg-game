package controllers;

import javafx.collections.ObservableList;
import models.Bugemon.TeamBugemon;
import views.PreBuildTeamView;

import java.io.IOException;

/**
 * Controller for the team management screen.
 * Handles creating, renaming, deleting and modifying saved teams.
 */
public class PreBuildTeamController extends Controller<PreBuildTeamView> {

    /** The shared list of saved teams. */
    private final ObservableList<TeamBugemon> teams;

    /**
     * @param metaController the central controller
     * @param teams the shared observable list of saved teams
     * @throws IOException if the FXML fails to load
     */
    public PreBuildTeamController(MetaController metaController, ObservableList<TeamBugemon> teams) throws IOException {
        super(metaController, new PreBuildTeamView(teams));
        this.teams = teams;
        this.view.setController(this);
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.metaController.switchScene(MetaController.Scenes.MENU);
    }

    /** Opens the selection screen to create a new team. */
    public void goToNewTeam() {
        this.metaController.openNewSelection();
    }

    /**
     * Opens the selection screen to edit an existing team.
     *
     * @param team the team to modify
     */
    public void modifyTeam(TeamBugemon team) {
        this.metaController.openSelectionForEdit(team);
    }

    /**
     * Removes a team from the saved list.
     *
     * @param team the team to delete
     */
    public void deleteTeam(TeamBugemon team) {
        this.teams.remove(team);
    }

    /**
     * Renames a team and refreshes the list display.
     *
     * @param team the team to rename
     * @param newName the new name to assign
     */
    public void renameTeam(TeamBugemon team, String newName) {
        team.setName(newName);
        this.view.refreshTeamList();
    }

    /**
     * Starts a game with the selected team.
     *
     * @param team the team to play with
     */
    public void play(TeamBugemon team) {
        System.out.println("Jeu lancé");
    }
}