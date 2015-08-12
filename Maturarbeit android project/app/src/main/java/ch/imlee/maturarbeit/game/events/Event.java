package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.events.gameActionEvents.GameActionEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.PlayerMotionEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameCancelledEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLeftEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLoadedEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GamePausedEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.StunEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStateEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.PlayerStatsSelectedEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Sandro on 13.06.2015.
 */
public class Event {
    private static final String invalidEvent = "INVALID EVENT";

    public String toString(){
        return invalidEvent;
    }

    public void apply(GameThread gameThread){
        // the events get applied in the subclasses
    }

    public EventType getType(){
        return getType(this.toString());
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
        }
        // invalid event:
        return new Event();
    }

    public void send(){
        if(StartActivity.deviceType == DeviceType.HOST){
            this.sendAsHost();
        } else{
            this.sendAsClient();
        }
        System.out.println("...");
        System.out.println("Event sent: " + this.toString());
    }

    private void sendAsHost(){
        for(int i = 0; i < Host.outputStreams.size(); ++i){
            // the '|' character is to let the other device know that the Event is finished
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

}


















