package com.multichoice.navigation;

import java.util.ArrayList;

import com.multichoice.navigation.map.Map;
import com.multichoice.navigation.map.Tile;
import com.multichoice.navigation.rules.RuleEngine;

public class Navigator {

	private static final char FLATLAND = '.';
	private static final char FORREST = '*';
	private static final char MOUNTAIN = '^';
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please provide the file containing the map.");
		} else {
			try {
				Map map = new Map(args[0]);
				map.readMap();
				Navigator navigator = new Navigator();
				navigator.walkMap(map);
				map.mapRoute();
			} catch (Exception e) {
				System.out.println("Error reading specifid file: "+args[0]);
			}
		}

	}
	
	public void walkMap(Map map) {
		Tile end = map.fetchFinish();
		Tile current = map.fetchStart();
		
		map.setCurrent(current);
		// We've reached the end when the current tile is the final tile
		while (current != end) {
			Tile nextTile = next(map, current);
			if (!RuleEngine.isWalkable(nextTile)) {
				ArrayList<Tile> detour = detour(map);
				map.getRoute().addAll(detour);
				map.setTotalCost(map.getTotalCost() + calculateDetourCost(map, detour));
				current = detour.get(detour.size()-1);
			} else {
				map.getRoute().add(nextTile);
				map.setTotalCost(map.getTotalCost() + moveCost(map.fetchFinish(), nextTile));
				current = nextTile;
			}

		}
	}
	
	private Tile next(Map map, Tile current) {
		Tile next = null;

		int row = current.getMapPosition().getRow() + 1;
		int col = current.getMapPosition().getCol() + 1;
		
		next = map.getGrid().get(row).get(col);
		
		return next;
	}
	
	private ArrayList<Tile> detour(Map map) {
		ArrayList<Tile> detourRight = detourRight(map);
		ArrayList<Tile> detourDown = detourDown(map);
		int dtDownCost = calculateDetourCost(map, detourDown);
		int dtRightCost = calculateDetourCost(map, detourRight);
		if (dtDownCost < 0 ) {
			return detourRight;
		}
		if (dtRightCost < 0) {
			return detourDown;
		}
		return dtDownCost < dtRightCost?detourDown:detourRight;
	}
	
	private int calculateDetourCost(Map map, ArrayList<Tile> detour) {
		int cost = 0;
		if (detour == null || detour.isEmpty()) {
			return -1;
		}
		for (Tile tile:detour) {
			cost += moveCost(map.fetchFinish(), tile);
		}
		return cost;
	}
	
	private int moveCost(Tile finish, Tile tile) {
		int cost = 0;
		
		int rowDiff = finish.getMapPosition().getRow() - tile.getMapPosition().getRow();
		int colDiff = finish.getMapPosition().getCol() - tile.getMapPosition().getCol();
		cost = rowDiff + colDiff;
		if (tile.getType() == FLATLAND) {
			cost+=1;
		}
		if (tile.getType() == FORREST) {
			cost+=2;
		}
		if (tile.getType() == MOUNTAIN) {
			cost+=3;
		}
		return cost;
	}
	
	private ArrayList<Tile> detourRight(Map map) {
		ArrayList<Tile> detour = new ArrayList<Tile>();
		Tile next = null;
		int right = map.getCurrent().getMapPosition().getCol()+1;
		next = map.getGrid().get(map.getCurrent().getMapPosition().getRow()).get(right);
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		next = next(map, next);
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		int down = next.getMapPosition().getRow()+1;
		next = map.getGrid().get(down).get(next.getMapPosition().getCol());
		detour.add(next);
		
		return detour;
	}
	
	private ArrayList<Tile> detourDown(Map map) {
		ArrayList<Tile> detour = new ArrayList<Tile>();
		Tile next = null;
		
		int down = map.getCurrent().getMapPosition().getRow()+1;
		next = map.getGrid().get(down).get(map.getCurrent().getMapPosition().getCol());
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		next = next(map, next);
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		int right = next.getMapPosition().getCol()+1;
		next = map.getGrid().get(next.getMapPosition().getRow()).get(right);
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);

		return detour;
		
	}

}
