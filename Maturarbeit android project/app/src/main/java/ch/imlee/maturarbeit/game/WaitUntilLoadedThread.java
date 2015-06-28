package ch.imlee.maturarbeit.game;

import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLoadedEvent;

/**
 * Created by Lukas on 28.06.2015.
 */
public class WaitUntilLoadedThread extends Thread {
    private static int ready = 0;

    public static synchronized void incrementReady(){
        ++ready;
    }

    public void run(){
        while(ready <= Host.sockets.size()){
            try {
                Thread.sleep(10);
            } catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        new GameLoadedEvent().send();
        new GameLoadedEvent().handle((byte)0);
    }

}
