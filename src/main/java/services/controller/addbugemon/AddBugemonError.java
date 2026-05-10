package services.controller.addbugemon;

/**
 * Possible validation errors returned by {@link AddBugemonService}
 * when adding a new Bugémon. Pure data — view-side formatting is handled by
 * {@code services.view.formatter.AddBugemonErrorFormatter}.
 */
public enum AddBugemonError {
    NAME_REQUIRED,
    TYPE_REQUIRED,
    STATS_NOT_NUMERIC,
    STATS_INVALID,
    ATTACK_COUNT_INVALID,
    ATTACKS_NOT_DISTINCT,
    SPRITE_REQUIRED,
    SPRITE_NOT_SELECTED,
    SPRITE_FILE_NOT_FOUND,
    SPRITE_NOT_A_FILE,
    SPRITE_WRONG_FORMAT,
    SPRITE_NOT_VALID_IMAGE,
    STARTER_REQUIRED
}
