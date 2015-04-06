package com.ch.android.diploma.Client.Entities.Bombs;

import com.ch.android.diploma.GameLoop;
import com.ch.android.diploma.Client.Entities.Entity;

public class Bomb extends Entity {
	
	public static enum bombTypes{
		
	}

	private final int EXPLOSION_TICK;

	public Bomb(float bombXCoordinate, float bombYCoordinate, int bombExplosionTick, int type) {
		super(bombXCoordinate, bombYCoordinate);
		EXPLOSION_TICK = bombExplosionTick;
		//use the type number to put the right damage and picture
	}

	public void update() {
		if (GameLoop.synchronizedTick >= EXPLOSION_TICK) {
			
		}
	}

}
