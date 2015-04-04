package com.ch.android.diploma.Client.Event;

public class AddParticle extends Event {

	float xCoordinate;
	float yCoordinate;
	byte typeNumber;

	public EventType eventType = EventType.PARTICLE_ADDED;

	public AddParticle(float particleXCoordinate, float particleYCoordinate, byte particleTypeNumber) {
		xCoordinate = particleXCoordinate;
		yCoordinate = particleYCoordinate;
		typeNumber = particleTypeNumber;
	}

}
