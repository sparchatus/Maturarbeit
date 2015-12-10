package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.utils.LogView;
import ch.imlee.maturarbeit.views.GameSurface;

public class LoadingScreen implements Tick{

    private static int i, halfSurfaceHeight, thirdSurfaceWidth, ninthSurfaceWidth;
    private static final int TEXT_SIZE = 64;

    private static long lastTime, timeLeft;

    private static float textWidth = 0;

    private static String loadingText = "Loading...";
    private static Paint textPaint;

    private static SurfaceHolder holder;
    private static Bitmap fluffy, slime, ghost;

    public static void loadingLoop(SurfaceHolder surfaceHolder) {

        // initializing variables
        i = 3;
        halfSurfaceHeight = GameSurface.getSurfaceHeight() / 2;
        thirdSurfaceWidth = GameSurface.getSurfaceWidth() / 3;
        ninthSurfaceWidth = thirdSurfaceWidth / 3;
        holder = surfaceHolder;

        // setting up the tex and pictures that will be drawn
        textPaint = new Paint();

        //white
        textPaint.setColor(0xffffffff);
        textPaint.setTextSize(TEXT_SIZE);
        float[] textWidths = new float[loadingText.length()];
        textPaint.getTextWidths(loadingText, textWidths);
        for (int j = 0; j < loadingText.length(); ++j) {
            textWidth += textWidths[i];
        }
        fluffy = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.fluffy), ninthSurfaceWidth, ninthSurfaceWidth, false);
        slime = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.slime), ninthSurfaceWidth, ninthSurfaceWidth, false);
        ghost = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ghost), ninthSurfaceWidth, ninthSurfaceWidth, false);

        // end of content setup
        while (GameThread.getLoading()) {
            //todo:remove in end product
            LogView.update();
            render();
            i--;
            if (i < 0) i = 3;

            if ((timeLeft = TIME_PER_LOADING_TICK - (System.currentTimeMillis() - lastTime)) > 0) {
                try {
                    Thread.sleep(timeLeft);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lastTime = System.currentTimeMillis();
        }
    }

    private static void render(){
        Canvas c = null;
        try {
            c = holder.lockCanvas(null);
            synchronized (holder) {
                if (c != null) {
                    c.drawColor(Color.BLACK);
                    c.drawColor(0x99ff0000);
                    switch (i) {
                        case 0:
                            c.drawBitmap(fluffy, thirdSurfaceWidth + 2 * ninthSurfaceWidth, halfSurfaceHeight, null);
                        case 1:
                            c.drawBitmap(slime, thirdSurfaceWidth + ninthSurfaceWidth, halfSurfaceHeight, null);
                        case 2:
                            c.drawBitmap(ghost, thirdSurfaceWidth, halfSurfaceHeight, null);
                        default:
                            c.drawText(loadingText, GameSurface.getSurfaceWidth() / 2 - textWidth / 2, halfSurfaceHeight - TEXT_SIZE, textPaint);
                    }
                    LogView.render(c);
                }
            }
            // todo:remove in end product
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    public static void setRestart(){
        loadingText = "Restarting...";
    }
}
