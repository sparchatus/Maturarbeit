package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ch.imlee.maturarbeit.views.GameSurface;

public class ServerEndGameScreen extends EndGameScreen{

    @Override
    protected void subRender(Canvas canvas) {
        super.subRender(canvas);
        Paint buttonColors = new Paint();
        buttonColors.setAlpha(0x99);

        buttonColors.setColor(Color.RED);
        canvas.drawRect(0, 0, GameSurface.getSurfaceWidth() / 2, GameSurface.getSurfaceHeight(), buttonColors);
        buttonColors.setColor(Color.GREEN);
        canvas.drawRect(GameSurface.getSurfaceWidth() / 2, GameSurface.getSurfaceHeight(), GameSurface.getSurfaceWidth(), GameSurface.getSurfaceHeight(), buttonColors);

        buttonColors.setTextSize(64);
        buttonColors.setColor(Color.WHITE);
        canvas.drawText("End", 10, GameSurface.getSurfaceHeight()/2 + buttonColors.getTextSize(), buttonColors);
        canvas.drawText("Restart", GameSurface.getSurfaceWidth()/2+10, GameSurface.getSurfaceHeight()/2 + buttonColors.getTextSize(), buttonColors);
    }
}
