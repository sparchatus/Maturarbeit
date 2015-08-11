package ch.imlee.maturarbeit.game.Controller;

import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 11.08.2015.
 */
public class Controller {

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
        for ()
       return false;
    }
}
