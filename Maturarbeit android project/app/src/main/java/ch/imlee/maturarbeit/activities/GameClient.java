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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        particleButton = (ParticleButton) findViewById(R.id.button_particle);
        skillButton = (SkillButton) findViewById(R.id.button_skill);
        miniMap = (MiniMap) findViewById(R.id.mini_map_surface);
        context = getApplicationContext();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("tag", "GameClient Activity started");
        gameSurface = (GameSurface) (findViewById(R.id.game_surface));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        ChooseActivity.eventReceiver.setRunning(false);
    }

    public static void initializeStartData(GameStartEvent gameStartEvent) {
        gameThread = gameSurface.getGameThread();
        gameThread.setStartData(gameStartEvent);
    }

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

    public static GameThread getGameThread(){
        return gameThread;
    }

    public static MiniMap getMiniMap(){
        return miniMap;
    }

    public static Context getContext(){
        return  context;
    }
}