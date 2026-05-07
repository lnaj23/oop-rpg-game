package controllers.tower;


import controllers.Controller;
import controllers.ControllerManager;
import exceptions.JsonResourceNotFoundException;
import models.bugemon.Attack;
import models.game.Item;
import models.game.LevelUpBonus;
import services.models.dataprovider.AttackProvider;
import services.models.dataprovider.ItemProvider;
import services.GameStateHolder;
import services.models.game.LevelUpBonusGenerator;
import views.tower.RewardView;

import java.io.IOException;

public class RewardController extends Controller<RewardView> {

    Item itemToGain;
    Attack attackToLearn;
    LevelUpBonus bonus;

    ItemProvider itemProvider = new ItemProvider();
    AttackProvider attackProvider = new AttackProvider();
    LevelUpBonusGenerator bonusGenerator = new LevelUpBonusGenerator();

    public RewardController(ControllerManager controllerManager) throws IOException {
        super(controllerManager, new RewardView());
        this.view.setController(this);
    }

    public void setReward() throws JsonResourceNotFoundException {
        itemToGain = itemProvider.getRandomItem();
        attackToLearn = attackProvider.getRandomAttack();
        bonus = bonusGenerator.generateSingleBonus();
        this.view.setRewardDisplay(itemToGain, attackToLearn.getName(),bonus.toString());
    }

    public void onGainItemBtnClicked() {
        GameStateHolder.getInstance().gainItem(itemToGain);
        this.controllerManager.goToNextTowerStep();
    }

    public void onlearnAttackBtnClicked() {
        this.controllerManager.openLearnAttackWindow(attackToLearn);
    }

    public void onGainBonusBtnClicked() {
        this.controllerManager.openBonusRewardWindow(bonus);
    }

}
