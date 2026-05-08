package services.models.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.Effect;
import models.bugemon.TeamBugemon;
import models.bugemon.ActiveEffect;
import models.fight.EffectEvent;
import models.fight.HealSelfEvent;
import models.fight.HealTeamEvent;
import models.fight.StatModifierEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class EffectManager {

    /**
     * Applies the effects of an attack and ticks ongoing temporary effects.
     * Returns a list of structured events describing what happened — no UI strings.
     */
    public static List<EffectEvent> handleEffect(Bugemon attacker, Bugemon target,
                                                 TeamBugemon attackerTeam, TeamBugemon targetTeam,
                                                 Attack attack) {
        List<EffectEvent> events = new ArrayList<>();
        if (attack.getEffects() == null) return events;

        for (Effect effet : attack.getEffects()) {
            if (effet.getType().equals("soin")) {
                int valeur = effet.getValue();
                if (effet.getTarget().equals("equipe")) {
                    for (Bugemon membre : attackerTeam.getTeam()) {
                        if (!membre.isKO()) {
                            membre.getStats().heal(valeur);
                        }
                    }
                    events.add(new HealTeamEvent(attacker.getName(), valeur));
                } else if (effet.getTarget().equals("lanceur")) {
                    if (!attacker.isKO()) {
                        attacker.getStats().heal(valeur);
                        events.add(new HealSelfEvent(attacker.getName(), valeur));
                    }
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
                    events.add(new StatModifierEvent(finalTarget.getName(), effet.getStat(), effet.getModifier()));
                }
            }
        }

        tickEffects(attacker);
        tickEffects(target);

        return events;
    }

    public static void applyStatModifier(Bugemon target, Effect effect) {
        int delta = effect.getModifier();
        switch (effect.getStat()) {
            case "attaque"    -> target.getStats().modifyAttack(delta);
            case "defense"    -> target.getStats().modifyDefense(delta);
            case "initiative" -> target.getStats().modifyInitiative(delta);
        }
    }

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

    private static void reverseStatModifier(Bugemon target, Effect effect) {
        int delta = effect.getModifier();
        switch (effect.getStat()) {
            case "attaque"    -> target.getStats().modifyAttack(-delta);
            case "defense"    -> target.getStats().modifyDefense(-delta);
            case "initiative" -> target.getStats().modifyInitiative(-delta);
        }
    }
}