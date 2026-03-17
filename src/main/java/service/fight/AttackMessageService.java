package service.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;

public class AttackMessageService {
    public static String predictEfficiency(Bugemon target, Attack attack){
        String attackType = attack.getType();
        String targetType = target.getType();
        String combo = attackType.toLowerCase() + "_" + targetType.toLowerCase();

        return switch (combo) {

            case "flora_aqua"  -> "Super efficace !";
            case "aqua_pyro"   -> "Super efficace !";
            case "pyro_litho"  -> "Super efficace !";
            case "litho_flora" -> "Super efficace !";

            case "aqua_flora"  -> "Peu efficace…";
            case "pyro_aqua"   -> "Peu efficace…";
            case "litho_pyro"  -> "Peu efficace…";
            case "flora_litho" -> "Peu efficace…";

            default -> "Rien de spécial";
        };
    }
}
