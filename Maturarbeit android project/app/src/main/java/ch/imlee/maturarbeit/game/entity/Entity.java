package ch.imlee.maturarbeit.game.entity;

import java.util.ArrayList;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 05.06.2015.
 */
public class Entity {
    protected float xCoordinate, yCoordinate;

    public Entity(float entityXCoordinate, float entityYCoordinate){
        xCoordinate = entityXCoordinate;
        yCoordinate = entityYCoordinate;
    }

    public void setXCoordinate(float playerXCoordinate){
        xCoordinate = playerXCoordinate;
    }

    public void setYCoordinate(float playerYCoordinate){
        yCoordinate = playerYCoordinate;
    }

    public void setCoordinates(float playerXCoordinate, float playerYCoordinate){
        xCoordinate = playerXCoordinate;
        yCoordinate = playerYCoordinate;
    }

    public float getXCoordinate(){
        return xCoordinate;
    }

    public float getYCoordinate(){
        return yCoordinate;
    }
}
