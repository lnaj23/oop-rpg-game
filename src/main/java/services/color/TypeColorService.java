package services.color;

import models.bugemon.Attack;
import models.bugemon.Bugemon;

/**
 * Provides CSS hex colour strings associated with Bugémon and attack types.
 * Intended for UI rendering only.
 */
public class TypeColorService {

    /** Returns the hex colour for the given Bugémon's type. */
    public static String getTypeColorOfBugemon(Bugemon bug) {
        if (bug.getType() == null) return "#AAAAAA";
        return fromType(bug.getType());
    }

    /** Returns the hex colour for the given attack's type. */
    public static String getTypeColorOfBugemon(Attack attack) {
        if (attack.getType() == null) return "#AAAAAA";
        return fromType(attack.getType());
    }

    /**
     * Maps a type name to its hex colour.
     *
     * @param type the type string (case-insensitive)
     * @return a CSS hex colour, or {@code "#AAAAAA"} if unknown
     */
    private static String fromType(String type) {
        return switch (type.toLowerCase()) {
            case "pyro"  -> "#FF4500";
            case "aqua"  -> "#1E90FF";
            case "flora" -> "#32CD32";
            case "litho" -> "#A0522D";
            default      -> "#AAAAAA";
        };
    }


}