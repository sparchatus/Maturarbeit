package com.ch.android.diploma.Client.Event;

public class BombEvent extends Event {

	public int xCoordinate, yCoordinate;
	public int type;
	public int explosionTick;

	BombEvent(int bombXCoordinate, int bombYCoordinate, int bombType, int bombExplosionTick) {
		eventType = EventTypes.ADD_BOMB_EVENT;
		xCoordinate = bombXCoordinate;
		yCoordinate = bombYCoordinate;
		type = bombType;
		explosionTick = bombExplosionTick;
	}
}
