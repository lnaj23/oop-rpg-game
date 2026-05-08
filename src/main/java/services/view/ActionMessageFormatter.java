package services.view;

import models.fight.*;

/**
 * Formats an {@link AttackActionResult} into the user-facing combat message.
 * View-side only — pulls all the i18n / phrasing concerns out of the model.
 */
public abstract class ActionMessageFormatter {

    /** Builds the full combat message displayed after an attack resolves. */
    public static String format(AttackActionResult r) {
        StringBuilder sb = new StringBuilder();
        sb.append(r.attackerName()).append(" utilise ").append(r.attackName()).append("! ");
        sb.append(formatEffectiveness(r.effectiveness())).append("\n");
        sb.append(r.targetName()).append(" perd ").append(r.damage()).append(" PV ! ");
        if (r.targetIsKO()) sb.append(r.targetName()).append(" est mis KO");

        for (EffectEvent e : r.effects()) {
            sb.append("\n").append(formatEffect(e));
        }

        return sb.toString();
    }

    private static String formatEffect(EffectEvent e) {
        return switch (e) {
            case HealSelfEvent(var name, var value) ->
                    name + " récupère " + value + " PV !";
            case HealTeamEvent(var leader, var value) ->
                    "L'équipe de " + leader + " récupère " + value + " PV !";
            case StatModifierEvent(var name, var stat, var delta) ->
                    name + " : " + stat + (delta > 0 ? " +" : " ") + delta + " !";
        };
    }

    private static String formatEffectiveness(Effectiveness e) {
        return switch (e) {
            case SUPER_EFFECTIVE     -> "C'est super efficace !";
            case NOT_VERY_EFFECTIVE  -> "Ce n'est pas très efficace...";
            case NEUTRAL             -> "";
        };
    }

    /** Compact form used on attack buttons (short labels). */
    public static String formatEffectivenessShort(Effectiveness e) {
        return switch (e) {
            case SUPER_EFFECTIVE     -> "Super efficace !";
            case NOT_VERY_EFFECTIVE  -> "Peu efficace...";
            case NEUTRAL             -> "Rien de spécial";
        };
    }
}