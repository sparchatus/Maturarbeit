package ch.imlee.maturarbeit.game.Sound;

import android.media.MediaPlayer;

import ch.imlee.maturarbeit.R;

/**
 * Created by Lukas on 21.08.2015.
 */
public class ParticleCollisionSound extends Sound{
    public ParticleCollisionSound(){
        mediaPlayer = MediaPlayer.create(context, R.raw.particle_collision);
    }
}
