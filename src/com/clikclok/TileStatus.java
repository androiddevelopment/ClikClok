package com.clikclok;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TileStatus {
	
	private Tile[][] tiles;
	private Map<TileColour, Set<TilePosition>> tilesWithColours;
	
	public TileStatus(Tile[][] tiles)
	{
		this.tiles = tiles;
		tilesWithColours = new HashMap<TileColour, Set<TilePosition>>();
		
	}
	
	public TileStatus()
    {
		tilesWithColours = new HashMap<TileColour, Set<TilePosition>>();
		initializeTileGrid();
    }
	
	public void initializeTilesWithColoursSet(TilePosition initialGreenPosition, TilePosition initialRedPosition)
	{
    	// Initialize the first 2 tiles in top left and bottom right corners to be green and red respectively
    	tiles[initialGreenPosition.getPositionAcrossGrid()][initialGreenPosition.getPositionDownGrid()].setColour(TileColour.GREEN);
    	tiles[initialRedPosition.getPositionAcrossGrid()][initialRedPosition.getPositionDownGrid()].setColour(TileColour.RED);
    	
    	tilesWithColours.put(TileColour.GREEN, new HashSet<TilePosition>());
    	tilesWithColours.put(TileColour.RED, new HashSet<TilePosition>());
    	// Only necessary to add the red tiles as the initial green tile will be added after it's clicked on
    	tilesWithColours.get(TileColour.RED).add(initialRedPosition);
	}
	
	private void initializeTilesWithColoursSet()
	{
		initializeTilesWithColoursSet(Constants.initialGreenPosition, Constants.initialRedPosition);
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public Map<TileColour, Set<TilePosition>> getTilesWithColours() {
		return tilesWithColours;
	}
	
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public void setTilesWithColours(
			Map<TileColour, Set<TilePosition>> tilesWithColours) {
		this.tilesWithColours = tilesWithColours;
	}

	private void initializeTileGrid()
	{
		Random random = new Random();
    	
    	tiles = new Tile[Constants.GRID_WIDTH][Constants.GRID_HEIGHT];
    	
    	for(int i = 0; i < Constants.GRID_WIDTH; i++)
    	{
    		for(int j = 0; j < Constants.GRID_HEIGHT; j++)
    		{
    			// Generate a number between 0 and 3, and multiply this by 90 to get the correct number of degrees
    			int degrees = (random.nextInt(4)) * 90;
    			tiles[i][j] = new Tile();
    			tiles[i][j].setDirection(TileDirection.getTileDirection(degrees));
    			tiles[i][j].setColour(TileColour.GREY);
    			tiles[i][j].setTilePosition(new TilePosition(i, j));
    		}
    	}
    	
    	initializeTilesWithColoursSet();
	}
	
	
}
