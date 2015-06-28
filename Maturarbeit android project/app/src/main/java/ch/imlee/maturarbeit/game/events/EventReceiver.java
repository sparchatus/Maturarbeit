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
    public static ArrayList<Queue<Event>> events = new ArrayList<>();

    @Override
    public void run(){
        if(StartActivity.deviceType == DeviceType.CLIENT){
            events.add(new LinkedBlockingQueue<Event>() {
            });
            while(true){
                ArrayList<Event> temp = Util.receiveEvents(Client.inputStream);
                for(Event event:temp){
                    // if the event doesn't handle itself, add it to the queue
                    if(!event.handle((byte)0)){
                        events.get(0).add(event);
                    }
                }
            }

        } else{
            for(int i = 0; i < Host.inputStreams.size(); ++i){
                events.add(new LinkedBlockingQueue<Event>());
            }
            while(true){
                for(byte i = 0; i < Host.inputStreams.size(); ++i){
                    ArrayList<Event> temp = Util.receiveEvents(Host.inputStreams.get(i));
                    for(Event event:temp){
                        // if the event doesn't handle itself, add it to the queue
                        if(!event.handle((byte)(i+(byte)1))){
                            events.get(i).add(event);
                        }
                    }
                }
            }
        }
    }
}
