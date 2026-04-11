package models.game;

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
     * Returns the current floor number.
     *
     * @return the current floor (2–10)
     */
    public int getCurrentFloor() {
        return currentFloor;
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


    public int getCurrentStep() {
        return currentStep;
    }
}