package ch.imlee.maturarbeit.game.Controller;

import android.graphics.Canvas;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.entity.LightBulb;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 11.08.2015.
 */
public class Controller {

    private LightBulb holdingBulb;
    private int holdingTicks;
    private final int PICK_UP_TICKS = 5 * Tick.TICK;
    protected GameThread gameThread;
    protected User user;
    protected final int TILE_SIDE;
    protected final float PLAYER_RADIUS;

    public Controller(User user, Map map, GameThread gameThread){
        this.TILE_SIDE = map.TILE_SIDE;
        this.user = user;
        this.gameThread = gameThread;
        PLAYER_RADIUS = user.getPLAYER_RADIUS();
    }

    public boolean onTouch(MotionEvent event){
        for (LightBulb lightBulb : gameThread.getLightBulbArray()) {
            if (lightBulb.getPossessor() != null){
                continue;
            }
            if (Math.sqrt(Math.pow((event.getX() - GameClient.getHalfScreenWidth()) / TILE_SIDE + user.getXCoordinate() - lightBulb.getXCoordinate(), 2) + Math.pow((event.getY() - GameClient.getHalfScreenHeight()) / TILE_SIDE + user.getYCoordinate() - lightBulb.getYCoordinate(), 2)) < user.getPLAYER_RADIUS()) {
                if (holdingBulb == lightBulb) {
                    holdingTicks++;
                    if (holdingTicks == PICK_UP_TICKS){
                        //todo: send a lightbulb wanting to get event
                        lightBulb.pickUp(user);
                    }
                } else {
                    holdingBulb = lightBulb;
                    holdingTicks = 0;
                }
                return true;
            }
        }
        holdingBulb = null;
        holdingTicks = 0;
        return false;
    }
}
