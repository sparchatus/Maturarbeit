package ch.imlee.maturarbeit.game.events;

import android.util.Log;

import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.events.gameActionEvents.GameActionEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStateEvent;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Sandro on 13.06.2015.
 */
public class Event {
    public boolean serverEvent = false;
    private static final String invalidEvent = "INVALID EVENT";
    //senderID = -1 happens when the event is invalid
    protected byte senderID;

    public Event(){}

    public Event (byte senderID){
        this.senderID = senderID;
    }

    public String toString(){
        return invalidEvent;
    }

    public void apply(GameThread gameThread){
        // the events get applied in the subclasses
    }

    public static EventType getType(String string){
        switch (string.charAt(0)){
            case 'G': return EventType.GAMESTATE;
            case 'A': return EventType.GAMEACTION;
            default: return null;
        }
    }

    public static Event fromString(String string){
        switch(getType(string)){
            case GAMESTATE: return GameStateEvent.fromString(string);
            case GAMEACTION: return GameActionEvent.fromString(string);
            // invalid event:
            default: return new Event((byte) -1);
        }
    }

    public void send(){
        if(StartActivity.deviceType == DeviceType.HOST){
            if(!serverEvent) {
                this.sendAsHost();
            }
        } else{
            this.sendAsClient();
        }
        Log.v("event", "Event sent: " + this.toString());
    }

    private void sendAsHost(){
        for(int i = 0; i < Host.outputStreams.size(); ++i){
            // the '|' character is to let the other device know that the Event is finished
            if(senderID != i+1);
            Util.sendString(Host.outputStreams.get(i), this.toString() + '|');
        }
    }
    private void sendAsClient(){
        Util.sendString(Client.outputStream, this.toString() + '|');
    }

    public boolean handle(byte i){
        // handled in the update method, return false. If a subclass handles itself, it returns true
        return false;
    }

    public EventType getType(){
        return getType(this.toString());
    }

    public byte getSenderID() {
        return senderID;
    }
}


















