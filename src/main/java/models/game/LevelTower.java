package models.game;

import models.TowerNodeType;

import java.util.List;

public class LevelTower {
    // méthodes avancer niveau et reculer niveau
    //    connaître l’étage en cours,
    //    connaître l'étape en cours dans l'etage,
    //    savoir si etage fini,
    //    savoir si toute la tour est finie,
    // liste des etapes

    public static final int FIRST_FLOOR = 2;
    public static final int LAST_FLOOR = 10; // 2..9 + boss final = 10
    private static final List<TowerNodeType> FLOOR_PATTERN = List.of(
            TowerNodeType.COMBAT,
            TowerNodeType.REWARD,
            TowerNodeType.COMBAT,
            TowerNodeType.COMBAT,
            TowerNodeType.REWARD,
            TowerNodeType.BOSS
    );

    private int currentFloor;
    private int currentStep;
    private boolean finished;

    public LevelTower() {
        this.currentFloor = FIRST_FLOOR;
        this.currentStep = 0; // y'en a 6 par étage mais on commence à 0 et finit à 5 dans le code
        this.finished = false;
    }
    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getCurrentStep() {
        return currentStep;
    }
    public TowerNodeType getCurrentStepType() {
        if (finished) {
            throw new IllegalStateException("La tour est terminée.");
        }
        return FLOOR_PATTERN.get(currentStep);
    }
    public boolean isFinished() {
        return finished;
    }
    public boolean isBossStep() {
        return getCurrentStepType() == TowerNodeType.BOSS;
    }

    public boolean isRewardStep() {
        return getCurrentStepType() == TowerNodeType.REWARD;
    }

    public boolean isCombatStep() {
        return getCurrentStepType() == TowerNodeType.COMBAT;
    }

    public void advance() {
        if (finished) {
            return;
        }
        currentStep++;
        if (currentStep >= FLOOR_PATTERN.size()) { // étage d'après le pattern, on passe à l'étage suivant
            currentStep = 0;
            currentFloor++;
            if (currentFloor > LAST_FLOOR) { // on a dépassé le dernier étage, la tour est finie
                finished = true;
            }
        }
    }
    public void retreat() {
        if (finished) {
            return;
        }
        currentStep--;
        if (currentStep < 0) {
            currentStep = FLOOR_PATTERN.size() - 1;
            currentFloor--;
            if (currentFloor < FIRST_FLOOR) {
                currentFloor = FIRST_FLOOR;
                currentStep = 0;
            }
        }
    }

    public void resetAll() {
        this.currentFloor = FIRST_FLOOR;
        this.currentStep = 0;
        this.finished = false;
    }
    public void resetFloor() {
        this.currentStep = 0;
    }
}
