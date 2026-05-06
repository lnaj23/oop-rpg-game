package services;

import models.GameState;
import controllers.ControllerManager.Scenes;

/**
 * Service responsible for managing the application's musical logic.
 * <p>
 * This service implements the <b>Singleton</b> design pattern to ensure a single instance
 * and maintain the current music state across scene transitions.
 * It prevents unnecessary audio cuts when two consecutive scenes share
 * the same musical atmosphere.
 * </p>
 */
public class MusicService {

    /** The unique instance of the service (Singleton Pattern). */
    private static MusicService instance;

    /** The game state corresponding to the currently playing music. */
    private GameState currentState;

    /**
     * Private constructor to prevent external instantiation.
     */
    private MusicService() {}

    /**
     * Retrieves the unique instance of the music service.
     *
     * @return The {@link MusicService} instance.
     */
    public static MusicService getInstance() {
        if (instance == null) {
            instance = new MusicService();
        }
        return instance;
    }

    /**
     * Analyzes the target scene and updates the background music if necessary.
     * <p>
     * If the requested scene matches the currently playing atmosphere, the method
     * ignores the request to ensure a seamless transition (no audio stutter or restart).
     * </p>
     *
     * @param scene The target application scene defined in {@link Scenes}.
     */
    public void updateMusicForScene(Scenes scene) {
        GameState nextState = determineGameState(scene);
        if (nextState == currentState) {
            return;
        }

        currentState = nextState;
        MusicPlayer.chose_ambiance(currentState);
    }

    /**
     * Maps a UI scene to a specific musical game state.
     *
     * @param scene The scene to be analyzed.
     * @return The {@link GameState} corresponding to the desired musical ambiance.
     */
    private GameState determineGameState(Scenes scene) {
        return switch (scene) {
            case FIGHT_AGAINST_CPU -> GameState.COMBAT_STATE;
            case DEFEAT -> GameState.DEFEAT;
            case TITLE_SCREEN, PREBUILD_TEAM, SELECTION, ADD_BUGEMON -> GameState.TEAM_CREATION;
            default -> GameState.EXPLORATION;
        };
    }
}