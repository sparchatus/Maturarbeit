package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.LightBulbStand;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.views.GameSurface;

public class LightBulb extends Entity{

    public final byte ID;

    private final int SIDE;

    private final Bitmap LIGHT_BULB_OFF, LIGHT_BULB_ON;
    private final Bitmap LIGHT_BULB_OFF_SMALL, LIGHT_BULB_ON_SMALL;

    // the Player holding the LightBulb. this is null if the LightBulb isn't possessed by a Player
    private Player possessor=null;

    // the LightBulbStand that the LightBulb is on. this is null if the LightBulb isn't on a LightBulbStand
    private LightBulbStand lightBulbStand;

    public LightBulb(byte team, byte ID) {
        super(Map.getFriendlyLightBulbStands(team)[0].CENTER_X, Map.getFriendlyLightBulbStands(team)[0].CENTER_Y);
        SIDE = Map.TILE_SIDE;
        LIGHT_BULB_OFF = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_off), SIDE, SIDE, false);
        LIGHT_BULB_ON = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_on), SIDE, SIDE, false);
        LIGHT_BULB_OFF_SMALL = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_off), SIDE / 3 * 2, SIDE / 3 * 2, false);
        LIGHT_BULB_ON_SMALL = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.light_bulb_on), SIDE / 3 * 2, SIDE / 3 * 2, false);
        this.ID = ID;
        // every LightBulb starts on a LightBulbStand.
        lightBulbStand = Map.getFriendlyLightBulbStands(team)[0].putLightBulbOn();
    }

    // the LightBulb only moves when it is possessed by a Player.
    public void update(){
        if (possessor != null){
            setCoordinates(possessor.getXCoordinate(), possessor.getYCoordinate());
        }
    }

    // the look of the LightBulb depends on who it's possessor is or if it is on a stand or lies an the floor
    // when possessed by a Player it appears as a small icon above
    // the Objects are generally drawn in relation to the User position on the Map because the User's position on the screen is constant
    public Canvas render(Canvas canvas){
        if (possessor!=null){
            if (possessor.getInvisible()){
                return canvas;
            }
            // Fluffy is electric thus the LightBulb is on when possessed by him
            if (possessor.getClass() == Fluffy.class){
                canvas.drawBitmap(LIGHT_BULB_ON_SMALL, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2 - SIDE / 3f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2  - SIDE, null);
                return canvas;
            }
            // on all the other Players the LightBulb is off
            canvas.drawBitmap(LIGHT_BULB_OFF_SMALL, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2 - SIDE / 3f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE, null);
            return canvas;
        }
        // on the LightBulbStand the LightBulb is normal size and on
        if(lightBulbStand != null){
            canvas.drawBitmap(LIGHT_BULB_ON, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2  - SIDE / 2f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE / 2f, null);
            return canvas;
        }
        // on the floor the LightBulb is normal size and off
        canvas.drawBitmap(LIGHT_BULB_OFF, (xCoordinate - GameThread.getUser().getXCoordinate()) * SIDE + GameSurface.getSurfaceWidth() / 2  - SIDE / 2f, (yCoordinate - GameThread.getUser().getYCoordinate()) * SIDE + GameSurface.getSurfaceHeight() / 2 - SIDE / 2f, null);
        return canvas;
    }

    // when the LigtBulb is picked up by a Player
    public void pickUp(Player possessor){
        this.possessor = possessor;
        // the lightBulbStand has to be informed that he is free
        if (lightBulbStand != null){
            lightBulbStand = lightBulbStand.removeLightbulb();
        }
    }

    public void fallOnFloor(){
        possessor = null;
    }

    // called when a Player comes close enough to a LightBulbStand while in possession of a LightBulb
    public void putOnLightBulbStand(LightBulbStand lightBulbStand){
        possessor = null;
        // LightBulbStand isn't free anymore
        this.lightBulbStand = lightBulbStand.putLightBulbOn();

        xCoordinate = lightBulbStand.CENTER_X;
        yCoordinate = lightBulbStand.CENTER_Y;
    }

    public LightBulbStand getLightBulbStand(){
        return lightBulbStand;
    }

    // returns if the LightBulb is possessed by a player or not
    public boolean isPickable(){
        if (possessor == null){
            return true;
        }
        return false;
    }

    public byte getLightBulbStandTeam (){
        // by definition a LightBulb that isn't on a stand has the stand ID "-1"
        if (lightBulbStand == null){
            return (byte)-1;
        }
        return lightBulbStand.TEAM;
    }

    public byte getID(){
        return ID;
    }
}
