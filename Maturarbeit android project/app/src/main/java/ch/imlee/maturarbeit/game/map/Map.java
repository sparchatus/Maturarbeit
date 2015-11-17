package ch.imlee.maturarbeit.game.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.utils.LogView;
import ch.imlee.maturarbeit.views.GameSurface;

// the map is built up by little squares which get placed on the screen.
// the information about the Map is taken from a picture file where every pixel represents a Tile
public class Map implements MapDimensions {

    public static int TILE_SIDE, TILES_IN_MAP_WIDTH, TILES_IN_MAP_HEIGHT;
    private int halfGameSurfaceWidth, halfGameSurfaceHeight;

    // used to distribute the different spawn points
    private static int blueCoordinateDistributionIndex, greenCoordinateDistributionIndex;

    private static float[][] playerStartCoordinates = new float[8][2];

    // the actual map data consisting of references to Tiles
    public static Tile[][] TILE_MAP;
    public static LightBulbStand[] blueLightBulbStandArray = new LightBulbStand[2];
    public static LightBulbStand[] greenLightBulbStandArray = new LightBulbStand[2];
    private static Bitmap pixelMap;
    private Tile voidTile, groundTile, wallTile, greenBaseTile, blueBaseTile, spawnTile;
    private Bitmap currentBmp;

    public Map(Resources rec, int pixelMapID) {
        halfGameSurfaceWidth = GameSurface.getSurfaceWidth() / 2;
        halfGameSurfaceHeight = GameSurface.getSurfaceHeight() / 2;
        TILE_SIDE = GameSurface.getSurfaceHeight() / TILES_IN_SCREEN_HEIGHT;
        pixelMap = BitmapFactory.decodeResource(rec, pixelMapID);
        TILES_IN_MAP_WIDTH = pixelMap.getWidth();
        TILES_IN_MAP_HEIGHT = pixelMap.getHeight();

        // initializing the different Tiles
        voidTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.void_tile), TILE_SIDE, TILE_SIDE, false), false, true);
        groundTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.ground_tile), TILE_SIDE, TILE_SIDE, false), false, false);
        wallTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.wall_tile), TILE_SIDE, TILE_SIDE, false), true, false);
        greenBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.green_base_tile), TILE_SIDE, TILE_SIDE, false), true, false);
        blueBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.blue_base_tile), TILE_SIDE, TILE_SIDE, false), true, false);
        spawnTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.spawn_tile), TILE_SIDE, TILE_SIDE, false), false, false);
        TILE_MAP = new Tile[pixelMap.getWidth()][pixelMap.getHeight()];
        scanPixelMap(rec);

        //minus one because the ++ in the coordinate distribution function has to be before the return
        blueCoordinateDistributionIndex = -1;
        greenCoordinateDistributionIndex = -1;
    }
    public void render(Canvas canvas){
        User user = GameThread.getUser();
        int  userXCoordinateInt = (int)(user.getXCoordinate());
        int userYCoordinateInt = (int)(user.getYCoordinate());

        // distance of the user to the tile origin(top left corner of the tile)
        float userXTranslation = userXCoordinateInt - user.getXCoordinate();
        float userYTranslation = userYCoordinateInt - user.getYCoordinate();
        for (int y = - (TILES_IN_SCREEN_HEIGHT / 2 + 1); y <= (TILES_IN_SCREEN_HEIGHT / 2 + 1); y++){
            for (int x = - (TILES_IN_SCREEN_WIDTH / 2 + 1); x <= (TILES_IN_SCREEN_WIDTH / 2 + 1); x++){
                if (userXCoordinateInt + x < 0 || userYCoordinateInt + y < 0 || userXCoordinateInt + x >= TILES_IN_MAP_WIDTH || userYCoordinateInt + y >= TILES_IN_MAP_HEIGHT){
                    currentBmp = voidTile.BMP;
                }else {
                    currentBmp = TILE_MAP[userXCoordinateInt + x][userYCoordinateInt + y].BMP;
                }
                canvas.drawBitmap(currentBmp, halfGameSurfaceWidth + (userXTranslation + x) * TILE_SIDE, halfGameSurfaceHeight + (userYTranslation + y) * TILE_SIDE, null);
            }
        }
        LogView.addLog(String.valueOf(halfGameSurfaceHeight));
    }

    private void scanPixelMap(Resources rec){
        Bitmap blueLightBulbTileBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.light_bulb_tile_team_blue), TILE_SIDE, TILE_SIDE, false);
        Bitmap greenLightBulbTileBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.light_bulb_tile_team_green), TILE_SIDE, TILE_SIDE, false);

        // used to place the LightBulb Stands in the according array
        int blueLightBulbStandDistributionIndex = 0;
        int greenLightBulbStandDistributionIndex = 0;

        //
        blueCoordinateDistributionIndex = 0;
        greenCoordinateDistributionIndex = 0;

        for(int y = 0; y < pixelMap.getHeight(); y++){
            for (int x = 0; x < pixelMap.getWidth(); x++){
                if(pixelMap.getPixel(x, y) == 0xffffffff) {
                    TILE_MAP[x][y] = groundTile;
                }else if(pixelMap.getPixel(x, y) == 0xffff0000) {
                    TILE_MAP[x][y] = wallTile;
                }else if (pixelMap.getPixel(x, y) == 0xff00ff00){
                    TILE_MAP[x][y] = greenBaseTile;
                }else if (pixelMap.getPixel(x, y) == 0xff0000ff){
                    TILE_MAP[x][y] = blueBaseTile;
                }else if (pixelMap.getPixel(x, y) == 0xff00ffff){
                    TILE_MAP[x][y] = spawnTile;
                    playerStartCoordinates[blueCoordinateDistributionIndex][0] = x + 0.5f;
                    playerStartCoordinates[blueCoordinateDistributionIndex][1] = y + 0.5f;
                    blueCoordinateDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xff01ffff){
                    TILE_MAP[x][y] = spawnTile;
                    playerStartCoordinates[greenCoordinateDistributionIndex + 4][0] = x + 0.5f;
                    playerStartCoordinates[greenCoordinateDistributionIndex + 4][1] = y + 0.5f;
                    greenCoordinateDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xffffff00){
                    TILE_MAP[x][y] = new LightBulbStand(x, y, blueLightBulbTileBmp, (byte) 0, (byte)blueLightBulbStandDistributionIndex);
                    blueLightBulbStandArray[blueLightBulbStandDistributionIndex] = (LightBulbStand)TILE_MAP[x][y];
                    blueLightBulbStandDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xffffff01){
                    TILE_MAP[x][y] = new LightBulbStand(x, y, greenLightBulbTileBmp, (byte) 1, (byte) greenLightBulbStandDistributionIndex);
                    greenLightBulbStandArray[greenLightBulbStandDistributionIndex] = (LightBulbStand) TILE_MAP[x][y];
                    greenLightBulbStandDistributionIndex++;
                }else{
                    TILE_MAP[x][y] = voidTile;
                }
            }
        }
    }

    // returns if the Tile at the targeted location is solid
    public static boolean getSolid(int xTileCoordinate, int yTileCoordinate){
        if (xTileCoordinate < 0 || yTileCoordinate < 0 || xTileCoordinate >= TILE_MAP.length || yTileCoordinate >= TILE_MAP[0].length) {
            return false;
        }
        return TILE_MAP[xTileCoordinate][yTileCoordinate].SOLID;
    }

    public static float getStartX(byte team) {
        if (team == 0){
            blueCoordinateDistributionIndex++;
            return playerStartCoordinates[blueCoordinateDistributionIndex % 4][0];
        }else {
            greenCoordinateDistributionIndex++;
            return playerStartCoordinates[greenCoordinateDistributionIndex % 4 + 4][0];
        }
    }

    public static float getStartY(byte team) {
        if (team == 0){
            return playerStartCoordinates[blueCoordinateDistributionIndex % 4][1];
        }else {
            return playerStartCoordinates[greenCoordinateDistributionIndex % 4 + 4][1];
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