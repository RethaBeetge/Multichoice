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
	private ArrayList<ArrayList<Tile>> grid;
	private Tile current;
	private ArrayList<Tile> route;
	private int totalCost  = 0;
	private String map;
	
	public Map(String mapName) {
		map = mapName;
	}
	
	public void readMap() throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(map));
		String line = null;
		ArrayList<Tile> colList = null;
		int rowNr = 0;
		
		try {
			line = br.readLine();
			while (line != null) {
				char[] cols = line.toCharArray();
				colList = new ArrayList<Tile>();
				for (int i = 0; i < cols.length; i++) {
					Tile tile = new Tile();
					Coordinate coord = new Coordinate(rowNr, i);
					tile.setMapPosition(coord);
					tile.setType(cols[i]);
					colList.add(tile);
				}
				grid.add(colList);
				br.readLine();
				rowNr++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void mapRoute() {
		File file = new File(map);
		String path = file.getPath();
		String fileName = path + File.separatorChar + "Map_Solution.txt";

		try {
			file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (ArrayList<Tile> row : grid) {
				StringBuilder sb = new StringBuilder();
				for (Tile tile : row) {
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
		}
		
	}
	
	public Tile fetchStart() {
		return grid.get(0).get(0);
	}
	
	public Tile fetchFinish() {
		ArrayList<Tile> lastRow = grid.get(grid.size() - 1);
		return lastRow.get(lastRow.size() - 1);
	}

	public ArrayList<ArrayList<Tile>> getGrid() {
		return grid;
	}

	public void setGrid(ArrayList<ArrayList<Tile>> grid) {
		this.grid = grid;
	}

	public Tile getCurrent() {
		return current;
	}

	public void setCurrent(Tile current) {
		this.current = current;
	}

	public ArrayList<Tile> getRoute() {
		return route;
	}

	public void setRoute(ArrayList<Tile> route) {
		this.route = route;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
	
}
