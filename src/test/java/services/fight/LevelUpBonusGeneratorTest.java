package services.fight;

import junit.framework.TestCase;
import models.game.LevelUpBonus;
import org.junit.Test;

import java.util.List;


public class LevelUpBonusGeneratorTest extends TestCase {

    /**
     * Verifies that exactly 3 options are generated.
     */
    @Test
    public void testGeneratesThreeOptions() {
        LevelUpBonusGenerator generator = new LevelUpBonusGenerator();
        List<LevelUpBonus> options = generator.generateOptions();
        assertEquals(3, options.size());
    }

    /**
     * Verifies that each option has a consistent point total.
     * The total in "points" should be 10:
     * HP/2 + Attack + Defense + Initiative/2 = 10.
     * Runs 20 times to account for randomness.
     */
    @Test
    public void testEachOptionTotalsTenPoints() {
        LevelUpBonusGenerator generator = new LevelUpBonusGenerator();
        for (int i = 0; i < 20; i++) {
            List<LevelUpBonus> options = generator.generateOptions();
            for (LevelUpBonus bonus : options) {
                int totalPoints = bonus.getHpBonus() / 2
                        + bonus.getAttackBonus()
                        + bonus.getDefenseBonus()
                        + bonus.getInitiativeBonus() / 2;
                assertEquals("Total points must be 10", 10, totalPoints);
            }
        }
    }

    /**
     * Verifies that no bonus contains negative values.
     */
    @Test
    public void testNoNegativeValues() {
        LevelUpBonusGenerator generator = new LevelUpBonusGenerator();
        List<LevelUpBonus> options = generator.generateOptions();
        for (LevelUpBonus bonus : options) {
            assertTrue(bonus.getHpBonus() >= 0);
            assertTrue(bonus.getAttackBonus() >= 0);
            assertTrue(bonus.getDefenseBonus() >= 0);
            assertTrue(bonus.getInitiativeBonus() >= 0);
        }
    }
}