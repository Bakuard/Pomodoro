package com.bakuard.pomodoro.controller;

import javafx.scene.media.MediaPlayer;

public class TimerSignalPlayer {

    private volatile MediaPlayer mediaPlayer;
    private volatile boolean playing;
    private final ResourcesLoader resourcesLoader;

    public TimerSignalPlayer(ResourcesLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
    }

    public void start() {
        if(!playing) {
            mediaPlayer = createMediaPlayer();
            mediaPlayer.play();
            playing = true;
        }
    }

    public void stop() {
        if(playing) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            playing = false;
        }
    }


    private MediaPlayer createMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer(resourcesLoader.getSound("beep.wav"));
        mediaPlayer.setCycleCount(1000);
        return mediaPlayer;
    }

}
