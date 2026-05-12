package controllers.teamchoice;

import controllers.Controller;
import controllers.ControllerManager;
import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.LoadingTeamFailedException;
import exceptions.SavingTeamFailedException;
import models.bugemon.TeamBugemon;
import services.controller.prebuiltteam.TeamService;
import services.app.GameStateHolder;
import views.teamchoice.PreBuildTeamView;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the team management screen.
 * Handles creating, renaming, deleting and modifying saved teams.
 */
public class PreBuildTeamController extends Controller<PreBuildTeamView> {

    private final TeamService teamProvider = new TeamService();

    public PreBuildTeamController(ControllerManager metaController)
            throws IOException, AttackNotFoundException, JsonResourceNotFoundException {

        super(metaController, new PreBuildTeamView());
        this.view.setController(this);
        refreshView();
    }

    public void refreshView() {
        try {
            List<TeamBugemon> teams = teamProvider.loadTeams();
            this.view.refreshTeams(teams);
        } catch (LoadingTeamFailedException | AttackNotFoundException | JsonResourceNotFoundException e) {
            controllerManager.fatalError("Une erreur est survenue lors du chargement des équipes.");
        }
    }

    public void modifyTeam(String teamId) {
        this.controllerManager.openSelectionForEdit(teamId);
    }

    public void deleteTeam(String teamId) {
        try {
            teamProvider.deleteTeam(teamId);
            refreshView();
        } catch (SavingTeamFailedException e) {
            controllerManager.fatalError("Une erreur est survenue lors de la suppression de l'équipe.");
        }
    }

    public void renameTeam(String teamId, String newName) {
        try {
            teamProvider.renameTeam(teamId, newName);
            refreshView();
        } catch (IllegalArgumentException e) {
            view.showError("Nom déjà utilisé", e.getMessage());
        } catch (SavingTeamFailedException e) {
            controllerManager.fatalError("Une erreur est survenue lors du renommage de l'équipe.");
        }
    }

    public void play(String teamId) {
        try {
            TeamBugemon team = teamProvider.findTeamById(teamId);

            GameStateHolder.getInstance().startSavedTeam(team, teamId);

            this.controllerManager.startNewTower();

        } catch (LoadingTeamFailedException e) {
            controllerManager.fatalError("Une erreur est survenue lors du chargement de l'équipe.");
        }
    }

    @Override
    protected void onshow() {
        refreshView();
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }

    /** Opens the selection screen to create a new team. */
    public void goToNewTeam() {
        this.controllerManager.openNewSelection();
    }

}