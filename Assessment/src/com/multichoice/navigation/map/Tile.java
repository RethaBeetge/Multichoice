package com.multichoice.navigation.map;

public class Tile {
	private char type;
	private Coordinate mapPosition;
	
	public Tile() {
	}
	
	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public Coordinate getMapPosition() {
		return mapPosition;
	}
	public void setMapPosition(Coordinate position) {
		mapPosition = position;
	}
}
