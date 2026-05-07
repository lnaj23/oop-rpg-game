package services;

import configuration.AppConfig;
import controllers.ControllerManager.Scenes;
import models.GameState;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class MusicService {

    private static MusicService instance;

    private GameState currentState;
    private Clip currentClip;

    private MusicService() {}

    public static MusicService getInstance() {
        if (instance == null) {
            instance = new MusicService();
        }
        return instance;
    }

    public void updateMusicForScene(Scenes scene) {
        GameState nextState = determineGameState(scene);

        if (nextState == currentState) {
            return;
        }

        currentState = nextState;
        chooseAmbiance(currentState);
    }

    private GameState determineGameState(Scenes scene) {
        return switch (scene) {
            case FIGHT_AGAINST_CPU -> GameState.COMBAT_STATE;
            case DEFEAT -> GameState.DEFEAT;
            case TITLE_SCREEN, PREBUILD_TEAM, SELECTION, ADD_BUGEMON -> GameState.TEAM_CREATION;
            default -> GameState.EXPLORATION;
        };
    }

    private void chooseAmbiance(GameState state) {
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