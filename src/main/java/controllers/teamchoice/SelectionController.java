package controllers.teamchoice;

import controllers.Controller;
import controllers.ControllerManager;
import models.GameState;
import models.bugemon.dto.BugemonDTO;
import models.bugemon.TeamBugemon;
import services.Music_player;
import services.dataprovider.TeamProvider;
import services.dataprovider.BugemonProvider;
import services.game.GameStateHolder;
import views.teamchoice.selection.SelectionView;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the Bugémon selection screen.
 * Handles both new team creation and editing of existing teams.
 * Coordinates BugemonGridView and TeamPanelView via SelectionView.
 */
public class SelectionController extends Controller<SelectionView> {

    /** The shared list of saved teams. */
    private final List<TeamBugemon> teams;

    /** The team currently being edited, or null if creating a new team. */
    private TeamBugemon teamBeingEdited = null;

    /** The team currently being built by the player. */
    private TeamBugemon playerTeam;

    /**
     * @param metaController the central controller
     * @throws IOException if the FXML fails to load
     */
    public SelectionController(ControllerManager metaController) throws IOException {
        super(metaController, new SelectionView());
        this.teams = GameStateHolder.getInstance().getSavedTeams();
        this.playerTeam = new TeamBugemon();
        this.view.setController(this);
        BugemonProvider.getInstance().getAllBugemonsDTO();
    }

    /**
     * Returns the number of Bugémons currently in the player's team.
     * Used by sub-views to check team size without pulling the full team object.
     *
     * @return the size of the player's current team
     */
    public int getPlayerTeamSize() {
        return playerTeam.getSize();
    }

    /** Navigates back to the Title Screen. */
    public void goToTitleScreen() {
        this.controllerManager.goToTitleScreen();
    }

    /** Navigates to the pre-built team screen. */
    public void goToPrebuildTeam() {
        this.controllerManager.goToPrebuildTeam();
    }

    /**
     * Enters edit mode for an existing team and pre-selects its Bugémons in the view.
     *
     */
    public void loadTeamForEditing(int index) {
        TeamBugemon team = GameStateHolder.getInstance().getSavedTeams().get(index);
        this.teamBeingEdited = team;
        team.getTeamDTO().forEach(bug -> this.playerTeam.addBugemonDTO(bug));
        this.view.preSelectTeam(team);
        this.view.setEditMode(true);
    }

    /** Resets the selection screen to its initial empty state. */
    public void reset() {
        this.teamBeingEdited = null;
        this.playerTeam = new TeamBugemon();
        this.view.reset(BugemonProvider.getInstance().getAllBugemonsDTO());
        this.view.setEditMode(false);
        Music_player.chose_ambiance(GameState.TEAM_CREATION);
    }

    /**
     * Returns whether the controller is currently in edit mode.
     *
     * @return {@code true} if editing an existing team, {@code false} if creating a new one
     */
    public boolean isEditing() {
        return this.teamBeingEdited != null;
    }


    /**
     * Handles a click on a Bugémon button in the grid.
     * Adds or removes the Bugémon from the player's team, notifies the view,
     * and pushes the updated team list to the view.
     *
     * @param btn         the button that was clicked
     * @param bugemon     the Bugémon associated with that button
     * @param wasSelected true if the button was already selected before the click
     * @param teamFull    true if the team has already reached the maximum size
     */
    public void onBugemonClicked(Button btn, BugemonDTO bugemon, boolean wasSelected, boolean teamFull) {
        if (wasSelected) {
            playerTeam.removeBugemonDTO(bugemon);
            this.view.deselectButton(btn);
            this.view.onBugemonRemoved(bugemon);
        } else {
            if (teamFull) {
                this.view.showTeamFullError();
                return;
            }
            playerTeam.addBugemonDTO(bugemon);
            this.view.selectButton(btn, bugemon);
            this.view.onBugemonAdded(bugemon);
            this.view.showBugemonStats(bugemon);
        }
        this.view.refreshTeamGrid(playerTeam.getTeamDTO());
    }

    /**
     * Confirms the current selection. Updates the existing team if editing,
     * or adds a new named team to the shared list and persists it to disk.
     *
     * @param teamName the name for the new team (ignored in edit mode, can be null)
     */
    public void saveSelection(String teamName) {
        if (this.teamBeingEdited != null) {
            this.teamBeingEdited.getTeam().clear();
            this.teamBeingEdited.getTeam().addAll(playerTeam.getTeam());
            this.teamBeingEdited = null;
        } else {
            playerTeam.setName(teamName);
            this.teams.add(playerTeam);
        }
        TeamProvider.saveTeams(this.teams);
    }

    public void goToFight() {
        GameStateHolder.getInstance().setCurrentTeamPlayer(playerTeam);
        this.controllerManager.startNewTower();
    }
}