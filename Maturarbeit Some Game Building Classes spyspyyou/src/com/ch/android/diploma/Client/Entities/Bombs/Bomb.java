package com.ch.android.diploma.Client.Entities.Bombs;

import com.ch.android.diploma.Client.Entities.Entity;

public class Bomb extends Entity {
	
	public static enum BombTypes{
		TIME_BOMB
	}


	public Bomb(float bombXCoordinate, float bombYCoordinate) {
		super(bombXCoordinate, bombYCoordinate);
	}

	public void update() {
		
	}

	public void explode(){
		
	}
}
