package ch.imlee.maturarbeit.game.Sound;

import android.content.Context;
import android.media.MediaPlayer;

import ch.imlee.maturarbeit.R;

/**
 * Created by Sandro on 11.08.2015.
 */
public class BackgroundMusic extends Sound{

    public BackgroundMusic(){
        mediaPlayer = MediaPlayer.create(context, R.raw.background_test_1);
        mediaPlayer.setLooping(true);
    }
}
