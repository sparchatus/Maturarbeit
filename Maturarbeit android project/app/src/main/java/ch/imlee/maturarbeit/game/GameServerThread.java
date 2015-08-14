package ch.imlee.maturarbeit.game;

import android.content.Context;
import android.view.SurfaceHolder;

import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.Player;

/**
 * Created by Sandro on 14.08.2015.
 */
public class GameServerThread extends GameThread{

    public GameServerThread(SurfaceHolder holder, Context context) {
        super(holder, context);
    }

    @Override
    protected void update() {
        super.update();
        for (Particle particle : particleList) {
            for (Player player : playerArray) {
                if (Math.sqrt(Math.pow(player.getXCoordinate() - particle.getXCoordinate(), 2) + Math.pow(player.getYCoordinate() - particle.getYCoordinate(), 2)) <= map.TILE_SIDE) {
                    particleList.remove(particle);
                    break;
                }
            }
            if (map.getSolid((int) particle.getXCoordinate(), (int) particle.getYCoordinate())) {
                particleList.remove(particle);
            }
        }
    }
}
