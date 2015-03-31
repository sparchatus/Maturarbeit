package com.ch.android.diploma.Utility.Log;

import java.io.IOException;
import java.io.PipedInputStream;

public class LogWriter {

	private PipedInputStream logInStream;

	public LogWriter(PipedInputStream logInStream) {
		this.logInStream = logInStream;
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
