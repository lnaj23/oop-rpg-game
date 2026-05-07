package services.models.game;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import repositories.BugemonRepository;
import services.GameStateHolder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EnemyTeamGenerator {
        public static TeamBugemon generateEnemyTeam() throws AttackNotFoundException, JsonResourceNotFoundException {
            int teamSize = GameStateHolder.getInstance().getCurrentTeamPlayer().getSize();
            List<Bugemon> allBugemons = BugemonRepository.getInstance().getAllBugemons()
                    .stream()
                    .map(bug -> {
                        Bugemon copy = new Bugemon(bug);
                        copy.initPv();
                        return copy;
                    })
                    .collect(Collectors.toList());
            Collections.shuffle(allBugemons);
            TeamBugemon enemyTeam = new TeamBugemon();
            for(int i=0; i<teamSize; i++) {
               enemyTeam.addBugemon(allBugemons.get(i));
            }
            return enemyTeam;
        }
}
