package ch.imlee.maturarbeit.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;

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

    // true while the game is going on and nobody has yet won
    private static boolean running;

    // true before the game starts
    // while this is true the LoadingScreen is displayed
    private static boolean loading;

    // true as soon as a team has won
    private static boolean endGameActive = false;

    // time when the last Update ended
    private static long lastTime;
    // difference between Tick.TIME_PER_TICK and the difference between System.currentTimeMillis() and lastTime
    // timeLeft = TIME_PER_TICK - (System.currentTimeMillis() - lastTime)
    private static long timeLeft;

    // the Game's own time system. it counts the number of updates that have passed since the start of the game
    protected static int synchronizedTick;

    // -1 means that nobody has won yet
    private static byte winningTeam = -1;


    public static LightBulb[] lightBulbArray;
    // used to draw things onto the GameSurface
    private static SurfaceHolder holder;
    protected static Map map;
    protected static User user;
    protected static Player[] playerArray;
    // SlimeTrails are produced by the Slime
    protected static ArrayList<SlimeTrail> slimeTrailList;
    // this array contains an ArrayList for every Player in the game
    // this way every Player can manage the Particle IDs of the Particles he spawned by himself
    protected static ArrayList<Particle>[] particleListArray;
    // Sweets are produced by the GameServerThread
    public static ArrayList<Sweet> sweets;
    // all the Sweets that have to be removed in the next update
    public static Set<Integer> sweetsToRemove;


    public GameThread(SurfaceHolder holder){
        this.holder = holder;
        // reset all the static data to make the GameThread ready to start again
        endGameActive = false;
        winningTeam = -1;
        lightBulbArray = null;
        map = null;
        user = null;
        playerArray = null;
        slimeTrailList = null;
        particleListArray = null;
        sweets = null;
        sweetsToRemove = null;
    }

    // the method is called when the gameThread is started. It contains the main game loop.
    @Override
    public void run() {
        // making sure that those important variables are both true
        loading = true;
        running = true;
        // display the LoadingScreen
        LoadingScreen.loadingLoop(holder);

        LogView.addLog("main loop starting");
        // this loop is repeating as long as the game is going on
        while (running) {
            // actualize the date
            update();

            //draw the data
            render();

            // making the cycles f the loop periodic
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
        LogView.addLog("main loop quit");

        // the Server has his own EndGameScreen where he can restart or exit the game
        if (StartActivity.deviceType == DeviceType.HOST) {
            // draw the EndGameScreen of the Server
            new ServerEndGameScreen().endGameLoop(holder);
        } else {
            // draw the EndGameScreen of the Client
            new EndGameScreen().endGameLoop(holder);
        }
        Log.e("GameThread run", "exiting run");
    }


    // this method applies GameActionEvents and calls all the update methods of Particles, Players, etc...
    protected void update(){
        // apply all collected Events
        for(Queue<Event> eventQueue:EventReceiver.events){
            while(!eventQueue.isEmpty()){
                try {
                    eventQueue.remove().apply();
                } catch (Exception e){
                    // an Exception can be thrown when the game is restarting but the device tries to apply events
                    e.printStackTrace();
                }
            }
        }

        // remove the Sweets marked for removal
        for(int i = 0; i < sweets.size(); ++i){
            if(sweetsToRemove.contains(sweets.get(i).getID())){
                sweets.remove(i);
            }
        }

        // apply the User commands issued since the last tick
        JoystickSurface.update();
        GameSurface.update();

        // remove all the SlimeTrails that have been existing long enough
        for(int i = 0; i < slimeTrailList.size(); ++i){
            slimeTrailList.get(i).update();
            if(slimeTrailList.get(i).removable){
                slimeTrailList.remove(i);
                --i;
            }
        }

        // update all the Players including the User
        for (Player player:playerArray){
            player.update();
        }

        // move all the Particles
        for (ArrayList<Particle> particleList:particleListArray) {
            for (Particle particle:particleList) {
                // if a Particle is removed from the ArrayList there is a null reference Object in the ArrayList until it is replaced with a new Particle
                if (particle != null) {
                    particle.update();
                }
            }
        }

        // move the LightBulb
        for (LightBulb lightBulb:lightBulbArray){
            lightBulb.update();
        }

        // move the Logs and remove old Logs
        LogView.update();
    }


    // this method does everything required for the game's graphics.
    private void render(){
        Canvas c = null;

        try {
            // tell the System that it is not allowed to draw the Canvas while we are changing it
            // we get the Canvas of the GameSurface in return
            c = holder.lockCanvas(null);
            // Google recommends to synchronize the drawing process with the SurfaceHolder
            synchronized (holder) {
                // if we got a Canvas we can draw
                if (c != null) {
                    // erase the all drawing
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

                    if (user.getDead()) {
                        DeathScreen.render(c);
                    }

                    LogView.render(c);
                }
            }
        } finally {
            if (c != null) {
                // if we locked the Canvas before we have to unlock it now and give the System the Canvas to draw back
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    // start the actual game loop and start the game time
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
        // if the particle ID is already existent in the List the Particle can be set directly
        if (pSE.PARTICLE_ID < particleListArray[pSE.getSenderID()].size()) {
            particleListArray[pSE.getSenderID()].set(pSE.PARTICLE_ID, new Particle(pSE.X_COORDINATE, pSE.Y_COORDINATE, pSE.ANGLE, playerArray[pSE.getSenderID()].TEAM, pSE.SPAWN_TICK, pSE.PARTICLE_ID));
        }
        // otherwise the List has to be extended which is done by adding the Particle at the end
        else{
            particleListArray[pSE.getSenderID()].add(new Particle(pSE.X_COORDINATE, pSE.Y_COORDINATE, pSE.ANGLE, playerArray[pSE.getSenderID()].TEAM, pSE.SPAWN_TICK, pSE.PARTICLE_ID));
        }
    }

    //
    public static void removeParticle(ParticleHitEvent pHE){
        // this would throw an IndexOutOfBoundException if the Events get applied in the wrong order
        // namely when the ParticleShotEvent creating the particle somehow reaches a Client after the ParticleHitEvent that is sent by the Server removing the Particle again
        if(pHE.PARTICLE_ID < particleListArray[pHE.PLAYER_SOURCE_ID].size())
        particleListArray[pHE.PLAYER_SOURCE_ID].set(pHE.PARTICLE_ID, null);
    }

    public static int getCurrentFreeParticleID(){
        // searches for free spaces in the list
        for (int i = 0; i < particleListArray[user.getID()].size(); i++){
            if (particleListArray[user.getID()].get(i) == null){
                return i;
            }
        }
        // if there is no free space the ArrayList has to be increased in size
        return particleListArray[user.getID()].size();
    }

    public static void activateEndGame(){
        endGameActive = true;
    }

    public static void stopEndGame(){endGameActive = false;}

    public static void setRunning(boolean isRunning){
        running = isRunning;
    }

    public static void setWinningTeam(byte team){
        winningTeam = team;
    }

    // when changing the time of the game nothing has to be recalculated
    // only the Particles' positions could be slightly wrong but their life time is too short to be worth correcting the difference
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