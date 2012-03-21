package com.clikclok.service;

import java.util.Comparator;

import com.clikclok.domain.GameState;
import com.clikclok.domain.Level;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TilePosition;

public interface TileUpdateLogicService {

	/**
	 * Updates the colours and direction for the specified tile. This uses recursion to update all tiles in the grid
	 * @param tile we are updating
	 * @param gameState tiles, directions and colours of the grid
	 * @param colourToUpdate
	 * @param otherColour
	 * @param enemyTilesGained number of enemy tiles captured so far 
	 * @param updateDirection whether we are updating the direction or not
	 * @return
	 */
	public abstract int updateColoursAndDirection(Tile tile,
			GameState gameState, TileColour colourToUpdate,
			TileColour otherColour, int enemyTilesGained,
			boolean updateDirection);

	/**
	 * See below
	 * @param tileStatus
	 * @param currentLevel
	 * @return
	 */
	public abstract Tile calculateOptimumAITile(GameState tileStatus,
			Level currentLevel);

	/**
	 * Calculates the optimum tile for the AI to select
	 * @param gameState tiles, directions and colours of the grid
	 * @param currentLevel
	 * @param tilePositionComparator that we will use to determine the AI tiles to choose from
	 * @param performedRetry flag to indicate whether we have performed the retry. This is performed when we did not find any tiles to capture
	 * @return
	 */
	@SuppressWarnings("all")
	public abstract Tile calculateOptimumAITile(GameState gameState,
			Level currentLevel,
			Comparator<TilePosition> tilePositionComparator,
			boolean performedRetry);

}