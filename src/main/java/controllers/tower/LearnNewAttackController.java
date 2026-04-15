package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.dto.BugemonDTO;
import views.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;

import java.io.IOException;

public class LearnNewAttackController extends Controller<SelectBugemonFromTeamView> implements SelectBugemonFromTeamListener {
    /**
     * @param metaController the central controller handling navigation
     */
    public LearnNewAttackController(ControllerManager metaController) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
    }

    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {

    }

    @Override
    public void onCancelClickedRequest() {

    }
}
