package services.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.Effect;
import models.bugemon.TeamBugemon;
import models.bugemon.ActiveEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class EffectManager {
    /**
     * Applies the effects of an attack on the attacker and/or the target,
     * then ticks all ongoing temporary effects on both combatants.
     *
     * @param attacker     the Bugémon using the attack
     * @param target       the Bugémon receiving the attack
     * @param attackerTeam the team of the attacker
     * @param targetTeam   the team of the target
     * @param attack       the attack being used
     */
    public static String handleEffect(Bugemon attacker, Bugemon target, TeamBugemon attackerTeam, TeamBugemon targetTeam, Attack attack) {
        if (attack.getEffects() == null) return "";

        List<String> lines = new ArrayList<>();

        for (Effect effet : attack.getEffects()) {
            if (effet.getType().equals("soin")) {
                int valeur = effet.getValeur();
                if (effet.getTarget().equals("equipe")) {
                    for (Bugemon membre : attackerTeam.getTeam()) {
                        membre.getStats().heal(valeur);
                    }
                    lines.add("L'équipe de " + attacker.getName() + " récupère " + valeur + " PV !");
                } else if (effet.getTarget().equals("lanceur")) {
                    attacker.getStats().heal(valeur);
                    lines.add(attacker.getName() + " récupère " + valeur + " PV !");
                }

            } else if (effet.getType().equals("stat_modifier")) {
                Bugemon finalTarget = switch (effet.getTarget()) {
                    case "lanceur"    -> attacker;
                    case "adversaire" -> target;
                    default           -> null;
                };
                if (finalTarget != null) {
                    applyStatModifier(finalTarget, effet);
                    finalTarget.addOngoingEffect(effet);
                    int delta = Integer.parseInt(effet.getModifier());
                    lines.add(finalTarget.getName() + " : " + effet.getStat()
                            + (delta > 0 ? " +" : " ") + delta + " !");
                }
            }
        }

        tickEffects(attacker);
        tickEffects(target);

        return String.join("\n", lines);
    }
    /**
     * Applies a stat modifier effect to the given Bugémon.
     *
     * @param target the Bugémon to apply the modifier to
     * @param effect the effect containing the stat and modifier value
     */
    public static void applyStatModifier(Bugemon target, Effect effect) {
        int delta = Integer.parseInt(effect.getModifier());
        switch (effect.getStat()) {
            case "attaque"    -> target.getStats().modifyAttack(delta);
            case "defense"    -> target.getStats().modifyDefense(delta);
            case "initiative" -> target.getStats().modifyInitiative(delta);
        }
    }

    /**
     * Decrements all temporary effects on a Bugémon.
     * Reverses and removes any effect that has expired.
     *
     * @param bugemon the Bugémon whose effects should be ticked
     */
    private static void tickEffects(Bugemon bugemon) {
        List<ActiveEffect> toRemove = new ArrayList<>();
        for (ActiveEffect ae : bugemon.getOngoingEffects()) {
            if (ae.getTurnsLeft() == null) continue;

            ae.decrementTurns();

            if (ae.isExpired()) {
                reverseStatModifier(bugemon, ae.getEffect());
                toRemove.add(ae);
            }
        }
        bugemon.getOngoingEffects().removeAll(toRemove);
    }

    /**
     * Reverses a stat modifier by applying the opposite delta.
     *
     * @param target the Bugémon to reverse the modifier on
     * @param effect the effect to reverse
     */
    private static void reverseStatModifier(Bugemon target, Effect effect) {
        int delta = Integer.parseInt(effect.getModifier());
        switch (effect.getStat()) {
            case "attaque"    -> target.getStats().modifyAttack(-delta);
            case "defense"    -> target.getStats().modifyDefense(-delta);
            case "initiative" -> target.getStats().modifyInitiative(-delta);
        }
    }
}
