package com.ch.android.diploma;

import java.util.ArrayList;
import java.util.List;

import com.ch.android.diploma.Client.Entities.ThisPlayer;
import com.ch.android.diploma.Client.Event.Event;

public class GameLoop implements Runnable {
	// Ms
	private final int TIME_PER_TICK = 50;

	private boolean running;
	protected boolean isMultiplayer;

	public static int synchronizedTick;
	public static int lagCount;

	private long tickSpareTime;
	private long lastTickTime;

	protected List<Event> eventList = new ArrayList<Event>();

	public ThisPlayer referenceToThisPlayer;

	GameLoop(boolean gameIsMultiplayer) {
		isMultiplayer = gameIsMultiplayer;

	}

	@Override
	public void run() {
		running = true;
		while (running) {
			lastTickTime = System.currentTimeMillis();

			update();
			render();

			if ((tickSpareTime = System.currentTimeMillis() - (lastTickTime + TIME_PER_TICK)) > 0) {
				try {
					Thread.sleep(tickSpareTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (isMultiplayer) {
					lagCount += tickSpareTime;
					if(lagCount >= 200){
						//add a tickRequest Event to the Server Event List
						lagCount = 0;
					}
				}
			}
			synchronizedTick++;
		}
	}

	protected void update() {
	}

	protected void render() {
	}

	public void pauseGame() {

	}

	private void destroyRecources() {
		// end all threads...
	}

}
