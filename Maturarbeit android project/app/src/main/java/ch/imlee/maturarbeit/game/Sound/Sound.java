package ch.imlee.maturarbeit.game.Sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashSet;
import java.util.Set;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.activities.GameClient;


public class Sound {
    // from API 21 on, you should use SoundPool.Builder to create a SoundPool, but we want our app to be compatible with earlier versions on, so we use the deprecated constructor
    private static SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    // the int 1 as the last element of the constructor has currently no effect. More info on
    // http://developer.android.com/reference/android/media/SoundPool.html
    public static final int PARTICLE_HIT = soundPool.load(GameClient.getContext(), R.raw.particle_collision, 1);
    public static final int SLIME = soundPool.load(GameClient.getContext(), R.raw.slime, 1);
    public static final int STUN = soundPool.load(GameClient.getContext(), R.raw.stun, 1);
    public static final int BACKGROUND = soundPool.load(GameClient.getContext(), R.raw.background_test_1, 1);

    private static Set<Integer> loopedIDs = new HashSet<>();

    public static void setup(){
        loopedIDs.add(BACKGROUND);
        loopedIDs.add(STUN);
        loopedIDs.add(SLIME);
    }

    public static void play(int id){
        soundPool.play(id, 1, 1, 0, getLoop(id), 1);
    }

    // this method plays a sound for a certain amount of time
    public static void play(final int id, final long time){
        play(id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e){
                    // it isn't that important to keep the sound going, so ignore
                }
                stop(id);
            }
        }).start();
    }

    public static void stop(int id){
        soundPool.stop(id);
    }

    private static int getLoop(int id){
        // -1 stands for an infinite loop, 0 stands for a single playback
        if(loopedIDs.contains(id)) return -1;
        return 0;
    }
























}