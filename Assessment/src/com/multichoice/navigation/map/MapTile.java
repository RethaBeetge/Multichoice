package com.multichoice.navigation.map;

public class MapTile {
	private char type;
	private MapCoord mapPosition;
	
	public MapTile() {
	}
	
	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public MapCoord getMapPosition() {
		return mapPosition;
	}
	public void setMapPosition(MapCoord position) {
		mapPosition = position;
	}
}
