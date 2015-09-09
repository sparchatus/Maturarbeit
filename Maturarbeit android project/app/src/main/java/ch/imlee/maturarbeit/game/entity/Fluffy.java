package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.Controller.FluffyGameSurfaceController;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.StunEvent;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 07.06.2015.
 */
public class Fluffy extends User {

    private Player focusedPlayer = null;
    private int MANA_CONSUMPTION = MAX_MANA;
    private final float MAX_FOCUS_RANGE = 4.0f;

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
        if (focusedPlayer != null && Math.pow(xCoordinate - focusedPlayer.getXCoordinate(), 2) +  Math.pow(yCoordinate - focusedPlayer.getYCoordinate(), 2) > Math.pow(MAX_FOCUS_RANGE, 2)){
            focusedPlayer = null;
            FluffyGameSurfaceController.focusedPlayerNull();
        }
    }

    @Override
    public Canvas render(Canvas canvas) {
        canvas =  super.render(canvas);
        return canvas;
    }

    public void skillActivation() {
        if (mana == MAX_MANA && focusedPlayer != null){
            focusedPlayer.stun(GameThread.getSynchronizedTick() + STUN_TIME);
            new StunEvent(focusedPlayer.getID(), GameThread.getSynchronizedTick() + STUN_TIME).send();
            focusedPlayer = null;
            FluffyGameSurfaceController.focusedPlayerNull();
            mana -= MANA_CONSUMPTION;
        }
    }

    public void setFocusedPlayer(Player focusedPlayer){
        this.focusedPlayer = focusedPlayer;
    }
    public Player getFocusedPlayer(){
        return focusedPlayer;
    }
}
