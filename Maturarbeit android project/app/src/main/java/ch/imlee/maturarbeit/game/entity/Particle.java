package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.map.MapDimensions;

/**
 * Created by Sandro on 09.06.2015.
 */
public class Particle extends Entity {

    public final float PARTICLE_RADIUS = 1f / 5;
    public final int RENDER_RADIUS = (int) (GameClient.getHalfScreenHeight() / MapDimensions.TILES_IN_SCREEN_HEIGHT * PARTICLE_RADIUS);
    private final float SPEED;
    private final Paint color;
    public static int TEAM;
    private float xSpeed;
    private float ySpeed;

    public Particle(float xCoordinate, float yCoordinate, byte team, double angle) {
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
    }

    public void update(){
        xCoordinate += xSpeed;
        yCoordinate += ySpeed;
    }

    public Canvas render(Canvas canvas){
        canvas.drawCircle((xCoordinate - GameThread.getUser().getXCoordinate()) * GameThread.getUser().TILE_SIDE + GameClient.getHalfScreenWidth(), (yCoordinate - GameThread.getUser().getYCoordinate()) * GameThread.getUser().TILE_SIDE + GameClient.getHalfScreenHeight(), RENDER_RADIUS, color);
        return canvas;
    }
}
