package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.dto.BugemonDTO;
import views.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;
import services.GameStateHolder;

import java.io.IOException;

/**
 * Controller for the Bugemon switch sub-view used during a fight.
 * <p>
 * Reads the player's team, the current on-field Bugemon id, and the switch mode
 * directly from {@link GameStateHolder}. Delegates selection and cancellation
 * back to the {@link ControllerManager} which forwards them to {@link FightAgainstCPUController}.
 * </p>
 */
public class SwitchBugemonController extends Controller<SelectBugemonFromTeamView> implements SelectBugemonFromTeamListener {

    /** The identifier of the Bugemon currently on the field, used to prevent re-selecting it. */
    private final String currentOnFieldId;

    /**
     * Creates the controller for the Bugemon switch sub-view.
     * Reads all required state from {@link GameStateHolder}.
     *
     * @param metaController the central controller responsible for application navigation
     * @throws IOException if the switch sub-view cannot be loaded
     */
    public SwitchBugemonController(ControllerManager metaController) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
        this.currentOnFieldId = GameStateHolder.getInstance().getCurrentOnFieldId();
        this.view.setController(this);
        this.view.setListener(this);
        this.view.displayTeam(
                GameStateHolder.getInstance().getCurrentPlayerTeamDTO(),
                GameStateHolder.getInstance().isSwitchOnPurpose()
        );
    }

    /**
     * Handles the selection of a Bugemon in the switch sub-view.
     * <p>
     * Displays an error message if the selected Bugemon is KO or is already on the field.
     * Otherwise, closes the sub-window and notifies the meta-controller.
     * </p>
     *
     * @param bugemon the Bugemon selected by the user
     */
    public void onBugemonClicked(BugemonDTO bugemon) {
        if (bugemon.getCurrenPv() <= 0) {
            view.showMessageKO("Erreur", "Ce Bugemon est KO.");
            return;
        }
        if (bugemon.getId().equals(currentOnFieldId)) {
            view.showMessageKO("Erreur", "Ce Bugemon est deja sur le terrain !");
            return;
        }
        view.closeWindow();
        controllerManager.onSwitchBugemonChosen(bugemon.getId());
    }

    /**
     * Handles the click on the cancel button by closing the sub-window
     * and notifying the meta-controller.
     */
    public void onCancelClicked() {
        view.closeWindow();
        controllerManager.onSwitchCancelled();
    }

    /**
     * Implements {@link SelectBugemonFromTeamListener}.
     * Delegates to {@link #onBugemonClicked(BugemonDTO)}.
     *
     * @param bugemon the Bugemon selected by the user
     */
    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        onBugemonClicked(bugemon);
    }

    /**
     * Implements {@link SelectBugemonFromTeamListener}.
     * Delegates to {@link #onCancelClicked()}.
     */
    @Override
    public void onCancelClickedRequest() {
        onCancelClicked();
    }

    /**
     * Returns the view managed by this controller.
     *
     * @return the Bugemon switch sub-view
     */
    public SelectBugemonFromTeamView getView() {
        return view;
    }
}