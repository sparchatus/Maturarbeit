package ch.imlee.maturarbeit.game;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.views.ParticleButton;
import ch.imlee.maturarbeit.game.views.SkillButton;

public class GameClient extends Activity {
    //TODO: change halve to half :D

    private static int screenWidth;
    private static int screenHeight;
    private static int halveScreenWidth;
    private static int halveScreenHeight;
    private static Resources rec;
    public static ParticleButton particleButton;
    public static SkillButton skillButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        halveScreenWidth = screenWidth / 2;
        halveScreenHeight = screenHeight / 2;
        rec = getResources();
        particleButton = (ParticleButton) findViewById(R.id.button_particle);
        skillButton = (SkillButton) findViewById(R.id.button_skill);
    }

    public static int getScreenWidth(){
        return screenWidth;
    }

    public static int getScreenHeight(){
        return screenHeight;
    }

    public static int getHalveScreenWidth(){
        return halveScreenWidth;
    }

    public static int getHalveScreenHeight(){
        return halveScreenHeight;
    }

    public static Resources getRec(){
        return rec;
    }
}