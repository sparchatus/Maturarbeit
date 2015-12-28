package ch.imlee.maturarbeit.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Lukas on 21.10.2015.
 */
public class LogView {
    private static ArrayList<String> logs = new ArrayList<>();
    private static ArrayList<Integer> logTimes = new ArrayList<>();
    private static final int MAX_LOGS = 20;
    private static final int LOG_TIME = Tick.TICK * 10;
    private static Paint logPaint = new Paint();
    private static final float TEXT_SIZE = 24;
    private static boolean active = false;

    public static void addLog(String message){
        logs.add(0, message);
        logTimes.add(0, GameThread.getSynchronizedTick());
    }

    public static void update(){
        try {
            while (logs.size() > MAX_LOGS) {
                logs.remove(MAX_LOGS);
                logTimes.remove(MAX_LOGS);
            }
            int tick = GameThread.getSynchronizedTick();
            for (int i = logTimes.size() - 1; i >= 0; --i) {
                if (tick - LOG_TIME > logTimes.get(i)) {
                    logTimes.remove(i);
                    logs.remove(i);
                } else break;
            }
        } catch (Exception e){}
    }

    public static void render(Canvas c){
        if(active){
        logPaint.setTextSize(TEXT_SIZE);
        for(int i = 0; i < logs.size(); ++i) {
            logPaint.setColor(Color.WHITE);
            c.drawText(logs.get(i), 4, GameSurface.getSurfaceHeight() - (i + 1) * (TEXT_SIZE + 2), logPaint);
            c.drawText(logs.get(i), 6, GameSurface.getSurfaceHeight() - (i + 1) * (TEXT_SIZE + 2), logPaint);

            c.drawText(logs.get(i), 5, GameSurface.getSurfaceHeight() - (i + 1) * (TEXT_SIZE + 2) + 1, logPaint);
            c.drawText(logs.get(i), 5, GameSurface.getSurfaceHeight() - (i + 1) * (TEXT_SIZE + 2) - 1, logPaint);
            logPaint.setColor(Color.BLACK);
            logPaint.setFakeBoldText(false);
            c.drawText(logs.get(i), 5, GameSurface.getSurfaceHeight() - (i + 1) * (TEXT_SIZE + 2), logPaint);
        }
        }
    }


}
