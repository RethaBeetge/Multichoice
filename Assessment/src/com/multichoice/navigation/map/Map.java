package com.multichoice.navigation.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
	private ArrayList<ArrayList<MapTile>> grid;
	private MapTile current;
	private ArrayList<MapTile> route;
	private int totalCost  = 0;
	private String map;
	
	public Map(String mapName) {
		map = mapName;
	}
	
	public void readMap() throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(map));
		String line = null;
		ArrayList<MapTile> colList = null;
		int rowNr = 0;
		
		try {
			System.out.println("Reading map");
			line = br.readLine();
			while (line != null) {
				char[] cols = line.toCharArray();
				colList = new ArrayList<MapTile>();
				for (int i = 0; i < cols.length; i++) {
					MapTile tile = new MapTile();
					MapCoord coord = new MapCoord(rowNr, i);
					tile.setMapPosition(coord);
					tile.setType(cols[i]);
					colList.add(tile);
				}
				getGrid().add(colList);
				line = br.readLine();
				rowNr++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Map read");
		
	}
	
	public void mapRoute() {
		File file = new File(map);
		String fileName = "Map_Solution.txt";

		try {
			file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (ArrayList<MapTile> row : grid) {
				StringBuilder sb = new StringBuilder();
				for (MapTile tile : row) {
					sb.append(tile.getType());
					if (getRoute().contains(tile)) {
						sb.append("#");
					}
				}
				writer.write(sb.toString());
				writer.newLine();
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error occured while writing route.");
			e.printStackTrace();
		}
		
	}
	
	public MapTile fetchStart() {
		return grid.get(0).get(0);
	}
	
	public MapTile fetchFinish() {
		ArrayList<MapTile> lastRow = grid.get(grid.size() - 1);
		return lastRow.get(lastRow.size() - 1);
	}

	public boolean isOffMap(int row, int col) {
		MapTile finish = fetchFinish();
		
		if (row > finish.getMapPosition().getRow()) {
			return true;
		}
		if (col > finish.getMapPosition().getCol()) {
			return true;
		}
		return false;
	}
	public ArrayList<ArrayList<MapTile>> getGrid() {
		if (grid == null) {
			grid = new ArrayList<ArrayList<MapTile>>();
		}
		return grid;
	}

	public void setGrid(ArrayList<ArrayList<MapTile>> grid) {
		this.grid = grid;
	}

	public MapTile getCurrent() {
		return current;
	}

	public void setCurrent(MapTile current) {
		this.current = current;
	}

	public ArrayList<MapTile> getRoute() {
		if (route == null) {
			route = new ArrayList<MapTile>();
		}
		return route;
	}

	public void setRoute(ArrayList<MapTile> route) {
		this.route = route;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
	
}
