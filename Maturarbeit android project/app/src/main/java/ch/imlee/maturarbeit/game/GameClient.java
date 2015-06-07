package ch.imlee.maturarbeit.game;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;

import ch.imlee.maturarbeit.R;

public class GameClient extends Activity {

    private static int screenWidth;
    private static int screenHeight;
    private static int halveScreenWidth;
    private static int halveScreenHeight;
    private static Resources rec;

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