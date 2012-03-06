package com.clikclok.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.clikclok.domain.GameState;
import com.clikclok.domain.Level;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TilePosition;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TileUpdateLogicService {
	@Inject
	private TilePositionComparator tilePositionComparator;
	
	public int updateColoursAndDirection(Tile tile, GameState tileStatus, TileColour colourToUpdate, TileColour otherColour, int enemyTilesGained, boolean updateDirection)
	{
		if(updateDirection) {			
			tileStatus.updateTileColourAndDirection(tile, colourToUpdate);
		}
		else {
			// Update the grid with the updated tile
			tileStatus.updateTileColour(tile, colourToUpdate);
		}		
		
		Collection<TilePosition> adjacentTiles = tile.getTilePosition().getAdjacentTilePositions();
		
		for(TilePosition adjacentTilePosition : adjacentTiles)
		{
			// If this position does not exist on the grid then nothing should happen
			if(isInvalidTilePosition(adjacentTilePosition, tileStatus))
			{
				continue;
			}
			
			// Get this adjacent tile's information
			Tile adjacentTile = tileStatus.getTileInformation(adjacentTilePosition);
			
			// If the source tile and the adjacent tile do not touch then we should not proceed processing the adjacent tile
			if(!(tile.isPointingTo(adjacentTile) || adjacentTile.isPointingTo(tile)))
			{
				continue;
			}
			// If the adjacent file is the same colour then no need to update it
			// Otherwise update the adjacent tile recursively
			if(adjacentTile.getColour().equals(colourToUpdate))
			{
				continue;
			}
			else
			{
				if(adjacentTile.getColour().equals(otherColour))
				{
					enemyTilesGained ++;
				}
				
				enemyTilesGained = updateColoursAndDirection(adjacentTile, tileStatus, colourToUpdate, otherColour, enemyTilesGained, false);
			}
		}
		
		return enemyTilesGained;
		
	}
	
	public Tile calculateOptimumAITile(GameState tileStatus, Level currentLevel)
	{
		return calculateOptimumAITile(tileStatus, currentLevel, tilePositionComparator, false);
	}
	
	@SuppressWarnings("all")
	public Tile calculateOptimumAITile(GameState tileStatus, Level currentLevel, Comparator<TilePosition> tilePositionComparator, boolean performedRetry)
	{
		int highestNumberOfAITilesAttained = tileStatus.getNumberOfTilesForColour(TileColour.RED);
		// User will never have more tiles than the grid size
		int lowestNumberOfUserTilesExisting = tileStatus.getNumberOfTilesForColour(TileColour.GREEN);
		
		// Will be used to hold the two best tiles found for the AI to update
		Tile bestAITileForTilesGained = null;
		Tile bestAITileForEnemyTilesGained = null;
		
		// Loop through relevant AI tiles to determine which is the best one to update
		for(TilePosition aiPosition : getAITilesToSearch(tileStatus.getTilePositionsForColour(TileColour.RED), currentLevel, tilePositionComparator))
		{
			// Clone the TileStatus so that we can update it without affecting the "actual" TileStatus
			GameState copyOfTileStatus = tileStatus.clone();
			
			Tile aiTile = copyOfTileStatus.getTileInformation(aiPosition);			
			// Need to initialize this to avoid null pointers
			bestAITileForTilesGained = bestAITileForTilesGained == null ? aiTile : bestAITileForTilesGained;
			
			// Update the colour of this tile and all adjacent tiles in the cloned TileStatus
			// Effectively, we are simulating what will happen if this tile was updated
			updateColoursAndDirection(aiTile, copyOfTileStatus, TileColour.RED, TileColour.GREEN, 0, true);
			
			// Retrieve the counts of the numbers of red and green tiles if this tile was updated
			int numberOfAITilesAttained = copyOfTileStatus.getNumberOfTilesForColour(TileColour.RED);
			int numberOfExistingUserTiles = copyOfTileStatus.getNumberOfTilesForColour(TileColour.GREEN);
			
			if(numberOfExistingUserTiles < lowestNumberOfUserTilesExisting)
			{
				lowestNumberOfUserTilesExisting = numberOfExistingUserTiles;
				bestAITileForEnemyTilesGained = aiTile;
			}
			if(numberOfAITilesAttained > highestNumberOfAITilesAttained)
			{ 
				// If this is greater than the highest number to date then this becomes our best tile
				// If it is equal then there is additional logic to be performed
				highestNumberOfAITilesAttained = numberOfAITilesAttained;
				bestAITileForTilesGained = aiTile;
			}
		}
		if(bestAITileForEnemyTilesGained != null)
		{
			return tileStatus.getTileInformation(bestAITileForEnemyTilesGained.getTilePosition());
		}
		else
		{
			if(highestNumberOfAITilesAttained == tileStatus.getNumberOfTilesForColour(TileColour.RED) && !performedRetry)
			{
				TilePosition greenTileToTarget = tileStatus.getTilePositionForAIToTarget(TileColour.GREEN);
				// Will search the entire grid for the tile closest to a green tile
				Tile closetToAGreenTile = calculateOptimumAITile(tileStatus, Level.FIVE, new TilePositionComparator(greenTileToTarget), true);
				return closetToAGreenTile;
			}
			if(bestAITileForTilesGained == null) {
				return null;
			}
			else
			{
				return tileStatus.getTileInformation(bestAITileForTilesGained.getTilePosition()); 	
			}
		}		
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
	
	private List<TilePosition> getAITilesToSearch(Set<TilePosition> aiTilePositions, Level currentLevel, Comparator<TilePosition> comparator)
	{
		// Convert this set to a sorted array list for easy access
		List<TilePosition> aiTilesToSearch = new ArrayList<TilePosition>(aiTilePositions);
		Collections.sort(aiTilesToSearch, comparator);
		// Receive the number that we can search for this level. 
		int numberToSearch = currentLevel.getNumberToSearch();
		// If the number to search is greater than the size of the grid then we set this to the size of the grid
		numberToSearch = numberToSearch > aiTilePositions.size() ? aiTilePositions.size() : numberToSearch;
		// Retrieve the appropriate sub list
		// Because numberToSearch is an exclusive endpoint we do not subtract one for array indexing
		aiTilesToSearch = aiTilesToSearch.subList(0, numberToSearch);
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
