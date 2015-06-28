package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.events.gameActionEvents.StunEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 07.06.2015.
 */
public class Fluffy extends User {

    private Player focusedPlayer = null;
    private int MAX_RANGE = 5;
    private int MANA_CONSUMPTION = MAX_MANA;
    private final Bitmap FOCUS_BMP;

    public Fluffy(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, byte team, byte playerId) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, team, playerId, null);
        FOCUS_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.focus_overlay), PLAYER_SIDE, PLAYER_SIDE, false);
    }

    @Override
    public void update() {
        super.update();
        mana += realSpeed / MAX_SPEED;
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
        if (focusedPlayer != null){
            if (Math.sqrt(Math.pow(xCoordinate - focusedPlayer.getXCoordinate(), 2) + Math.pow(yCoordinate - focusedPlayer.getYCoordinate(), 2)) > MAX_RANGE){
                focusedPlayer = null;
            }

        }
    }

    @Override
    public Canvas render(Canvas canvas) {
        canvas =  super.render(canvas);
        if (focusedPlayer != null){
            canvas.drawBitmap(FOCUS_BMP, (focusedPlayer.getXCoordinate() - xCoordinate - PLAYER_RADIUS) *    PLAYER_SIDE + GameClient.getHalfScreenWidth(), (focusedPlayer.getYCoordinate() - yCoordinate - PLAYER_RADIUS) * PLAYER_SIDE + GameClient.getHalfScreenHeight(), null);
        }
        return canvas;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Player player : gameThread.getPlayerArray()) {
                if (Math.sqrt(Math.pow((event.getX() - GameClient.getHalfScreenWidth()) / PLAYER_SIDE + user.getXCoordinate() - player.getXCoordinate(), 2) + Math.pow((event.getY() - GameClient.getHalfScreenHeight()) / PLAYER_SIDE + user.getYCoordinate() - player.getYCoordinate(), 2)) < player.PLAYER_RADIUS) {
                    if (player.TEAM != user.TEAM) {
                        focusedPlayer = player;
                        return false;
                    }
                }
            }
        }
        return super.onTouch(event);
    }

    public void skillActivation() {
        if (mana == MAX_MANA && focusedPlayer != null){
            focusedPlayer.stun(gameThread.getSynchronizedTick() + STUN_TIME);
            new StunEvent(focusedPlayer.ID, focusedPlayer.stunTick).send();
            focusedPlayer = null;
            mana -= MANA_CONSUMPTION;
        }
    }
}
