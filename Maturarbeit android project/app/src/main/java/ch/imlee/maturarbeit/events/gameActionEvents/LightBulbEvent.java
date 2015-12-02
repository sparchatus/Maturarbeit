package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/*
this Event gets sent only by the host
if the host picks up a LightBulb, he sends this Event straight away
if a client picks up a LightBulb, he first sends a LightBulbServerEvent for the host to verify that the LightBulb hasn't been picked up yet to avoid two players picking up the same LightBulb
 */
public class LightBulbEvent extends GameActionEvent{

    private final byte LIGHT_BULB_ID;
    private final boolean PICKED_UP;
    private final byte PLAYER_ID;

    // this constructor is used by the host when he picks up a LightBulb
    public LightBulbEvent(byte lightBulbID){
        super((byte)0);
        PICKED_UP = true;
        LIGHT_BULB_ID = lightBulbID;
        PLAYER_ID = 0;
    }
    // this constructor is called when a player loses a LightBulb
    public LightBulbEvent(byte lightBulbID, byte playerID){
        super(playerID);
        LIGHT_BULB_ID = lightBulbID;
        PICKED_UP = false;
        PLAYER_ID = playerID;
    }
    // this constructor is called in the apply method of the LightBulbServerEvent
    // the last argument is to assure that the method is called by the server
    public LightBulbEvent(byte playerID, byte lightBulbID, LightBulbServerEvent lightBulbServerEvent){
        super((byte)0);
        LIGHT_BULB_ID = lightBulbID;
        PICKED_UP = true;
        PLAYER_ID = playerID;
    }
    public LightBulbEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        LIGHT_BULB_ID = Byte.valueOf(eventString.substring(eventString.indexOf('l') + 1, eventString.indexOf('b')));
        PICKED_UP = eventString.charAt(eventString.indexOf('b') + 1) == 0;
        PLAYER_ID = Byte.valueOf(eventString.substring(eventString.indexOf('p') + 1, eventString.indexOf('i')));
    }

    @Override
    public String toString() {
        return super.toString() + 'B' + 'l' + LIGHT_BULB_ID + 'b' + ((PICKED_UP) ? 1 : 0) + 'p' + PLAYER_ID + 'i' + senderID;
    }

    @Override
    public void apply() {
        if (PICKED_UP) {
            GameThread.getPlayerArray()[PLAYER_ID].bulbReceived(LIGHT_BULB_ID);
        }else {
            GameThread.getPlayerArray()[PLAYER_ID].bulbLost();
        }
    }
}
