package ch.imlee.maturarbeit.utils;

// a general vector class useful for the hit box resolution
public class Vector2D {

    // coordinates the vector points to
    public double x, y;

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(double xPoint1, double yPoint1, double xPoint2, double yPoint2){
        x = xPoint2 - xPoint1;
        y = yPoint2 - yPoint1;
    }

    // make the vector length 1
    public void normalize(){
        double abs = Math.sqrt(x * x + y * y);
        x /= abs;
        y /= abs;
    }

    // multiply the vectors length with the scalar
    public void scale(double scalar){
        x *= scalar;
        y *= scalar;
    }

    // set the length to l
    public void scaleTo(double newLength){
        normalize();
        scale(newLength);
    }

    // return the length of the vector
    public double getLength(){
        return Math.sqrt(x * x + y * y);
    }


    // returns the digits after the comma of x
    public double xMod1(){
        return x - (int)x;
    }

    // returns the digits after the comma of y
    public double yMod1(){
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

    public void add(double x, double y){
        this.x += x;
        this.y += y;
    }

    public void addX(double x){
        add(x,0);
    }

    public void addY(double y){
        add(0,y);
    }
}

