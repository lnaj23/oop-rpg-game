package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.dto.BugemonDTO;
import services.game.GameStateHolder;
import views.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;

import java.io.IOException;

public class ApplyBonusRewardController extends Controller<SelectBugemonFromTeamView> implements SelectBugemonFromTeamListener {
    /**
     * @param metaController the central controller handling navigation
     */
    public ApplyBonusRewardController(ControllerManager metaController) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
    }

    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        int id = GameStateHolder.getInstance().getCurrentTeamPlayer().getBugemonById(bugemon.getId());


    }

    @Override
    public void onCancelClickedRequest() {

    }
}
