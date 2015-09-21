package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.game.Controller.GameSurfaceController;
import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.game.GameServerThread;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

/**
 * Created by Sandro on 04.06.2015.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder holder;
    private GameThread gameThread;
    private static GameSurfaceController surfaceController;
    private static Resources rec;
    private static int width, height;

    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GameSurface", "surface gets created");
        if(StartActivity.deviceType == DeviceType.HOST) {
            gameThread = new GameServerThread(holder, getContext());
        }else{
            gameThread = new GameThread(holder, getContext());
        }
        gameThread.setRunning(true);
        gameThread.start();
        rec = getResources();
        invalidate();
        width = getWidth();
        height = getHeight();
        GameClient.setSurfaceCreated(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        invalidate();
        this.width = getWidth();
        this.height = getHeight();
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
        if (GameThread.getLoading()){
            return false;
        }
        return surfaceController.onTouchEvent(event);
    }

    public static void setup(GameSurfaceController gameSurfaceController){
        surfaceController = gameSurfaceController;
    }

    public GameThread getGameThread(){
        return gameThread;
    }

    public static Resources getRec(){
        return rec;
    }

    public static int getSurfaceWidth(){
        return width;
    }
    public static int getSurfaceHeight(){
        return height;
    }
}