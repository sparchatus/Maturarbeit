package com.ch.android.diploma.Client.Event;

public class TickSynchronizationEvent implements Event {

	public EventType syncEvent = EventType.SYNC_EVENT;
	
	public int synchronizedTick;

	public TickSynchronizationEvent(int newSynchronizedTick) {
		synchronizedTick = newSynchronizedTick;
	}

}
