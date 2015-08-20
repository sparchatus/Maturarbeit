package ch.imlee.maturarbeit.game.Controller;

import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Fluffy;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 11.08.2015.
 */
public class FluffyController1 extends MovementController1 {

    private Fluffy fluffy;
    public FluffyController1(Fluffy fluffy, Map map) {
        super(fluffy, map);
        this.fluffy = fluffy;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Player player : GameThread.getPlayerArray()) {
                if (Math.sqrt(Math.pow((event.getX() - GameClient.getHalfScreenWidth()) / TILE_SIDE + user.getXCoordinate() - player.getXCoordinate(), 2) + Math.pow((event.getY() - GameClient.getHalfScreenHeight()) / TILE_SIDE + user.getYCoordinate() - player.getYCoordinate(), 2)) < user.getPLAYER_RADIUS()) {
                    if (player.TEAM != user.TEAM) {
                        fluffy.setFocusedPlayer(player);
                        return false;
                    }
                }
            }
        }
        return super.onTouch(event);
    }
}
