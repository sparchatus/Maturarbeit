package ch.imlee.maturarbeit.game;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;
import ch.imlee.maturarbeit.game.views.ParticleButton;
import ch.imlee.maturarbeit.game.views.SkillButton;

public class GameClient extends Activity {

    private static int screenWidth;
    private static int screenHeight;
    private static int halfScreenWidth;
    private static int halfScreenHeight;
    private static Resources rec;
    public static ParticleButton particleButton;
    public static SkillButton skillButton;
    public static GameSurface.GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        halfScreenWidth = screenWidth / 2;
        halfScreenHeight = screenHeight / 2;
        rec = getResources();
        particleButton = (ParticleButton) findViewById(R.id.button_particle);
        skillButton = (SkillButton) findViewById(R.id.button_skill);
    }

    public static void initializeStartData(GameStartEvent startData){

    }

    public static void startSynchronizedTick(){

    }

    public static int getScreenWidth(){
        return screenWidth;
    }

    public static int getScreenHeight(){
        return screenHeight;
    }

    public static int getHalfScreenWidth(){
        return halfScreenWidth;
    }

    public static int getHalfScreenHeight(){
        return halfScreenHeight;
    }

    public static Resources getRec(){
        return rec;
    }
}