package services.models.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;

import java.util.Random;

/**
 * Computes damage dealt during a fight, applying attack/defence factors,
 * type multipliers, and a 10 % critical-hit chance.
 */
public abstract class DamageCalculator {

    /** Shared RNG for critical-hit rolls. */
    private static final Random RANDOM = new Random();

    /**
     * Computes the damage inflicted by {@code attacker} on {@code target} using {@code attack}.
     *
     * @param attacker the attacking Bugémon
     * @param target   the defending Bugémon
     * @param attack   the attack used
     * @return the final damage as a truncated integer
     */
    public static int computeDamage(Bugemon attacker, Bugemon target, Attack attack) {

        double attackFactor = (100.0 + attacker.getAttaque()) / 100.0;

        double reductionFactor = 100.0 / (100.0 + target.getDefense());

        double damage = attack.getPower() * attackFactor * reductionFactor;

        double typeMultiplier = TypeEffectivenessService.getTypeMultiplier(
                attack.getType(),
                target.getType()
        );
        damage *= typeMultiplier;

        boolean isCritical = RANDOM.nextInt(100) < 10;
        if (isCritical) damage *= 1.5;

        return (int) damage;
    }
}
