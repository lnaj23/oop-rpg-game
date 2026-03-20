package services.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;
import services.fight.TypeEffectivenessService;

public abstract class DamageCalculator {

    public static int computeDamage(Bugemon attacker, Bugemon target, Attack attack) {

        double attackFactor = (100.0 + attacker.getAttaque()) / 100.0;

        double reductionFactor = 100.0 / (100.0 + target.getDefense());

        double damage = attack.getPower() * attackFactor * reductionFactor;

        double typeMultiplier = TypeEffectivenessService.getTypeMultiplier(
                attack.getType(),
                target.getType()
        );
        damage *= typeMultiplier;

        boolean isCritical = new java.util.Random().nextInt(100) < 10;
        if (isCritical) damage *= 1.5;

        return (int) damage;
    }
}
