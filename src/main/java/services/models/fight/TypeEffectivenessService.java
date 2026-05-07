package services.models.fight;


/**
 * Utility service for type-based calculations and colour mappings.
 * Type chart: Flora > Aqua > Pyro > Litho > Flora ; Flora = Pyro ; Aqua = Litho.
 */
public abstract class TypeEffectivenessService{

    /**
     * Returns the damage multiplier for an attack type against a target type.
     * Returns {@code 1.5} (super effective), {@code 0.75} (not very effective),
     * or {@code 1.0} (neutral / null input).
     *
     * @param attackType the attack's type
     * @param targetType the target Bugémon's type
     * @return the type multiplier
     */
    public static double getTypeMultiplier(String attackType, String targetType) {
        if (attackType == null || targetType == null) return 1.0;

        String combo = attackType.toLowerCase() + "_" + targetType.toLowerCase();

        return switch (combo) {

            case "flora_aqua", "aqua_pyro", "pyro_litho", "litho_flora" -> 1.5;

            case "aqua_flora", "pyro_aqua", "litho_pyro", "flora_litho" -> 0.75;

            default -> 1.0;
        };
    }

    /**
     * Returns the in-combat effectiveness message shown after an attack lands.
     *
     * @param attackType the attack's type
     * @param targetType the target's type
     * @return {@code "C'est super efficace !"}, {@code "Ce n'est pas très efficace..."}, or {@code ""}
     */
    public static String getEffectivenessMessage(String attackType, String targetType){

        double score = getTypeMultiplier(attackType, targetType);

        if (score == 1.5)  return "C'est super efficace !";
        if (score == 0.75) return "Ce n'est pas très efficace...";
        return "";
        }
}



