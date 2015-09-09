package ch.imlee.maturarbeit.game;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;

import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.Sweet;
import ch.imlee.maturarbeit.events.gameActionEvents.ParticleHitEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.SweetSpawnEvent;

/**
 * Created by Sandro on 14.08.2015.
 */
public class GameServerThread extends GameThread{

    private static int currentParticleID = 0;
    private static final int SWEET_SPAWN_RATE = Tick.TICK * 10;
    private static int lastSweetSpawn = 0;
    private static int currentSweetId = 0;

    public GameServerThread(SurfaceHolder holder, Context context) {
        super(holder, context);
    }

    @Override
    protected void update() {
        super.update();
        if(lastSweetSpawn + SWEET_SPAWN_RATE <= getSynchronizedTick()){
            spawnSweet();
        }
        for (Particle particle:particleList) {
            if(particle != null) {
                // the particles can get an X or Y coordinate below zero, so we have to check that first to not get an ArrayIndexOutOfBoundsException
                if ((int) particle.getXCoordinate() < 0 || (int) particle.getYCoordinate() >= map.TILES_IN_MAP_HEIGHT || (int) particle.getXCoordinate() >= map.TILES_IN_MAP_WIDTH || (int) particle.getYCoordinate() < 0 || map.getSolid((int) particle.getXCoordinate(), (int) particle.getYCoordinate())) {
                    removeParticle(particle.getID());
                   continue;
                }
                for (Player player : playerArray) {
                    if (player.TEAM != particle.TEAM && Math.pow(player.getXCoordinate() - particle.getXCoordinate(), 2) +
                            Math.pow(player.getYCoordinate() - particle.getYCoordinate(), 2) <= Math.pow(player.getPlayerRadius(), 2)) {
                        new ParticleHitEvent(particle.getID(), player.getID(), user.getID()).send();
                        player.particleHit();
                        break;
                    }
                }
            }
        }
    }

    public static int getCurrentParticleID(){
        if (!freeParticleIDs.isEmpty()){
            int currentID = freeParticleIDs.get(0);
            freeParticleIDs.remove(0);
            Log.v("particle", "particle id: " + currentID);
            return currentID;
        }

        Log.v("particle", "particle id: " + currentParticleID);
        currentParticleID++;
        return currentParticleID - 1;
    }

    @Override
    public void removeParticle(int ID) {
        super.removeParticle(ID);
        freeParticleIDs.add(ID);
    }

    public static void spawnSweet(){
        int tempX, tempY;
        Sweet tempSweet;
        boolean possible;
        do{
            tempX = (int)(Math.random() * map.TILES_IN_MAP_WIDTH);
            tempY = (int)(Math.random() * map.TILES_IN_MAP_HEIGHT);
            possible = !map.getSolid(tempX, tempY);
        }while(!possible);
        tempSweet = new Sweet(tempX, tempY, currentSweetId);
        sweets.add(tempSweet);
        new SweetSpawnEvent(tempSweet).send();
        ++currentSweetId;
    }
}
