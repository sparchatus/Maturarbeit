package ch.imlee.maturarbeit.game;

import android.util.Log;

import java.util.ArrayList;

import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.events.gameStateEvents.GameLoadedEvent;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.Sound.Sound;
import ch.imlee.maturarbeit.game.entity.Fluffy;
import ch.imlee.maturarbeit.game.entity.Ghost;
import ch.imlee.maturarbeit.game.entity.LightBulb;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.Slime;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.views.GameSurface;
import ch.imlee.maturarbeit.views.JoystickSurface;
import ch.imlee.maturarbeit.views.MiniMap;

public class StartDataInitializer {

    public static void setStartData(GameStartEvent startData, GameThread gameThread){
        Sound.setup();
        Sound.play(Sound.BACKGROUND);
        gameThread.map = new Map(startData.getMapID());
        gameThread.playerArray = new Player[startData.getPlayerCount()];
        gameThread.particleListArray = new ArrayList[startData.getPlayerCount()];
        for(int i = 0; i < gameThread.particleListArray.length; ++i){
            gameThread.particleListArray[i] = new ArrayList<>();
        }
        for (byte i = 0; i < startData.getPlayerCount(); i++){
            if (i == startData.getUserID()){
                switch (startData.getPlayerTypes().get(i)){
                    case FLUFFY:gameThread.user = new Fluffy(gameThread.map, startData.getTeams().get(i), i, startData.getName(i));
                        break;
                    case GHOST:gameThread.user = new Ghost(gameThread.map, startData.getTeams().get(i), i, startData.getName(i));
                        break;
                    case SLIME:gameThread.user = new Slime(gameThread.map, startData.getTeams().get(i), i, startData.getName(i));
                        break;
                    case NULL: Log.e("GameThread", "User PlayerType is NULL");
                }
                gameThread.playerArray[i] = gameThread.user;
                Player.setUser(gameThread.user);
            }else {
                gameThread.playerArray[i] = new Player(startData.getPlayerTypes().get(i), gameThread.map, startData.getTeams().get(i), i, startData.getName(i));
            }
        }
        GameSurface.setup();
        JoystickSurface.setup();
        MiniMap.setup();
        // adding the LightBulbs to the game
        gameThread.lightBulbArray = new LightBulb[2];
        gameThread.lightBulbArray[0] = new LightBulb((byte) 0, (byte) 0);
        gameThread.lightBulbArray[1] = new LightBulb((byte) 1, (byte) 1);
        if(StartActivity.deviceType == DeviceType.CLIENT) {
            new GameLoadedEvent().send();
        } else {
            WaitUntilLoadedThread.incrementReady();
        }
    }
}
