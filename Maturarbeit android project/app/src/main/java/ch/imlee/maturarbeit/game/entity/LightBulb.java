package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.LightBulbStand;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 29.06.2015.
 */
public class LightBulb extends Entity{
    private final Bitmap LIGHT_BULB_OFF, LIGHT_BULB_OFF_SMALL, LIGHT_BULB_ON_SMALL;
    private final int SIDE;
    public final byte ID;
    private Player possessor;
    private LightBulbStand lightBulbStand;

    public LightBulb(byte team, byte ID) {
        super(Map.getFriendlyLightBulbStands(team)[0].CENTER_X, Map.getFriendlyLightBulbStands(team)[0].CENTER_Y);
        SIDE = Map.TILE_SIDE;
        LIGHT_BULB_OFF = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_off), SIDE, SIDE, false);
        LIGHT_BULB_OFF_SMALL = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_off), SIDE / 3 * 2, SIDE / 3 * 2, false);
        LIGHT_BULB_ON_SMALL = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_on), SIDE / 3 * 2, SIDE / 3 * 2, false);
        this.ID = ID;
        lightBulbStand = Map.getFriendlyLightBulbStands(team)[0].putLightBulbOn();
    }

    public void update(){
        if (possessor != null){
            setCoordinates(possessor.getXCoordinate(), possessor.getYCoordinate());
        }
    }

    public Canvas render(Canvas canvas){
        if (possessor!=null){
            if (possessor.getClass() == Fluffy.class){
                canvas.drawBitmap(LIGHT_BULB_ON_SMALL, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2 - SIDE / 3f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2  - SIDE, null);
                return canvas;
            }
            canvas.drawBitmap(LIGHT_BULB_OFF_SMALL, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2 - SIDE / 3f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE, null);
            return canvas;
        }
        canvas.drawBitmap(LIGHT_BULB_OFF, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2  - SIDE / 2f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE / 2f, null);
        return canvas;
    }

    public void pickUp(Player possessor){
        this.possessor = possessor;
        if (lightBulbStand != null){
            lightBulbStand = lightBulbStand.removeLightbulb();
        }
        xCoordinate = possessor.getXCoordinate();
        yCoordinate = possessor.getYCoordinate();
    }

    public void fallOnFloor(){
        possessor = null;
        //todo:check if landed on a impossible location?
    }

    public void putOnLightBulbStand(LightBulbStand lightBulbStand){
        possessor = null;
        this.lightBulbStand = lightBulbStand.putLightBulbOn();
        xCoordinate = lightBulbStand.CENTER_X;
        yCoordinate = lightBulbStand.CENTER_Y;
    }

    public LightBulbStand getLightBulbStand(){
        return lightBulbStand;
    }

    public byte getLightBulbStandTeam (){
        if (lightBulbStand == null){
            return (byte)-1;
        }
        return lightBulbStand.TEAM;
    }

    public boolean isPickable(){
        if (possessor == null){
            return true;
        }
        return false;
    }

    public byte getID(){
        return ID;
    }
}
