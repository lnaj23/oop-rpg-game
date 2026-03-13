package controllers;

import models.Bugemon.Bugemon;
import models.Game.Fight;
import views.FightView;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import models.Bugemon.TeamBugemon;

public class FightController extends Controller<FightView> {

    private TeamBugemon playerTeam;
    private TeamBugemon cpuTeam;

    private Bugemon onFieldPlayerBugemon;
    private Bugemon onFieldEnemyBugemon;

    private Fight fight;

    private boolean isPlayerTurn = true;
    Random random = new Random();
    public FightController(MetaController metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setController(this);
    }

    public void setPlayerTeam(TeamBugemon team) {

        //truc un peu bzr juste prc là les 2 equipe sont les même mais dcp faut faire une copie de lequipe
        List<String> ids = team.getTeam().stream().map(Bugemon::getId).toList();
        TeamBugemon cpuTeam = new TeamBugemon(ids);

        fight = new Fight(team,cpuTeam);

        this.playerTeam = fight.getTeamPlayer();
        this.cpuTeam = fight.getTeamCPU();

        this.onFieldPlayerBugemon = playerTeam.getTeam().getFirst();
        this.onFieldEnemyBugemon = cpuTeam.getTeam().getLast();

        view.displayTeam(playerTeam.getTeamDTO());
        view.updateActiveBugemonSprite(onFieldPlayerBugemon);
        view.updateActiveBugemonSprite(onFieldEnemyBugemon);
        view.markPlayerBugemonAsFainted(playerTeam.getTeam().get(3));
        view.setCombatMessage("Un " + onFieldEnemyBugemon.getNom() + " sauvage apparaît !");
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
        Bugemon randomBug = playerTeam.getTeam().get(random.nextInt(playerTeam.getTeam().size()));
        view.updateActiveBugemonSprite(randomBug);
        view.updateBugemonHP(randomBug);
    }

    public void onForfeitClicked() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }
}