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


}
