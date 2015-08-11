package ch.imlee.maturarbeit.game.events;

import android.util.Log;

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
    private boolean running;
    public static ArrayList<Queue<Event>> events = new ArrayList<>();

    @Override
    public void run(){
        if(StartActivity.deviceType == DeviceType.CLIENT){
            events.add(new LinkedBlockingQueue<Event>() {
            });
            while(running){
                //Log.v("Event", "EventReceiver is on it again");
                Util.receiveEvents(Client.inputStream, (byte)0);
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
}
