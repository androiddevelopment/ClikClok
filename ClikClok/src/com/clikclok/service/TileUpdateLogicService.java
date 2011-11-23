package com.clikclok.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.clikclok.domain.GameState;
import com.clikclok.domain.Level;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;
import com.google.inject.Singleton;

@Singleton
public class TileUpdateLogicService {
	
	public void updateColours(Tile tile, GameState tileStatus, TileColour colourToUpdate, TileColour otherColour)
	{
		Log.i(this.getClass().toString(), "Entering updateColours for tile " + tile);
		Log.v(this.getClass().toString(), "Tile Status is " + tileStatus);
		
		Log.i(this.getClass().toString(), tileStatus.getNumberOfTilesForColour(TileColour.GREEN) +  " green tiles exist at present");
		Log.i(this.getClass().toString(), tileStatus.getNumberOfTilesForColour(TileColour.RED) +  " red tiles exist at present");
		
		// Update the colour of the tile to be updated
		tile.setColour(colourToUpdate);
		
		Log.i(this.getClass().toString(), "After updating colour, tile is now " + tile);
		
		// Update the grid with the updated tile
		tileStatus.updateTile(tile);
		
		Log.i(this.getClass().toString(), tileStatus.getNumberOfTilesForColour(TileColour.GREEN) +  " green tiles exist at present");
		Log.i(this.getClass().toString(), tileStatus.getNumberOfTilesForColour(TileColour.RED) +  " red tiles exist at present");
		
		Collection<TilePosition> adjacentTiles = tile.getTilePosition().getAdjacentTilePositions();
		Log.v(this.getClass().toString(), adjacentTiles.size() + " adjacent tiles exist");
		
		for(TilePosition adjacentTilePosition : adjacentTiles)
		{
			Log.i(this.getClass().toString(), "Adjacent tile position is " + adjacentTilePosition);
			// If this position does not exist on the grid then nothing should happen
			if(isInvalidTilePosition(adjacentTilePosition, tileStatus))
			{
				Log.i(this.getClass().toString(), "No such adjacent tile exists");
				continue;
			}
			
			// Get a *copy* of this adjacent tile's information
			Tile adjacentTile = tileStatus.getTileInformation(adjacentTilePosition);
			Log.i(this.getClass().toString(), "Adjacent tile is " + adjacentTile);
			
			// If the source tile and the adjacent tile do not touch then we should not proceed processing the adjacent tile
			if(!(tile.isPointingTo(adjacentTile) || adjacentTile.isPointingTo(tile)))
			{
				Log.i(this.getClass().toString(), "Tiles do not touch");
				continue;
			}
			// If the adjacent file is the same colour then no need to update it
			// Otherwise update the adjacent tile recursively
			if(adjacentTile.getColour().equals(colourToUpdate))
			{
				Log.i(this.getClass().toString(), "Adjacent tile is same colour so no more to update");
				continue;
			}
			else
			{
				Log.i(this.getClass().toString(), "Adjacent tile is different colour so will update it");
				updateColours(adjacentTile, tileStatus, colourToUpdate, otherColour);
			}
		}
		
	}
	
	public Tile calculateOptimumAITile(GameState tileStatus, Level currentLevel)
	{
		Log.i(this.getClass().toString(), "Entering calculateOptimumAITile");
		
		Log.i(this.getClass().toString(), tileStatus.getNumberOfTilesForColour(TileColour.GREEN) +  " green tiles exist at present");
		Log.i(this.getClass().toString(), tileStatus.getNumberOfTilesForColour(TileColour.RED) +  " red tiles exist at present");
		
		int highestNumberOfAITilesAttained = 0;
		// User will never have more tiles than the grid size
		int lowestNumberOfUserTilesExisting = tileStatus.getTileGridSize() + 1;
		
		// Will be used to hold the best tile found for the AI to update
		Tile bestAITile = null;
		
		// Loop through relevant AI tiles to determine which is the best one to update
		for(TilePosition aiPosition : getAITilesToSearch(tileStatus.getTilePositionsForColour(TileColour.RED), currentLevel))
		{
			// Clone the TileStatus so that we can update it without affecting the "actual" TileStatus
			GameState copyOfTileStatus = tileStatus.clone();
			
			Log.d(this.getClass().toString(), "Processing tile position " + aiPosition);
			
			// Retrieve the tile in the current position
			Tile aiTile = copyOfTileStatus.getTileInformation(aiPosition);
			
			Log.d(this.getClass().toString(), "Processing tile " + aiTile);
			
			// If this is the first time in the loop then we'll initialize the best tile found
			if(bestAITile == null)
			{
				bestAITile = aiTile;
			}
			
			// Update the direction of this tile
			updateDirection(aiTile);
			
			// Update the colour of this tile and all adjacent tiles in the cloned TileStatus
			// Effectively, we are simulating what will happen if this tile was updated
			updateColours(aiTile, copyOfTileStatus, TileColour.RED, TileColour.GREEN);
			
			// Retrieve the counts of the numbers of red and green tiles if this tile was updated
			int numberOfAITilesAttained = copyOfTileStatus.getNumberOfTilesForColour(TileColour.RED);
			int numberOfExistingUserTiles = copyOfTileStatus.getNumberOfTilesForColour(TileColour.GREEN);
			
			Log.d(this.getClass().toString(), "Selecting this tile will result in " + numberOfAITilesAttained + " red tiles and " + numberOfExistingUserTiles
			+ " green tiles");
			
			// If this is greater than the highest number to date then this becomes our best tile
			// If it is equal then there is additional logic to be performed
			if(numberOfAITilesAttained > highestNumberOfAITilesAttained)
			{
				highestNumberOfAITilesAttained = numberOfAITilesAttained;
				lowestNumberOfUserTilesExisting = numberOfExistingUserTiles;
				Log.d(this.getClass().toString(), "This tile will result in more gains than the previous best AI tile " + bestAITile);
				bestAITile = aiTile;
			}
			else if(numberOfAITilesAttained == highestNumberOfAITilesAttained)
			{
				// If two tiles attained the same number of tiles then we select the tile that attained more user tiles
				if(numberOfExistingUserTiles < lowestNumberOfUserTilesExisting)
				{
					lowestNumberOfUserTilesExisting = numberOfExistingUserTiles;
					Log.d(this.getClass().toString(), "This tile will result in the same number of gains than the previous best AI tile " + bestAITile);
					Log.d(this.getClass().toString(), "However, it will result in more gains of user tiles");
					bestAITile = aiTile;
				}
			}
		}
		
		// Retrieve the actual tile information associated with this position as it will be updated after completion of the processing here
		bestAITile = tileStatus.getTileInformation(bestAITile.getTilePosition());
		Log.i(this.getClass().toString(), "Optimum AI tile is " + bestAITile);
		return bestAITile;		
		
	}
	
	public void updateDirection(Tile tile)
	{
		int degrees = (int) tile.getDirection().getDegrees();
		
		// Add 90 to the number of degrees. If it's 270 then we are effectively adding 90 and resetting to 0 as it's 360
		degrees = (degrees == 270) ? 0 : (degrees += 90);
		
		// Set it's new direction
		tile.setDirection(TileDirection.getTileDirection(degrees));
	}
		
	/**
	 * Determines whether the specified TilePosition exists on the grid by checking its coordinates
	 * @param tilePosition
	 * @param tileStatus
	 * @return a boolean indicating it's validity
	 */
	private boolean isInvalidTilePosition(TilePosition tilePosition, GameState tileStatus)
	{
		return(tilePosition.getPositionAcrossGrid() < 0) || (tilePosition.getPositionDownGrid() < 0)
		|| (tilePosition.getPositionAcrossGrid() >= tileStatus.getGridWidth()) || (tilePosition.getPositionDownGrid() >= tileStatus.getGridHeight());
	}
	
	private List<TilePosition> getAITilesToSearch(Set<TilePosition> aiTilePositions, Level currentLevel)
	{
		Log.d(this.getClass().toString(), "Entering getAITilesToSearch for level " + currentLevel);
		// Convert this set to a sorted array list for easy access
		List<TilePosition> aiTilesToSearch = new ArrayList<TilePosition>(aiTilePositions);
		// Receive the number that we can search for this level. 
		int numberToSearch = currentLevel.getNumberToSearch();
		Log.d(this.getClass().toString(), "Number to search is " + numberToSearch);
		// If the number to search is greater than the size of the grid then we set this to the size of the grid
		numberToSearch = numberToSearch > aiTilePositions.size() ? aiTilePositions.size() : numberToSearch;
		Log.d(this.getClass().toString(), "Updated number to search is " + numberToSearch);
		// Retrieve the appropriate sub list
		// Because numberToSearch is an exclusive endpoint we do not subtract one for array indexing
		aiTilesToSearch = aiTilesToSearch.subList(0, numberToSearch);
		Log.d(this.getClass().toString(), "Returning an ordered list with " + aiTilesToSearch.size() + " tiles");
		return aiTilesToSearch;
	}
	
	@SuppressWarnings("unused")
	private void printAllTiles(GameState tileStatus)
	{
		Log.d(this.getClass().toString(), "Printing all tiles");
		
		for(int i = 0; i < tileStatus.getGridWidth(); i++)
		{
			for(int j = 0; j < tileStatus.getGridHeight(); j++)
			{
				TilePosition tilePosition = new TilePosition(i, j);
				Log.d(this.getClass().toString(), tileStatus.getTileInformation(tilePosition).toString());
			}
		}
	}
}
