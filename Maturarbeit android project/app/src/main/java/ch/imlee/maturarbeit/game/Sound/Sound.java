package ch.imlee.maturarbeit.game.Sound;

import android.content.Context;
import android.media.MediaPlayer;

import ch.imlee.maturarbeit.game.GameClient;

/**
 * Created by Lukas on 21.08.2015.
 */
public class Sound {
    protected MediaPlayer mediaPlayer;
    protected static Context context = GameClient.getContext();

    public void start(){
        mediaPlayer.start();
    }

    public void stop(){
        mediaPlayer.stop();
    }
}
