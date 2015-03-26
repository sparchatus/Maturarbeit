package com.android.diploma.imLee.Client.Entities;

public class Projectile extends Entity {

	private final int X_VELOCITY;
	private final int Y_VELOCITY;
	public final int DAMAGE;

	public Projectile(float xCoordinate, float yCoordinate, int xVelocity, int yVelocity, int projectileDamage) {
		super(xCoordinate, yCoordinate);
		X_VELOCITY = xVelocity;
		Y_VELOCITY = yVelocity;
		DAMAGE = projectileDamage;
	}

	public void update() {
		xCoordinate += X_VELOCITY;
		yCoordinate += Y_VELOCITY;
	}

	public void render() {

	}
	
	public Player checkHitbox(){
		
		return null;
	}
}
