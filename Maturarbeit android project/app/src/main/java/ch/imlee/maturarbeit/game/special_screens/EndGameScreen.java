package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.utils.LogView;

public class EndGameScreen {

    private static final int OVERLAY_COLOR = 0xcc000000;
    private static SurfaceHolder holder;

    public void endGameLoop(SurfaceHolder surfaceHolder){
        holder = surfaceHolder;
        render();
        while(GameThread.getEndGameActive()){
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

    protected void subRender(Canvas canvas){
        canvas.drawColor(OVERLAY_COLOR);
        Paint paint = new Paint();
        paint.setTextSize(64);
        paint.setColor(Color.WHITE);
        canvas.drawText("Game Finished!", 10, 10 + paint.getTextSize(), paint);
        if (GameThread.getWinningTeam() == 0) {
            paint.setColor(Color.BLUE);
            canvas.drawText("Team Blue won!", 10, 20 + 2 * paint.getTextSize(), paint);
        } else if (GameThread.getWinningTeam() == 1) {
            paint.setColor(Color.GREEN);
            canvas.drawText("Team Green won!", 10, 20 + 2 * paint.getTextSize(), paint);
        }
    }

    public static boolean onTouch(MotionEvent event){
        return false;
    }
}
