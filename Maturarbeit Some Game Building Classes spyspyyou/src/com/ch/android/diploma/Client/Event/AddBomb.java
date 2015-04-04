package com.ch.android.diploma.Client.Event;

public class AddBomb extends Event {

	public EventType eventType = EventType.BOMB_ADDED;

	public int xCoordinate, yCoordinate;
	public int type;
	public int explosionTick;

	AddBomb(int bombXCoordinate, int bombYCoordinate, int bombType, int bombExplosionTick) {
		xCoordinate = bombXCoordinate;
		yCoordinate = bombYCoordinate;
		type = bombType;
		explosionTick = bombExplosionTick;
	}
}
