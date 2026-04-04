package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import models.TowerNodeType;
import models.game.LevelTower;
import services.game.GameStateHolder;


public class TowerController {
    private final ControllerManager controllerManager;

    public TowerController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    public void advanceTower(){
        GameStateHolder.getInstance().advanceStep();
        if(GameStateHolder.getInstance().getCurrentLevelTower().isFinished()) {
            controllerManager.goToVictoryScreen();
            return;
        }
        TowerNodeType nextStep = GameStateHolder.getInstance().getCurrentStepType();
        switch (nextStep) {
            case COMBAT, BOSS -> controllerManager.goToFightAgainstCPU();
            case REWARD -> controllerManager.goToRewardScreen();
        }

    }

    public void setTower(){
        GameStateHolder.getInstance().setCurrentLevelTower(new LevelTower());
        this.controllerManager.goToFightAgainstCPU();
    }


}
