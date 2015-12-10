package ch.imlee.maturarbeit.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import ch.imlee.maturarbeit.events.gameActionEvents.ErrorEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.ParticleHitEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.ParticleShotEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.PlayerMotionEvent;
import ch.imlee.maturarbeit.game.entity.LightBulb;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.SlimeTrail;
import ch.imlee.maturarbeit.game.entity.Sweet;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.events.Event;
import ch.imlee.maturarbeit.events.EventReceiver;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.special_screens.DeathScreen;
import ch.imlee.maturarbeit.game.special_screens.EndGameScreen;
import ch.imlee.maturarbeit.game.special_screens.LoadingScreen;
import ch.imlee.maturarbeit.game.special_screens.ServerEndGameScreen;
import ch.imlee.maturarbeit.utils.LogView;
import ch.imlee.maturarbeit.views.GameSurface;
import ch.imlee.maturarbeit.views.JoystickSurface;
import ch.imlee.maturarbeit.views.MiniMap;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

public class GameThread extends Thread implements Tick{

    private static boolean running;
    private static boolean loading;
    private static boolean endGameActive = false;

    private static long lastTime;
    private static long timeLeft;

    protected static int synchronizedTick;

    private static byte winningTeam = -1;

    public static LightBulb[] lightBulbArray;
    private static SurfaceHolder holder;
    protected static Map map;
    protected static User user;
    protected static Player[] playerArray;
    protected static ArrayList<SlimeTrail> slimeTrailList = new ArrayList<>();
    protected static ArrayList<Particle>[] particleListArray;
    public static ArrayList<Sweet> sweets = new ArrayList<>();
    public static Set<Integer> sweetsToRemove = new HashSet<>();


    public GameThread(SurfaceHolder holder){
        this.holder = holder;
    }

    // the method is called when the gameThread is started. It contains the main game loop.
    @Override
    public void run() {
        loading = true;
        //todo:sound rework
        LoadingScreen.loadingLoop(holder);
        try {
            while (running) {
                update();
                render();
                if ((timeLeft = TIME_PER_TICK - (System.currentTimeMillis() - lastTime)) > 0) {
                    try {
                        sleep(timeLeft);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lastTime = System.currentTimeMillis();
                ++synchronizedTick;
            }
            Log.e("GameThread run", "main loop quit");
            if (StartActivity.deviceType == DeviceType.HOST) {
                new ServerEndGameScreen().endGameLoop(holder);
            } else {
                new EndGameScreen().endGameLoop(holder);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("GameThread run", "exiting run");
    }


    // this method applies events and calls all the update methods of particles, players, etc...
    protected void update(){
        for(Queue<Event> eventQueue:EventReceiver.events){
            while(!eventQueue.isEmpty()){
                eventQueue.remove().apply();
            }
        }

        for(int i = 0; i < sweets.size(); ++i){
            if(sweetsToRemove.contains(sweets.get(i).getID())){
                sweets.remove(i);
            }
        }

        // apply the User commands issued since the last tick
        JoystickSurface.update();
        GameSurface.update();

        for(int i = 0; i < slimeTrailList.size(); ++i){
            slimeTrailList.get(i).update();
            if(slimeTrailList.get(i).removable){
                slimeTrailList.remove(i);
                --i;
            }
        }
        for (Player player:playerArray){
            player.update();
        }

        for (ArrayList<Particle> particleList:particleListArray) {
            for (Particle particle:particleList) {
                if (particle != null) {
                    particle.update();
                }
            }
        }

        for (LightBulb lightBulb:lightBulbArray){
            lightBulb.update();
        }
        //TODO: remove logging stuff when not needed anymore
        LogView.update();
    }

    // this method does everything required for the game graphics.
    private void render(){
        Canvas c = null;
        try {
            c = holder.lockCanvas(null);
            synchronized (holder) {
                if (c != null) {
                    c.drawColor(Color.BLACK);
                    map.render(c);
                    for (SlimeTrail slimeTrail : slimeTrailList) {
                        slimeTrail.render(c);
                    }
                    for (Sweet sweet : sweets) {
                        sweet.render(c);
                    }
                    for (ArrayList<Particle> particleList : particleListArray) {
                        for (Particle particle : particleList) {
                            if (particle != null) {
                                particle.render(c);
                            }
                        }
                    }
                    for (Player player : playerArray) {
                        player.render(c);
                    }

                    for (LightBulb lightBulb : lightBulbArray) {
                        lightBulb.render(c);
                    }
                    JoystickSurface.render(c);
                    GameSurface.render(c);
                    MiniMap.render(c);
                    //todo:display pause button
                    if (user.getDead()) {
                        DeathScreen.render(c);
                    }
                }else{
                    new ErrorEvent("GameThread render()", "canvas is null").send();
                    new ErrorEvent("GameThread render()", "canvas is null").apply();
                }
                LogView.render(c);
            }
            // todo: remove in end product
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    public static void endLoading() {
        synchronizedTick = 0;
        loading = false;
        new PlayerMotionEvent(user).send();
    }

    public static void addSlimeTrail(SlimeTrail slimeTrail) {
        slimeTrailList.add(slimeTrail);
        Log.v("slime", "SlimeTrail added");
    }

    public static void addParticle(ParticleShotEvent pSE){
        if (pSE.PARTICLE_ID < particleListArray[pSE.getSenderID()].size()) {
            particleListArray[pSE.getSenderID()].set(pSE.PARTICLE_ID, new Particle(pSE.X_COORDINATE, pSE.Y_COORDINATE, pSE.ANGLE, playerArray[pSE.getSenderID()].TEAM, pSE.SPAWN_TICK, pSE.PARTICLE_ID));
        }else{
            particleListArray[pSE.getSenderID()].add(new Particle(pSE.X_COORDINATE, pSE.Y_COORDINATE, pSE.ANGLE, playerArray[pSE.getSenderID()].TEAM, pSE.SPAWN_TICK, pSE.PARTICLE_ID));
        }
    }

    public static void removeParticle(ParticleHitEvent pHE){
        particleListArray[pHE.PLAYER_SOURCE_ID].set(pHE.PARTICLE_ID, null);
    }

    public static int getCurrentFreeParticleID(){
        for (int i = 0; i < particleListArray[user.getID()].size(); i++){
            if (particleListArray[user.getID()].get(i) == null){
                return i;
            }
        }
        return particleListArray[user.getID()].size();
    }

    public static void activateEndGame(){
        endGameActive = true;
    }

    public void stopEndGame(){endGameActive = false;}

    public static void setRunning(boolean isRunning){
        running = isRunning;
    }

    public static void setWinningTeam(byte team){
        winningTeam = team;
    }

    public static void setSynchronizedTick(int newSynchronizedTick){
        synchronizedTick = newSynchronizedTick;
    }

    public static int getSynchronizedTick(){
        return synchronizedTick;
    }

    public static User getUser(){
        return user;
    }

    public static Player[] getPlayerArray() {
        return playerArray;
    }

    public static LightBulb[] getLightBulbArray() {
        return lightBulbArray;
    }

    public static ArrayList<SlimeTrail> getSlimeTrailList(){
        return slimeTrailList;
    }

    public static  boolean getLoading(){
        return  loading;
    }

    public static SurfaceHolder getHolder() {
        return holder;
    }

    public static boolean getEndGameActive(){
        return endGameActive;
    }

    public static byte getWinningTeam() {
        return winningTeam;
    }
}