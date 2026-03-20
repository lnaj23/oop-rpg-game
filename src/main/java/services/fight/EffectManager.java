package services.fight;

import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.Effect;
import models.bugemon.TeamBugemon;

import java.util.Objects;

public abstract class EffectManager {

    public static void handleEffect(Bugemon attacker, Bugemon target, TeamBugemon attackerTeam, TeamBugemon targetTeam,
                                    Attack attack){
        //TODO
        //est déjà appelé à chaque tour, mtn faut gerer les effects comme décrit ds l'énoncé
        //bonne chance
    }

}
