package ch.imlee.maturarbeit.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.StartDataInitializer;
import ch.imlee.maturarbeit.views.GameSurface;

public class GameClient extends Activity {

    private static boolean activityLoaded;
    private static boolean gameSurfaceLoaded;
    private static boolean joystickSurfaceLoaded;
    private static boolean miniMapSurfaceLoaded ;
    private static GameStartEvent gameStartEventSave;
    private static GameSurface gameSurface;
    private static GameThread gameThread;
    private static Context context;

    // system called method. The layout and thus the views are initialized and a reference is saved for later use in the GameThread.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onResume(){
        super.onResume();
        activityLoaded = false;
        gameSurfaceLoaded = false;
        joystickSurfaceLoaded = false;
        miniMapSurfaceLoaded = false;
        context = getApplicationContext();
        gameSurface = (GameSurface) (findViewById(R.id.game_surface));
        activityLoaded = true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        onStop();
    }

    @Override
    protected void onStop(){
        super.onStop();
        ChooseActivity.eventReceiver.setRunning(false);
        if(gameSurfaceLoaded){
            GameSurface.destroy();
        }
        gameSurface = null;
        NavUtils.navigateUpFromSameTask(this);
        //todo maybe remove
        finish();
    }

    @Override
    public void onBackPressed(){
        onStop();
    }

    // this method is for the restart
    public static void initializeStartData(){
        while(!gameSurfaceLoaded){
            try {
                Thread.sleep(10);
            } catch (Exception e){}
        }
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