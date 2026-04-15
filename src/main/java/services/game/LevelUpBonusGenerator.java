package services.game;

import models.game.LevelUpBonus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates the 3 bonus options proposed to the player
 * when a Bugemon levels up.
 * <p>
 * According to the specification (Story 7), possible options are:
 * - +20 HP maximum (pure bonus)
 * - +10 Attack (pure bonus)
 * - +10 Defense (pure bonus)
 * - +20 Initiative (pure bonus)
 * - Random combination totaling 10 points
 *   (1 point = 2 HP = 1 Attack = 1 Defense = 2 Initiative)
 * <p>
 * The generator selects 3 options among these possibilities,
 * including at least one random combination for variety.
 */
public class LevelUpBonusGenerator {

    /** Total number of points to distribute for each bonus option. */
    private static final int TOTAL_POINTS = 10;

    /** Number of options presented to the player. */
    private static final int NUM_OPTIONS = 3;

    /** The 4 possible pure bonuses. */
    private static final List<LevelUpBonus> PURE_BONUSES = List.of(
            new LevelUpBonus(20, 0, 0, 0),   // +20 HP
            new LevelUpBonus(0, 10, 0, 0),    // +10 Attack
            new LevelUpBonus(0, 0, 10, 0),    // +10 Defense
            new LevelUpBonus(0, 0, 0, 20)     // +20 Initiative
    );

    /** Random number generator for selecting bonuses. */
    private final Random random;

    /**
     * Default constructor using a standard Random.
     */
    public LevelUpBonusGenerator() {
        this.random = new Random();
    }

    /**
     * Constructor with an injected Random
     *
     * @param random
     */
    public LevelUpBonusGenerator(Random random) {
        this.random = random;
    }

    /**
     * Generates the 3 level-up bonus options.
     * Selects 2 random pure bonuses (no duplicates)
     * and 1 random combination for diversity.
     *
     * @return a list of 3 LevelUpBonus options
     */
    public List<LevelUpBonus> generateOptions() {
        List<LevelUpBonus> options = new ArrayList<>();

        // Select 2 different pure bonuses
        List<LevelUpBonus> availablePure = new ArrayList<>(PURE_BONUSES);
        for (int i = 0; i < 2 && !availablePure.isEmpty(); i++) {
            int index = random.nextInt(availablePure.size());
            options.add(availablePure.remove(index));
        }

        // Generate 1 random combination
        options.add(generateRandomCombination());

        return options;
    }

    /**
     * Generates a random combination of bonuses totaling TOTAL_POINTS.
     * Distributes points one by one into random stats.
     * Conversion:
     * - 1 point = 2 HP
     * - 1 point = 1 Attack
     * - 1 point = 1 Defense
     * - 1 point = 2 Initiative
     *
     * @return a LevelUpBonus with a random stat distribution
     */
    private LevelUpBonus generateRandomCombination() {
        int hpPoints = 0;
        int atkPoints = 0;
        int defPoints = 0;
        int initPoints = 0;

        // Distribute 10 points one by one into a random stat
        for (int i = 0; i < TOTAL_POINTS; i++) {
            int choice = random.nextInt(4); // 0=HP, 1=Atk, 2=Def, 3=Init
            switch (choice) {
                case 0 -> hpPoints++;
                case 1 -> atkPoints++;
                case 2 -> defPoints++;
                case 3 -> initPoints++;
            }
        }

        // Conversion: 1 point = 2 HP or 2 Init, 1 point = 1 Atk or 1 Def
        return new LevelUpBonus(
                hpPoints * 2,     // each point is worth 2 HP
                atkPoints,        // each point is worth 1 Attack
                defPoints,        // each point is worth 1 Defense
                initPoints * 2    // each point is worth 2 Initiative
        );
    }

    public  LevelUpBonus generateSingleBonus() {
        return generateRandomCombination();
    }
}