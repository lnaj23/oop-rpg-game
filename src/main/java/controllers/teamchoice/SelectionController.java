package controllers.teamchoice;

import controllers.Controller;
import controllers.ControllerManager;

import exceptions.*;

import javafx.scene.control.Button;
import models.bugemon.dto.BugemonDTO;
import models.bugemon.TeamBugemon;
import services.app.GameStateHolder;
import services.controller.dataprovider.BugemonProvider;
import services.controller.dataprovider.TeamProvider;
import views.listener.SelectionListener;
import views.teamchoice.selection.SelectionView;

import java.io.IOException;

/**
 * Controller for the Bugémon selection screen — <b>creation flow only</b>.
 * Edition is now handled by {@link EditTeamController}, which shares
 * the same {@link SelectionView} instance.
 *
 * <p>
 * Implements {@link SelectionListener}: the view does not know this is
 * a "creation" controller, it only sees a listener.
 * </p>
 */
public class SelectionController extends Controller<SelectionView> implements SelectionListener {

    /** The team currently being built by the player. */
    private TeamBugemon playerTeam;

    private final BugemonProvider bugemonProvider = new BugemonProvider();
    private final TeamProvider teamProvider = new TeamProvider();

    /**
     * @param metaController the central controller
     * @param view           the shared selection view (injected by {@link ControllerManager})
     */
    public SelectionController(ControllerManager metaController, SelectionView view) throws AttackNotFoundException, JsonResourceNotFoundException {
        super(metaController, view);
        this.playerTeam = new TeamBugemon();
    }

    @Override
    protected void onshow() {
        this.view.setListener(this);
        reset();
    }

    /** Resets the selection screen to its initial empty state. */
    public void reset() {
        this.playerTeam = new TeamBugemon();
        try {
            this.view.reset(bugemonProvider.getAllBugemonsDTO());
        } catch (JsonResourceNotFoundException | LoadingAddedBugemonFailedException | IOException e) {
            this.controllerManager.fatalError("Une erreur est survenue lors du chargement des Bugémons.");
        }
        this.view.setEditMode(false);
    }


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
        // In creation mode, teamName is always non-null (the view asked for it).
        try {
            playerTeam.setName(teamName);
            teamProvider.addTeam(playerTeam);
        } catch (IllegalArgumentException e) {
            view.showError("Nom déjà utilisé", e.getMessage());
        } catch (SavingTeamFailedException e) {
            controllerManager.fatalError("Une erreur est survenue lors de la sauvegarde de l'équipe.");
        }
    }

    @Override
    public void onPlay() {
        GameStateHolder.getInstance().startUnsavedTeam(playerTeam);
        this.controllerManager.startNewTower();
    }

    @Override
    public void onLoadTeam() {
        this.controllerManager.goToPrebuildTeam();
    }

    @Override
    public void onBack() {
        this.controllerManager.goToTitleScreen();
    }

    @Override
    public void onReset() {
        reset();
    }
}