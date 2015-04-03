package com.ch.android.diploma.Utility.Log;

import java.io.IOException;
import java.io.PipedInputStream;

import com.ch.android.diploma.Utility.TxtWriter;

public class LogWriter {

	private PipedInputStream logInStream;
	private TxtWriter logWriter;

	public LogWriter(PipedInputStream logInStream, String logFilePath) {
		this.logInStream = logInStream;
		logWriter = new TxtWriter(logFilePath);
	}

	public void update() {
		try {
			if (logInStream.available() != 0) {
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
