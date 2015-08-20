package ch.imlee.maturarbeit.game.Controller;

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

    protected LightBulb pickingBulb;
    protected int pickingTicks;
    private final int PICK_UP_TICKS = 2 * Tick.TICK;
    protected User user;
    protected final int TILE_SIDE;
    protected final float PLAYER_RADIUS;
    protected ControllerState controllerState;

    public Controller(User user, Map map){
        this.TILE_SIDE = map.TILE_SIDE;
        this.user = user;
        PLAYER_RADIUS = user.getPLAYER_RADIUS();
        controllerState = ControllerState.NULL;
    }

    public void update(){
        if (controllerState == ControllerState.PICKING || pickingBulb != null){
            pickingTicks ++;
            if (pickingTicks >= PICK_UP_TICKS){
                pickingBulb.setPossessor(user);
                pickingBulb = null;
                pickingTicks = 0;
                controllerState = ControllerState.NULL;
            }
        }
    }

    public boolean onTouch(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN ||controllerState == ControllerState.PICKING) {
            for (LightBulb lightBulb : GameThread.getLightBulbArray()) {
                if (lightBulb.getPossessor() != null){
                    return false;
                }
                if (Math.sqrt(Math.pow((event.getX() - GameClient.getHalfScreenWidth()) / TILE_SIDE + user.getXCoordinate() - lightBulb.getXCoordinate(), 2) + Math.pow((event.getY() - GameClient.getHalfScreenHeight()) / TILE_SIDE + user.getYCoordinate() - lightBulb.getYCoordinate(), 2)) <= user.getPLAYER_RADIUS() && Math.sqrt(Math.pow(user.getYCoordinate() - lightBulb.getXCoordinate(), 2) + Math.pow(user.getYCoordinate() - lightBulb.getYCoordinate(), 2)) <= TILE_SIDE * 3) {
                    if (pickingBulb != lightBulb) {
                        pickingBulb = lightBulb;
                        pickingTicks = 0;
                        controllerState = ControllerState.PICKING;
                    }
                    return true;
                }
            }
        }
        pickingBulb = null;
        pickingTicks = 0;
        return false;
    }
}