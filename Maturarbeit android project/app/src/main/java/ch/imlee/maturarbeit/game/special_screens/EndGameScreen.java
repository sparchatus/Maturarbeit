package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.utils.LogView;
import ch.imlee.maturarbeit.views.GameSurface;

public class EndGameScreen {

    // the order of the coordinates is left,top,right,bottom, height / 2 (difference between bottom and top divided by 2)
    protected static final int exitButtonCoords[] = {0, GameSurface.getSurfaceHeight() * 2 / 3, GameSurface.getSurfaceWidth() / 2, GameSurface.getSurfaceHeight(), GameSurface.getSurfaceHeight() / 6};
    protected static final int restartButtonCoords[] = {GameSurface.getSurfaceWidth() / 2, GameSurface.getSurfaceHeight() * 2 / 3, GameSurface.getSurfaceWidth(), GameSurface.getSurfaceHeight(), GameSurface.getSurfaceHeight() / 6};

    private static final int OVERLAY_COLOR = 0xcc000000;
    private static SurfaceHolder holder;
    protected static boolean isHost, isExit, isRestart;

    public void endGameLoop(SurfaceHolder surfaceHolder){
        holder = surfaceHolder;
        render();
        isHost = StartActivity.deviceType == DeviceType.HOST;
        while(GameThread.getEndGameActive()){
            update();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void render(){
        Canvas c = null;
        try {
            c = holder.lockCanvas(null);
            synchronized (holder) {
                if (c != null) {
                    subRender(c);
                }
            }
            // todo:remove in end product
            LogView.render(c);
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    protected void update(){

    }

    protected void subRender(Canvas canvas){
        // some transparent black ove every thing
        canvas.drawColor(OVERLAY_COLOR);
        Paint paint = new Paint();
        paint.setTextSize(64);
        paint.setColor(Color.WHITE);
        // finish text in white
        canvas.drawText("Game Finished!", 10, 10 + paint.getTextSize(), paint);
        // winner in corresponding color
        if (GameThread.getWinningTeam() == 0) {
            paint.setColor(Color.BLUE);
            canvas.drawText("Team Blue won!", 10, 20 + 2 * paint.getTextSize(), paint);
        } else if (GameThread.getWinningTeam() == 1) {
            paint.setColor(Color.GREEN);
            canvas.drawText("Team Green won!", 10, 20 + 2 * paint.getTextSize(), paint);
        }
    }

    public static boolean onTouch(MotionEvent event){
        if(!isHost){
            return false;
        }
        return false;
    }
}
