package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import exceptions.BugemonNotInTeamException;
import javafx.application.Platform;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import services.GameStateHolder;
import views.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;

import java.io.IOException;

/**
 * Controller for the "learn new attack" reward — Bugémon-selection step.
 * <p>
 * The player selects which Bugémon will learn the attack. Once a target is
 * chosen, this controller delegates to the {@link ControllerManager} to open
 * the attack-replacement sub-window.
 * </p>
 */
public class LearnNewAttackController extends Controller<SelectBugemonFromTeamView>
        implements SelectBugemonFromTeamListener {

    /** The new attack to teach to whichever Bugémon the player selects. */
    private final Attack newAttack;

    public LearnNewAttackController(ControllerManager metaController, Attack newAttack)
            throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
        this.newAttack = newAttack;
        this.view.setListener(this);
        this.view.displayTeam(GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO(), true);
    }

    /**
     * Closes the team window and asks the meta-controller to open the
     * attack-selection sub-window for the chosen Bugémon.
     */
    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        try {
            Bugemon selected = GameStateHolder.getInstance()
                    .getCurrentTeamPlayer()
                    .getBugemonById(bugemon.getId());
            view.closeWindow();
            Platform.runLater(() -> controllerManager.openSelectAttackWindow(selected, newAttack));
        } catch (BugemonNotInTeamException e) {
            view.showMessageKO("Erreur", "Ce Bugémon n'est pas dans votre équipe !");
        }
    }

    @Override
    public void onCancelClickedRequest() {
        view.closeWindow();
    }

    /** @return the team selection view managed by this controller. */
    public SelectBugemonFromTeamView getView() {
        return view;
    }
}