package ch.imlee.maturarbeit.game.events.gameStateEvents;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.WaitUntilLoadedThread;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Lukas on 20.06.2015.
 */
public class GameLoadedEvent extends GameStateEvent{
    // this Event is used by the Client to notify the Host that it's finished loading and by the Host to notify the clients that every device has finished loading.
    @Override
    public String toString(){
        return super.toString() + 'l';
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
