package ch.imlee.maturarbeit.game.events;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Lukas on 18.06.2015.
 */
public class EventReceiver extends Thread {
    private boolean running = false;
    public static ArrayList<Queue<Event>> events = new ArrayList<>();

    @Override
    public void run(){
        setRunning(true);
        if(StartActivity.deviceType == DeviceType.CLIENT){
            events.add(new LinkedBlockingQueue<Event>() {
            });
            while(running){
                //Log.v("Event", "EventReceiver is on it again");
                try {
                    Util.receiveEvents(Client.inputStream, (byte) 0);
                } catch (NullPointerException e){
                    e.printStackTrace();
                    setRunning(false);
                }
            }

        } else{
            for(int i = 0; i < Host.inputStreams.size(); ++i){
                events.add(new LinkedBlockingQueue<Event>());
            }
            while(running){
                //Log.v("Event", "EventReceiver is on it again");
                for(byte i = 1; i <= Host.inputStreams.size(); ++i){
                    Util.receiveEvents(Host.inputStreams.get(i-1), i);
                }
            }
        }
    }

    public void setRunning(boolean running){
        this.running = running;
    }
    public boolean getRunning(){
        return running;
    }
}
