package ch.imlee.maturarbeit.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.events.gameStateEvents.RestartGameEvent;
import ch.imlee.maturarbeit.game.GameServerThread;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.StartDataInitializer;
import ch.imlee.maturarbeit.game.WaitUntilLoadedThread;
import ch.imlee.maturarbeit.game.special_screens.LoadingScreen;
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
        while(!gameSurfaceLoaded){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
    protected void onStop(){
        super.onStop();
        activityLoaded = false;
        gameSurfaceLoaded = false;
        joystickSurfaceLoaded = false;
        miniMapSurfaceLoaded = false;
        ChooseActivity.eventReceiver.setRunning(false);
        if(gameThread != null){
            gameThread.setRunning(false);
            gameThread.stopEndGame();
            while(true) {
                try {
                    gameThread.join();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        NavUtils.navigateUpFromSameTask(this);
        //todo maybe remove
        finish();
    }

    @Override
    public void onBackPressed(){
        onStop();
    }

    private static void setupThread(){
        GameThread.setRunning(true);
        if (StartActivity.deviceType == DeviceType.HOST){
            gameThread = new GameServerThread(gameSurface.getHolder());
        }else{
            gameThread = new GameThread(gameSurface.getHolder());
        }
    }

    public static void restart(){
        Log.e("GameSurface", "restart soon");
        gameThread.stopEndGame();
        LoadingScreen.setRestart();
        setupThread();
        GameThread.reset();
        gameThread.start();
        if(StartActivity.deviceType == DeviceType.HOST) {
            WaitUntilLoadedThread.reset();
            new WaitUntilLoadedThread().start();
            new RestartGameEvent().send();
        }
        GameClient.initializeStartData();
    }

    // this method is for the restart
    public static void initializeStartData(){
        initializeStartData(gameStartEventSave);
    }

    // The gameThread object gets initialized and the start data is set.
    public static void initializeStartData(GameStartEvent gameStartEvent) {
        gameStartEventSave = gameStartEvent;
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