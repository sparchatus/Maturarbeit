package com.ch.android.diploma.Utility;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageReader {
	
	private static byte[] pixels;
	private static ImageReader imgReader;
	
	public static byte[] returnImagePixelData(String imagePath){
		return pixels = ((DataBufferByte) returnBuffImg(imagePath).getRaster().getDataBuffer()).getData();
	}
	
	public static BufferedImage returnBuffImg(String imagePath){
		return imgReader.returnBuffImg(imagePath);
	}
}
