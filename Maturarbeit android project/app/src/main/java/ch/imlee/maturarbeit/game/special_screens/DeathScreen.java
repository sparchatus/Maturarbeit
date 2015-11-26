package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;

public class DeathScreen implements Tick{

    // transparent black
    private static final int OVERLAY_COLOR = 0x99000000;
    private static final Paint textPaint = new Paint();

    // draws a transparent overlay over the normal GameSurface informing the player that he died and how long it will take until  respawn
    public static void render(Canvas canvas){
        canvas.drawColor(OVERLAY_COLOR);
        textPaint.setTextSize(64);
        textPaint.setColor(Color.RED);
        canvas.drawText("YOU DIED! (shame on you)", 20, 120, textPaint);
        textPaint.setTextSize(30);
        textPaint.setColor(Color.WHITE);
        canvas.drawText("Respawn in " + (int) (GameThread.getUser().getReviveTick() - GameThread.getSynchronizedTick()) / TICK + " seconds", 20, 200, textPaint);
    }
}
