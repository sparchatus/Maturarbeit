package ch.imlee.maturarbeit.game.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.game.Map;
import ch.imlee.maturarbeit.game.Player;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.User;

/**
 * Created by Sandro on 04.06.2015.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder holder;
        private GameThread gameThread;

        public GameSurface(Context context, AttributeSet attrs) {
            super(context, attrs);
            holder = getHolder();
            holder.addCallback(this);
        }


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            gameThread = new GameThread();
            gameThread.start();
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

    public class GameThread extends Thread implements Tick{

        /**
         * The running variable determines if the game loop goes on or not.
         * @see #run()
         */
        private boolean running;

        private long lastTime;
        private long timeLeft;

        private double synchronizedTick;
        private double predictedDelay;

        private Map map;
        private User user;
        private Player[] playerArray;

        /**
         * The method called when the gameThread is started. It contains the main game loop.
         */
        @Override
        public void run() {
            //display loading screen and wait untill all information is gathered
            while(running){
                update();
                render();

                if((timeLeft = TIME_PER_TICK - (System.currentTimeMillis() - lastTime)) > 0) {
                    try {
                        sleep(timeLeft);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    synchronizedTick -= timeLeft / TIME_PER_TICK;
                    predictedDelay -= timeLeft;
                    if(predictedDelay >= 0.5){
                        //request a fresh synchronizedTick
                    }
                }
                lastTime = System.currentTimeMillis();
                synchronizedTick++;
            }
        }

        /**
         * This method calls all the update methods of particles, players, etc...
         */
        private void update(){
            for (Player player:playerArray){
                player.update();
            }
        }

        /**
         * This method does everything required for the game graphics.
         */
        private void render(){
            Canvas c = null;
            try {
                c = holder.lockCanvas(null);
                synchronized (holder) {
                    if(c!=null) {
                        c.drawColor(Color.BLACK);
                        for (Player player:playerArray){
                            player.render(c);
                        }
                    }
                }
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }
        }

        /**
         * Simple setter for the running variable.
         * @see #running
         * @param running - the value the running variable is set to
         */
        public void setRunning(boolean running){
            this.running = running;
        }
        public double getSynchronizedTick(){
            return synchronizedTick;
        }

        public Player[] getPlayerArray() {
            return playerArray;
        }
    }
}