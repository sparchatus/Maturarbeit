package ch.imlee.maturarbeit.game;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.entity.Entity;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;
import ch.imlee.maturarbeit.game.views.ParticleButton;
import ch.imlee.maturarbeit.game.views.SkillButton;
import ch.imlee.maturarbeit.main.ChooseActivity;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

public class GameClient extends Activity {

    private static boolean surfaceCreated = false;

    private static int screenWidth;
    private static int screenHeight;
    private static int halfScreenWidth;
    private static int halfScreenHeight;
    private static ParticleButton particleButton;
    private static SkillButton skillButton;
    private static GameSurface gameSurface;
    private static GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        halfScreenWidth = screenWidth / 2;
        halfScreenHeight = screenHeight / 2;
        particleButton = (ParticleButton) findViewById(R.id.button_particle);
        skillButton = (SkillButton) findViewById(R.id.button_skill);
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

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getHalfScreenWidth() {
        return halfScreenWidth;
    }

    public static int getHalfScreenHeight() {
        return halfScreenHeight;
    }

    public static SkillButton getSkillButton() {
        return skillButton;
    }

    public static ParticleButton getParticleButton() {
        return particleButton;
    }
}