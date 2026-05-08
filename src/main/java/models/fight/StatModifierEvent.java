package models.fight;

/**
 * A stat modifier applied to a Bugémon: positive {@code delta} buffs, negative debuffs.
 *
 * @param stat one of {@code "attaque"}, {@code "defense"}, {@code "initiative"}
 */
public record StatModifierEvent(String targetName, String stat, int delta) implements EffectEvent {}