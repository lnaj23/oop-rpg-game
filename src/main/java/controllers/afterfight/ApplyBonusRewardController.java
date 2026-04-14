package controllers.afterfight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.dto.BugemonDTO;
import views.fight.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;

import java.io.IOException;

public class ApplyBonusRewardController extends Controller<SelectBugemonFromTeamView> implements SelectBugemonFromTeamListener {
    /**
     * @param metaController the central controller handling navigation
     * @param view           the view managed by this controller
     */
    public ApplyBonusRewardController(ControllerManager metaController) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
    }

    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {

    }

    @Override
    public void onCancelClickedRequest() {

    }
}
