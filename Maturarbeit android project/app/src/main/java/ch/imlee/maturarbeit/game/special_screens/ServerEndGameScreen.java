package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ch.imlee.maturarbeit.events.gameStateEvents.GameCancelledEvent;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.views.GameSurface;

public class ServerEndGameScreen extends EndGameScreen {

    private static Paint colors = new Paint();

    @Override
    protected void subRender(Canvas canvas) {
        // draw the buttons
        colors.setColor(Color.RED);
        canvas.drawRect(exitButtonCoords[0], exitButtonCoords[1], exitButtonCoords[2], exitButtonCoords[3], colors);
        colors.setColor(Color.GREEN);
        canvas.drawRect(restartButtonCoords[0], restartButtonCoords[1], restartButtonCoords[2], restartButtonCoords[3], colors);

        // draw the button texts
        colors.setTextSize(64);
        colors.setColor(Color.WHITE);
        canvas.drawText("Exit", 10, exitButtonCoords[3] / 2 - colors.getTextSize(), colors);
        canvas.drawText("Restart", restartButtonCoords[0] + 10, exitButtonCoords[3] / 2 - colors.getTextSize(), colors);
        super.subRender(canvas);

    }

    @Override
    protected void update() {
        super.update();
        if(isExit){
            GameCancelledEvent gameCancelledEvent = new GameCancelledEvent();
            gameCancelledEvent.send();
            gameCancelledEvent.handle((byte)0);
        }else if(isRestart){
            GameThread.stopEndGame();
            new Thread(){
                @Override
                public void run() {
                    GameSurface.restart();
                }
            }.start();
        }
    }
}