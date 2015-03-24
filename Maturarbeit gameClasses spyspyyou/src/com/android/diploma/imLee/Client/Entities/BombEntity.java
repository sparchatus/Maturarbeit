package com.android.diploma.imLee.Client.Entities;

public class BombEntity extends Entity {
	
	private enum Type{

	}

	private final int explosionTime;
	private Type type;

	public BombEntity(float xCooriateBomb, float yCooriateBomb, int bombExpolsionTime, Type bombType) {
		super(xCooriateBomb, yCooriateBomb);
		this.explosionTime = bombExpolsionTime;
		this.type = bombType;
		
	}

	/**
	 * @return the explosionTime
	 */
	public int getExplosionTime() {
		return explosionTime;
	}
	

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

}
