package com.multichoice.navigation.rules;

import java.util.Arrays;
import java.util.List;

import com.multichoice.navigation.map.MapTile;


public class RuleEngine {
	
	public static boolean isWalkable(MapTile tile) {
		List<Character> walkables = Arrays.asList('.','*','^');
		boolean isWalkable = false;
		
		isWalkable = walkables.contains(tile.getType())?true:false;
		
		return isWalkable;
	}
	
	public static boolean isForward(MapTile current, MapTile next) {
		boolean isForward = false;
		int colMove = 0;
		int rowMove = 0;
		
		colMove = next.getMapPosition().getCol() - current.getMapPosition().getCol();
		rowMove = next.getMapPosition().getRow() - current.getMapPosition().getRow();
		
		// Check current and next tile in same position
		if (colMove == 0 && rowMove == 0) {
			return false;
		}
		// next tile represents a move one row or one column back
		if (colMove < 0 || rowMove < 0) {
			isForward = false;
		} else {
			isForward = true;
		}
		
		return isForward;
	}
}
