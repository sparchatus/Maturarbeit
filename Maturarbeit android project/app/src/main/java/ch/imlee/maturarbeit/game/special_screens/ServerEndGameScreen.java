package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ch.imlee.maturarbeit.events.gameStateEvents.GameCancelledEvent;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.views.GameSurface;

public class ServerEndGameScreen extends EndGameScreen {

    private boolean threadStarted = true;
    @Override
    protected void subRender(Canvas canvas) {
        super.subRender(canvas);
        // draw the buttons
        Paint buttonColors = new Paint();
        buttonColors.setColor(Color.RED);
        canvas.drawRect(exitButtonCoords[0], exitButtonCoords[1], exitButtonCoords[2], exitButtonCoords[3], buttonColors);
        buttonColors.setColor(Color.GREEN);
        canvas.drawRect(restartButtonCoords[0], restartButtonCoords[1], restartButtonCoords[2], restartButtonCoords[3], buttonColors);

        // draw the button texts
        buttonColors.setTextSize(64);
        buttonColors.setColor(Color.WHITE);
        canvas.drawText("Exit", 10, exitButtonCoords[1] + exitButtonCoords[4] + buttonColors.getTextSize()/2, buttonColors);
        canvas.drawText("Restart", restartButtonCoords[0] + 10, restartButtonCoords[1] + restartButtonCoords[4] + buttonColors.getTextSize()/2, buttonColors);
    }

    @Override
    protected void update() {
        super.update();
        if(isExit){
            new GameCancelledEvent().send();
            System.exit(0);
        }else if(isRestart&&!threadStarted){
            new Thread(){
                @Override
                public void run() {
                    GameSurface.restart();
                }
            }.start();
            threadStarted = true;
            GameThread.stopEndGame();
        }
    }
}