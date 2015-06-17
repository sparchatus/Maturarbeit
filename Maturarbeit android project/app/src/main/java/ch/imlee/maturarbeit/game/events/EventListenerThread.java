package ch.imlee.maturarbeit.game.events;

/**
 * Created by Lukas on 17.06.2015.
 */
public class EventListenerThread extends Thread {
    public void run(){
        while (true){
            // listen to events here. has to be started with the ChooseActivity, because Clients have to listen to the Host's gameStartEvent and gameCancelledEvent and the Host has to listen to a Client's gameLeaveEvent
        }
    }
}
