package ch.imlee.maturarbeit.game.Sound;

import android.content.Context;
import android.media.MediaPlayer;

import ch.imlee.maturarbeit.activities.GameClient;

/**
 * Created by Lukas on 21.08.2015.
 */

public class Sound {
    //// TODO: 27.08.2015 use SoundPool instead of MediaPlayer 
    protected MediaPlayer mediaPlayer;
    protected static Context context = GameClient.getContext();

    public void start(){
//        mediaPlayer.start();
    }

    public void start(final long TIME){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                mediaPlayer.start();
                try {
                    Thread.sleep(TIME);
                } catch (Exception e){
                    e.printStackTrace();
                }
                stop();
            }
        }).start();
    }

    protected void setMedia(int media){
        mediaPlayer = MediaPlayer.create(context, media);
    }

    public void stop(){
//        mediaPlayer.stop();
//        mediaPlayer.release();
    }
}