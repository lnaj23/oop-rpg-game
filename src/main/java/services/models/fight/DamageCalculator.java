package services.models.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;

import java.util.Random;

public abstract class DamageCalculator {

    private static Random random = new Random();

    public static void setRandom(Random r) {
        random = r;
    }

    public static int computeDamage(Bugemon attacker, Bugemon target, Attack attack) {

        double attackFactor = (100.0 + attacker.getAttaque()) / 100.0;

        double reductionFactor = 100.0 / (100.0 + target.getDefense());

        double damage = attack.getPower() * attackFactor * reductionFactor;

        double typeMultiplier = TypeEffectivenessService.getTypeMultiplier(
                attack.getType(),
                target.getType()
        );
        damage *= typeMultiplier;

        boolean isCritical = random.nextInt(100) < 10;
        if (isCritical) damage *= 1.5;

        return (int) damage;
    }
}