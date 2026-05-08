package models.fight;

/** Heal applied to every alive member of a team, identified by its leader's name. */
public record HealTeamEvent(String teamLeaderName, int value) implements EffectEvent {}