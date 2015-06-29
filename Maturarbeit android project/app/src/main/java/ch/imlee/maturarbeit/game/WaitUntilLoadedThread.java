package ch.imlee.maturarbeit.game;

import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameLoadedEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Lukas on 28.06.2015.
 */
public class WaitUntilLoadedThread extends Thread {
    private static int ready = 0;

    public static synchronized void incrementReady(){
        ++ready;
    }
    private static synchronized int getReady(){
        return ready;
    }

    public void run(){
        while(getReady() <= Host.sockets.size()){
            try {
                Thread.sleep(10);
            } catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        new GameLoadedEvent().send();
        GameClient.startSynchronizedTick();
    }

}
