package ch.imlee.maturarbeit.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.entity.Fluffy;
import ch.imlee.maturarbeit.game.entity.Ghost;
import ch.imlee.maturarbeit.game.entity.LightBulb;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.Slime;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.events.Event;
import ch.imlee.maturarbeit.game.events.EventReceiver;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLoadedEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.views.GameSurface;
import ch.imlee.maturarbeit.game.views.ParticleButton;
import ch.imlee.maturarbeit.game.views.SkillButton;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Sandro on 04.07.2015.
 */
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
    private ArrayList<Particle> particleList = new ArrayList<>();
    private LightBulb[] lightBulbArray;
    private Bitmap loadingScreen0, loadingScreen1,loadingScreen2,loadingScreen3;
    private SurfaceHolder holder;

    public GameThread(SurfaceHolder holder){
        this.holder = holder;
    }
    /**
     * The method called when the gameThread is started. It contains the main game loop.
     */
    @Override
    public void run() {
        GameClient.setSurfaceCreated(true);
        loading = true;
        particleButton = GameClient.getParticleButton();
        skillButton = GameClient.getSkillButton();
        loadingScreen0 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_0);
        loadingScreen1 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_1);
        loadingScreen2 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_2);
        loadingScreen3 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_3);
        displayLoadingScreen();
        particleButton.setUser(user);
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
        for(Queue<Event> eventQueue:EventReceiver.events){
            while(!eventQueue.isEmpty()){
                eventQueue.remove().apply(this);
            }
        }
        // TODO the Host should handle the particle collision
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
    }

    public void setStartData(GameStartEvent startData){
        Log.d("initialization", "Start data is initialized");
        //if (!loading)return;
        map = new Map(GameSurface.getRec(), startData.getMapID());
        playerArray = new Player[startData.getPlayerCount()];
        switch (startData.getPlayerTypes().get(startData.getUserID())){
            case FLUFFY:user = new Fluffy(map.getStartX(startData.getTeams().get(startData.getUserID())), map.getStartY(startData.getTeams().get(startData.getUserID())), map, this, startData.getTeams().get(startData.getUserID()), startData.getUserID());
                break;
            case GHOST:user = new Ghost(map.getStartX(startData.getTeams().get(startData.getUserID())), map.getStartY(startData.getTeams().get(startData.getUserID())), map, this, startData.getTeams().get(startData.getUserID()), startData.getUserID());
                break;
            case SLIME:user = new Slime(map.getStartX(startData.getTeams().get(startData.getUserID())), map.getStartY(startData.getTeams().get(startData.getUserID())), map, this, startData.getTeams().get(startData.getUserID()), startData.getUserID());
                break;
            case NULL: Log.d("fail", "user PlayerType is NULL");
        }
        for (byte i = 0; i < startData.getPlayerCount(); i++){
            if (i == startData.getUserID()){
                Log.d("user", "The user is being initialized.");
                playerArray[i] = user;
            }else {
                Log.d("player", "A Player is being initialized");
                playerArray[i] = new Player(map.getStartX(startData.getTeams().get(i)), map.getStartY(startData.getTeams().get(i)), startData.getPlayerTypes().get(i), map, this, startData.getTeams().get(i), i, user);
            }
        }
        lightBulbArray = new LightBulb[2];
        if(StartActivity.deviceType == DeviceType.CLIENT) {
            new GameLoadedEvent().send();
        } else{
            WaitUntilLoadedThread.incrementReady();
        }
    }

    public void endLoading(){
        synchronizedTick = 0;
        loading = false;
    }

    public void addParticle(Particle newParticle){
        particleList.add(newParticle);
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

    public User getUser(){
        return user;
    }

    public Player[] getPlayerArray() {
        return playerArray;
    }

    public void setHolder(SurfaceHolder holder){
        this.holder = holder;
    }

    public LightBulb[] getLightBulbArray() {
        return lightBulbArray;
    }
}