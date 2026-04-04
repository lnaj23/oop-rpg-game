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

    private int count;
    private int expForEachBug;
    /**
     * @param metaController the central controller handling navigation
     */
    public VictoryController(ControllerManager metaController) throws IOException {
        super(metaController, new VictoryView());
        this.view.setController(this);
    }

    public void beginEXPDistribution(int TotalEXP) {
        List<BugemonDTO> team = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO();
        expForEachBug = distributeEXPEvenly(TotalEXP);
        this.view.updateEXPGainedLabel(count * expForEachBug);
        this.view.showBugemonXpList(team, expForEachBug);
    }

    private int distributeEXPEvenly(int TotalEXP){
        List<Bugemon> team = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeam();
        count = 0;
        for(Bugemon bug : team){
            if (bug.isHasParticipedToTrue()){
                count++;
            }
        }
        return TotalEXP / count  ;
    }

    private void giveBugemonEXPIfTheyParticipated() {
        List<Bugemon> team = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeam();
        for(Bugemon bug : team){
            if (bug.isHasParticipedToTrue()){
                //TODO Hist 7 : lui donner l'exp et s'il levelup passer sur levelupView pour ce bugemon
                bug.setHasParticipedToFalse();
            }
        }
    }

    public void continueTower() {
        giveBugemonEXPIfTheyParticipated();
        this.controllerManager.goToNextTowerStep();
        // TODO Hist 7, Faudra faire en sorte que levelUpController,si un levelUp apparait, s'occupe de continuer ds la tour aprés
        //  avoir choisi un amelioration
        //  Pour l'instant, il faut remplacer controllerManager.goToFightAgainstCPU par controllerManager.continueTower.
    }
}
