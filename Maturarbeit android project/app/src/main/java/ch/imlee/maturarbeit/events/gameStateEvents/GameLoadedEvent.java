package ch.imlee.maturarbeit.events.gameStateEvents;

import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.WaitUntilLoadedThread;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

// this Event is sent by the clients when they are done loading the game and then by the host when everyone is done loading
public class GameLoadedEvent extends GameStateEvent{

    public GameLoadedEvent() {
        super(GameThread.getUser().getID());
    }
    public GameLoadedEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
    }

    @Override
    public String toString(){
        return super.toString() + 'L' + 'i' + senderID;
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
