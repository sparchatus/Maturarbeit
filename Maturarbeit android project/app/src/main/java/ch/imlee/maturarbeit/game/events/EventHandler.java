package ch.imlee.maturarbeit.game.events;

import android.util.Log;

import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Lukas on 11.08.2015.
 */
public class EventHandler extends Thread {
    private Event event;
    private final byte ID;
    public EventHandler(Event event, byte id){
        this.event = event;
        ID = id;
    }

    @Override
    public void run(){
        if(StartActivity.deviceType == DeviceType.HOST){
            event.send();
        }
        if (!event.handle(ID)) {
            Log.v("Event", "Event " + event.toString() + " didn't handle itself, storing in queue...");
            synchronizedAdd(event, ID);
        } else Log.v("Event", "Event " + event.toString() + "handled itself, going on...");
    }

    private static synchronized void synchronizedAdd(Event event, byte id) {
        if(StartActivity.deviceType == DeviceType.HOST) --id;
        EventReceiver.events.get(id).add(event);
    }
}
