package controllers;

import models.Bugemon.Bugemon;
import models.Game.Fight;
import views.FightView;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import models.Bugemon.TeamBugemon;

public class FightController extends Controller<FightView> {

    private Fight fight;

    private boolean isPlayerTurn = true;

    Random random = new Random();
    public FightController(MetaController metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setController(this);
    }

    public void setPlayerTeam(TeamBugemon team) {

        fight = new Fight(team,new TeamBugemon(team));


        view.displayTeam(fight.getTeamPlayer().getTeamDTO());
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
        view.markPlayerBugemonAsFainted(fight.getOnFieldPlayerBugemon());
        view.setCombatMessage("Un " + fight.getOnFieldEnemyBugemon().getNom() + " sauvage apparaît !");
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
        List<Bugemon> available = fight.getTeamPlayer().getAvailableBugemons();
        Bugemon randomBug = available.get(random.nextInt(available.size()));
        fight.getTeamPlayer().changeOnFieldBugemon(randomBug);
        view.updateActiveBugemonSprite(randomBug);
        view.updateBugemonHP(randomBug);
    }

    public void onForfeitClicked() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }
}