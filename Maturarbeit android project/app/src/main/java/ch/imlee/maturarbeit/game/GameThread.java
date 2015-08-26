package ch.imlee.maturarbeit.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.Controller.GameSurfaceController;
import ch.imlee.maturarbeit.game.Controller.JoystickController;
import ch.imlee.maturarbeit.game.Sound.BackgroundMusic;
import ch.imlee.maturarbeit.game.entity.Fluffy;
import ch.imlee.maturarbeit.game.entity.Ghost;
import ch.imlee.maturarbeit.game.entity.LightBulb;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.Slime;
import ch.imlee.maturarbeit.game.entity.SlimeTrail;
import ch.imlee.maturarbeit.game.entity.Sweet;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.events.Event;
import ch.imlee.maturarbeit.game.events.EventReceiver;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLoadedEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.views.GameSurface;
import ch.imlee.maturarbeit.game.views.JoystickSurface;
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
    private static boolean running;
    private static boolean loading;
    private static long lastTime;
    private static long timeLeft;

    private static double synchronizedTick;
    private static double predictedDelay;

    private static ParticleButton particleButton;
    private static SkillButton skillButton;
    protected static Map map;
    protected static User user;
    protected static Player[] playerArray;
    protected static ArrayList<SlimeTrail> slimeTrailList = new ArrayList<>();
    protected static ArrayList<Particle> particleList = new ArrayList<>();
    protected static ArrayList<Integer> freeParticleIDs = new ArrayList<>();
    public static ArrayList<Sweet> sweets = new ArrayList<>();
    public static Set<Integer> sweetsToRemove = new HashSet<>();
    private static LightBulb[] lightBulbArray;
    private static SurfaceHolder holder;
    private static BackgroundMusic backgroundMusic;
    private static Context context;
    private static JoystickController joystickController;
    private static GameSurfaceController gameSurfaceController;

    public GameThread(SurfaceHolder holder, Context context){
        this.holder = holder;
        this.context = context;
    }
    /**
     * The method called when the gameThread is started. It contains the main game loop.
     */
    @Override
    public void run() {
        loading = true;
        particleButton = GameClient.getParticleButton();
        skillButton = GameClient.getSkillButton();
        backgroundMusic = new BackgroundMusic();
        displayLoadingScreen();
        backgroundMusic.start();
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
                    Log.d("main loop", "lag occurred");
                    //todo: request a fresh synchronizedTick
                    //todo: on receiving a new Tick, the old Data has to be reviewed
                }
            }
            lastTime = System.currentTimeMillis();
            synchronizedTick++;
        }
        backgroundMusic.stop();
    }

    /**
     * This method calls all the update methods of particles, players, etc...
     */
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
        joystickController.update();
        gameSurfaceController.update();
        for (Player player:playerArray){
            player.update();
        }
        for(int i = 0; i < slimeTrailList.size(); ++i){
            slimeTrailList.get(i).update();
            if(slimeTrailList.get(i).removable){
                slimeTrailList.remove(i);
                --i;
            }
        }
        for (Particle particle:particleList) {
            particle.update();
        }
        for (LightBulb lightBulb: lightBulbArray){
            lightBulb.update();
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
                    for(SlimeTrail slimeTrail:slimeTrailList){
                        slimeTrail.render(c);
                    }
                    for(Sweet sweet:sweets){
                        sweet.render(c);
                    }
                    for (Particle particle:particleList){
                        particle.render(c);
                    }
                    for (Player player:playerArray){
                        player.render(c);
                    }

                    for (LightBulb lightBulb:lightBulbArray){
                        lightBulb.render(c);
                    }
                    map.renderMinimap(c);
                    //todo:display pause button
                }
            }
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    private void displayLoadingScreen(){
        Bitmap loadingScreen0, loadingScreen1,loadingScreen2,loadingScreen3;
        loadingScreen0 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_0);
        loadingScreen1 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_1);
        loadingScreen2 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_2);
        loadingScreen3 = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.loading_screen_3);
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

    private Canvas displayPauseButton(Canvas c){
        //TODO: add pause button and functionality
        return c;
    }

     public  void setStartData(GameStartEvent startData){
         Log.d("initialization", "Start data is initialized");
         map = new Map(GameSurface.getRec(), startData.getMapID());
         playerArray = new Player[startData.getPlayerCount()];
         for (byte i = 0; i < startData.getPlayerCount(); i++){
            if (i == startData.getUserID()){
                Log.d("user", "The user is being initialized.");
                switch (startData.getPlayerTypes().get(i)){
                    case FLUFFY:user = new Fluffy(map, startData.getTeams().get(i), i);
                        break;
                    case GHOST:user = new Ghost(map, startData.getTeams().get(i), i);
                        break;
                    case SLIME:user = new Slime(map, startData.getTeams().get(i), i);
                        break;
                    case NULL: Log.d("fail", "user PlayerType is NULL");
                }
                playerArray[i] = user;
            }else {
                Log.d("player", "A Player is being initialized");
                playerArray[i] = new Player(startData.getPlayerTypes().get(i), map, startData.getTeams().get(i), i);
            }
         }
         lightBulbArray = new LightBulb[2];
         lightBulbArray[0] = new LightBulb((byte) 0, map, (byte) 0);
         lightBulbArray[1] = new LightBulb((byte) 1, map, (byte) 1);
         //todo: remove when created a new controller
         joystickController = new JoystickController(user, JoystickSurface.getJoystickSurfaceWidth(), JoystickSurface.getJoystickSurfaceHeight());
         gameSurfaceController = new GameSurfaceController(user, GameSurface.getSurfaceWidth(), GameSurface.getSurfaceHeight());
         if(StartActivity.deviceType == DeviceType.CLIENT) {
            new GameLoadedEvent().send();
         } else {
            WaitUntilLoadedThread.incrementReady();
         }
     }

    public static void endLoading(){
        synchronizedTick = 0;
        loading = false;
        particleButton.setUser(user);
        skillButton.setUser(user);
    }

    public static void addParticle(Particle newParticle){
        particleList.add(newParticle.getID(), newParticle);
    }

    public static void removeParticle(int ID){
        for (Particle particle:particleList){
            if (particle.getID() == ID){
                particleList.remove(particle);
                break;
            }
        }
        freeParticleIDs.add(ID, ID);
    }

    public static void playerHit(byte playerID){
        if (playerID < 0)return;
        playerArray[playerID].particleHit();
    }

    public static void addSlimeTrail(SlimeTrail slimeTrail){
        slimeTrailList.add(slimeTrail);
        Log.d("slime", "SlimeTrail added");
    }

    /**
     * Simple setter for the running variable.
     * @see #running
     * @param running - the value the running variable is set to
     */
    public void setRunning(boolean running){
        this.running = running;
    }

    public static double getSynchronizedTick(){
        return synchronizedTick;
    }

    public static User getUser(){
        return user;
    }

    public static Player[] getPlayerArray() {
        return playerArray;
    }

    public  void setHolder(SurfaceHolder holder){
        this.holder = holder;
    }

    public static LightBulb[] getLightBulbArray() {
        return lightBulbArray;
    }

    public static ArrayList<Particle> getParticleList(){
        return particleList;
    }

    public static ArrayList<SlimeTrail> getSlimeTrailList(){
        return slimeTrailList;
    }
}