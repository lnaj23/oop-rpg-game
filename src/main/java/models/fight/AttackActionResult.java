package models.fight;

import java.util.List;

/**
 * Structured outcome of a single attack action — pure data, no UI strings.
 * The view layer (e.g. {@code services.view.ActionMessageFormatter}) consumes this
 * to build the user-facing combat message.
 */
public record AttackActionResult(
        String attackerName,
        String attackName,
        String targetName,
        int damage,
        boolean targetIsKO,
        Effectiveness effectiveness,
        List<EffectEvent> effects
) {}