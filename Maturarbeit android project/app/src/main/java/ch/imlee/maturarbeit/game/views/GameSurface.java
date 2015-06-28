package ch.imlee.maturarbeit.game.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.entity.Fluffy;
import ch.imlee.maturarbeit.game.entity.Ghost;
import ch.imlee.maturarbeit.game.entity.Slime;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLoadedEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.map.Map;
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

    public GameThread getGameThread(){
        return gameThread;
    }

    public class GameThread extends Thread implements Tick{

        /**
         * The running variable determines if the game loop goes on or not.
         * @see #run()
         */
        private boolean running;
        private boolean loading;

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
        private Bitmap loadingScreen0, loadingScreen1,loadingScreen2,loadingScreen3;

        /**
         * The method called when the gameThread is started. It contains the main game loop.
         */
        @Override
        public void run() {
            loading = true;
            particleButton = GameClient.getParticleButton();
            particleButton.setUser(user);
            skillButton = GameClient.getSkillButton();
            skillButton.setUser(user);
            loadingScreen0 = BitmapFactory.decodeResource(getResources(), R.drawable.loading_screen_0);
            loadingScreen1 = BitmapFactory.decodeResource(getResources(), R.drawable.loading_screen_1);
            loadingScreen2 = BitmapFactory.decodeResource(getResources(), R.drawable.loading_screen_2);
            loadingScreen3 = BitmapFactory.decodeResource(getResources(), R.drawable.loading_screen_3);
            displayLoadingScreen();
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

        private void displayLoadingScreen(){
            int i = 0;
            while(loading) {
                Canvas c = null;
                try {
                    c = holder.lockCanvas(null);
                    synchronized (holder) {
                        if (c != null) {
                            c.drawColor(Color.BLACK);
                            switch (i) {
                                case 0:
                                    c.drawBitmap(loadingScreen0, 0, 0, null);
                                    break;
                                case 1:
                                    c.drawBitmap(loadingScreen1, 0, 0, null);
                                    break;
                                case 2:
                                    c.drawBitmap(loadingScreen2, 0, 0, null);
                                    break;
                                case 3:
                                    c.drawBitmap(loadingScreen3, 0, 0, null);
                                    break;
                            }
                        }
                    }
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
                i++;
                if (i > 3) i = 0;
                if((timeLeft = TIME_PER_LOADING_TICK - (System.currentTimeMillis() - lastTime)) > 0) {
                    try {
                        sleep(timeLeft);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lastTime = System.currentTimeMillis();
            }
            GameClient.getSkillButton().setVisibility(VISIBLE);
            GameClient.getParticleButton().setVisibility(VISIBLE);
        }

        public void setStartData(GameStartEvent startData){
            if (!loading)return;
            map = new Map(getResources(), startData.getMapID());
            playerArray = new Player[startData.getPlayerCount()];
            for (byte i = 0; i < startData.getPlayerCount(); i++){
                if (i == startData.getUserID()){
                    switch (startData.getPlayerTypes().get(i)){
                        case FLUFFY:playerArray[i] = new Fluffy(map.getStartX(startData.getTeams().get(i)), map.getStartY(startData.getTeams().get(i)), startData.getPlayerTypes().get(i), map, this, startData.getTeams().get(i), i);
                            break;
                        case GHOST:playerArray[i] = new Ghost(map.getStartX(startData.getTeams().get(i)), map.getStartY(startData.getTeams().get(i)), startData.getPlayerTypes().get(i), map, this, startData.getTeams().get(i), i);
                            break;
                        case SLIME:playerArray[i] = new Slime(map.getStartX(startData.getTeams().get(i)), map.getStartY(startData.getTeams().get(i)), startData.getPlayerTypes().get(i), map, this, startData.getTeams().get(i), i);
                    }
                }else {
                    playerArray[i] = new Player(map.getStartX(startData.getTeams().get(i)), map.getStartY(startData.getTeams().get(i)), startData.getPlayerTypes().get(i), map, this, startData.getTeams().get(i), i, user);
                }
            }
            //TODO dont know if working
            new GameLoadedEvent().send();
        }

        public void endLoading(){
            synchronizedTick = 0;
            loading = false;
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