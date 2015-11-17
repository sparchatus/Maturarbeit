package ch.imlee.maturarbeit.events.gameStateEvents;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.PlayerType;

/*
this Event is sent by the host when everyone has chosen a team and a PlayerType. It notifies the clients that they should start loading the game
this event contains all the information the clients need, like all the players teams, all their names etc.
*/
public class GameStartEvent extends GameStateEvent {

    private ArrayList<PlayerType> types = new ArrayList<>();
    private ArrayList<Byte> teams = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private final int MAP_ID;
    private byte userID = 1;

    public GameStartEvent(){
        MAP_ID = R.drawable.test_map_3;
    }
    public GameStartEvent(String eventString){
        eventString = eventString.substring(2);
        while(eventString.charAt(0) != 'i'){
            types.add(PlayerType.values()[Integer.parseInt(Character.toString(eventString.charAt(0)))]);
            teams.add(Byte.parseByte(Character.toString(eventString.charAt(1))));
            int i = eventString.indexOf('\n');
            names.add(eventString.substring(2, i-1));
            eventString = eventString.substring(i);
        }
        userID = Byte.parseByte(Character.toString(eventString.charAt(1)));
        MAP_ID = Integer.parseInt(eventString.substring(3));
    }

    // this toString method returns a different String for every userID
    public String toString() {
        String playerInfo = "";
        for (int i = 0; i < types.size(); i++) {
            playerInfo += types.get(i).ordinal();
            playerInfo += teams.get(i);
            playerInfo += names.get(i) + '\n';
            // the '\n' is the terminating character of every device's name. It is used to identify its last character
        }
        return super.toString() + 'S' + playerInfo + "i" + userID + "m" + MAP_ID;
    }

    // the send method needs to be overridden, because everyone's eventString contains their individual ID
    @Override
    public void send(){
        // Host has the userID 0, the others are 1 - N, where N is the number of Players. The numeration corresponds to the position in the Host.sockets ArrayList
        for(; userID <= Host.sockets.size(); ++userID){
            // so the client knows which userID he has, send him this.toString(), which contains it.
            Util.sendString(Host.outputStreams.get(userID - 1), toString() + '|');
            Log.i("game", "GameStartEvent sent to player " + userID);
            Log.w("GameStartEvent", toString());
        }
    }

    @Override
    public boolean handle(byte i){
        // start the game (finally)
        Util.c.startActivity(new Intent(Util.c, GameClient.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        //GameClient.initializeStartData(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!GameClient.getSurfaceCreated()){
                    try{
                        Thread.sleep(20);
                    }catch(Exception e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
                GameClient.initializeStartData(GameStartEvent.this);
            }
        }).start();
        return true;
    }

    // this method is a workaround for ArrayIndexOutOfBoundsExceptions
    private void initializeArrays(int size){
        for(int i = types.size(); i < size; ++i){
            types.add(PlayerType.NULL);
            teams.add((byte)(-1));
            names.add("");
        }
    }


    public void setPlayer(PlayerType type, byte team, byte id, String name){
        initializeArrays(id+1);
        types.set(id, type);
        teams.set(id, team);
        names.set(id, name);
    }

    public void setUserID(byte id){
        userID = id;
    }


    public int getPlayerCount(){
        int temp = 0;
        for(int i = 0; i < teams.size(); ++i){
            if(teams.get(i) >= 0){
                ++temp;
            }
        }
        return temp;
    }

    public String getName(byte id){
        return names.get(id);
    }

    public byte getUserID(){
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


}