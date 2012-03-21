package com.clikclok.service.impl;

import com.clikclok.domain.OperationType;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.service.AICalculationQueue;
import com.clikclok.service.GameLogicService;
import com.clikclok.service.TileOperationService;
import com.clikclok.service.TileUpdateLogicService;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.service.domain.GridUpdateTask;
import com.clikclok.service.domain.Task;
import com.clikclok.util.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This service performs the logic for when a user click's a tile. It performs the following tasks:
 * <ul>
 * <li> Flashes the tile the user selected
 * <li> Updates the grid after all tiles captured by this tile have been calculated
 * <li> Determines the optimum tile for the AI to select
 * <li> Flashes this tile temporarily
 * <li> Updates the grid after all tiles captured by this tile have been calculated
 * </ul>
 * 
 * @author David
 */
@Singleton
public class TileOperationServiceImpl implements TileOperationService{
	@Inject
	private TileUpdateLogicService tileUpdateLogicService; 
	@Inject
	private GameLogicService gameLogicService;
	@Inject
	private UIOperationQueue uiOperationQueue;
	@Inject
	private AICalculationQueue aiCalculationQueue;
	private Tile aiTile;
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.TileOperationService#performUserOperation(com.clikclok.domain.Tile)
	 */
	@Override
	public void performUserOperation(final Tile clickedTile) 
	{
		// If there are tasks still pending then we know that the previous turn has not yet completed
		if(uiOperationQueue.hasUnprocessedTasks()){
			return;
		}
		// Stop the timer if it's started
		gameLogicService.stopTimer();
		// Update the colour of the tile the user selected
		uiOperationQueue.addUITaskToQueue(new GridUpdateTask(gameLogicService, OperationType.USER_SELECTION_OPERATION) {
			@Override
			public void run() {
				gameLogicService.getGameState().updateTileColour(clickedTile, TileColour.GREEN_TURNING);
				// False is just set as default here
				refreshGrid(false);
			}
		});
		// Update the grid for all tiles the user has captured
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService, OperationType.USER_OPERATION) {
				@Override
				public void run() {
					// Update the selected tile and all adjacent tiles
					boolean enemyTilesGained = performOperation(clickedTile,TileColour.GREEN, TileColour.RED);
					// Kick off calculation of the AI's optimum tile
					aiCalculationQueue.addAICalculationTaskToQueue(new AICalculationTask());
					refreshGrid(enemyTilesGained);	
				}
			});
		// Perform the AI's turn
		performAIOperation();		
	}		
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.TileOperationService#performAIOperationAfterTimeOut()
	 */
	@Override
	public void performAIOperationAfterTimeOut() {
		aiCalculationQueue.addAICalculationTaskToQueue(new AICalculationTask());
		// Here we are specifying effectively that the next operation to be processed should be the operation to flash the AI's selected tile
		uiOperationQueue.startNextGridUpdateTask(OperationType.USER_OPERATION);
		performAIOperation();
	}
	
	/**
	 * This will be invoked after a user's turn, or when the AI gets a free turn on expiration of the timer
	 */
	private void performAIOperation()
	{
		// Update the colour of the AI's selected tile
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService, OperationType.AI_SELECTION_OPERATION) {
			@Override
			public void run() {
				gameLogicService.getGameState().updateTileColour(aiTile, TileColour.RED_TURNING);
				// False is just set as default here
				refreshGrid(false);
			}
		});
		// Update the grid for all tiles the AI has captured
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService, OperationType.AI_OPERATION) {
			@Override
			public void run() {
				// Update the AI tile and all adjacent tiles
				boolean enemyTilesGained = performOperation(aiTile,TileColour.RED, TileColour.GREEN);
				refreshGrid(enemyTilesGained);
			}
		});
	}
	
	/**
	 * Invokes the actual processing of the underlying grid to determine all tiles that are updated for the specified tile
	 * @param tile
	 * @param colourOfTile
	 * @param otherColour
	 * @return boolean indicating whether enough tiles were captured for a sound to be played
	 */
	private boolean performOperation(Tile tile, TileColour colourOfTile, TileColour otherColour)
	{
		int enemyTilesGained = tileUpdateLogicService.updateColoursAndDirection(tile, gameLogicService.getGameState(), colourOfTile, otherColour, 0, true);	
		return enemyTilesGained > Constants.ENEMY_GAINS_THRESHOLD;
	}
	
	/**
	 * The task to calculate the AI's optimum tile will be run on a separate thread/handler
	 * @author David
	 */
	public class AICalculationTask extends Task {
		@Override
		public void run() {
			// After this, determine the optimum AI tile to select
			aiTile = tileUpdateLogicService.calculateOptimumAITile(gameLogicService.getGameState(), gameLogicService.getCurrentLevel());
			uiOperationQueue.notifyAICalculationComplete();			
		}		
	}
	
	
	
		
}
