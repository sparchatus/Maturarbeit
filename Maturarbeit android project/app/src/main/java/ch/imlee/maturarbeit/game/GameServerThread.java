package ch.imlee.maturarbeit.game;

import android.content.Context;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import ch.imlee.maturarbeit.game.Sound.ParticleCollisionSound;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.Sweet;
import ch.imlee.maturarbeit.game.events.gameActionEvents.ParticleHitEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.SweetSpawnEvent;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 14.08.2015.
 */
public class GameServerThread extends GameThread{

    private ArrayList<Particle> particlesToRemove = new ArrayList<>();
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
                if ((int) particle.getXCoordinate() < 0 || (int) particle.getYCoordinate() >= map.MAP_HEIGHT || (int) particle.getXCoordinate() >= map.MAP_WIDTH || (int) particle.getYCoordinate() < 0 || map.getSolid((int) particle.getXCoordinate(), (int) particle.getYCoordinate())) {
                    particlesToRemove.add(particle);
                }
                for (Player player : playerArray) {
                    if (player.TEAM != particle.TEAM && Math.sqrt(Math.pow(player.getXCoordinate() - particle.getXCoordinate(), 2) +
                            Math.pow(player.getYCoordinate() - particle.getYCoordinate(), 2)) <= player.getPlayerRadius()) {
                        new ParticleHitEvent(particle.getID(), player.getID(), user.getID()).send();
                        player.particleHit();
                        particlesToRemove.add(particle);
                        break;
                    }
                }
            }
        }
        if (particlesToRemove.size() != 0){
            for (Particle particle:particlesToRemove) {
                new ParticleHitEvent(particle.getID(), (byte) -1, user.getID()).send();
                particleList.remove(particle);
                new ParticleCollisionSound().start();
            }
        }
    }

    public static int getCurrentParticleID(){
        if (!freeParticleIDs.isEmpty()){
            int currentID = freeParticleIDs.get(0);
            freeParticleIDs.remove(0);
            return currentID;
        }
        currentParticleID++;
        return currentParticleID - 1;
    }

    public static void spawnSweet(){
        int tempX, tempY;
        Sweet tempSweet;
        boolean possible;
        do{
            tempX = (int)(Math.random() * (map.MAP_WIDTH-map.BORDER_TILES_RIGHT*2)+map.BORDER_TILES_RIGHT);
            tempY = (int)(Math.random() * (map.MAP_HEIGHT-map.BORDER_TILES_TOP*2)+map.BORDER_TILES_TOP);
            possible = !map.getSolid(tempX, tempY);
        }while(!possible);
        tempSweet = new Sweet(tempX, tempY, currentSweetId);
        sweets.add(tempSweet);
        new SweetSpawnEvent(tempSweet).send();
        ++currentSweetId;
    }
}
