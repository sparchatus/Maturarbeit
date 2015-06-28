package ch.imlee.maturarbeit.game.events.gameStateEvents;

import android.content.Intent;

import java.util.ArrayList;

import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.main.ChooseActivity;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Lukas on 17.06.2015.
 */
public class GameStartEvent extends GameStateEvent {


    @Override
    public boolean handle(byte i){
        // start the game (finally)
        Util.c.startActivity(new Intent(Util.c, GameClient.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return true;
    }

    private ArrayList<PlayerType> types = new ArrayList<>();
    private ArrayList<Byte> teams = new ArrayList<>();
    private final int MAP_ID;
    private byte userID = 1;

    public GameStartEvent(String string){
        string = string.substring(2);
        while(string.charAt(0) != 'i'){
            byte i = 0;
            this.addPlayer(PlayerType.values()[Integer.parseInt(Character.toString(string.charAt(0)))], (byte) string.charAt(1), i);
            string = string.substring(2);
            ++i;
        }
        userID = Byte.parseByte(Character.toString(string.charAt(1)));
        MAP_ID = Integer.parseInt(string.substring(3));
    }

    public GameStartEvent(ArrayList<PlayerType> types, ArrayList<Byte> teams, byte userID, int mapID) {
        initializeArrays();
        this.types = types;
        this.teams = teams;
        this.userID = userID;
        MAP_ID = mapID;
    }

    public GameStartEvent(int mapID){
        initializeArrays();
        MAP_ID = mapID;
    }

    private void initializeArrays(){
        try{
            types.get(1);
            teams.get(1);
        } catch(Exception e){
            e.printStackTrace();
            for(int i = 0; i <= Host.sockets.size(); ++i){
                types.add(PlayerType.NULL);
                teams.add((byte)-1);
            }
        }
    }

    public void addPlayer(PlayerType type, byte team, byte id){
        initializeArrays();
        types.set(id, type);
        teams.set(id, team);
    }

    public int getPlayerCount(){
        int temp = 0;
        for(int i = 0; i < types.size(); ++i){
            if(types.get(i) != PlayerType.NULL){
                ++temp;
            }
        }
        return temp;
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
            playerInfo += types.get(i).ordinal();
            playerInfo += teams.get(i);
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
        // Host has the userID 0, the others are 1 - N, where N is the number of Clients. The numeration corresponds to the position in the Host.sockets position
        for(; userID <= Host.sockets.size(); ++userID){
            // so the client knows which userID he has, send him this.toString(), which contains it.
            Util.sendString(Host.outputStreams.get(userID-1), this.toString() + '|');
            System.out.println("...");
            System.out.println("sent Event: " + this.toString());
            System.out.println("...");
        }
    }
}