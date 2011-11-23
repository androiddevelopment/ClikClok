package com.clikclok.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import android.util.Log;

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
		Log.v(this.getClass().toString(), "Tile Status is " + this);
	}
	
	public GameState()
	{
		this(null);
    }
	
	private void initializeTilesWithColoursSet()
	{
    	tilesWithColours.put(TileColour.GREEN, new TreeSet<TilePosition>());
    	tilesWithColours.put(TileColour.RED, new TreeSet<TilePosition>());
    	tilesWithColours.put(TileColour.RED_TURNING, new TreeSet<TilePosition>());
    	
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
		Tile actualTile = tiles[tilePosition.getPositionAcrossGrid()][tilePosition.getPositionDownGrid()];
		return new Tile(actualTile.getDirection(), actualTile.getColour(), actualTile.getTilePosition());
	}
	
	public void updateTile(Tile proxyTile)
	{
		Log.d(this.getClass().toString(), "Entering updateTile for tile " + proxyTile);
		
		Tile actualTile = tiles[proxyTile.getTilePosition().getPositionAcrossGrid()][proxyTile.getTilePosition().getPositionDownGrid()];
		
		Log.d(this.getClass().toString(), "Tile currently in this position is " + actualTile);
		
		TileColour colourBefore = actualTile.getColour();
		TileColour colourAfter = proxyTile.getColour();
		
		if(!colourBefore.equals(colourAfter))
		{
			Log.d(this.getClass().toString(), "Colours are different between the two so we will update the position sets");
			
			Set<TilePosition> positionOfColoursBefore = tilesWithColours.get(colourBefore);
			
			if(positionOfColoursBefore != null)
			{
				positionOfColoursBefore.remove(actualTile.getTilePosition());
				Log.d(this.getClass().toString(), "After removal of " + actualTile + " there now exists " + positionOfColoursBefore.size() + " tiles with colour " + colourBefore);
			}
			Set<TilePosition> positionOfColoursAfter = tilesWithColours.get(colourAfter);
			boolean positionAdded = positionOfColoursAfter.add(proxyTile.getTilePosition());
			Log.v(this.getClass().toString(), proxyTile.getTilePosition() + " was added successfully? " + positionAdded);
			Log.d(this.getClass().toString(), "There now exists " + positionOfColoursAfter.size() + " tiles with colour " + colourAfter);
			Log.v(this.getClass().toString(), "These are " + positionOfColoursAfter);
		}
		
		actualTile.setColour(colourAfter);
		actualTile.setDirection(proxyTile.getDirection());	
		
	}

	public Set<TilePosition> getTilePositionsForColour(TileColour tileColour)
	{
		Log.d(this.getClass().toString(), "Entering getTilePositionsForColour for colour " + tileColour);
		Log.v(this.getClass().toString(), "Tile Status is " + this);
		Set<TilePosition> actualTilePositions = tilesWithColours.get(tileColour);
		
		Set<TilePosition> proxyTilePositions = new TreeSet<TilePosition>();
		
		for(TilePosition tilePosition : actualTilePositions)
		{
			TilePosition proxyTilePosition = new TilePosition(tilePosition.getPositionAcrossGrid(), tilePosition.getPositionDownGrid());
			proxyTilePositions.add(proxyTilePosition);
		}
		
		Log.v(this.getClass().toString(), "Returning ordered set containing " + proxyTilePositions);
		return proxyTilePositions;
	}
	
	/**
	 * @param tileColour
	 * @return number of tiles for the specified colour
	 */
	public int getNumberOfTilesForColour(TileColour tileColour)
	{
		Set<TilePosition> tilePositions = tilesWithColours.get(tileColour);
		
		int numberOfTiles = tilePositions.size();
		
		Log.d(this.getClass().toString(), numberOfTiles + " tiles exist for colour " + tileColour);
		
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

}
