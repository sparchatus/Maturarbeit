package ch.imlee.maturarbeit.game.views;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.entity.Ghost;
import ch.imlee.maturarbeit.game.Map;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.entity.User;

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
            gameThread.setRunning(true);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameThread.user.onTouch(event);
    }

    public class GameThread extends Thread implements Tick{

        /**
         * The running variable determines if the game loop goes on or not.
         * @see #run()
         */
        private boolean running;

        /**
         *
         */
        private long lastTime;
        private long timeLeft;

        private double synchronizedTick;
        private double predictedDelay;

        private ParticleButton particleButton;
        private SkillButton skillButton;
        private Map map;
        private User user;
        private Player[] playerArray;
        private ArrayList<Particle> particleList = new ArrayList();

        /**
         * The method called when the gameThread is started. It contains the main game loop.
         */
        @Override
        public void run() {
            //display loading screen and wait until all information is gathered
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            map = new Map(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.test_map_2, options));
            user = new Ghost(8.5f, 5.5f, PlayerType.GHOST, map, this, (byte) 1, (byte) 0,user);
            playerArray = new Player[1];
            playerArray[0] = new Player(27.5f, 24.5f, PlayerType.GHOST, map, this, (byte) 2, (byte) 1, user);
            particleButton = GameClient.particleButton;
            particleButton.setUser(user);
            skillButton = GameClient.skillButton;
            skillButton.setUser(user);
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
            user.update();
            for (Player player:playerArray){
                player.update();
            }
            for (Particle particle:particleList) {
                particle.update();
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
                        map.render(c, user);
                        for (Particle particle:particleList){
                            c = particle.render(c);
                        }
                        for (Player player:playerArray){
                            c = player.render(c);
                        }
                        c = user.render(c);
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
        public void addParticle(Particle newParticle){
            particleList.add(newParticle);
        }
    }
}