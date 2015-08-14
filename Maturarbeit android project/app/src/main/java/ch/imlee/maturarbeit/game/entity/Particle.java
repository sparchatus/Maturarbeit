package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 09.06.2015.
 */
public class Particle extends Entity {

    public final int RENDER_RADIUS = GameClient.getHalfScreenHeight() / 45;
    private final float SPEED = 0.3f;
    private static Paint color;
    private static int colorRotation;
    private static Player player;
    private static Paint enemyColor;
    public static int TEAM;
    private float xSpeed;
    private float ySpeed;

    public Particle(Player thePlayer) {
        super(thePlayer.getXCoordinate(), thePlayer.getYCoordinate());
        color = new Paint();
        enemyColor = new Paint();
        enemyColor.setColor(0xFFFF0000);
        switch(colorRotation){
            case 0:color.setColor(0xFFAA0000);
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
        if (TEAM == GameThread.getUser().getTeam()) {
            canvas.drawCircle((xCoordinate - GameThread.getUser().getXCoordinate()) * GameThread.getUser().TILE_SIDE + GameClient.getHalfScreenWidth(), (yCoordinate - GameThread.getUser().getYCoordinate()) * GameThread.getUser().TILE_SIDE + GameClient.getHalfScreenHeight(), RENDER_RADIUS, color);
        } else {
            canvas.drawCircle((xCoordinate - GameThread.getUser().getXCoordinate()) * GameThread.getUser().TILE_SIDE + GameClient.getHalfScreenWidth(), (yCoordinate - GameThread.getUser().getYCoordinate()) * GameThread.getUser().TILE_SIDE + GameClient.getHalfScreenHeight(), RENDER_RADIUS, enemyColor);
        }
        return canvas;
    }
}
