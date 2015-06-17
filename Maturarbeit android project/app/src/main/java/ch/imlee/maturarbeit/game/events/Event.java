package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.views.GameSurface;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Sandro on 13.06.2015.
 */
public class Event {
    private static final String invalidEvent = "INVALID EVENT";

    public String toString(){
        if(this instanceof GameStartEvent){
            return "G";
        }
        return invalidEvent;
    }

    public void apply(GameSurface.GameThread gameThread){

    }

    public EventType getType(){
        if(this.toString().equals(invalidEvent)) return null;
        switch (this.toString().toCharArray()[0]){
            case 'G': return EventType.GAMESTARTEVENT;
            case 'P': return EventType.PLAYERMOTIONEVENT;
            case 'S': return EventType.STUNEVENT;
        }
        return null;
    }

    public void send(){

        if(StartActivity.deviceType == DeviceType.HOST){
            this.sendAsHost();
        } else{
            this.sendAsClient();
        }
    }

    private void sendAsHost(){
        for(int i = 0; i < Host.outputStreams.size(); ++i){
            // the '|' character is to let the other device know that the Event is finished
            Util.sendString(Host.outputStreams.get(i), this.toString() + '|');
        }
    }
    private void sendAsClient(){
        Util.sendString(Client.outputStream, this.toString());
    }
}
