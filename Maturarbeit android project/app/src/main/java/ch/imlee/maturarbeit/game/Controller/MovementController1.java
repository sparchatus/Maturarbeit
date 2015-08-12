package ch.imlee.maturarbeit.game.Controller;

import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 11.08.2015.
 */
public class MovementController1 extends Controller{

    public MovementController1(User user, Map map, GameThread gameThread) {
        super(user, map, gameThread);
    }

    public boolean onTouch(MotionEvent event){
        if (!super.onTouch(event)){
            playerControl(event);
        }
        return true;
    }

    private boolean playerControl(MotionEvent event){
        float distance = (float) Math.sqrt(Math.pow(event.getX() - GameClient.getHalfScreenWidth(), 2) + Math.pow(event.getY() - GameClient.getHalfScreenHeight(), 2));
        float angle = (float) Math.acos((event.getX() - GameClient.getHalfScreenWidth()) / distance);
        if(event.getY() - GameClient.getHalfScreenHeight() < 0){
            angle *= -1;
        }
        user.setAngle(angle);
        float velocity;
        if (distance / TILE_SIDE <= PLAYER_RADIUS){
            user.setVelocity(0);
            return true;
        }else {
            if((velocity = distance / GameClient.getHalfScreenHeight()) > 1){
                velocity= 1;
            }
            user.setVelocity(velocity);
        }
        return true;
    }
}