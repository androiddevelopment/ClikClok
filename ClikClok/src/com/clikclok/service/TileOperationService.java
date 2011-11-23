package com.clikclok.service;

import android.util.Log;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.service.domain.GridUpdateTask;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TileOperationService{
	@Inject
	private TileUpdateLogicService tileUpdateLogicService;
	@Inject
	private GameLogicService gameLogicService;
	@Inject
	private GridOperationQueue gridOperationQueue;
	
	public void performUserOperation(final Tile clickedTile)
	{
		Log.d(this.getClass().toString(), "Entering performUserOperation for tile " + clickedTile);
		Log.v(this.getClass().toString(), "Tile Status is " + gameLogicService.getGameState());
		
		// Add a task to the queue to update the selected tile and all adjacent tiles
		gridOperationQueue.addTaskToQueue(new GridUpdateTask() {
			@Override
			public void run() {
				performOperation(clickedTile,TileColour.GREEN, TileColour.RED);
				refreshGrid();
			}
		});
		
		// After this, determine the optimum AI tile to select
		final Tile aiTile = tileUpdateLogicService.calculateOptimumAITile(gameLogicService.getGameState(), gameLogicService.getCurrentLevel());
		
		Log.i(this.getClass().toString(), "Optimum tile calculated for AI is " + aiTile);
		
		// Add a task to the queue to temporarily flash the selected AI tile
		gridOperationQueue.addTaskToQueue(new GridUpdateTask() {
			@Override
			public void run() {
				aiTile.setColour(TileColour.RED_TURNING);
				gameLogicService.getGameState().updateTile(aiTile);
				refreshGrid();
			}
		});
		
		// Then add a task to the queue to update the AI tile and all adjacent tiles
		gridOperationQueue.addTaskToQueue(new GridUpdateTask() {
			@Override
			public void run() {
				performOperation(aiTile,TileColour.RED, TileColour.GREEN);
				refreshGrid();
			}
		});
		
	}
		
	private void performOperation(Tile tile, TileColour colourOfTile, TileColour otherColour)
	{
		// Update the direction of the AI's tile
		tileUpdateLogicService.updateDirection(tile);
		// Then update its colour and all adjacent tiles colours
		tileUpdateLogicService.updateColours(tile, gameLogicService.getGameState(), colourOfTile, otherColour);			
	}	
}
