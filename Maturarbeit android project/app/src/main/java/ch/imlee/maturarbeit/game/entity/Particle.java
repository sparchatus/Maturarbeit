package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.views.GameSurface;

public class Particle extends Entity {

    public byte TEAM;

    private final int ID;

    public final float PARTICLE_RADIUS = 0.1f;
    public final int RENDER_RADIUS = (int) (Map.TILE_SIDE * PARTICLE_RADIUS);

    private final float SPEED = 8f / Tick.TICK;
    private final float X_SPEED, Y_SPEED;

    private final Paint color;

    public Particle(float xCoordinate, float yCoordinate, double angle, byte team, double spawnTick, int ID) {
        super(xCoordinate, yCoordinate);
        TEAM = team;
        color = new Paint();

        // allied Particles are green (0xFF00FF00) , enemy ones are red (0xFFFF0000)
        if (TEAM == GameThread.getUser().TEAM){
            color.setColor(0xFF00FF00);
        }else {
            color.setColor(0xFFFF0000);
        }
        // the Particle speed and angle are constant, thus the y and x speeds are final
        X_SPEED = (float) (Math.cos(angle) * SPEED);
        Y_SPEED = (float) (Math.sin(angle) * SPEED);

        // to correct the change of position the Particle went through during the time it takes for the Event to be sent
        this.xCoordinate += (GameThread.getSynchronizedTick() - spawnTick) * X_SPEED;
        this.yCoordinate += (GameThread.getSynchronizedTick() - spawnTick) * Y_SPEED;
        this.ID = ID;
    }

    public void update(){
        // moving
        xCoordinate += X_SPEED;
        yCoordinate += Y_SPEED;
    }

    // the Objects are generally drawn in relation to the User position on the Map because the User's position on the screen is constant
    public Canvas render(Canvas canvas){
        canvas.drawCircle((xCoordinate - GameThread.getUser().getXCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2, (yCoordinate - GameThread.getUser().getYCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2, RENDER_RADIUS, color);
        return canvas;
    }

    public int getID(){
        return ID;
    }
}
