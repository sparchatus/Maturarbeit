package ch.imlee.maturarbeit.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.views.GameSurface;
import ch.imlee.maturarbeit.views.MiniMap;
import ch.imlee.maturarbeit.views.ParticleButton;
import ch.imlee.maturarbeit.views.SkillButton;

public class GameClient extends Activity {

    private static boolean surfaceCreated = false;

    private static ParticleButton particleButton;
    private static SkillButton skillButton;
    private static GameSurface gameSurface;
    private static GameThread gameThread;
    private static MiniMap miniMap;
    private static Context context;

    /**
     * System called method. The layout and thus the views are initialized and a reference is saved for later use in the GameThread.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        particleButton = (ParticleButton) findViewById(R.id.button_particle);
        skillButton = (SkillButton) findViewById(R.id.button_skill);
        miniMap = (MiniMap) findViewById(R.id.mini_map_surface);
        context = getApplicationContext();
        gameSurface = (GameSurface) (findViewById(R.id.game_surface));
    }

    /**
     * This function properly stops the eventReceiver Thread when the Activity ends.
     */
    @Override
    protected void onStop(){
        super.onStop();
        ChooseActivity.eventReceiver.setRunning(false);
    }

    /**
     * The gameThread object gets initialized and the start data is set.
     * @param gameStartEvent
     */
    public static void initializeStartData(GameStartEvent gameStartEvent) {
        gameThread = gameSurface.getGameThread();
        gameThread.setStartData(gameStartEvent);
    }

    /**
     * The method is called when all Devices are done loading. It causes the game Loop to start.
     */
    public static void startSynchronizedTick() {
        gameThread.endLoading();
    }

    public static synchronized boolean getSurfaceCreated(){
        return surfaceCreated;
    }

    public static synchronized void setSurfaceCreated(boolean created){
        surfaceCreated = created;
    }

    public static SkillButton getSkillButton() {
        return skillButton;
    }

    public static ParticleButton getParticleButton() {
        return particleButton;
    }

    public static MiniMap getMiniMap(){
        return miniMap;
    }

    public static Context getContext(){
        return  context;
    }
}