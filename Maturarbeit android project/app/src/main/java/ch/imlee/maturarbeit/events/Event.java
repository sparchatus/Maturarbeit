package ch.imlee.maturarbeit.events;

import android.util.Log;

import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.events.gameActionEvents.GameActionEvent;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStateEvent;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.utils.LogView;

// this class is the parent of all other Event classes (excluding EventHandler and EventReceiver). Here are the methods that are used and / or overwritten by the child classes
public class Event {
    // the serverEvent boolean is used to determine whether or not an Event should be forwarded to the clients or not
    public boolean serverEvent = false;
    private static final String invalidEvent = "INVALID EVENT";
    //senderID = -1 happens when the event is invalid, otherwise it's the device ID of the sender
    protected byte senderID = 0; // this is overwritten in most events. If not, 0 is correct

    public Event(){}

    public Event (byte senderID){
        this.senderID = senderID;
    }

    // the toString method gets overwritten by every Event class. It returns a String containing all the information of the Event's variables, allowing you to reconstruct an Event Object out of this String
    public String toString(){
        return invalidEvent;
    }

    // apply() gets overwritten by every GameActionEvent child. It's purpose is to apply the Event to the actual game.
    public void apply(){}

    // getType gets called in the fromString method and is used to reconstruct an Event Object out of a String.
    public static EventType getType(String string){
        switch (string.charAt(0)){
            // the first character of every eventString is always a 'G' or an 'A'
            case 'G': return EventType.GAMESTATE;
            case 'A': return EventType.GAMEACTION;
            // the default case only applies if an eventString isn't sent or received properly
            default: return null;
        }
    }

    public static Event fromString(String string){
        switch(getType(string)){

            case GAMESTATE: return GameStateEvent.fromString(string);
            case GAMEACTION: return GameActionEvent.fromString(string);
            // for an invalid Event, return null
            default: return null;
        }
    }

    public void send(){
        if(StartActivity.deviceType == DeviceType.HOST){
            // send the Event only if it isn't a serverEvent
            if(!serverEvent) {
                this.sendAsHost();
            }
        } else{
            this.sendAsClient();
        }
        String logText = "Event sent: " + this;
        LogView.addLog(logText);
        Log.v("event", logText);
    }

    private void sendAsHost(){
        // this method sends the eventString to every client except the one who sent the event to the host (if it didn't originate from the host)
        for(int i = 0; i < Host.outputStreams.size(); ++i){
            // the '|' character is to let the other device know that the Event is finished
            if(senderID != i+1) Util.sendString(Host.outputStreams.get(i), this.toString() + '|');
        }
    }
    private void sendAsClient(){
        // this method sends the eventString to the host
        Util.sendString(Client.outputStream, this.toString() + '|');
    }

    public boolean handle(byte i){
        // if the child class didn't override the handle method, return false so that the Event will be handled in the GameThread
        return false;
    }

    public EventType getType(){
        return getType(this.toString());
    }

    public byte getSenderID() {
        return senderID;
    }
}


















