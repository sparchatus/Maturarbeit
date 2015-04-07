package com.ch.android.diploma.Client.Entities.Bombs;

import com.ch.android.diploma.GameLoop;

public class TimeBomb extends Bomb {

	private final int EXPLOSION_TICK;

	public TimeBomb(float bombXCoordinate, float bombYCoordinate, int bombExplosionTick) {
		super(bombXCoordinate, bombYCoordinate);
		EXPLOSION_TICK = bombExplosionTick;
	}

	@Override
	public void update() {
		if (EXPLOSION_TICK <= GameLoop.synchronizedTick) {
			explode();
		}
	}

	@Override
	public void explode() {

	}

}
