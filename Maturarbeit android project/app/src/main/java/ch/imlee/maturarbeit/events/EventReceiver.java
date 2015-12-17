package ch.imlee.maturarbeit.events;

import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

public class EventReceiver extends Thread {
    private boolean running = false;
    // the Events are stored in this ArrayList of Queues, so they can get processed in the same order they got received at the beginning of every loop of the GameThread
    public static ArrayList<Queue<Event>> events = new ArrayList<>();

    @Override
    public void run(){
        // this Thread's purpose is to receive the incoming Events through the InputStream of the BluetoothSocket
        setRunning(true);
        Log.i("EventReceiver", "Thread started");
        if(StartActivity.deviceType == DeviceType.CLIENT){
            // the client has only one InputStream it has to listen to
            events.add(new LinkedBlockingQueue<Event>() {
            });
            while(running){
                receiveEvents(Client.inputStream, (byte) 0);
            }

        } else{
            // the server has to listen to the InputStream from every client
            for(int i = 0; i < Host.inputStreams.size(); ++i){
                events.add(new LinkedBlockingQueue<Event>());
            }
            while(running){
                for(byte i = 1; i <= Host.inputStreams.size(); ++i){
                    receiveEvents(Host.inputStreams.get(i-1), i);
                }
            }
        }
    }
    public void receiveEvents(InputStream inputStream, byte i){
        // this method needs to be surrounded by try/catch, because if the connection is lost, Util.receiveEvents() throws an Exception
        try {
            Util.receiveEvents(inputStream, i);
        } catch (Exception e){
            e.printStackTrace();
            setRunning(false);
        }
    }

    public void setRunning(boolean running){
        this.running = running;
    }
    public boolean getRunning(){
        return running;
    }
}
