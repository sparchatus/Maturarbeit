package ch.imlee.maturarbeit.utility.Log;

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
		logWriter = new LogWriter(logInStream, LogCollector.dateFormat.format(LogCollector.date));
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
		
			logWriter.update();
			
			try {
				if ((tickSpareTime = System.currentTimeMillis() - (lastTickTime + TIME_PER_TICK)) > 0 || logInStream.available() != 0) {
					try {
						Thread.sleep(tickSpareTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
