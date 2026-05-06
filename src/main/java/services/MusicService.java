package services;

import models.GameState;
import controllers.ControllerManager.Scenes;

public class MusicService {
    private static MusicService instance;
    private GameState currentState;

    private MusicService() {}

    public static MusicService getInstance() {
        if (instance == null) instance = new MusicService();
        return instance;
    }

    public void updateMusicForScene(Scenes scene) {
        GameState nextState = determineGameState(scene);
        if (nextState == currentState) {
            return;
        }
        currentState = nextState;
        Music_player.chose_ambiance(currentState);
    }

    private GameState determineGameState(Scenes scene) {
        return switch (scene) {
            case FIGHT_AGAINST_CPU -> GameState.COMBAT_STATE;
            case DEFEAT -> GameState.DEFEAT;
            case TITLE_SCREEN, PREBUILD_TEAM, SELECTION, ADD_BUGEMON -> GameState.TEAM_CREATION;
            default -> GameState.EXPLORATION;
        };
    }
}