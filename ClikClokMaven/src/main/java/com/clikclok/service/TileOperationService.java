package com.clikclok.service;

import android.util.Log;

import com.clikclok.domain.OperationType;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.service.domain.GridUpdateTask;
import com.clikclok.util.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TileOperationService{
	@Inject
	private TileUpdateLogicService tileUpdateLogicService; 
	@Inject
	private GameLogicService gameLogicService;
	@Inject
	private UIOperationQueue uiOperationQueue;
	private Tile aiTile;
	private int operationCounter;
	
	public void performUserOperation(final Tile clickedTile) 
	{
		Log.d(this.getClass().toString(), "Entering performUserOperation for tile " + clickedTile);
		
		if(operationCounter > 0)
		{
			Log.d(this.getClass().toString(), operationCounter + " operations already being processed so ignoring this operation");
			return;
		}
		else
		{
			// Stop the timer if it is active
			gameLogicService.stopTimer();
		}
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService) {
				@Override
				public void run() {
					Log.d(this.getClass().toString(), "Performing user operation on " + clickedTile);
					// Update the selected tile and all adjacent tiles
					boolean enemyTilesGained = performOperation(clickedTile,TileColour.GREEN, TileColour.RED);
					refreshGridForOperationType(OperationType.USER_OPERATION, enemyTilesGained);					
					operationCounter--;
					
				}
			});
		operationCounter++;			
		performAIOperation();
		
	}		
	
	protected void performAIOperation()
	{
		if(operationCounter > 1)
		{
			Log.d(this.getClass().toString(), "AI operations already in the queue so no need to add any further ones");
			return;
		}
		
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService) {
			@Override
			public void run() {
				// After this, determine the optimum AI tile to select
				aiTile = tileUpdateLogicService.calculateOptimumAITile(gameLogicService.getGameState(), gameLogicService.getCurrentLevel());
				Log.i(this.getClass().toString(), "Optimum tile calculated for AI is " + aiTile);
				// Flash the red light so user knows the selected tile
				gameLogicService.getGameState().updateTileColour(aiTile, TileColour.RED_TURNING);
				// False is just set as default here
				refreshGridForOperationType(OperationType.AI_SELECTION_OPERATION, false);
				operationCounter--;
			}
		});
		operationCounter++;
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService) {
			@Override
			public void run() {
				Log.d(this.getClass().toString(), "Performing AI operation on " + aiTile);
				// Update the AI tile and all adjacent tiles
				boolean enemyTilesGained = performOperation(aiTile,TileColour.RED, TileColour.GREEN);
				refreshGridForOperationType(OperationType.AI_OPERATION, enemyTilesGained);
				operationCounter--;
			}
		});
		operationCounter++;
	}
	
	private boolean performOperation(Tile tile, TileColour colourOfTile, TileColour otherColour)
	{
		Log.d(this.getClass().toString(), "About to update colours for a tile");		
		int enemyTilesGained = tileUpdateLogicService.updateColoursAndDirection(tile, gameLogicService.getGameState(), colourOfTile, otherColour, 0, true);	
		return enemyTilesGained > Constants.ENEMY_GAINS_THRESHOLD;
	}
	
	public void clearOperationsFromQueue()
	{
		Log.d(this.getClass().toString(), "Clearing operations from operation queue");
		uiOperationQueue.clearQueue();
		operationCounter = 0;
	}
	
		
}
