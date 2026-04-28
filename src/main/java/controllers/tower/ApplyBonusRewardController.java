package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import exceptions.BugemonNotInTeamException;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import models.game.LevelUpBonus;
import services.game.GameStateHolder;
import views.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;

import java.io.IOException;

public class ApplyBonusRewardController extends Controller<SelectBugemonFromTeamView> implements SelectBugemonFromTeamListener {
    private LevelUpBonus bonus;

    public void setBonus(LevelUpBonus bonus) {
        this.bonus = bonus;
    }
    /**
     * @param metaController the central controller handling navigation
     */
    public ApplyBonusRewardController(ControllerManager metaController) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
    }

    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        try {
            Bugemon bug = GameStateHolder.getInstance().getCurrentTeamPlayer().getBugemonById(bugemon.getId());
            bonus.applyTo(bug);
            this.controllerManager.goToNextTowerStep();
        } catch (BugemonNotInTeamException e) {
            view.showMessageKO("Erreur", "Ce Bugémon n'est pas dans votre équipe !");
        }
    }

    @Override
    public void onCancelClickedRequest() {

    }
}
