package ch.imlee.maturarbeit.game;

import android.view.SurfaceHolder;
import ch.imlee.maturarbeit.events.gameActionEvents.ParticleHitEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.TickEvent;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.Sweet;
import ch.imlee.maturarbeit.events.gameActionEvents.SweetSpawnEvent;
import ch.imlee.maturarbeit.game.map.Map;

public class GameServerThread extends GameThread {
    
    private static final int SWEET_SPAWN_RATE = Tick.TICK * 2;
    private static int currentSweetId;

    private static double lastSweetSpawn;

    private final int TICK_SEND_PERIOD = 3000 / TIME_PER_TICK;
    private static int nextTimeTickSend;

    public GameServerThread(SurfaceHolder holder) {
        super(holder);
        currentSweetId = 0;
        lastSweetSpawn = 0;
        nextTimeTickSend = 0;
    }

    @Override
    protected void update() {
        super.update();
        if (lastSweetSpawn + SWEET_SPAWN_RATE <= getSynchronizedTick()) {
            spawnSweet();
            lastSweetSpawn = getSynchronizedTick();
        }
        // checks if the particles hit anything
        particlePhysics();
        if (nextTimeTickSend <= synchronizedTick) {
            new TickEvent().send();
            nextTimeTickSend = synchronizedTick + TICK_SEND_PERIOD;
        }
    }

    public void particlePhysics() {
        // checks all the lists
        for (int i = 0; i < particleListArray.length; i++) {
            // checks every particle in the list
            for (Particle particle : particleListArray[i]) {
                if(particle == null){
                    continue;
                }
                // if the particle is inside a wall or out of the map
                if (Map.getSolid((int) particle.getXCoordinate(), (int) particle.getYCoordinate())) {
                    // id -1 means that it didn't hit a player but something else
                    ParticleHitEvent particleHitEvent = new ParticleHitEvent(particle.getID(), (byte) -1, (byte) i);
                    particleHitEvent.send();
                    particleHitEvent.apply();
                    continue;
                }
                // if the particle hit a player
                for (Player player : playerArray) {
                    if (player.TEAM != particle.TEAM && Math.pow(player.getXCoordinate() - particle.getXCoordinate(), 2) + Math.pow(player.getYCoordinate() - particle.getYCoordinate(), 2) <= Math.pow(player.getPlayerRadius(), 2)) {
                        ParticleHitEvent particleHitEvent = new ParticleHitEvent(particle.getID(), player.getID(), (byte) i);
                        particleHitEvent.send();
                        particleHitEvent.apply();
                        break;
                    }
                }
            }
        }
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
