package com.multichoice.navigation;

import java.util.ArrayList;

import com.multichoice.navigation.map.Map;
import com.multichoice.navigation.map.MapTile;
import com.multichoice.navigation.rules.RuleEngine;

public class Navigator {

	private static final char FLATLAND = '.';
	private static final char FORREST = '*';
	private static final char MOUNTAIN = '^';
	private static final char FINISH = 'X';
	
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
				e.printStackTrace();
			}
		}

	}
	
	public void walkMap(Map map) {
		MapTile end = map.fetchFinish();
		MapTile current = map.fetchStart();
		
		map.setCurrent(current);
		// We've reached the end when the current tile is the final tile
		do {
			MapTile nextTile = next(map, current);
			if (nextTile == null) {
				break;
			}
			if (!RuleEngine.isWalkable(nextTile) && FINISH != nextTile.getType()) {
				ArrayList<MapTile> detourRoute = detour(map);
				map.getRoute().addAll(detourRoute);
				map.setTotalCost(map.getTotalCost() + calculateDetourCost(map, detourRoute));
				current = detourRoute.get(detourRoute.size()-1);
			} else {
				map.getRoute().add(nextTile);
				map.setTotalCost(map.getTotalCost() + moveCost(map.fetchFinish(), nextTile));
				current = nextTile;
			}
			map.setCurrent(current);

		} while (FINISH != current.getType());
	}
	
	private MapTile next(Map map, MapTile current) {
		MapTile next = null;

		int row = current.getMapPosition().getRow() + 1;
		int col = current.getMapPosition().getCol() + 1;
		
		if (map.isOffMap(row, col)) {
			return null;
		}
		next = map.getGrid().get(row).get(col);
		
		return next;
	}
	
	private ArrayList<MapTile> detour(Map map) {
		ArrayList<MapTile> detourRight = detourRight(map);
		ArrayList<MapTile> detourDown = detourDown(map);
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
	
	private int calculateDetourCost(Map map, ArrayList<MapTile> detour) {
		int cost = 0;
		if (detour == null || detour.isEmpty()) {
			return -1;
		}
		for (MapTile tile:detour) {
			cost += moveCost(map.fetchFinish(), tile);
		}
		return cost;
	}
	
	private int moveCost(MapTile finish, MapTile tile) {
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
	
	private ArrayList<MapTile> detourRight(Map map) {
		ArrayList<MapTile> detour = new ArrayList<MapTile>();
		MapTile next = null;
		int right = map.getCurrent().getMapPosition().getCol()+1;
		if (map.isOffMap(map.getCurrent().getMapPosition().getRow(), right)) {
			return null;
		}
		next = map.getGrid().get(map.getCurrent().getMapPosition().getRow()).get(right);
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		next = next(map, next);
		if (next == null) {
			return null;
		}
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		int down = next.getMapPosition().getRow()+1;
		if (map.isOffMap(down, next.getMapPosition().getCol())) {
			return null;
		}
		next = map.getGrid().get(down).get(next.getMapPosition().getCol());
		detour.add(next);
		
		return detour;
	}
	
	private ArrayList<MapTile> detourDown(Map map) {
		ArrayList<MapTile> detour = new ArrayList<MapTile>();
		MapTile next = null;
		
		int down = map.getCurrent().getMapPosition().getRow()+1;
		if (map.isOffMap(down, map.getCurrent().getMapPosition().getCol())) {
			return null;
		}
		next = map.getGrid().get(down).get(map.getCurrent().getMapPosition().getCol());
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		next = next(map, next);
		if (next == null) {
			return null;
		}
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);
		int right = next.getMapPosition().getCol()+1;
		if (map.isOffMap(next.getMapPosition().getRow(), right)) {
			return null;
		}
		next = map.getGrid().get(next.getMapPosition().getRow()).get(right);
		if (!RuleEngine.isWalkable(next)) {
			return null;
		}
		detour.add(next);

		return detour;
		
	}
}
