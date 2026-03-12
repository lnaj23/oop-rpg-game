package controllers;

import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;
import models.bugemon.TeamBugemon;
import services.bugemon.BugemonProvider;
import views.selection.SelectionView;
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
     * @param teams          the shared list of saved teams
     * @throws IOException if the FXML fails to load
     */
    public SelectionController(MetaController metaController,
                               List<TeamBugemon> teams) throws IOException {
        super(metaController, new SelectionView());
        this.teams = teams;
        this.playerTeam = new TeamBugemon();
        this.view.setController(this);
        this.view.displayBugemons(BugemonProvider.getAllBugemonsDTO());
    }

    /**
     * Returns the team currently being built by the player.
     *
     * @return the player's current team as a DTO
     */
    public TeamBugemonDTO getPlayerTeam() {
        return playerTeam;
    }

    /** Navigates back to the Title Screen. */
    public void goToTitleScreen() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }

    /**
     * Enters edit mode for an existing team and pre-selects its Bugémons in the view.
     *
     * @param team the team to edit
     */
    public void loadTeamForEditing(TeamBugemon team) {
        this.teamBeingEdited = team;
        // Populate playerTeam so it matches the visually pre-selected buttons
        team.getTeamDTO().forEach(bug -> this.playerTeam.addBugemonDTO(bug));
        this.view.preSelectTeam(team);
    }

    /** Resets the selection screen to its initial empty state. */
    public void reset() {
        this.teamBeingEdited = null;
        this.playerTeam = new TeamBugemon();
        this.view.reset(BugemonProvider.getAllBugemonsDTO());
    }

    /**
     * @return true if currently editing an existing team, false if creating a new one
     */
    public boolean isEditing() {
        return this.teamBeingEdited != null;
    }

    /**
     * Builds and returns the stats text for a given Bugémon, for display in the popup.
     *
     * @param bugemon the Bugémon whose stats to format
     * @return formatted stats string
     */
    public String buildStatsText(BugemonDTO bugemon) {
        return "PV: "       + bugemon.getPv()               + "\n"
                + "ATK: "      + bugemon.getAttaque()          + "\n"
                + "DEF: "      + bugemon.getDefense()          + "\n"
                + "INIT: "     + bugemon.getInitiative()       + "\n"
                + "Attaques: " + bugemon.getAttackNamesAsString();
    }

    /**
     * Handles a click on a Bugémon button in the grid.
     * Adds or removes the Bugémon from the player's team and notifies the view.
     *
     * @param btn         the button that was clicked
     * @param bugemon     the Bugémon associated with that button
     * @param wasSelected true if the button was already selected before the click
     * @param teamFull    true if the team has already reached the maximum size
     */
    public void onBugemonClicked(Button btn, BugemonDTO bugemon,
                                 boolean wasSelected, boolean teamFull) {
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
            this.view.selectButton(btn);
            this.view.onBugemonAdded(bugemon);
            this.view.showBugemonStats(bugemon);
        }
    }

    /**
     * Confirms the current selection. Updates the existing team if editing,
     * or adds a new named team to the shared list.
     *
     * @param playerTeamDTO the team built by the player
     * @param teamName      the name for the new team (ignored in edit mode)
     */
    public void confirmSelection(TeamBugemonDTO playerTeamDTO, String teamName) {
        TeamBugemon team = (TeamBugemon) playerTeamDTO;

        if (this.teamBeingEdited != null) {
            this.teamBeingEdited.getTeam().clear();
            this.teamBeingEdited.getTeam().addAll(team.getTeam());
            this.teamBeingEdited = null;
        } else {
            team.setName(teamName);
            this.teams.add(team);
        }
        this.metaController.switchScene(MetaController.Scenes.PREBUILD_TEAM);
    }
}