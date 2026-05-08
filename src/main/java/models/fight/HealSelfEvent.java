package models.fight;

/** Heal applied to a single Bugémon (typically the attacker). */
public record HealSelfEvent(String targetName, int value) implements EffectEvent {}