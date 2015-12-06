package ch.imlee.maturarbeit.game.entity;

public class Entity {
    // every entity has these position coordinates.
    protected float xCoordinate, yCoordinate;

    public Entity(float entityXCoordinate, float entityYCoordinate){
        xCoordinate = entityXCoordinate;
        yCoordinate = entityYCoordinate;
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
