package models.game;

import models.TowerNodeType;

import java.util.List;

/**
 * Manages the state and progression of a procedural combat tower.
 * <p>
 * Tracks the player's current floor (2–10) and step within that floor (0–5).
 * Each floor follows a fixed pattern of 6 steps: Combat, Reward, Combat, Combat, Reward, Boss.
 * The tower is complete once the player advances past floor 10.
 * </p>
 */
public class LevelTower {

    /** The starting floor of the tower. */
    public static final int FIRST_FLOOR = 2;

    /** The final floor of the tower (floors 2–10 inclusive). */
    public static final int LAST_FLOOR = 10;

    /** The repeating pattern of node types for each floor (6 steps per floor). */
    private static final List<TowerNodeType> FLOOR_PATTERN = List.of(
            TowerNodeType.COMBAT,
            TowerNodeType.REWARD,
            TowerNodeType.COMBAT,
            TowerNodeType.COMBAT,
            TowerNodeType.REWARD,
            TowerNodeType.BOSS
    );

    /** The player's current floor (2–10). */
    private int currentFloor;

    /** The player's current step within the floor (0–5). */
    private int currentStep;

    /** {@code true} if the player has progressed past floor 10. */
    private boolean finished;

    /**
     * Constructs a new tower at the initial state.
     * Player starts at floor 2, step 0.
     */
    public LevelTower() {
        this.currentFloor = FIRST_FLOOR;
        this.currentStep = 0;
        this.finished = false;
    }

    /**
     * Returns the player's current floor.
     *
     * @return the current floor (2–10)
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Returns the player's current step within the floor.
     *
     * @return the current step (0–5)
     */
    public int getCurrentStep() {
        return currentStep;
    }

    /**
     * Returns the type of node at the current step.
     *
     * @return the {@link TowerNodeType} (Combat, Reward, or Boss)
     * @throws IllegalStateException if the tower is finished
     */
    public TowerNodeType getCurrentStepType() {
        if (finished) {
            throw new IllegalStateException("La tour est terminée.");
        }
        return FLOOR_PATTERN.get(currentStep);
    }

    /**
     * Checks if the tower is complete.
     *
     * @return {@code true} if the player has passed floor 10
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Checks if the current step is a boss encounter.
     *
     * @return {@code true} if the current step type is Boss
     */
    public boolean isBossStep() {
        return getCurrentStepType() == TowerNodeType.BOSS;
    }

    /**
     * Checks if the current step is a reward node.
     *
     * @return {@code true} if the current step type is Reward
     */
    public boolean isRewardStep() {
        return getCurrentStepType() == TowerNodeType.REWARD;
    }

    /**
     * Checks if the current step is a combat encounter.
     *
     * @return {@code true} if the current step type is Combat
     */
    public boolean isCombatStep() {
        return getCurrentStepType() == TowerNodeType.COMBAT;
    }

    /**
     * Advances the player to the next step.
     * <p>
     * When all 6 steps of a floor are complete, moves to the next floor's first step.
     * If the player advances past floor 10, marks the tower as finished.
     * </p>
     */
    public void advance() {
        if (finished) {
            return;
        }
        currentStep++;
        if (currentStep >= FLOOR_PATTERN.size()) {
            currentStep = 0;
            currentFloor++;
            if (currentFloor > LAST_FLOOR) {
                finished = true;
            }
        }
    }

    /**
     * Retreats the player to the previous step.
     * <p>
     * When stepping back past step 0, moves to the previous floor's last step.
     * Will not go below floor 2, step 0.
     * </p>
     */
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

    /**
     * Resets the entire tower to its initial state.
     * Player returns to floor 2, step 0, and the finished flag is cleared.
     */
    public void resetAll() {
        this.currentFloor = FIRST_FLOOR;
        this.currentStep = 0;
        this.finished = false;
    }

    /**
     * Resets the current floor to step 0 without changing the floor number.
     */
    public void resetFloor() {
        this.currentStep = 0;
    }

    public String getProgressAsString() {
        if (finished) {
            return "Tour terminée !";
        }
        TowerNodeType typeStep = getCurrentStepType();
        if(typeStep ==TowerNodeType.BOSS){
            return "NO" + currentFloor + " - Boss";
        }
        return "NO" + currentFloor + " - Étape" + (currentStep+1) + "/6";
    }
}