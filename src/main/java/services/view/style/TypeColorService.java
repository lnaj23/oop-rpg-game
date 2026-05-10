package services.view.style;

/**
 * Provides CSS hex color strings associated with Bugémon and attack types.
 * Pure utility — takes a type name, returns a color. Not coupled to any model class.
 */
public class TypeColorService {

    private static final String DEFAULT_COLOR = "#AAAAAA";

    /**
     * Maps a type name to its display color.
     *
     * @param type the type string (case-insensitive); may be null
     * @return a CSS hex color, or {@code "#AAAAAA"} if the type is null or unknown
     */
    public static String getColor(String type) {
        if (type == null) return DEFAULT_COLOR;
        return switch (type.toLowerCase()) {
            case "pyro"  -> "#FF4500";
            case "aqua"  -> "#1E90FF";
            case "flora" -> "#32CD32";
            case "litho" -> "#A0522D";
            default      -> DEFAULT_COLOR;
        };
    }
}