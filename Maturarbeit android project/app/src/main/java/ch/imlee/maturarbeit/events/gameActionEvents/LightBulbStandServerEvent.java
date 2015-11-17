package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.LightBulbStand;
import ch.imlee.maturarbeit.game.map.Map;

// this Event gets created by a client when he wants to put his LightBulb onto one of his team's LightBulbStands
public class LightBulbStandServerEvent extends GameActionEvent {

    public final byte STAND_ID;

    public LightBulbStandServerEvent(byte standID){
        super(GameThread.getUser().getID());
        serverEvent = true;
        STAND_ID = standID;
    }
    public LightBulbStandServerEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        serverEvent = true;
        STAND_ID = Byte.valueOf(eventString.substring(eventString.indexOf('s') + 1, eventString.indexOf('i')));
    }

    @Override
    public String toString() {
        return super.toString() + 'F' + 's' + STAND_ID + 'i' + senderID;
    }
    @Override
    public void apply() {
        /*
         when the server receives a LightBulbStandServerEvent, he has to check whether the LightBulbStand in question is still free
         otherwise it would lead to conflicts if two allies wanted to put a LightBulb on the same LightBulbStand at the same time.
         When this check is successful, the host checks if the other LightBulbStand of this team is also occupied. If so, he sends a GameWinEvent and the game ends
        */
        if (Map.getFriendlyLightBulbStands(GameThread.getPlayerArray()[senderID].TEAM)[STAND_ID].isFree()){
            LightBulbStandEvent lightBulbStandEvent = new LightBulbStandEvent(this);
            lightBulbStandEvent.send();
            lightBulbStandEvent.apply();
            for (LightBulbStand lightBulbStand : Map.getFriendlyLightBulbStands(GameThread.getPlayerArray()[senderID].TEAM)){
                if (lightBulbStand.isFree()){
                    return;
                }
            }
            new GameWinEvent(GameThread.getPlayerArray()[senderID].TEAM).send();
            new GameWinEvent(GameThread.getPlayerArray()[senderID].TEAM).apply();
        }

    }
}
