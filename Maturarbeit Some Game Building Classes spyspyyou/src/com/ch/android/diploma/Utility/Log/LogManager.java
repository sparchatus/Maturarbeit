package com.ch.android.diploma.Utility.Log;

import java.io.IOException;
import java.io.PipedInputStream;

public class LogManager implements Runnable {

	public static boolean logging;

	private static PipedInputStream logInStream;

	public LogManager() {
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

		}
	}
}
