package com.android.diploma.imLee.Client.Entities;

public class Flag extends Entity{
	
	private final byte TEAM;
	
	private boolean isInBase;
	
	private Player flagOwner;
	
	private int flagResetTick;

	public Flag(float xCoordinate, float yCoordinate, byte team) {
		super(xCoordinate, yCoordinate);
		TEAM = team;
		flagOwner = null;
	}
	
	public void update(int synhronizedTick){
		
		if(!isInBase){
			if(flagResetTick>=synhronizedTick){
				isInBase = true;
				xCoordinate = 0;
				yCoordinate = 0;
			}
		}
	}

	public void render(){
		
	}
}
