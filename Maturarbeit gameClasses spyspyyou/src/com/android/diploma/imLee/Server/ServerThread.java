package com.android.diploma.imLee.Server;

import com.android.diploma.imLee.Client.GameLoop;
import com.android.diploma.imLee.Client.Entities.Projectile;

public class ServerThread implements Runnable {

	private boolean isRunning;

	@Override
	public void run() {
		isRunning = true;
		while (isRunning == true) {
			checkStreams();
			checkProjectiles();
		}
	}
	
	private void checkProjectiles(){
		for(Projectile projectile:GameLoop.projectileList){
		}
	}
	
	private void checkStreams(){
		
	}
}
