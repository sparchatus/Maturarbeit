package ch.imlee.maturarbeit.game.Sound;

import android.content.Context;
import android.media.MediaPlayer;

import ch.imlee.maturarbeit.R;

/**
 * Created by Sandro on 11.08.2015.
 */
public class BackgroundMusic {
    MediaPlayer backgroundPlayer;

    public BackgroundMusic(Context context){
        backgroundPlayer = MediaPlayer.create(context, R.raw.background_test_1);
        backgroundPlayer.setLooping(true);
    }

    public void startMusic(){
        backgroundPlayer.start();
    }

    public void stop(){
        backgroundPlayer.stop();
    }
}
