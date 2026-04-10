package controllers.afterfight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import services.fight.ExperienceService;
import services.game.GameStateHolder;
import views.fight.VictoryView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VictoryController extends Controller<VictoryView> {

    private int totalExpToDistribute;
    private List<Bugemon> bugemonsToLevelUp = new ArrayList<>();
    /**
     * @param metaController the central controller handling navigation
     */
    public VictoryController(ControllerManager metaController) throws IOException {
        super(metaController, new VictoryView());
        this.view.setController(this);
    }

    public void updateExp(int totalXp) {
        this.totalExpToDistribute = totalXp;
        List<Bugemon> team = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeam();
        List<BugemonDTO> teamDTO = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO();

        int participants = 0;
        for (Bugemon bug : team) {
            if (bug.isHasParticipedToTrue()) {
                participants++;
            }
        }

        int xpPerBugemon = totalXp / participants;
        this.view.updateEXPGainedLabel(totalExpToDistribute);
        this.view.showBugemonXpList(teamDTO, xpPerBugemon);
        applyExpAndIdentifyLevelUps();
    }

    private void applyExpAndIdentifyLevelUps() {
        List<Bugemon> team = GameStateHolder.getInstance().getCurrentTeamPlayer().getTeam();
        List<Bugemon> participants = new ArrayList<>();

        for (Bugemon bug : team) {
            if (bug.isHasParticipedToTrue()) {
                participants.add(bug);
                bug.setHasParticipedToFalse();
            }
        }

        this.bugemonsToLevelUp = ExperienceService.distributeXp(totalExpToDistribute, participants);
    }

    public void continueTower() {
        if (!bugemonsToLevelUp.isEmpty()) {
            // Plusieurs levelUp, on switch de scene
            this.controllerManager.goToLevelUpScreen(this.bugemonsToLevelUp);
        } else {
            // Pas de level up, on continue l'ascension
            this.controllerManager.goToNextTowerStep();
        }
    }
}
