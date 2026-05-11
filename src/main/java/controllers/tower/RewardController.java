package controllers.tower;


import controllers.Controller;
import controllers.ControllerManager;
import exceptions.JsonResourceNotFoundException;
import models.bugemon.Attack;
import models.game.Item;
import models.game.LevelUpBonus;
import services.controller.dataprovider.AttackProvider;
import services.controller.dataprovider.ItemProvider;
import services.app.GameStateHolder;
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
        this.view.setRewardDisplay(itemToGain, attackToLearn, bonus);
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

    @Override
    protected void onshow() {
        try {
            setReward();
        } catch (JsonResourceNotFoundException e) {
            controllerManager.fatalError("Une erreur est survenue lors du chargement des récompenses");
        }
    }
}
