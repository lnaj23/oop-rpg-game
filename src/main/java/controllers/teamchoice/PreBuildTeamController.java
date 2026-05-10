package controllers.teamchoice;

import controllers.Controller;
import controllers.ControllerManager;
import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.SavingTeamFailedException;
import models.bugemon.TeamBugemon;
import services.controller.dataprovider.TeamProvider;
import services.app.GameStateHolder;
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
    private TeamProvider teamProvider = new TeamProvider();

    /**
     * @param metaController the central controller
     * @throws IOException if the FXML fails to load
     */
    public PreBuildTeamController(ControllerManager metaController) throws IOException, AttackNotFoundException, JsonResourceNotFoundException {
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
        try {
            teamProvider.saveTeams(teams);
        } catch (SavingTeamFailedException e) {
            controllerManager.fatalError("Une erreur est survenue lors de la suppression de l'équipe.");
        }
        this.view.refreshTeams(teams);
    }

    /**
     * Renames a team and refreshes the list display.
     *
     * @param newName the new name to assign
     */
    public void renameTeam(int index, String newName) {
        teams.get(index).setName(newName);
        try {
            teamProvider.saveTeams(teams);
        } catch (SavingTeamFailedException e) {
            controllerManager.fatalError("Une erreur est survenue lors du renommage de l'équipe.");
            return;
        }
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
        this.controllerManager.startNewTower();
    }

}