package com.clikclok.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This maintains the state of the grid with colours, tiles and directions. A new instance will be created
 * for each level
 * @author David
 */
public class GameState { 
	private Tile[][] tiles;
	private Map<TileColour, Set<TilePosition>> tilesWithColours;
	
	protected GameState(Tile[][] tiles) {
		this.tiles = tiles;
		tilesWithColours = new HashMap<TileColour, Set<TilePosition>>();
		initializeTilesWithColoursSet();
	}
	
	/**
	 * A set of {@link TilePosition}s is maintained for each colour. This initializes this set when the grid is first initialized
	 */
	private void initializeTilesWithColoursSet()
	{
    	tilesWithColours.put(TileColour.GREEN, new HashSet<TilePosition>());
    	tilesWithColours.put(TileColour.GREEN_TURNING, new HashSet<TilePosition>());
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
	
	/**
	 * Returns the tile in the specified position
	 * @param tilePosition
	 * @return
	 */
	public Tile getTileInformation(TilePosition tilePosition)
	{
		return tiles[tilePosition.getPositionAcrossGrid()][tilePosition.getPositionDownGrid()];
	}
	
	/**
	 * Returns the tile in the specified position
	 * @param positionAcross
	 * @param positionDown
	 * @return
	 */
	public Tile getTileInformation(int positionAcross, int positionDown)
	{
		return tiles[positionAcross][positionDown];
	}
	
	/**
	 * Update the colour and direction of the specified tile. The direction will be updated to turn clockwise
	 * @param tile
	 * @param tileColour
	 */
	public void updateTileColourAndDirection(Tile tile, TileColour tileColour) {
		tile.updateDirection();
		updateTileColour(tile, tileColour);
	}
	
	/**
	 * Update the colour and direction of the specified tile. 
	 * @param tile
	 * @param tileColour
	 */
	public void updateTileColourAndDirection(Tile tile, TileColour tileColour, TileDirection tileDirection) {
		tile.setDirection(tileDirection);
		updateTileColour(tile, tileColour);
	}
	
	/**
	 * Update the colour of the specified tile. This will also update the relevant sets of {@link TilePosition}s
	 * @param tile
	 * @param tileColour
	 */
	public void updateTileColour(Tile tile, TileColour tileColour) 
	{
		TileColour colourBefore = tile.getColour();	
		
		tile.setColour(tileColour);
		
		// Only update the tile positions if the colours are different
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

	/**
	 * Get the set of {@link TilePosition}s for the specified colour
	 * @param tileColour
	 * @return
	 */
	public Set<TilePosition> getTilePositionsForColour(TileColour tileColour)
	{
		return tilesWithColours.get(tileColour); 
	}
	
	/**
	 * When the AI cannot update any tiles on the current turn we select a tile for the AI to target and then update the tile closest to this
	 * @param tileColour
	 * @return
	 */
	public TilePosition getTilePositionForAIToTarget(TileColour tileColour) {
		
		Set<TilePosition> tilePositions = getTilePositionsForColour(tileColour);
		// Will always return the first tile position so that we are picking a consistent tile to target
		return new ArrayList<TilePosition>(tilePositions).get(0);
	}
	
	/**
	 * @param tileColour
	 * @return number of tiles for the specified colour
	 */
	public int getNumberOfTilesForColour(TileColour tileColour)
	{
		return getTilePositionsForColour(tileColour).size();
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
}
