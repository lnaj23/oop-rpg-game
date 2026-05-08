package services.models.fight;

import models.fight.Effectiveness;

/**
 * Pure type-chart calculations. Returns multipliers and effectiveness categories.
 * UI string formatting is the view layer's responsibility.
 * Type chart: Flora > Aqua > Pyro > Litho > Flora ; Flora = Pyro ; Aqua = Litho.
 */
public abstract class TypeEffectivenessService {

    /** Returns the damage multiplier (1.5, 1.0 or 0.75). */
    public static double getTypeMultiplier(String attackType, String targetType) {
        if (attackType == null || targetType == null) return 1.0;

        String combo = attackType.toLowerCase() + "_" + targetType.toLowerCase();
        return switch (combo) {
            case "flora_aqua", "aqua_pyro", "pyro_litho", "litho_flora" -> 1.5;
            case "aqua_flora", "pyro_aqua", "litho_pyro", "flora_litho" -> 0.75;
            default -> 1.0;
        };
    }

    /** Returns the effectiveness category (enum, no UI string). */
    public static Effectiveness getEffectiveness(String attackType, String targetType) {
        double score = getTypeMultiplier(attackType, targetType);
        if (score == 1.5)  return Effectiveness.SUPER_EFFECTIVE;
        if (score == 0.75) return Effectiveness.NOT_VERY_EFFECTIVE;
        return Effectiveness.NEUTRAL;
    }
}