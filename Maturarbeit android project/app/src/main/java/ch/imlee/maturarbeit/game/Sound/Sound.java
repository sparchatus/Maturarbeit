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
    // the int 1 as the last element of the constructor has currently no effect. More info on
    // http://developer.android.com/reference/android/media/SoundPool.html
    private static SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    private static boolean soundsLoaded = false;

    // these should be final, but we can't initialize static final variables after their creation
    public static int PARTICLE_HIT;
    public static int SLIME;
    public static int STUN;
    public static int BACKGROUND;

    private static Set<Integer> loopedIDs = new HashSet<>();

    public static void initialize(){
        // setOnLoadCompleteListener requires a min SDK of 8, so we have to set this the build.gradle file
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Sound.setSoundsLoaded(true);
            }
        });

        PARTICLE_HIT = soundPool.load(GameClient.getContext(), R.raw.particle_collision, 1);
        SLIME = soundPool.load(GameClient.getContext(), R.raw.slime, 1);
        STUN = soundPool.load(GameClient.getContext(), R.raw.stun, 1);
        BACKGROUND = soundPool.load(GameClient.getContext(), R.raw.background_test_1, 1);

        loopedIDs.add(BACKGROUND);
        loopedIDs.add(STUN);
        loopedIDs.add(SLIME);

        // block until all sounds are loaded
        while(!soundsLoaded){
            try {
                Thread.sleep(50);
            } catch (Exception e){
                // ignore
            }
        }
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

    private static void setSoundsLoaded(boolean soundsLoaded1){
        soundsLoaded = soundsLoaded1;
    }




















}