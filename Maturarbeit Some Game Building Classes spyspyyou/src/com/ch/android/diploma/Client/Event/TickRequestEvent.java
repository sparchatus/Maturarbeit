package com.ch.android.diploma.Client.Event;

public class TickRequestEvent extends Event {

	public final int OLD_SYNC_TICK;

	public TickRequestEvent(int oldSynchronizedTick) {
		eventType = EventTypes.SYNC_REQUEST_EVENT;
		OLD_SYNC_TICK = oldSynchronizedTick;
	}

}
