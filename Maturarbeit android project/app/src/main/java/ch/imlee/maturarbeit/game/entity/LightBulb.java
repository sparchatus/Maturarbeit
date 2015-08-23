package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 29.06.2015.
 */
public class LightBulb extends Entity{
    private final Bitmap LIGHT_BULB_OFF, LIGHT_BULB_OFF_SMALL, LIGHT_BULB_ON_SMALL;
    private final int SIDE;
    private Player possessor;
    private final byte LIGHT_BULB_ID;

    public LightBulb(byte team, Map map, byte lightBulbId) {
        super(map.getStartX(team), map.getStartY(team));
        LIGHT_BULB_ID = lightBulbId;
        SIDE = map.TILE_SIDE;
        LIGHT_BULB_OFF = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_off), SIDE, SIDE, false);
        LIGHT_BULB_OFF_SMALL = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_off), SIDE / 3 * 2, SIDE / 3 * 2, false);
        LIGHT_BULB_ON_SMALL = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_on), SIDE / 3 * 2, SIDE / 3 * 2, false);
    }

    public void update(){
        if (possessor != null){
            setCoordinates(possessor.getXCoordinate(), possessor.getYCoordinate());
        }
    }

    public Canvas render(Canvas canvas){
        if (possessor!=null){
            if (possessor.getClass() == Fluffy.class){
                canvas.drawBitmap(LIGHT_BULB_ON_SMALL, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2 - SIDE / 3f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE, null);
                return canvas;
            }
            canvas.drawBitmap(LIGHT_BULB_OFF_SMALL, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2- SIDE / 3f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE, null);
            return canvas;
        }
        canvas.drawBitmap(LIGHT_BULB_OFF, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2- SIDE / 2f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE / 2f, null);
        return canvas;
    }

    public void setPossessor(Player possessor){
        this.possessor = possessor;
    }

    public Player getPossessor() {
        return possessor;
    }

    public byte getLIGHT_BULB_ID(){
        return LIGHT_BULB_ID;
    }
}
