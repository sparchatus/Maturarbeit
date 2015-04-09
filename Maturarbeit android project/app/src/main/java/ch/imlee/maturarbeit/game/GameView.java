package ch.imlee.maturarbeit.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.R;

/**
 * Created by Sandro on 09.04.2015.
 */
public class GameView extends SurfaceView{

    private SurfaceHolder surfaceHolder;

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            public void surfaceCreated(SurfaceHolder holder) {
                Canvas theCanvas = surfaceHolder.lockCanvas(null);
                displayLoadingScreen(theCanvas);
                surfaceHolder.unlockCanvasAndPost(theCanvas);
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }
        });
      //  bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        //draw all the things
    }

    protected void displayLoadingScreen(Canvas canvas){
        canvas.drawColor(Color.BLACK);
        canvas.drawText("Loading...", 100, 100, null);
    }
}

