package services.fight;

import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;

import java.util.List;
import java.util.ArrayList;

/**
 * Service responsible for computing and distributing XP
 * after a victorious combat.
 *
 * The formula is defined in section 3.2.4 of the specification:
 * XP_combat = 30 * floor_number * type_multiplier * enemy_count
 *
 * XP is split equally among all Bugemons that participated
 * in the combat (sent to the field at least once).
 */
public class ExperienceService {

    /** Base constant for XP computation (Section 3.2.4.2). */
    private static final int BASE_XP = 30;

    /** Multiplier for a boss fight. */
    private static final int BOSS_MULTIPLIER = 2;

    /** Multiplier for a normal fight. */
    private static final int NORMAL_MULTIPLIER = 1;

    /**
     * Computes the total XP earned for a victorious combat.
     *
     * @param floorNumber   the floor number (e.g. 2 for NO2, 5 for NO5)
     * @param isBossFight   true if this is a boss fight, false otherwise
     * @param enemyCount    the number of enemy Bugemons in the combat
     * @return the total combat XP
     */
    public static int computeCombatXp(int floorNumber, boolean isBossFight, int enemyCount) {
        int multiplier = isBossFight ? BOSS_MULTIPLIER : NORMAL_MULTIPLIER;
        return BASE_XP * floorNumber * multiplier * enemyCount;
    }

    /**
     * Distributes combat XP equally among participating Bugemons.
     * Only Bugemons that were sent to the field at least once
     * receive XP (as specified in section 3.2.4.3).
     *
     * @param totalXp       the total combat XP (computed by computeCombatXp)
     * @param participants  the list of Bugemons that participated in the combat
     * @return the list of Bugemons that can level up after this distribution
     */
    public static List<Bugemon> distributeXp(int totalXp, List<Bugemon> participants) {
        if (participants.isEmpty()) return new ArrayList<>();

        // Integer division, XP is split equally
        int xpPerBugemon = totalXp / participants.size();

        List<Bugemon> leveledUpQueue = new ArrayList<>();

        for (Bugemon bugemon : participants) {
            bugemon.addXp(xpPerBugemon);

            while (bugemon.canLevelUp()) {
                bugemon.levelUp();
                leveledUpQueue.add(bugemon);
            }
        }
        return leveledUpQueue;
    }
}