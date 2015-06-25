package ch.imlee.maturarbeit.game.events.gameStateEvents;

import android.content.Intent;

import java.util.ArrayList;

import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Lukas on 17.06.2015.
 */
public class GameStartEvent extends GameStateEvent {


    @Override
    public boolean handle(){
        // start the game (finally)
        Util.c.startActivity(new Intent(Util.c, GameClient.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return true;
    }

    private ArrayList<PlayerType> types = new ArrayList<>();
    private ArrayList<Byte> teams = new ArrayList<>();
    private final int MAP_ID;
    private byte userID = 0;

    public GameStartEvent(ArrayList<PlayerType> types, ArrayList<Byte> teams,  int mapID) {
        this.types = types;
        this.teams = teams;
        MAP_ID = mapID;
    }

    public GameStartEvent(int mapID){
        MAP_ID = mapID;
    }

    public void addPlayer(PlayerType type, byte team){
        types.add(type);
        teams.add(team);
    }

    public int getPlayerCount(){
        return types.size();
    }

    public int getUserID(){
        return userID;
    }

    public int getMapID(){
        return MAP_ID;
    }

    public ArrayList<PlayerType> getPlayerTypes(){
        return types;
    }

    public ArrayList<Byte> getTeams(){
        return teams;
    }

    public String toString() {
        String playerInfo = "";
        for (int i = 0; i < types.size(); i++) {
            playerInfo += "p" + types.get(i).ordinal() + "t" + teams.get(i);
        }
        return super.toString() + 'S' + playerInfo + "i" + userID + "m" + MAP_ID;
    }

    @Override
    public void send(){
        // should only be sent as Host
        if(StartActivity.deviceType == DeviceType.CLIENT){
            try{
                throw new Exception("GameStartEvent can only be sent by the Host");
            } catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        // Host has the userID 0, the others are 1 - N, where N is the number of Clients
        while(userID < teams.size()){
            ++userID;
            // so the client knows which userID he has, send him this.toString(), which contains it.
            Util.sendString(Host.outputStreams.get(userID-1), this.toString());
        }
    }
}