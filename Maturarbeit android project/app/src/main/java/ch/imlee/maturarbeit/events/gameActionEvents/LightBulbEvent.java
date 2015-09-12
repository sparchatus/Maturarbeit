package ch.imlee.maturarbeit.events.gameActionEvents;

import java.io.OutputStream;

import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 12.08.2015.
 */
public class LightBulbEvent extends GameActionEvent{

    private final byte LIGHT_BULB_ID;
    private final boolean PICKED_UP;

    public LightBulbEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        LIGHT_BULB_ID = Byte.valueOf(eventString.substring(eventString.indexOf("l") + 1, eventString.indexOf("b")));;
        if (eventString.substring(eventString.indexOf("b") + 1, eventString.indexOf("i")).equals("0")){
            PICKED_UP = false;
        } else {
            PICKED_UP = true;
        }
    }

    public LightBulbEvent(LightBulbServerEvent lightBulbServerEvent){
        super(lightBulbServerEvent.getSenderID());
        LIGHT_BULB_ID = lightBulbServerEvent.getLIGHT_BULB_ID();
        PICKED_UP = true;
    }

    public LightBulbEvent(byte lightBulbID, byte playerID){
        super(playerID);
        LIGHT_BULB_ID =lightBulbID;
        PICKED_UP = false;
    }

    @Override
    public void send(){
        // this has to be overwritten because the sender has to receive it again
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
        if (PICKED_UP) {
            return super.toString() + 'B'+ 'l' + LIGHT_BULB_ID + 'b' + "1" + 'i' + senderID;
        }else{
            return super.toString()+ 'B' + 'l' + LIGHT_BULB_ID + 'b' + "0" + 'i' + senderID;
        }
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
