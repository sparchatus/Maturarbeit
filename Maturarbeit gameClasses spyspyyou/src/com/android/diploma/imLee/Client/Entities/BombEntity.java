package com.android.diploma.imLee.Client.Entities;

public class BombEntity extends Entity {
	
	private final int explosionTime;

	public BombEntity(float xCooriateBomb, float yCooriateBomb, int bombExpolsionTime) {
		super(xCooriateBomb, yCooriateBomb);
		this.explosionTime = bombExpolsionTime;
		
	}

	/**
	 * @return the explosionTime
	 */
	public int getExplosionTime() {
		return explosionTime;
	}
	
}
