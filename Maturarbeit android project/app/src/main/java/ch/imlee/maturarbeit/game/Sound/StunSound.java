package ch.imlee.maturarbeit.game.Sound;

import android.media.MediaPlayer;

import ch.imlee.maturarbeit.R;

/**
 * Created by Lukas on 23.08.2015.
 */
public class StunSound extends Sound {
    public StunSound(){
        mediaPlayer = MediaPlayer.create(context, R.raw.stun);
        mediaPlayer.setLooping(true);
    }
}
