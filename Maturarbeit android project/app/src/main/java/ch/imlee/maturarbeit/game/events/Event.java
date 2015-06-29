package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.events.gameActionEvents.PlayerMotionEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameCancelledEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLeftEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLoadedEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GamePausedEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.StunEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
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

    public void apply(GameSurface.GameThread gameThread){

    }

    public EventType getType(){
        if(this.toString().equals(invalidEvent)) return null;
        switch (this.toString().toCharArray()[0]){
            case 'G': return EventType.GAMESTATE;
            case 'P': return EventType.PLAYERMOTION;
            case 'S': return EventType.SKILL;
        }
        return null;
    }

    public static Event fromString(String string){
        switch (string.toCharArray()[0]){
            case 'P': return new PlayerMotionEvent(string);
            case 'S': switch (string.toCharArray()[1]){
                case 'S': return new StunEvent(string);
            }
            case 'G': switch (string.toCharArray()[1]){
                case 'C': return new GameCancelledEvent();
                case 'L': return new GameLeftEvent();
                case 's': return new PlayerStatsSelectedEvent(string);
                case 'P': return new GamePausedEvent();
                case 'S': return new GameStartEvent(string);
                case 'l': return new GameLoadedEvent();
            }
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


















