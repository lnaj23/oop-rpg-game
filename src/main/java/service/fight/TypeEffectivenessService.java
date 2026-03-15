package service.fight;

import models.bugemon.Bugemon;

public abstract class TypeEffectivenessService{
    public static double getTypeMultiplier(String attackType, String targetType) {
        if (attackType == null || targetType == null) return 1.0;

    // Combine les deux types en une seule clé
    // ex: "flora_aqua", "aqua_pyro"...
        String combo = attackType.toLowerCase() + "_" + targetType.toLowerCase();

        return switch (combo) {
            // ── Super efficace x1.5 ──────────────────
            case "flora_aqua"  -> 1.5;
            case "aqua_pyro"   -> 1.5;
            case "pyro_litho"  -> 1.5;
            case "litho_flora" -> 1.5;

            // ── Peu efficace x0.75 ───────────────────
            case "aqua_flora"  -> 0.75;
            case "pyro_aqua"   -> 0.75;
            case "litho_pyro"  -> 0.75;
            case "flora_litho" -> 0.75;

            // ── Neutre x1.0 ──────────────────────────
            default -> 1.0;
        };
    }

    public static String getTypeColor(Bugemon bug) {
        if (bug.getType() == null) return "#AAAAAA";

        return switch (bug.getType().toLowerCase()) {
            case "pyro"  -> "#FF4500";   // rouge  🔥
            case "aqua"  -> "#1E90FF";   // bleu   💧
            case "flora" -> "#32CD32";   // vert   🌿
            case "litho" -> "#A0522D";   // marron 🪨
            default      -> "#AAAAAA";   // gris   ❓
        };
    }
    public static String getEffectivenessMessage(String attackType, String targetType){
        String Attack_type = attackType;
        String Target_type = targetType;
        double score = getTypeMultiplier(Attack_type, Target_type);
        if (score == 1.5)  return "C'est super efficace !";
        if (score == 0.75) return "Ce n'est pas très efficace...";
        return "";
        };
}



