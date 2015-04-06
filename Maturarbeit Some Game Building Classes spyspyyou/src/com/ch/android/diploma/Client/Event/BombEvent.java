package com.ch.android.diploma.Client.Event;

import java.util.List;

import com.ch.android.diploma.Client.Entities.Bombs.Bomb;

public class BombEvent implements Event {

	public EventType evenType = EventType.BOMB_EVENT;

	public int xCoordinate, yCoordinate;
	public int type;
	public int explosionTick;

	BombEvent(int bombXCoordinate, int bombYCoordinate, int bombType, int bombExplosionTick) {
		xCoordinate = bombXCoordinate;
		yCoordinate = bombYCoordinate;
		type = bombType;
		explosionTick = bombExplosionTick;
	}

	public void addBomb(List<Bomb> bombList) {
		bombList.add(new Bomb(xCoordinate, yCoordinate, explosionTick, type));
	}
}
