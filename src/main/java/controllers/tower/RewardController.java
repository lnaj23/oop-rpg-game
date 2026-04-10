package controllers.tower;


import controllers.Controller;
import controllers.ControllerManager;
import services.dataprovider.ItemProvider;
import services.game.GameStateHolder;
import views.tower.RewardView;

import java.io.IOException;

public class RewardController extends Controller<RewardView> {

    public RewardController(ControllerManager controllerManager) throws IOException {
        super(controllerManager, new RewardView());
        this.view.setController(this);
    }

    public void setReward() {
        this.view.setRewardText("Baie Tonique x2");
    }

    public void onContinueClicked() {
        this.controllerManager.goToNextTowerStep();
    }

}
