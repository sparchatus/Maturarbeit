package com.ch.android.diploma.Client.Entities;

import com.ch.android.diploma.GameLoop;

public class Bomb extends Entity {

	private final int EXPLOSION_TICK;

	public Bomb(float bombXCoordinate, float bombYCoordinate, int bombExplosionTick) {
		super(bombXCoordinate, bombYCoordinate);
		EXPLOSION_TICK = bombExplosionTick;
	}

	public void update() {
		if (GameLoop.synchronizedTick >= EXPLOSION_TICK) {
			
		}
	}

}
