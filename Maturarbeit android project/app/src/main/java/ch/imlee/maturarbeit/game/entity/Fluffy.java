package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;

import ch.imlee.maturarbeit.game.Controller.FluffyGameSurfaceController;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.StunEvent;

/**
 * Created by Sandro on 07.06.2015.
 */
public class Fluffy extends User {

    private int MANA_CONSUMPTION = MAX_MANA;

    public Fluffy(Map map, byte team, byte playerId) {
        super(PlayerType.FLUFFY, map, team, playerId);
    }

    @Override
    public void update() {
        super.update();
        mana += speed / maxSpeed;
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    @Override
    public Canvas render(Canvas canvas) {
        canvas =  super.render(canvas);
        return canvas;
    }

    public void skillActivation() {
        Player focusedPlayer = FluffyGameSurfaceController.getFocusedPlayer();
        if (mana == MAX_MANA && focusedPlayer != null) {
            focusedPlayer.stun(GameThread.getSynchronizedTick() + STUN_TIME);
            new StunEvent(focusedPlayer.getID(), GameThread.getSynchronizedTick() + STUN_TIME).send();
            FluffyGameSurfaceController.nullFocusedPlayer();
            mana -= MANA_CONSUMPTION;
        }
    }
}
