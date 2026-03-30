package controllers.teamchoice;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.TeamBugemon;
import services.dataprovider.TeamService;
import services.game.GameStateHolder;
import views.teamchoice.PreBuildTeamView;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the team management screen.
 * Handles creating, renaming, deleting and modifying saved teams.
 */
public class PreBuildTeamController extends Controller<PreBuildTeamView> {

    /** The shared list of saved teams. */
    private final List<TeamBugemon> teams;

    /**
     * @param metaController the central controller
     * @param teams the shared observable list of saved teams
     * @throws IOException if the FXML fails to load
     */
    public PreBuildTeamController(ControllerManager metaController) throws IOException {
        super(metaController, new PreBuildTeamView());
        this.teams = GameStateHolder.getInstance().getSavedTeams();
        this.view.setController(this);
        this.view.refreshTeams(teams);
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }

    /** Opens the selection screen to create a new team. */
    public void goToNewTeam() {
        this.controllerManager.openNewSelection();
    }

    /**
     * Opens the selection screen to edit an existing team.
     *
     */
    public void modifyTeam(int index) {
        this.controllerManager.openSelectionForEdit(index);
    }

    /**
     * Removes a team from the saved list and refreshes the view.
     *
     */
    public void deleteTeam(int index) {
        this.teams.remove(index);
        TeamService.saveTeams(teams);
        this.view.refreshTeams(teams);
    }

    /**
     * Renames a team and refreshes the list display.
     *
     * @param newName the new name to assign
     */
    public void renameTeam(int index, String newName) {
        teams.get(index).setName(newName);
        TeamService.saveTeams(teams);
        this.view.refreshTeamList();
    }

    /**
     * Refreshes the view's team list (e.g. after a new team is added).
     */
    public void refreshView() {
        this.view.refreshTeams(teams);
    }

    /**
     * Starts a game with the selected team.
     *
     */
    public void play(int index) {
        GameStateHolder.getInstance().setCurrentTeamPlayer(teams.get(index));
        this.controllerManager.goToFight();
    }

    /**
     * Loads a saved team for editing and returns to the selection screen.
     *
     * @param index the index of the team to load
     */
    public void loadTeam(int index) {
        this.controllerManager.openSelectionForEdit(index);
    }

}