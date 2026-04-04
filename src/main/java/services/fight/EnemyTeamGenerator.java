package services.fight;

import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import repositories.BugemonRepository;
import services.game.GameStateHolder;

import java.util.Collections;
import java.util.List;

public abstract class EnemyTeamGenerator {
        public static TeamBugemon generateEnemyTeam() {
            int teamSize = GameStateHolder.getInstance().getCurrentTeamPlayer().getSize();
            List<Bugemon> allBugemons = BugemonRepository.getAllBugemons();
            Collections.shuffle(allBugemons);
            TeamBugemon enemyTeam = new TeamBugemon();
            for(int i=0; i<teamSize; i++) {
               enemyTeam.addBugemon(allBugemons.get(i));
            }
            return enemyTeam;
        }
}
