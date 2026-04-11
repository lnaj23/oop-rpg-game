package services.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;

public abstract class AttackMessageService {

    /**
     * Builds the message displayed after an attack.
     *
     * @param attacker the attacking Bugemon
     * @param target   the defending Bugemon
     * @param attack   the attack used
     * @param damage   the damage dealt
     * @return the attack result message
     */
    public static String buildAttackMessage(Bugemon attacker, Bugemon target, Attack attack, int damage, EffectResult effects) {
        String base = attacker.getName() + " utilise " + attack.getName() + "! "
                + TypeEffectivenessService.getEffectivenessMessage(attack.getType(), target.getType()) + "\n"
                + target.getName() + " perd " + damage + " PV ! "
                + (target.isKO() ? target.getName() + " est mis KO" : "");
        String effectStr = String.join("\n", effects.getMessages());
        return effectStr.isEmpty() ? base : base + "\n" + effectStr;
    }
}