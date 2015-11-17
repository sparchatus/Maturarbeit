package ch.imlee.maturarbeit.events.gameActionEvents;

import java.io.OutputStream;

import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameThread;

/*
this Event gets sent only by the host
if the host picks up a LightBulb, he sends this Event straight away
if a client picks up a LightBulb, he first sends a LightBulbServerEvent for the host to verify that the LightBulb hasn't been picked up yet to avoid two players picking up the same LightBulb
 */
public class LightBulbEvent extends GameActionEvent{

    private final byte LIGHT_BULB_ID;
    private final boolean PICKED_UP;

    // this constructor is used by the host when he picks up a LightBulb
    public LightBulbEvent(byte lightBulbID){
        super((byte)0);
        PICKED_UP = true;
        LIGHT_BULB_ID = lightBulbID;
    }
    // this constructor is called when a player loses a LightBulb
    public LightBulbEvent(byte lightBulbID, byte playerID){
        super(playerID);
        LIGHT_BULB_ID =lightBulbID;
        PICKED_UP = false;
    }
    // this constructor is called in the apply method of the LightBulbServerEvent
    public LightBulbEvent(LightBulbServerEvent lightBulbServerEvent){
        super(lightBulbServerEvent.getSenderID());
        LIGHT_BULB_ID = lightBulbServerEvent.getLIGHT_BULB_ID();
        PICKED_UP = true;
    }
    public LightBulbEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        LIGHT_BULB_ID = Byte.valueOf(eventString.substring(eventString.indexOf('1') + 1, eventString.indexOf('b')));
        PICKED_UP = eventString.charAt(eventString.indexOf('b') + 1) == '0';
    }

    @Override
    public void send(){
        // this has to be overwritten because every client including the sender of the LightBulbServerEvent has to receive it
        if(StartActivity.deviceType == DeviceType.CLIENT){
            Util.sendString(Client.outputStream, this.toString() + '|');
        } else {
            for(OutputStream outputStream : Host.outputStreams){
                Util.sendString(outputStream, this.toString() + '|');
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + 'B' + 'l' + LIGHT_BULB_ID + 'b' + ((PICKED_UP) ? 1 : 0) + 'i' + senderID;
    }

    @Override
    public void apply() {
        if (PICKED_UP) {
            GameThread.getPlayerArray()[senderID].bulbReceived(LIGHT_BULB_ID);
        }else {
            GameThread.getPlayerArray()[senderID].bulbLost();
        }
    }
}
