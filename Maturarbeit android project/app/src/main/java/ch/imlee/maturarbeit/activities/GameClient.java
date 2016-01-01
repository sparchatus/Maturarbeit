package ch.imlee.maturarbeit.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.events.gameStateEvents.GameCancelledEvent;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.StartDataInitializer;
import ch.imlee.maturarbeit.views.GameSurface;

public class GameClient extends Activity {


    private static boolean activityLoaded;
    // variables which indicate if the onCreate() of the respective SurfaceView has been called
    private static boolean gameSurfaceLoaded;
    private static boolean joystickSurfaceLoaded;
    private static boolean miniMapSurfaceLoaded ;

    // the event is required to restart the game
    private static GameStartEvent gameStartEventSave;
    private static GameThread gameThread;
    private static Context context;

    // system called method. the content of the Activity is set.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    // whenever the GameClient Activity is reentered, it has to be reset to allow a new game to be loaded
    @Override
    protected void onResume(){
        super.onResume();
        activityLoaded = false;
        gameSurfaceLoaded = false;
        joystickSurfaceLoaded = false;
        miniMapSurfaceLoaded = false;
        context = getApplicationContext();
        GameCancelledEvent.giveActivity(this);
        activityLoaded = true;
    }

    // because the variables are static and leaving the Activity does not necessarily destroy them, we have to reset them to their initial state
    @Override
    protected void onStop(){
        super.onStop();
        activityLoaded = false;
        gameSurfaceLoaded = false;
        joystickSurfaceLoaded = false;
        miniMapSurfaceLoaded = false;
        GameSurface.destroy();
        ChooseActivity.eventReceiver.setRunning(false);
    }

    @Override
    public void onBackPressed(){
        // if the back button gets pressed, cancel the game
        new GameCancelledEvent().send();
        activityLoaded = false;
        gameSurfaceLoaded = false;
        joystickSurfaceLoaded = false;
        miniMapSurfaceLoaded = false;
        GameSurface.destroy();
        ChooseActivity.eventReceiver.setRunning(false);
        NavUtils.navigateUpFromSameTask(this);
    }

    // this method is for the restart
    public static void initializeStartData(){
        initializeStartData(gameStartEventSave);
    }

    // The gameThread object gets initialized and the start data is set.
    public static void initializeStartData(GameStartEvent gameStartEvent) {
        gameStartEventSave = gameStartEvent;
        gameThread = GameSurface.getGameThread();
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