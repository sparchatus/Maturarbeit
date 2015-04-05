package com.ch.android.diploma;

import java.util.ArrayList;
import java.util.List;

import com.ch.android.diploma.Client.Event.Event;

public class GameLoop implements Runnable {
	// ms
	private final int TIME_PER_TICK = 50;

	private boolean running;

	public static int synchronizedTick;

	private long tickSpareTime;
	private long lastTickTime;

	protected List<Event> eventList = new ArrayList<Event>();

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
				// lag occurred! running behind? Check Server Tick!
			}
			synchronizedTick++;
		}
	}

	protected void update() {
		if (!eventList.isEmpty()) {
			for (Event currentEvent : eventList) {

			}
		}
	}

	protected void render() {
	}

	public void processBombEvent() {

	}

	public void processParticleAddingEvent() {

	}

	public void processParticleRemovingEvent() {

	}

	public void processPlayerEvent() {

	}

	private void destroyRecources() {

	}

}
