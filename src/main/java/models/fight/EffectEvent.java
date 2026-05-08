package models.fight;

/**
 * Sealed type representing a single effect produced during a fight action.
 * Sub-types are exhaustively listed; new effect kinds must be added to the {@code permits} list.
 * This is pure data — no UI text. The view layer is responsible for formatting.
 */
public sealed interface EffectEvent permits HealSelfEvent, HealTeamEvent, StatModifierEvent {}