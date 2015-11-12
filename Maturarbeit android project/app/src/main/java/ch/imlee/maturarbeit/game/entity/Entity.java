package ch.imlee.maturarbeit.game.entity;

/**
 * Created by Sandro on 05.06.2015.
 */
public class Entity {
    // every entity has these position coordinates.
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
