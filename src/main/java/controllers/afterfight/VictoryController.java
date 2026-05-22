package controllers.afterfight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import models.game.LevelTower;
import models.game.TowerNodeType;
import services.models.game.ExperienceService;
import services.app.GameStateHolder;
import views.afterfight.VictoryView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the victory screen, shown after the player wins a fight.
 * <p>
 * Computes and distributes combat XP to the Bugémons that took part in the
 * fight, then routes either to the level-up screen (if any Bugémon levels up)
 * or directly to the next tower step.
 * </p>
 */
public class VictoryController extends Controller<VictoryView> {

    private int totalExpToDistribute;
    private List<Bugemon> bugemonsToLevelUp = new ArrayList<>();
    int enemyCount;
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
            if (bug.hasParticipated()) {
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
            if (bug.hasParticipated()) {
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

    public void prepareVictory(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    @Override
    protected void onshow() {
        LevelTower tower = GameStateHolder.getInstance().getCurrentLevelTower();
        int floorNumber = tower.getCurrentFloor();
        boolean isBossFight = tower.getCurrentStepType() == TowerNodeType.BOSS;
        int totalXp = ExperienceService.computeCombatXp(floorNumber, isBossFight, enemyCount);
        updateExp(totalXp);
    }
}
