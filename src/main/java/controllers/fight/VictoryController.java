package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.Bugemon;
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

    public void updateVictoryView(int TotalEXP) {
        List<BugemonDTO> team = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO();
        this.view.updateEXPGainedLabel(TotalEXP);
        int expForEachBug = distributeEXPEvenly(TotalEXP);
        this.view.showBugemonXpList(team, expForEachBug);
    }

    private int distributeEXPEvenly(int TotalEXP){
        List<Bugemon> team = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeam();
        int count = 0;
        for(Bugemon bug : team){
            if (bug.isHasParticipedToTrue()){
                count++;
            }
        }

        return count != 0 ? TotalEXP / count : 0 ;
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }
}
