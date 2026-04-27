package services;

import configuration.AppConfig;
import models.GameState;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music_player {


    private static Clip currentClip = null;


    public static void chose_ambiance(GameState state){
        String filepath = "";
        switch(state){
            case COMBAT_STATE -> filepath = AppConfig.MUSIC_COMBAT;
            case TEAM_CREATION ->  filepath = AppConfig.MUSIC_TEAM_CREATION;
            case EXPLORATION ->   filepath =   AppConfig.MUSIC_EXPLORATION;
        }

        if (currentClip != null){
            currentClip.stop();

        }

        PlayMusic(filepath);
    }
    public static void PlayMusic(String filepath){
        try
        {
            File musicPath = new File(filepath);

            if (musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                currentClip = AudioSystem.getClip();
                currentClip.open(audioInput);
                currentClip.start();
            }
            else{
                System.out.println("cant find");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}
