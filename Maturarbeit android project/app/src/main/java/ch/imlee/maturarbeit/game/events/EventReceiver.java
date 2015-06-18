package ch.imlee.maturarbeit.game.events;

import java.util.ArrayList;
import java.util.Queue;

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
            while(true){
                events.get(0).addAll(Util.receiveEvents(Client.inputStream));
            }
        } else{
            while(true){
                for(int i = 0; i < Host.inputStreams.size(); ++i){
                    events.get(i).addAll(Util.receiveEvents(Host.inputStreams.get(i)));
                }
            }
        }
    }
}
