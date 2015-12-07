package ch.imlee.maturarbeit.game.Sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.activities.GameClient;


public class Sound {
/*    //// TODO: 27.08.2015 use SoundPool instead of MediaPlayer
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

    */

    // from API 21 on, you should use SoundPool.Builder to create a SoundPool, but we want our app to be compatible with earlier versions on, so we use the deprecated constructor
    private static SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    // the int 1 as the last element of the constructor has currently no effect. More info on
    // http://developer.android.com/reference/android/media/SoundPool.html
    private static int particleHit = soundPool.load(GameClient.getContext(), R.raw.particle_collision, 1);
    private static int slime = soundPool.load(GameClient.getContext(), R.raw.slime, 1);
    private static int stun = soundPool.load(GameClient.getContext(), R.raw.stun, 1);
    private static int background = soundPool.load(GameClient.getContext(), R.raw.background_test_1, 1);

    public static void initialize(){
        // the -1 stands for an infinite loop
        soundPool.setLoop(background, -1);
        soundPool.setLoop(slime, -1);
        soundPool.setLoop(stun, -1);
    }

    public static void play(int id){
        soundPool.play(id, 1, 1, )
    }



























}