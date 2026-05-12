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
import java.util.Optional;

public abstract class EffectManager {

    /**
     * Applies the effects of an attack and ticks ongoing temporary effects.
     * Returns a list of structured events describing what happened — no UI strings.
     */
    public static List<EffectEvent> handleEffect(Bugemon attacker, Bugemon target, TeamBugemon attackerTeam, Attack attack) {
        List<EffectEvent> events = new ArrayList<>();

        if (attack.getEffects() == null) {
            return events;
        }

        for (Effect effet : attack.getEffects()) {
            Optional<EffectEvent> event = handleSingleEffect(attacker, target, attackerTeam, effet);
            event.ifPresent(events::add);
        }

        tickEffects(attacker);
        tickEffects(target);

        return events;
    }

    private static Optional<EffectEvent> handleSingleEffect(Bugemon attacker, Bugemon target, TeamBugemon attackerTeam, Effect effet) {
        return switch (effet.getType()) {
            case "soin" -> handleHealEffect(attacker, attackerTeam, effet);
            case "stat_modifier" -> handleStatModifierEffect(attacker, target, effet);
            default -> Optional.empty();
        };
    }

    private static Optional<EffectEvent> handleHealEffect(Bugemon attacker, TeamBugemon attackerTeam, Effect effet) {
        int valeur = effet.getValue();
        return switch (effet.getTarget()) {
            case "equipe" -> healTeam(attacker, attackerTeam, valeur);
            case "lanceur" -> healSelf(attacker, valeur);
            default -> Optional.empty();
        };
    }

    private static Optional<EffectEvent> healTeam(Bugemon attacker, TeamBugemon attackerTeam, int valeur) {
        for (Bugemon membre : attackerTeam.getTeam()) {
            if (!membre.isKO()) {
                membre.getStats().heal(valeur);
            }
        }
        return Optional.of(new HealTeamEvent(attacker.getName(), valeur));
    }

    private static Optional<EffectEvent> healSelf(Bugemon attacker, int valeur) {
        if (attacker.isKO()) {
            return Optional.empty();
        }
        attacker.getStats().heal(valeur);
        return Optional.of(new HealSelfEvent(attacker.getName(), valeur));
    }

    private static Optional<EffectEvent> handleStatModifierEffect(Bugemon attacker, Bugemon target, Effect effet) {
        Bugemon finalTarget = resolveStatModifierTarget(attacker, target, effet);
        if (finalTarget == null) {
            return Optional.empty();
        }
        applyStatModifier(finalTarget, effet);
        finalTarget.addOngoingEffect(effet);
        return Optional.of(new StatModifierEvent(finalTarget.getName(), effet.getStat(), effet.getModifier()));
    }

    private static Bugemon resolveStatModifierTarget(Bugemon attacker, Bugemon target, Effect effet) {
        return switch (effet.getTarget()) {
            case "lanceur" -> attacker;
            case "adversaire" -> target;
            default -> null;
        };
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