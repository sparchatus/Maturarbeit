package ch.imlee.maturarbeit.utils;

/**
 * Created by Sandro on 10.09.2015.
 */
public class Vector2D {
    public float x, y;

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(float xPoint1, float yPoint1, float xPoint2, float yPoint2){
        x = xPoint2 - xPoint1;
        y = yPoint2 - yPoint1;
    }

    public void normalize(){
        double absolute = Math.sqrt(x * x + y * y);
        x /= absolute;
        y /= absolute;
    }

    public static float scalarProduct(Vector2D vector1, Vector2D vector2){
        return vector1.x * vector2.x  + vector1.y * vector2.y;
    }

    public static float scalarProduct(float xVector1, float yVector1, float xVector2, float yVector2){
        return xVector1 * xVector2  + yVector1 * yVector2;
    }

    public float scalarProduct(float xVector, float yVector) {
        return x * xVector + y * yVector;
    }

    public float scalarProduct(Vector2D vector) {
        return x * vector.x + y * vector.y;
    }

    public void scale(float scalar){
        x *= scalar;
        y *= scalar;
    }

    public void scaleTo(float newLength){
        double length = Math.sqrt(x * x + y * y);
        x /= length;
        x *= newLength;
        y /= length;
        y *= newLength;
    }

    public double getLength(){
        return Math.sqrt(x * x + y * y);
    }

    public double getDirectedLength(){
        if (x < 0 && y < 0 || x > 0 && y > 0){
            return Math.sqrt(x * x + y * y);
        }
        return - Math.sqrt(x * x + y * y);
    }

    public float xMod1(){
        return x - (int)x;
    }
    public float yMod1(){
        return y - (int)y;
    }

    public int xIntPos(){
        return (int)x;
    }

    public int yIntPos(){
        return (int)y;
    }

    public void add(Vector2D vector){
        x += vector.x;
        y += vector.y;
    }

    public void add(float x, float y){
        this.x += x;
        this.y += y;
    }

    public void addX(float x){
        add(x, 0);
    }

    public void addY(float y){
        add(0, y);
    }
}

