package controllers;

import models.bugemon.dto.TeamBugemonDTO;
import models.bugemon.TeamBugemon;
import services.bugemon.TeamService;
import views.PreBuildTeamView;

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
    public PreBuildTeamController(MetaController metaController, List<TeamBugemon> teams) throws IOException {
        super(metaController, new PreBuildTeamView());
        this.teams = teams;
        this.view.setController(this);
        this.view.refreshTeams(teams);
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }

    /** Opens the selection screen to create a new team. */
    public void goToNewTeam() {
        this.metaController.openNewSelection();
    }

    /**
     * Opens the selection screen to edit an existing team.
     *
     * @param teamDTO the team to modify, cast internally to {@link TeamBugemon}
     */
    public void modifyTeam(TeamBugemonDTO teamDTO) {
        this.metaController.openSelectionForEdit((TeamBugemon) teamDTO);
    }

    /**
     * Removes a team from the saved list and refreshes the view.
     *
     * @param teamDTO the team to delete, cast internally to {@link TeamBugemon}
     */
    public void deleteTeam(TeamBugemonDTO teamDTO) {
        this.teams.remove((TeamBugemon) teamDTO);
        TeamService.saveTeams(teams);
        this.view.refreshTeams(teams);
    }

    /**
     * Renames a team and refreshes the list display.
     *
     * @param teamDTO the team to rename, cast internally to {@link TeamBugemon}
     * @param newName the new name to assign
     */
    public void renameTeam(TeamBugemonDTO teamDTO, String newName) {
        ((TeamBugemon) teamDTO).setName(newName);
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
     * @param teamDTO the team to play with
     */
    public void play(TeamBugemonDTO teamDTO) {
        this.metaController.goToFight((TeamBugemon) teamDTO);
    }

    @Override
    public void onItemClicked() {

    }
}