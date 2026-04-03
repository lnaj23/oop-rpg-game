package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.dto.BugemonDTO;
import services.game.GameStateHolder;
import views.fight.VictoryView;

import java.io.IOException;
import java.util.List;

public class VictoryController extends Controller<VictoryView> {
    /**
     * @param metaController the central controller handling navigation
     */
    public VictoryController(ControllerManager metaController) throws IOException {
        super(metaController, new VictoryView());
        this.view.setController(this);
    }

    public void updateVictoryView() {
        List<BugemonDTO> team = GameStateHolder.getInstance()
                .getCurrentTeamPlayer()
                .getTeamDTO();
        this.view.showBugemonXpList(team);
        this.view.updateEXPGainedLabel(42);
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }
}
