package services;

import configuration.AppConfig;
import models.GameState;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Music_player {


    private static Clip currentClip = null;

    public static void chose_ambiance(GameState state){
        String filepath = "";
        float volume = 0.0f;
        switch(state) {
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

        if (currentClip != null){
            currentClip.stop();
            currentClip.close();
        }

        PlayMusic(filepath, volume);
    }
    public static void PlayMusic(String filepath, float volume){
        try
        {
            File musicPath = new File(filepath);

            if (musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                currentClip = AudioSystem.getClip();
                currentClip.open(audioInput);
                if (currentClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(volume);
                }
                currentClip.start();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}
