package com.clikclok.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.clikclok.util.Constants;
import com.google.inject.Singleton;

@Singleton
public class GameState {
	
	private Tile[][] tiles;
	private Map<TileColour, Set<TilePosition>> tilesWithColours;
	
	public GameState(Tile[][] tiles)
	{
		this.tiles = (tiles == null) ? initializeTileGrid() : tiles;
		tilesWithColours = new HashMap<TileColour, Set<TilePosition>>();
		initializeTilesWithColoursSet();
	}
	
	public GameState() 
	{
		this(null);
    }
	
	private void initializeTilesWithColoursSet()
	{
    	tilesWithColours.put(TileColour.GREEN, new HashSet<TilePosition>());
    	tilesWithColours.put(TileColour.RED, new HashSet<TilePosition>());
    	tilesWithColours.put(TileColour.RED_TURNING, new HashSet<TilePosition>());
    	
    	for(int i = 0; i < getGridWidth(); i++)
		{
			for(int j = 0; j < getGridHeight(); j++)
			{
				TilePosition tilePosition = new TilePosition(i, j);
				TileColour tileColour = getTileInformation(tilePosition).getColour();
				
				Set<TilePosition> tilePositions = tilesWithColours.get(tileColour);
				
				if(tilePositions != null)
				{
					tilePositions.add(tilePosition);
				}
			}
		}
    	
	}
	
	public Tile getTileInformation(TilePosition tilePosition)
	{
		return tiles[tilePosition.getPositionAcrossGrid()][tilePosition.getPositionDownGrid()];
	}
	
	public void updateTileColourAndDirection(Tile tile, TileColour tileColour) {
		updateDirection(tile);
		updateTileColour(tile, tileColour);
	}
	
	/**
	 * This is only used for unit tests
	 * @param tile
	 * @param tileColour
	 */
	public void updateTileColourAndDirection(Tile tile, TileColour tileColour, TileDirection tileDirection) {
		tile.setDirection(tileDirection);
		updateTileColour(tile, tileColour);
	}
	
	public void updateTileColour(Tile tile, TileColour tileColour) 
	{
		TileColour colourBefore = tile.getColour();	
		
		tile.setColour(tileColour);
		
		if(!colourBefore.equals(tileColour))
		{
			Set<TilePosition> positionOfColoursBefore = tilesWithColours.get(colourBefore);
			
			//Intialize this to an empty Set to avoid NullPointerExceptions below
			positionOfColoursBefore = (positionOfColoursBefore == null) ? new HashSet<TilePosition>() : positionOfColoursBefore;
			
			if(positionOfColoursBefore.size() > 0)
			{
				positionOfColoursBefore.remove(tile.getTilePosition());
			}
			Set<TilePosition> positionOfColoursAfter = tilesWithColours.get(tileColour);
			positionOfColoursAfter.add(tile.getTilePosition());
		}					
	}

	public Set<TilePosition> getTilePositionsForColour(TileColour tileColour)
	{
		return tilesWithColours.get(tileColour); 
	}
	
	public TilePosition getTilePositionForAIToTarget(TileColour tileColour) {
		
		Set<TilePosition> tilePositions = getTilePositionsForColour(tileColour);
		
		int randomNumber = (int) (Math.random() * tilePositions.size());
		
		return new ArrayList<TilePosition>(tilePositions).get(randomNumber);
	}
	
	/**
	 * @param tileColour
	 * @return number of tiles for the specified colour
	 */
	public int getNumberOfTilesForColour(TileColour tileColour)
	{
		Set<TilePosition> tilePositions = tilesWithColours.get(tileColour);
		
		int numberOfTiles = tilePositions.size();
		return numberOfTiles;
	}
	
	public int getGridWidth()
	{
		return tiles.length;
	}
	
	public int getGridHeight()
	{
		return tiles[0].length;
	}
	
	public int getTileGridSize()
	{
		return getGridWidth() * getGridHeight();
	}
	
	@Override
	public GameState clone()
	{
		Tile[][] deepCopyOfTiles = new Tile[getGridWidth()][getGridHeight()];
		
		for(int i = 0; i < getGridWidth(); i++)
		{
			for(int j = 0; j < getGridHeight(); j++)
			{
				Tile beingCopied = tiles[i][j];
				Tile newTile = new Tile(beingCopied.getDirection(), beingCopied.getColour(), beingCopied.getTilePosition());
				deepCopyOfTiles[i][j] = newTile;
			}
		}
		
		return new GameState(deepCopyOfTiles);
	}
	
	private Tile[][] initializeTileGrid()
	{
		Random random = new Random();
    	
    	tiles = new Tile[Constants.GRID_WIDTH][Constants.GRID_HEIGHT];
    	
    	for(int i = 0; i < Constants.GRID_WIDTH; i++)
    	{
    		for(int j = 0; j < Constants.GRID_HEIGHT; j++)
    		{
    			// Generate a number between 0 and 3, and multiply this by 90 to get the correct number of degrees
    			int degrees = (random.nextInt(4)) * 90;
    			tiles[i][j] = new Tile(TileDirection.getTileDirection(degrees), TileColour.GREY, new TilePosition(i, j));
    		}
    	}
    	
    	// Initialize the first 2 tiles in top left and bottom right corners to be green and red respectively
		Tile initialGreenTile = tiles[0][0];
		initialGreenTile.setColour(TileColour.GREEN);
		Tile initialRedTile = tiles[getGridWidth() - 1][getGridHeight() - 1];
		initialRedTile.setColour(TileColour.RED);
    	
    	return tiles;
	}
	
	private void updateDirection(Tile tile)
	{
		int degrees = (int) tile.getDirection().getDegrees();
		
		// Add 90 to the number of degrees. If it's 270 then we are effectively adding 90 and resetting to 0 as it's 360
		degrees = (degrees == 270) ? 0 : (degrees += 90);
		
		// Set it's new direction
		tile.setDirection(TileDirection.getTileDirection(degrees));
	}

}
