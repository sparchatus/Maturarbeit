package ch.imlee.maturarbeit.game.map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 04.06.2015.
 */
public class Map implements MapDimensions {
    public static int TILE_SIDE;
    public final int TILES_IN_MAP_WIDTH, TILES_IN_MAP_HEIGHT;
    private static float[][] playerStartCoordinates = new float[8][2];
    private static int blueCoordinateDistributionIndex;
    private static int greenCoordinateDistributionIndex;
    private int halfGameSurfaceWidth;
    private int halfGameSurfaceHeight;

    public static Tile[][] TILE_MAP;
    private static Bitmap pixelMap;
    private static LightBulbStand[] blueLightBulbStandArray = new LightBulbStand[2];
    private static LightBulbStand[] greenLightBulbStandArray = new LightBulbStand[2];
    private Bitmap currentBmp;

    public Map(Resources rec, int pixelMapID) {
        halfGameSurfaceWidth = GameSurface.getSurfaceWidth() / 2;
        halfGameSurfaceHeight = GameSurface.getSurfaceHeight() / 2;
        TILE_SIDE = GameSurface.getSurfaceHeight() / TILES_IN_SCREEN_HEIGHT;
        pixelMap = BitmapFactory.decodeResource(rec, pixelMapID);
        TILES_IN_MAP_WIDTH = pixelMap.getWidth();
        TILES_IN_MAP_HEIGHT = pixelMap.getHeight();
        TILE_MAP = new Tile[pixelMap.getWidth()][pixelMap.getHeight()];
        scanPixelMap(rec);
        //minus one because the ++ in the coordinate distribution function has to be before the return
        blueCoordinateDistributionIndex = -1;
        greenCoordinateDistributionIndex = -1;
    }
    //todo:enhance the rendering and the maps
    // lololololo done
    public void render(Canvas canvas){
        User user = GameThread.getUser();
        int userXCoordinateInt = (int)(user.getXCoordinate());
        int userYCoordinateInt = (int)(user.getYCoordinate());
        float userXTranslation = userXCoordinateInt - user.getXCoordinate();
        float userYTranslation = userYCoordinateInt - user.getYCoordinate();
        for (int y = - (TILES_IN_SCREEN_HEIGHT / 2 + 1); y <= (TILES_IN_SCREEN_HEIGHT / 2 + 1); y++){
            for (int x = - (TILES_IN_SCREEN_WIDTH / 2); x <= (TILES_IN_SCREEN_WIDTH / 2 + 1); x++){
                /*
                if (userXCoordinateInt + x < 0 || userYCoordinateInt + y < 0 || userXCoordinateInt + x >= TILES_IN_MAP_WIDTH || userYCoordinateInt + y >= TILES_IN_MAP_HEIGHT){
                    currentBmp = voidTile.BMP;
                }else {
                    currentBmp = TILE_MAP[userXCoordinateInt + x][userYCoordinateInt + y].BMP;
                }
                */
                try {
                    currentBmp = TILE_MAP[userXCoordinateInt + x][userYCoordinateInt + y].BMP;
                } catch(ArrayIndexOutOfBoundsException e){
                    // this means its outside the map, so its set to a void tile
                    currentBmp = new VoidTile().BMP;
                }
                canvas.drawBitmap(currentBmp, halfGameSurfaceWidth + (userXTranslation + x) * TILE_SIDE, halfGameSurfaceHeight + (userYTranslation + y) * TILE_SIDE, null);

            }
        }
    }

    private void scanPixelMap(Resources rec){
        Bitmap blueLightBulbTileBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.light_bulb_tile_team_blue), TILE_SIDE, TILE_SIDE, false);
        Bitmap greenLightBulbTileBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.light_bulb_tile_team_green), TILE_SIDE, TILE_SIDE, false);
        int blueLightBulbStandDistributionIndex = 0;
        int greenLightBulbStandDistributionIndex = 0;
        blueCoordinateDistributionIndex = 0;
        greenCoordinateDistributionIndex = 0;
        for(int y = 0; y < pixelMap.getHeight(); y++){
            for (int x = 0; x < pixelMap.getWidth(); x++){
                if(pixelMap.getPixel(x, y) == 0xffffffff) {
                    TILE_MAP[x][y] = new GroundTile();
                }else if(pixelMap.getPixel(x, y) == 0xffff0000) {
                    TILE_MAP[x][y] = new WallTile();
                }else if (pixelMap.getPixel(x, y) == 0xff00ff00){
                    TILE_MAP[x][y] = new GreenBaseTile();
                }else if (pixelMap.getPixel(x, y) == 0xff0000ff){
                    TILE_MAP[x][y] = new BlueBaseTile();
                }else if (pixelMap.getPixel(x, y) == 0xff00ffff){
                    TILE_MAP[x][y] = new SpawnTile();
                    playerStartCoordinates[blueCoordinateDistributionIndex][0] = x + 0.5f;
                    playerStartCoordinates[blueCoordinateDistributionIndex][1] = y + 0.5f;
                    blueCoordinateDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xff01ffff){
                    TILE_MAP[x][y] = new SpawnTile();
                    playerStartCoordinates[greenCoordinateDistributionIndex + 4][0] = x + 0.5f;
                    playerStartCoordinates[greenCoordinateDistributionIndex + 4][1] = y + 0.5f;
                    greenCoordinateDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xffffff00){
                    TILE_MAP[x][y] = new LightBulbStand(x, y, blueLightBulbTileBmp, true, (byte) 0);
                    blueLightBulbStandArray[blueLightBulbStandDistributionIndex] = (LightBulbStand)TILE_MAP[x][y];
                    blueLightBulbStandDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xffffff01){
                    TILE_MAP[x][y] = new LightBulbStand(x, y, greenLightBulbTileBmp, true, (byte) 1);
                    greenLightBulbStandArray[greenLightBulbStandDistributionIndex] = (LightBulbStand) TILE_MAP[x][y];
                    greenLightBulbStandDistributionIndex++;
                }else{
                    TILE_MAP[x][y] = new VoidTile();
                }
            }
        }
    }

    public static boolean getSolid(int xTileCoordinate, int yTileCoordinate){
        return TILE_MAP[xTileCoordinate][yTileCoordinate].SOLID;
    }

    public static float getStartX(byte team) {
        if (team == 0){
            blueCoordinateDistributionIndex++;
            return playerStartCoordinates[blueCoordinateDistributionIndex][0];
        }else {
            greenCoordinateDistributionIndex++;
            return playerStartCoordinates[greenCoordinateDistributionIndex + 4][0];
        }
    }

    public static float getStartY(byte team) {
        if (team == 0){
            return playerStartCoordinates[blueCoordinateDistributionIndex][1];
        }else {
            return playerStartCoordinates[greenCoordinateDistributionIndex + 4][1];
        }
    }

    public static LightBulbStand[] getFriendlyLightBulbStands(byte team){
        if (team == 0){
            return blueLightBulbStandArray;
        }else {
            return greenLightBulbStandArray;
        }
    }

    public static LightBulbStand[] getHostileLightBulbStands(byte team){
        if (team == 1){
            return blueLightBulbStandArray;
        }else {
            return greenLightBulbStandArray;
        }
    }

    public static Bitmap getPixelMap() {
        return pixelMap;
    }
}