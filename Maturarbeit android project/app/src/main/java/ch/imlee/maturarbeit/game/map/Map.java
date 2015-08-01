package ch.imlee.maturarbeit.game.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 04.06.2015.
 */
public class Map {
    private final int TILES_IN_SCREEN_WIDTH= 16;
    private final int TILES_IN_SCREEN_HEIGHT = 9;
    private final int BORDER_TILES_TOP = 5;
    private final int BORDER_TILES_RIGHT = 8;
    public final int TILE_SIDE;
    private final float MAP_WIDTH, MAP_HEIGHT;
    private final float TEAM_1_START_X, TEAM_1_START_Y, TEAM_2_START_X, TEAM_2_START_Y;

    private final Tile[][]TILE_MAP;

    public Map(Resources rec, int pixelMapID) {
        TILE_SIDE = GameClient.getScreenHeight() / TILES_IN_SCREEN_HEIGHT;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap pixelMap = BitmapFactory.decodeResource(rec, pixelMapID, options);
        MAP_WIDTH = pixelMap.getWidth();
        MAP_HEIGHT = pixelMap.getHeight();
        TEAM_1_START_X = 8.5f;
        TEAM_1_START_Y = 5.5f;
        TEAM_2_START_X = MAP_WIDTH - 8.5f;
        TEAM_2_START_Y = MAP_HEIGHT - 5.5f;
        Tile voidTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.void_tile), TILE_SIDE, TILE_SIDE, false), false);
        Tile groundTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.ground_tile), TILE_SIDE, TILE_SIDE, false), false);
        Tile wallTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.wall_tile), TILE_SIDE, TILE_SIDE, false), true);
        Tile greenBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.green_base_tile), TILE_SIDE, TILE_SIDE, false), true);
        Tile blueBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.blue_base_tile), TILE_SIDE, TILE_SIDE, false), true);
        TILE_MAP = new Tile[pixelMap.getWidth()][pixelMap.getHeight()];
        for(int i = 0; i < pixelMap.getHeight(); i++){
            for (int j = 0; j < pixelMap.getWidth(); j++){
                if(pixelMap.getPixel(j, i) == 0xffffffff) {
                    TILE_MAP[j][i] = groundTile;
                }else if(pixelMap.getPixel(j, i) == 0xffff0000) {
                    TILE_MAP[j][i] = wallTile;
                }else if (pixelMap.getPixel(j, i) == 0xff00ff00){
                    TILE_MAP[j][i] = greenBaseTile;
                }else if (pixelMap.getPixel(j, i) == 0xff0000ff){
                    TILE_MAP[j][i] = blueBaseTile;
                }else{
                    TILE_MAP[j][i] = voidTile;
                }
            }
        }
    }

    public Canvas render(Canvas canvas, User user){
        //TODO: is it really necessary to return the canvas?
        for (int i = - BORDER_TILES_TOP; i <= BORDER_TILES_TOP; i++){
            for (int j = - BORDER_TILES_RIGHT; j <= BORDER_TILES_RIGHT; j++){
                canvas.drawBitmap(TILE_MAP[((int) user.getXCoordinate()) + j][((int) user.getYCoordinate()) + i].BMP, GameClient.getHalfScreenWidth() + (((int) user.getXCoordinate()) + j - user.getXCoordinate()) * user.PLAYER_SIDE, GameClient.getHalfScreenHeight() + (((int) user.getYCoordinate()) + i - user.getYCoordinate())* user.PLAYER_SIDE, null);
            }
        }
        return canvas;
    }
    public Canvas renderMinimap(Canvas canvas){

        return canvas;
    }

    public boolean getSolid(int xTileCoordinate, int yTileCoordinate){
        return TILE_MAP[xTileCoordinate][yTileCoordinate].SOLID;
    }

    public float getStartX(byte team) {
        if (team == 1){
            return TEAM_1_START_X;
        }else {
            return TEAM_2_START_X;
        }
    }

    public float getStartY(byte team) {
        if (team == 1){
            return TEAM_1_START_Y;
        }else {
            return TEAM_2_START_Y;
        }
    }
}
