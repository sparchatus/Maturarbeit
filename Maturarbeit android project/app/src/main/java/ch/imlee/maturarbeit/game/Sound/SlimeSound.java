package ch.imlee.maturarbeit.game.Sound;

import android.media.MediaPlayer;

import ch.imlee.maturarbeit.R;

/**
 * Created by Lukas on 23.08.2015.
 */
public class SlimeSound extends Sound{
    public SlimeSound(){
        setMedia(R.raw.slime);
        mediaPlayer.setLooping(true);
    }

}
