package com.clikclok.service.impl;

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
import com.clikclok.service.TileUpdateLogicService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Performs the logic to determine the tiles that are updated when a user selects a tile, and the optimum tile for the AI to select for a given level
 * @author David
 */
@Singleton
public class TileUpdateLogicServiceImpl implements TileUpdateLogicService {
	@Inject
	private TilePositionComparator tilePositionComparator;
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.TileUpdateLogicService#updateColoursAndDirection(com.clikclok.domain.Tile, com.clikclok.domain.GameState, com.clikclok.domain.TileColour, com.clikclok.domain.TileColour, int, boolean)
	 */
	@Override
	public int updateColoursAndDirection(Tile tile, GameState gameState, TileColour colourToUpdate, TileColour otherColour, int enemyTilesGained, boolean updateDirection)
	{
		if(updateDirection) {			
			gameState.updateTileColourAndDirection(tile, colourToUpdate);
		}
		else {
			// Update the grid with the updated tile
			gameState.updateTileColour(tile, colourToUpdate);
		}		
		
		// Get all adjacent tile positions. These may be positions that are illegal such as -1, -1
		Collection<TilePosition> adjacentTiles = tile.getTilePosition().getAdjacentTilePositions();
		// Loop through all adjacent tiles
		for(TilePosition adjacentTilePosition : adjacentTiles)
		{
			// If this position does not exist on the grid then nothing should happen
			if(isInvalidTilePosition(adjacentTilePosition, gameState))
			{
				continue;
			}
			
			// Get this adjacent tile's information
			Tile adjacentTile = gameState.getTileInformation(adjacentTilePosition);
			
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
				// If we have found an enemy tile with the opposite colour then we should increment our counter
				if(adjacentTile.getColour().equals(otherColour))
				{
					enemyTilesGained ++;
				}
				// Recursively invoke this method again for the adjacent tile that is touching and of a different colour
				enemyTilesGained = updateColoursAndDirection(adjacentTile, gameState, colourToUpdate, otherColour, enemyTilesGained, false);
			}
		}
		
		return enemyTilesGained;
		
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.TileUpdateLogicService#calculateOptimumAITile(com.clikclok.domain.GameState, com.clikclok.domain.Level)
	 */
	@Override
	public Tile calculateOptimumAITile(GameState tileStatus, Level currentLevel)
	{
		return calculateOptimumAITile(tileStatus, currentLevel, tilePositionComparator, false);
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.TileUpdateLogicService#calculateOptimumAITile(com.clikclok.domain.GameState, com.clikclok.domain.Level, java.util.Comparator, boolean)
	 */
	@Override
	@SuppressWarnings("all")
	public Tile calculateOptimumAITile(GameState gameState, Level currentLevel, Comparator<TilePosition> tilePositionComparator, boolean performedRetry)
	{
		// These 2 values will be used to compare each tile to see if it can beat the "best" tile found so far
		int highestNumberOfAITilesAttained = gameState.getNumberOfTilesForColour(TileColour.RED);
		int lowestNumberOfUserTilesExisting = gameState.getNumberOfTilesForColour(TileColour.GREEN);
		// Will be used to hold the two best tiles found for the AI to update
		Tile bestAITileForTilesGained = null;
		Tile bestAITileForEnemyTilesGained = null;
		
		// Loop through relevant AI tiles to determine which is the best one to update
		for(TilePosition aiPosition : getAITilesToSearch(gameState.getTilePositionsForColour(TileColour.RED), currentLevel, tilePositionComparator))
		{
			// Clone the TileStatus so that we can update it without affecting the "actual" TileStatus
			GameState copyOfTileStatus = gameState.clone();
			
			Tile aiTile = copyOfTileStatus.getTileInformation(aiPosition);			
			// Need to initialize this to avoid null pointers
			bestAITileForTilesGained = bestAITileForTilesGained == null ? aiTile : bestAITileForTilesGained;
			
			// Update the colour of this tile and all adjacent tiles in the cloned TileStatus
			// Effectively, we are simulating what will happen if this tile was updated
			updateColoursAndDirection(aiTile, copyOfTileStatus, TileColour.RED, TileColour.GREEN, 0, true);
			
			// Retrieve the counts of the numbers of red and green tiles if this tile was updated
			int numberOfAITilesAttained = copyOfTileStatus.getNumberOfTilesForColour(TileColour.RED);
			int numberOfExistingUserTiles = copyOfTileStatus.getNumberOfTilesForColour(TileColour.GREEN);
			
			// If we have found a better tile then update our values accordingly
			if(numberOfExistingUserTiles < lowestNumberOfUserTilesExisting)
			{
				lowestNumberOfUserTilesExisting = numberOfExistingUserTiles;
				bestAITileForEnemyTilesGained = aiTile;
			}
			if(numberOfAITilesAttained > highestNumberOfAITilesAttained)
			{ 
				highestNumberOfAITilesAttained = numberOfAITilesAttained;
				bestAITileForTilesGained = aiTile;
			}
		}
		// We should always return the tile that captures the most enemy tiles first
		if(bestAITileForEnemyTilesGained != null)
		{
			return gameState.getTileInformation(bestAITileForEnemyTilesGained.getTilePosition());
		}
		else
		{
			// If we did not find any neutral files to capture then we should perform the retry
			// This will select a target enemy tile and find the tile closest to this to select
			if(highestNumberOfAITilesAttained == gameState.getNumberOfTilesForColour(TileColour.RED) && !performedRetry)
			{
				TilePosition greenTileToTarget = gameState.getTilePositionForAIToTarget(TileColour.GREEN);
				// Will search the entire grid for the tile closest to a green tile
				Tile closetToAGreenTile = calculateOptimumAITile(gameState, Level.FIVE, new TilePositionComparator(greenTileToTarget), true);
				return closetToAGreenTile;
			}
			// Return the best tile found for neutral tiles gained
			if(bestAITileForTilesGained == null) {
				return null;
			}
			else
			{
				return gameState.getTileInformation(bestAITileForTilesGained.getTilePosition()); 	
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
		// This could probably be in the TilePosition class but would require the GameState to be injected there
		return(tilePosition.getPositionAcrossGrid() < 0) || (tilePosition.getPositionDownGrid() < 0)
		|| (tilePosition.getPositionAcrossGrid() >= tileStatus.getGridWidth()) || (tilePosition.getPositionDownGrid() >= tileStatus.getGridHeight());
	}
	
	/**
	 * Determines the TilePositions to choose for the current level and comparator
	 * @param aiTilePositions
	 * @param currentLevel
	 * @param comparator
	 * @return
	 */
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
	
	/**
	 * Used for debugging
	 * @param tileStatus
	 */
	@SuppressWarnings("unused")
	private void printAllTiles(GameState tileStatus)
	{
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
