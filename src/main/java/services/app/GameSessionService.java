package services.app;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.SavingTeamFailedException;
import models.bugemon.TeamBugemon;
import services.controller.prebuiltteam.TeamService;

public class GameSessionService {

    private final TeamService teamProvider = new TeamService();

    public GameSessionService() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    public void returnToMenu() throws SavingTeamFailedException {
        GameStateHolder state = GameStateHolder.getInstance();

        TeamBugemon currentTeam = state.getCurrentTeamPlayer();
        String savedTeamId = state.getCurrentSavedTeamId();

        if (currentTeam != null && savedTeamId != null) {
            teamProvider.updateTeamById(savedTeamId, currentTeam);
        }

        state.clearCurrentTeamPlayer();
        state.clearCurrentLevelTower();
    }
}
