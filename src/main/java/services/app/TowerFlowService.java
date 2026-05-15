package services.app;

import exceptions.JsonResourceNotFoundException;
import exceptions.TowerNotInitializedException;
import models.game.LevelTower;
import models.game.TowerNodeType;

public class TowerFlowService {

    public TowerNextAction advanceToNextAction() throws TowerNotInitializedException {
        GameStateHolder.getInstance().advanceStep();

        if (GameStateHolder.getInstance().getCurrentLevelTower().isFinished()) {
            return TowerNextAction.TITLE_SCREEN;
        }

        TowerNodeType nextStep = GameStateHolder.getInstance().getCurrentStepType();

        return switch (nextStep) {
            case COMBAT, BOSS -> TowerNextAction.FIGHT;
            case REWARD -> TowerNextAction.REWARD;
        };
    }

    public void startNewTower() throws JsonResourceNotFoundException {
        GameStateHolder.getInstance().setCurrentLevelTower(new LevelTower());
        GameStateHolder.getInstance().resetItems();
    }
}
