package services.view;

import models.game.LevelUpBonus;

/**
 * Formats a {@link LevelUpBonus} into the user-facing description string.
 * Example output: {@code "+20 HP"} or {@code "+5 Attaque, +5 Défense"}.
 */
public abstract class LevelUpBonusFormatter {

    public static String format(LevelUpBonus bonus) {
        StringBuilder sb = new StringBuilder();
        appendIfPositive(sb, bonus.getHpBonus(),         "HP");
        appendIfPositive(sb, bonus.getAttackBonus(),     "Attaque");
        appendIfPositive(sb, bonus.getDefenseBonus(),    "Défense");
        appendIfPositive(sb, bonus.getInitiativeBonus(), "Initiative");
        return sb.toString();
    }

    private static void appendIfPositive(StringBuilder sb, int value, String label) {
        if (value <= 0) return;
        if (sb.length() > 0) sb.append(", ");
        sb.append("+").append(value).append(" ").append(label);
    }
}