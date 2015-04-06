package com.ch.android.diploma.Client.Entities.Particles;

import com.ch.android.diploma.Client.Entities.Entity;

public class Particle extends Entity {

	private final float X_VELOCITY, Y_VELOCITY;
	private final int DAMAGE;

	// private final Image...

	public Particle(float particleXCoordinate, float particleYCoordinate, byte particleTypeNumber, int particleDamage, float particleXVelocity, float particleYVelocity) {
		super(particleXCoordinate, particleYCoordinate);
		X_VELOCITY = particleXVelocity;
		Y_VELOCITY = particleYVelocity;
		DAMAGE = particleDamage;
	}

	public void update() {
		xCoordinate += X_VELOCITY;
		yCoordinate += Y_VELOCITY;
	}

	public void render() {

	}
}
