package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.map.MapDimensions;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 09.06.2015.
 */
public class Particle extends Entity {

    public final float PARTICLE_RADIUS = 1 / 10f;
    public final int RENDER_RADIUS = (int) (GameSurface.getSurfaceHeight() / 2 / MapDimensions.TILES_IN_SCREEN_HEIGHT * PARTICLE_RADIUS);
    private final float SPEED;
    private final Paint color;
    public byte TEAM;
    private float xSpeed;
    private float ySpeed;
    private final int ID;

    public Particle(float xCoordinate, float yCoordinate, byte team, double angle, double spawnTick, int ID) {
        super(xCoordinate, yCoordinate);
        TEAM = team;
        color = new Paint();
        if (TEAM == GameThread.getUser().TEAM){
            color.setColor(0xFF00FF00);
        }else {
            color.setColor(0xFFFF0000);
        }
        SPEED = 6f / Tick.TICK;
        xSpeed = (float) (Math.cos(angle) * SPEED);
        ySpeed = (float) (Math.sin(angle) * SPEED);
        this.xCoordinate += (GameThread.getSynchronizedTick() - spawnTick) * xSpeed;
        this.yCoordinate += (GameThread.getSynchronizedTick() - spawnTick) * ySpeed;
        this.ID = ID;
    }

    public void update(){
        xCoordinate += xSpeed;
        yCoordinate += ySpeed;
    }

    public Canvas render(Canvas canvas){
        canvas.drawCircle((xCoordinate - GameThread.getUser().getXCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2, (yCoordinate - GameThread.getUser().getYCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2, RENDER_RADIUS, color);
        return canvas;
    }

    public int getID(){
        return ID;
    }
}
