package ch.imlee.maturarbeit.events.gameStateEvents;

import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.WaitUntilLoadedThread;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

/**
 * Created by Lukas on 20.06.2015.
 */
public class GameLoadedEvent extends GameStateEvent{
    // this Event is used by the Client to notify the Host that it's finished loading and by the Host to notify the clients that every device has finished loading.
    public GameLoadedEvent() {
        super(GameThread.getUser().getID());
    }

    public GameLoadedEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
    }

    @Override
    public String toString(){
        return super.toString() + 'l' + 'i' + senderID;
    }

    @Override
    public boolean handle(byte i){
        if(StartActivity.deviceType == DeviceType.CLIENT){
            GameClient.startSynchronizedTick();
        } else {
            WaitUntilLoadedThread.incrementReady();
        }
        return true;
    }
}