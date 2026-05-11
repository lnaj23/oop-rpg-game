package services.app;

import configuration.AppConfig;
import controllers.ControllerManager.Scenes;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * Background music manager.
 * <p>
 * Holds the currently playing audio clip and switches it whenever the active
 * scene changes. Stateless from the caller's point of view — exposes only
 * static methods.
 * </p>
 */
public class GlobalMusicService {

    private static GlobalMusicState currentState;
    private static Clip currentClip;

    /** Switches the background music to match the given scene, if needed. */
    public void updateMusicForScene(Scenes scene) {
        GlobalMusicState nextState = determineGameState(scene);
        if (nextState == currentState) {
            return;
        }
        currentState = nextState;
        chooseAmbiance(currentState);
    }

    private GlobalMusicState determineGameState(Scenes scene) {
        return switch (scene) {
            case FIGHT_AGAINST_CPU -> GlobalMusicState.COMBAT_STATE;
            case DEFEAT -> GlobalMusicState.DEFEAT;
            case TITLE_SCREEN, PREBUILD_TEAM, SELECTION, ADD_BUGEMON -> GlobalMusicState.TEAM_CREATION;
            default -> GlobalMusicState.EXPLORATION;
        };
    }

    private void chooseAmbiance(GlobalMusicState state) {
        String filepath = "";
        float volume = 0.0f;

        switch (state) {
            case COMBAT_STATE -> {
                filepath = AppConfig.MUSIC_COMBAT;
                volume = -6.0f;
            }
            case TEAM_CREATION -> {
                filepath = AppConfig.MUSIC_TEAM_CREATION;
                volume = 0.0f;
            }
            case EXPLORATION -> {
                filepath = AppConfig.MUSIC_EXPLORATION;
                volume = 0.0f;
            }
            case DEFEAT -> {
                filepath = AppConfig.MUSIC_DEFEAT;
                volume = -16.0f;
            }
        }

        stopCurrentMusic();
        playMusic(filepath, volume);
    }

    private void playMusic(String filepath, float volume) {
        try {
            File musicPath = new File(filepath);
            if (!musicPath.exists()) {
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioInput);

            if (currentClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
            }

            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            currentClip.start();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void stopCurrentMusic() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }
}