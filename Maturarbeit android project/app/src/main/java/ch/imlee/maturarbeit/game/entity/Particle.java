package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 09.06.2015.
 */
public class Particle extends Entity {

    public final int RENDER_RADIUS = GameClient.getHalfScreenHeight() / 45;
    public final float PARTICLE_RADIUS = (float) (1.0 / 5.0);
    private final float SPEED = 0.3f;
    private final Paint color;
    private static int colorRotation;
    private static Player player;
    public static int TEAM;
    private float xSpeed;
    private float ySpeed;

    public Particle(Player thePlayer, GameThread gameThread) {
        super(thePlayer.getXCoordinate(), thePlayer.getYCoordinate(), gameThread);
        color = new Paint();
        switch(colorRotation){
            case 0:color.setColor(0xFFFF0000);
                break;
            case 1:color.setColor(0xFFFFFF00);
                break;
            case 2:color.setColor(0xFF00FF00);
                break;
            case 3:color.setColor(0xFF00FFFF);
                break;
            case 4:color.setColor(0xFF0000FF);
                break;
            case 5:
                color.setColor(0xFFFF00FF);
                colorRotation = -1;
                break;
            default:color.setColor(0xFFFFFFFF);
        }
        colorRotation++;
        player = thePlayer;
        TEAM = player.TEAM;
        xSpeed = (float) (Math.cos(player.getAngle()) * SPEED);
        ySpeed = (float) (Math.sin(player.getAngle()) * SPEED);
    }

    public void update(){
        xCoordinate += xSpeed;
        yCoordinate += ySpeed;
    }

    public Canvas render(Canvas canvas){
        canvas.drawCircle((xCoordinate - gameThread.getUser().getXCoordinate()) * gameThread.getUser().PLAYER_SIDE + GameClient.getHalfScreenWidth(), (yCoordinate - gameThread.getUser().getYCoordinate()) * gameThread.getUser().PLAYER_SIDE + GameClient.getHalfScreenHeight(), RENDER_RADIUS, color);
        return canvas;
    }
}
