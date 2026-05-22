package models.game;

/**
 * The kind of node encountered at a step of the tower: a normal combat,
 * a reward step, or a boss combat.
 */
public enum TowerNodeType {
    COMBAT,
    REWARD,
    BOSS
}
