package com.ch.android.diploma.Utility.Log;

import java.io.IOException;
import java.io.PipedInputStream;

public class LogManager implements Runnable {
	
	//ms
	private final int TIME_PER_TICK;

	public static boolean logging;

	private long tickSpareTime;
	private long lastTickTime;
	
	private static PipedInputStream logInStream;
	private static LogWriter logWriter;

	public LogManager() {
		TIME_PER_TICK = 50;
		logWriter = new LogWriter(logInStream, null);
		try {
			logInStream.connect(LogCollector.logOutStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		logging = true;
		while (logging) {
		
			
			
			if ((tickSpareTime = System.currentTimeMillis() - (lastTickTime + TIME_PER_TICK)) > 0) {
				try {
					Thread.sleep(tickSpareTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else{
				//lag occurred! running behind? Check Server Tick!
			}
		}
	}
}
