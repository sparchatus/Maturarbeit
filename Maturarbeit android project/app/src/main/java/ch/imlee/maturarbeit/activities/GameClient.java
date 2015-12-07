package ch.imlee.maturarbeit.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.StartDataInitializer;
import ch.imlee.maturarbeit.views.GameSurface;
import ch.imlee.maturarbeit.views.MiniMap;

public class GameClient extends Activity {

    private static boolean activityLoaded = false;
    private static boolean gameSurfaceLoaded = false;
    private static boolean joystickSurfaceLoaded = false;
    private static boolean miniMapSurfaceLoaded = false;
    private static GameStartEvent gameStartEventSave;
    private static GameSurface gameSurface;
    private static GameThread gameThread;
    private static Context context;

    // system called method. The layout and thus the views are initialized and a reference is saved for later use in the GameThread.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = getApplicationContext();
        gameSurface = (GameSurface) (findViewById(R.id.game_surface));
        activityLoaded = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // This function properly stops the eventReceiver Thread when the Activity ends.
    @Override
    protected void onStop(){
        super.onStop();
        ChooseActivity.eventReceiver.setRunning(false);
    }

    // this method is for the restart
    public static void initializeStartData(){
        initializeStartData(gameStartEventSave);
    }

    // The gameThread object gets initialized and the start data is set.
    public static void initializeStartData(GameStartEvent gameStartEvent) {
        gameStartEventSave = gameStartEvent;
        gameThread = gameSurface.getGameThread();
        StartDataInitializer.setStartData(gameStartEvent, gameThread);
    }

    // The method is called when all Devices are done loading. It causes the game Loop to start.
    public static void startSynchronizedTick() {
        gameThread.endLoading();
    }

    public static synchronized boolean getLoaded(){
        return activityLoaded&&miniMapSurfaceLoaded&&joystickSurfaceLoaded&&gameSurfaceLoaded;
    }
    public static Context getContext(){
        return  context;
    }

    public static void gameSurfaceLoaded(){
        gameSurfaceLoaded = true;
    }
    public static void joystickSurfaceLoaded(){
        joystickSurfaceLoaded = true;
    }
    public static void miniMapSurfaceLoaded(){
        miniMapSurfaceLoaded = true;
    }
}