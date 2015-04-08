package com.ch.android.diploma.Client.Map;

import com.ch.android.diploma.Utility.ImageReader;

public class Map {

	private final byte[] mapData;

	public enum TileTypes {
		FLOOR, WALL, SPWANTILE_GREEN, SPAWNTILE_RED, BASEWALL_GREEN, BASEWALL_RED, FLAGSTAND_GREEN, FLAGSTAND_RED
	}

	public Map(byte[] map) {
		mapData = map;
	}

	/**
	 * For deafult maps that are saved as imageFiles and will be loaded with
	 * help of such.
	 * 
	 * @param imagePath
	 *            -path of the map to load.
	 */
	public Map(String imagePath) {
		mapData = ImageReader.returnImagePixelData(imagePath);;
	}
	
	public void render(){
		
	}
}
