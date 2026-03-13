package controllers;

import models.Bugemon.Bugemon;
import views.FightView;
import java.io.IOException;

import models.Bugemon.TeamBugemon;

public class FightController extends Controller<FightView> {

    private TeamBugemon playerTeam;
    private TeamBugemon cpuTeam;

    private Bugemon onFieldPlayerBugemon;
    private Bugemon onFieldEnemyBugemon;

    private boolean isPlayerTurn = true;

    public FightController(MetaController metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setController(this);
    }

    public void setPlayerTeam(TeamBugemon team) {
        this.playerTeam = team;
        this.cpuTeam = team;

        this.onFieldPlayerBugemon = playerTeam.getTeam().getFirst();
        this.onFieldEnemyBugemon = cpuTeam.getTeam().getLast();

        view.displayTeam(playerTeam.getTeamDTO());
        view.updateActiveBugemonSprite(onFieldPlayerBugemon, true);
        view.updateActiveBugemonSprite(onFieldEnemyBugemon, false);
        view.markPlayerBugemonAsFainted(playerTeam.getTeam().get(3));
        view.setCombatMessage("Un " + onFieldEnemyBugemon.getNom() + " sauvage apparaît !");
        view.updateBugemonHP(onFieldPlayerBugemon,true, (float) (onFieldPlayerBugemon.getPv() - 45));
        view.updateBugemonHP(onFieldEnemyBugemon,false, (float) (onFieldEnemyBugemon.getPv() - 23.23));
    }

    public void goToTitleScreen() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }

    public void onAttackSelected(int attackIndex) {
        // choisir attack
    }

    public void onItemClicked() {
        // choisir objet
    }

    public void onSwitchClicked() {
        // changer de Bugémon
    }

    public void onForfeitClicked() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }
}