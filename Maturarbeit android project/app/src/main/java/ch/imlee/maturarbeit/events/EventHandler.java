package ch.imlee.maturarbeit.events;

import android.util.Log;

import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.utils.LogView;

public class EventHandler extends Thread {
    private Event event;
    private final byte ID;
    public EventHandler(Event event, byte id){
        this.event = event;
        ID = id;
    }

    @Override
    public void run(){
        // this Thread starts for each received Event. It's purpose is to apply them to the game, and if you are the host, to send them to the other clients
        LogView.addLog("received event: " + event.toString());
        if(event == null){
            // this can happen when the eventString wasn't sent correctly
            Log.w("event", "event is corrupt, ignoring it...");
            return;
        }
        if(StartActivity.deviceType == DeviceType.HOST){
            // if you are the host, you have to let all clients know of this Event (except for the one who sent it)
            event.send();
        }
        if (!event.handle(ID)) {
            // Event.handle() returns false when it's a GameActionEvent. In this case, Event.apply() needs to be called
            Log.v("Event", "Event " + event.toString() + " didn't handle itself, storing in queue...");
            // synchronizedAdd adds the Event into the event Queue ArrayList described in the EventReceiver class. They get applied at the beginning of the next GameThread loop
            synchronizedAdd(event, ID);
        } else
            // if Event.handle() returns true, it was a GameStateEvent and has handled itself instantly without waiting for the next GameThread loop
            Log.v("Event", "Event " + event.toString() + "handled itself, going on...");
    }

    private static synchronized void synchronizedAdd(Event event, byte id) {
        // this method adds the Event to the ArrayList of Queues of Events described in the EventReceiver class
        if(StartActivity.deviceType == DeviceType.HOST) --id;
        EventReceiver.events.get(id).add(event);
    }
}
