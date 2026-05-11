package services.app;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.SavingTeamFailedException;
import services.controller.dataprovider.TeamProvider;

public class GameSessionService {

    private final TeamProvider teamProvider = new TeamProvider();

    public GameSessionService() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    public void returnToMenu() throws SavingTeamFailedException {
        if (GameStateHolder.getInstance().getCurrentTeamPlayer() != null) {
            teamProvider.saveTeams(GameStateHolder.getInstance().getSavedTeams());
        }

        GameStateHolder.getInstance().clearCurrentTeamPlayer();
        GameStateHolder.getInstance().clearCurrentLevelTower();
    }
}
