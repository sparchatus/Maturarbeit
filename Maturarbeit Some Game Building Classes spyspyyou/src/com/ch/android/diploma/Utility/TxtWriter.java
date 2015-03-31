package com.ch.android.diploma.Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TxtWriter {

	private File fileToWrite;
	private FileWriter systemTxtWriter;
	private BufferedWriter buffTxtWriter;

	public TxtWriter(String FilePath, String fileName) {
		fileToWrite = new File(FilePath + "/" + fileName);
		if (!fileToWrite.exists()) {
			try {
				fileToWrite.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			systemTxtWriter = new FileWriter(fileToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffTxtWriter = new BufferedWriter(systemTxtWriter);
	}

	public void append(String textToAppend) {
		try {
			buffTxtWriter.write(textToAppend);
			buffTxtWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		try {
			buffTxtWriter.flush();
			buffTxtWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
