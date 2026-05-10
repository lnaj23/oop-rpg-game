package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.dto.BugemonDTO;
import views.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;
import services.app.GameStateHolder;

import java.io.IOException;

/**
 * Controller for the Bugemon switch sub-view used during a fight.
 * <p>
 * Reads the player's team, the current on-field Bugemon id, and the switch mode
 * directly from {@link GameStateHolder}. Delegates selection and cancellation
 * back to the {@link ControllerManager} which forwards them to {@link FightAgainstCPUController}.
 * </p>
 */
public class SwitchBugemonController extends Controller<SelectBugemonFromTeamView>
        implements SelectBugemonFromTeamListener {

    /** The identifier of the Bugemon currently on the field, used to prevent re-selecting it. */
    private final String currentOnFieldId;

    private final boolean onPurpose;

    public SwitchBugemonController(ControllerManager metaController, boolean onPurpose) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
        this.onPurpose = onPurpose;
        this.currentOnFieldId = GameStateHolder.getInstance()
                .getCurrentTeamPlayer()
                .getOnFieldBugemon()
                .getId();
        this.view.setController(this);
        this.view.setListener(this);
        this.view.displayTeam(
                GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO(),
                onPurpose
        );
    }

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
        controllerManager.onSwitchBugemonChosen(bugemon.getId(), onPurpose);
    }

    public void onCancelClicked() {
        view.closeWindow();
        controllerManager.onSwitchCancelled();
    }

    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        onBugemonClicked(bugemon);
    }

    @Override
    public void onCancelClickedRequest() {
        onCancelClicked();
    }

    public SelectBugemonFromTeamView getView() {
        return view;
    }
}