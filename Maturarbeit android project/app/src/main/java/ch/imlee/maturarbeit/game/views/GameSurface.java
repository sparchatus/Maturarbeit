package ch.imlee.maturarbeit.game.views;
//TODO comment everything
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 04.06.2015.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder holder;
        private GameThread gameThread;
        private static Resources rec;

        public GameSurface(Context context, AttributeSet attrs) {
            super(context, attrs);
            holder = getHolder();
            holder.addCallback(this);
        }


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d("tag", "surface gets created");
            gameThread = new GameThread(holder);
            gameThread.setRunning(true);
            gameThread.start();
            rec = getResources();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            gameThread.setRunning(false);
            boolean retry = true;
            while(retry){
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameThread.getUser().onTouch(event);
    }

    public GameThread getGameThread(){
        return gameThread;
    }

    public static Resources getRec(){
        return rec;
    }
}