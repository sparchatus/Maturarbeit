package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.util.Log;

import ch.imlee.maturarbeit.game.Controller.FluffyGameSurfaceController;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.StunEvent;

/**
 * Created by Sandro on 07.06.2015.
 */
public class Fluffy extends User {

    private int MANA_CONSUMPTION = MAX_MANA;

    public Fluffy(Map map, byte team, byte playerId, String name) {
        super(PlayerType.FLUFFY, map, team, playerId, name);
    }

    @Override
    public void update() {
        super.update();
        // This player type generates mana by moving around. the faster he moves the more mana he gets
        mana += 2* speed / maxSpeed;
        // the cap for the maximum mana
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    public void skillActivation() {
        // getting the focused player from the corresponding controller
        Player focusedPlayer = FluffyGameSurfaceController.getFocusedPlayer();
        //only with sufficient mana and a player as focus this player type is able to activate his skill
        if (mana == MAX_MANA && focusedPlayer != null) {
            focusedPlayer.stun(GameThread.getSynchronizedTick() + STUN_TIME);
            // sending the information to the other devices
            new StunEvent(ID, focusedPlayer.getID(), GameThread.getSynchronizedTick() + STUN_TIME).send();
            // reset the focus
            FluffyGameSurfaceController.nullFocusedPlayer();
            // consuming the mana
            mana -= MANA_CONSUMPTION;
        }
    }
}
