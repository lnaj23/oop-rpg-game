package controllers.teamchoice;

import controllers.Controller;
import controllers.ControllerManager;

import exceptions.*;

import javafx.scene.control.Button;
import models.bugemon.TeamBugemon;
import models.bugemon.dto.BugemonDTO;
import services.controller.dataprovider.BugemonProvider;
import services.controller.prebuiltteam.TeamService;
import views.listener.SelectionListener;
import views.teamchoice.selection.SelectionView;

import java.io.IOException;

/**
 * Controller for the Bugémon selection screen — <b>edition flow only</b>.
 * Shares the same {@link SelectionView} instance with {@link SelectionController}
 * and registers itself as the active {@link SelectionListener} in {@link #onshow()}.
 *
 * <p>
 * Edition is launched from {@link ControllerManager#openSelectionForEdit(String)},
 * which first calls {@link #loadTeamForEditing(String)} to prepare the team to edit,
 * then switches to the edit scene — {@link #onshow()} pushes the prepared state
 * into the view.
 * </p>
 */
public class EditTeamController extends Controller<SelectionView> implements SelectionListener {

    /** Id of the team being edited. */
    private String editingTeamId = null;

    /** Working copy of the team being edited. */
    private TeamBugemon playerTeam = new TeamBugemon();

    private final BugemonProvider bugemonProvider = new BugemonProvider();
    private final TeamService teamProvider = new TeamService();

    /**
     * @param metaController the central controller
     * @param view           the shared selection view (injected by {@link ControllerManager})
     */
    public EditTeamController(ControllerManager metaController, SelectionView view) throws AttackNotFoundException, JsonResourceNotFoundException {
        super(metaController, view);
    }

    /**
     * Prepares the controller to edit the team with the given id.
     * Does <b>not</b> touch the view — the view is synchronized in {@link #onshow()}.
     *
     * @param teamId the id of the team to edit
     */
    public void loadTeamForEditing(String teamId) {
        try {
            TeamBugemon team = teamProvider.findTeamById(teamId);
            this.editingTeamId = teamId;
            this.playerTeam = new TeamBugemon(team);
        } catch (LoadingTeamFailedException e) {
            this.controllerManager.fatalError("Une erreur est survenue lors du chargement de l'équipe.");
        }
    }

    @Override
    protected void onshow() {
        this.view.setListener(this);
        try {
            this.view.reset(bugemonProvider.getAllBugemonsDTO());
            this.view.setEditMode(true);
            this.view.preSelectTeam(playerTeam);
        } catch (JsonResourceNotFoundException | LoadingAddedBugemonFailedException | IOException e) {
            this.controllerManager.fatalError("Une erreur est survenue lors du chargement de l'équipe.");
        }
    }

    // ----------------------------------------------------------------
    // SelectionListener
    // ----------------------------------------------------------------

    @Override
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
        try {
            this.view.refreshTeamGrid(playerTeam.getTeamDTO());
        } catch (IOException e) {
            this.controllerManager.fatalError("Une erreur est survenue lors du chargement des Bugémons.");
        }
    }

    @Override
    public void onConfirm(String teamName) {
        // In edit mode the name is ignored — we keep the team's existing name.
        try {
            teamProvider.updateTeamById(editingTeamId, playerTeam);
            this.editingTeamId = null;
            this.controllerManager.goToPrebuildTeam();
        } catch (IllegalArgumentException e) {
            view.showError("Nom déjà utilisé", e.getMessage());
        } catch (SavingTeamFailedException e) {
            controllerManager.fatalError("Une erreur est survenue lors de la sauvegarde de l'équipe.");
        }
    }

    /**
     * Reverts the edition by reloading the original team from storage.
     */
    @Override
    public void onReset() {
        if (editingTeamId != null) {
            loadTeamForEditing(editingTeamId);
            onshow();
        }
    }

    // The following events are tied to buttons that are hidden in edit mode
    // (see TeamPanelSubView.setEditMode). They should not be triggered in practice;
    // we forward the user back to the team list as a safe fallback.

    @Override
    public void onPlay()     { this.controllerManager.goToPrebuildTeam(); }

    @Override
    public void onLoadTeam() { this.controllerManager.goToPrebuildTeam(); }

    @Override
    public void onBack()     { this.controllerManager.goToPrebuildTeam(); }
}