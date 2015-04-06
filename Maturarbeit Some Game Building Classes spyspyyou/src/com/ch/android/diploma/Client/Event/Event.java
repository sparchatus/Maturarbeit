package com.ch.android.diploma.Client.Event;

public interface Event {
	
	public EventType evenType = null;

	public static enum EventType {
		SYNC_EVENT, PLAYER_START_DATA_PACKAGE, BOMB_EVENT
	}
}
