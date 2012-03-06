package com.clikclok.service;

import com.clikclok.domain.OperationType;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.service.domain.GridUpdateTask;
import com.clikclok.service.domain.Task;
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
	@Inject
	private AICalculationQueue aiCalculationQueue;
	private Tile aiTile;
	private int operationCounter;
	
	public void performUserOperation(final Tile clickedTile) 
	{
		if(operationCounter > 0)
		{
			return;
		}
		else
		{
			// Stop the timer if it is active
			gameLogicService.stopTimer();
		}
		uiOperationQueue.addUITaskToQueue(new GridUpdateTask(gameLogicService, OperationType.USER_OPERATION) {
				@Override
				public void run() {
					// Update the selected tile and all adjacent tiles
					boolean enemyTilesGained = performOperation(clickedTile,TileColour.GREEN, TileColour.RED);
					aiCalculationQueue.addAICalculationTaskToQueue(new AICalculationTask());
					refreshGrid(enemyTilesGained);					
					operationCounter--;
					
				}
			});
		operationCounter++;			
		performAIOperation();
		
	}		
	
	protected void performAIOperationAfterTimeOut() {
		aiCalculationQueue.addAICalculationTaskToQueue(new AICalculationTask());
		uiOperationQueue.startNextGridUpdateTask(OperationType.USER_OPERATION);
		performAIOperation();
	}
	
	protected void performAIOperation()
	{
		if(operationCounter > 1)
		{
			return;
		}
				
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService, OperationType.AI_SELECTION_OPERATION) {
			@Override
			public void run() {
				gameLogicService.getGameState().updateTileColour(aiTile, TileColour.RED_TURNING);
				// False is just set as default here
				refreshGrid(false);
				operationCounter--;
			}
		});
		operationCounter++;
		uiOperationQueue.addGridUpdateTaskToQueue(new GridUpdateTask(gameLogicService, OperationType.AI_OPERATION) {
			@Override
			public void run() {
				// Update the AI tile and all adjacent tiles
				boolean enemyTilesGained = performOperation(aiTile,TileColour.RED, TileColour.GREEN);
				refreshGrid(enemyTilesGained);
				operationCounter--;
			}
		});
		operationCounter++;
	}
	
	private boolean performOperation(Tile tile, TileColour colourOfTile, TileColour otherColour)
	{
		int enemyTilesGained = tileUpdateLogicService.updateColoursAndDirection(tile, gameLogicService.getGameState(), colourOfTile, otherColour, 0, true);	
		return enemyTilesGained > Constants.ENEMY_GAINS_THRESHOLD;
	}
	
	public void clearOperationsFromQueue()
	{
		uiOperationQueue.clearQueue();
		operationCounter = 0;
	}
	
	public class AICalculationTask extends Task {
		@Override
		public void run() {
			// After this, determine the optimum AI tile to select
			aiTile = tileUpdateLogicService.calculateOptimumAITile(gameLogicService.getGameState(), gameLogicService.getCurrentLevel());
			uiOperationQueue.notifyAICalculationComplete();			
		}		
	}
	
		
}
