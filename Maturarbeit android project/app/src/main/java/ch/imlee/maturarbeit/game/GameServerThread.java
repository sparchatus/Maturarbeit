package ch.imlee.maturarbeit.game;

import android.content.Context;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import ch.imlee.maturarbeit.game.Sound.ParticleCollisionSound;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.events.gameActionEvents.ParticleHitEvent;

/**
 * Created by Sandro on 14.08.2015.
 */
public class GameServerThread extends GameThread{

    private ArrayList<Particle> particlesToRemove = new ArrayList<>();
    private static int currentParticleID = 0;

    public GameServerThread(SurfaceHolder holder, Context context) {
        super(holder, context);
    }

    @Override
    protected void update() {
        super.update();
        for (Particle particle:particleList) {
            if (map.getSolid((int) particle.getXCoordinate(), (int) particle.getYCoordinate())){
                particlesToRemove.add(particle);
            }
            for (Player player:playerArray) {
                if (player.TEAM != particle.TEAM && Math.sqrt(Math.pow(player.getXCoordinate() - particle.getXCoordinate(), 2) + Math.pow(player.getYCoordinate() - particle.getYCoordinate(), 2)) <= player.PLAYER_RADIUS) {
                    new ParticleHitEvent(particle.getID(), player.getID()).send();
                    player.particleHit();
                    particlesToRemove.add(particle);
                    break;
                }
            }
        }
        if (particlesToRemove.size() != 0){
            for (Particle particle:particlesToRemove) {
                new ParticleHitEvent(particle.getID(), (byte) -1).send();
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
}
