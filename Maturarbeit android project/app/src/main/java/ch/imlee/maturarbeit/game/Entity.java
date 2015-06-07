package ch.imlee.maturarbeit.game;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 05.06.2015.
 */
public class Entity {
    protected float xCoordinate, yCoordinate;
    protected static GameSurface.GameThread gameThread;

    public Entity(float entityXCoordinate, float entityYCoordinate, GameSurface.GameThread gameThread){
        xCoordinate = entityXCoordinate;
        yCoordinate = entityYCoordinate;
        this.gameThread = gameThread;
    }

    public float getXCoordinate(){
        return xCoordinate;
    }

    public float getYCoordinate(){
        return yCoordinate;
    }
}
