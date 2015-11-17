package ch.imlee.maturarbeit.utils;

// a general vector class usefull for the hit box resolution

public class Vector2D {
    // coordinates the vector points to
    public float x, y;

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(float xPoint1, float yPoint1, float xPoint2, float yPoint2){
        x = xPoint2 - xPoint1;
        y = yPoint2 - yPoint1;
    }

    // make the vector length 1

    public void normalize(){
        double abs = Math.sqrt(x * x + y * y);
        x /= abs;
        y /= abs;
    }

    public void scale(float scalar){
        x *= scalar;
        y *= scalar;
    }

    public void scaleTo(float newLength){
        normalize();
        scale(newLength);
    }

    public double getLength(){
        return Math.sqrt(x * x + y * y);
    }


    // returns the digits after the comma of x
    public float xMod1(){
        return x - (int)x;
    }

    // returns the digits after the comma of y
    public float yMod1(){
        return y - (int)y;
    }

    // returns the x value without the part after the comma
    public int xIntPos(){
        return (int)x;
    }

    // returns the y value without the part after the comma
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
        add(x,0);
    }

    public void addY(float y){
        add(0,y);
    }
}

