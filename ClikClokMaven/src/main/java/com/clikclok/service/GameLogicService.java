package com.clikclok.service;

import android.util.Log;

import com.clikclok.domain.GameState;
import com.clikclok.domain.Level;
import com.clikclok.domain.OperationType;
import com.clikclok.domain.TileColour;
import com.clikclok.event.UpdateUIListener;
import com.clikclok.service.domain.Task;
import com.clikclok.util.AITimer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GameLogicService {
	private Level currentLevel;
	private UpdateUIListener updateUIListener;
	private GameState gameState;
	@Inject
	private AITimer aiTimer;
	@Inject
	private TileOperationService tileOperationService;
	@Inject
	private SoundsService soundsService;
	@Inject
	private UIOperationQueue uiOperationQueue;
	private boolean isSoundEnabled = true;
		
	public void nextLevel()
	{
		gameState = new GameState();
		updateUIListener.showNextLevelDialog(currentLevel); 
		currentLevel = Level.getNextLevel(currentLevel);
	}

	public GameState getGameState()
	{
		return gameState;
	}
	
	public void updateGrid(OperationType operationType, boolean enemyTilesGained)
	{
		Log.d(this.getClass().toString(), "Entering updateGrid. Next operation is user operation? " + operationType.isUserOperationNext());
		
		// Perform the update operation from non UI thread
		updateUIListener.updateGrid(gameState.getNumberOfTilesForColour(TileColour.GREEN), gameState.getNumberOfTilesForColour(TileColour.RED) + gameState.getNumberOfTilesForColour(TileColour.RED_TURNING)
				, uiOperationQueue);				
				
		// Sleeping for a fraction of a second is the only way we can ensure that the above update is performed immediately by the UI thread
//		try
//		{
//			Thread.sleep(operationType.getMillisecondsToPauseFor());
//		}
//		catch (InterruptedException e)
//		{
//			Log.e(this.getClass().toString(), "InterruptedException thrown:" + e.getMessage());
//		}
		soundsService.playMoveSound(operationType, enemyTilesGained);
		
		// Need to check for both, as when the first red turns there will be no reds at that point
		if(gameState.getNumberOfTilesForColour(TileColour.RED) + gameState.getNumberOfTilesForColour(TileColour.RED_TURNING) == 0) 
		{
			tileOperationService.clearOperationsFromQueue();
			Log.d(this.getClass().toString(), "No more AI tiles. Have cleared uiOperationQueue. Checking to see if there are more levels");
			// If the next level is null then we there are no more levels and the game is over
			if(Level.getNextLevel(currentLevel) == null)
			{
				soundsService.playUserWinsSound();
				updateUIListener.showUserWinnerDialog();
			}
			else 
			{ 
				nextLevel();
			}
		}
		else if(gameState.getNumberOfTilesForColour(TileColour.GREEN) == 0)
		{
			soundsService.playAIWinsSound();
			updateUIListener.showAIWinnerDialog();
		}
		else
		{
			checkWillStartTimer(operationType.isUserOperationNext());		
		}
	}

	private void checkWillStartTimer(boolean userOperationNext)
	{
		if(currentLevel.equals(Level.FIVE) && userOperationNext)
		{
			startLastLevelTimer();
		}
	}
	
	public void initialize() {
		currentLevel = Level.FIVE;
		checkWillStartTimer(true);	
		gameState = new GameState();
		soundsService.adjustVolume(isSoundEnabled);
		Level.setGridSize(gameState.getTileGridSize());
	}

	public void setUpdateUIListener(UpdateUIListener updateUIListener) {
		this.updateUIListener = updateUIListener;		
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}
	
	public void startLastLevelTimer()
	{
		uiOperationQueue.addTaskToQueue(new Task() {
			@Override
			public void run() {
				updateUIListener.updateCountdownTimer("6");
				aiTimer.startTimer();
				
			}
		});
	}

	public void setTimedOut() {
		tileOperationService.performAIOperation();
	}
	
	public void updateTimerText(final String secondsLeft)
	{
		uiOperationQueue.addTaskToQueue(new Task() {
			@Override
			public void run() {
				updateUIListener.updateCountdownTimer(secondsLeft);				
			}
		});
	}
	
	public void stopTimer() 
	{
		uiOperationQueue.addTaskToQueue(new Task() {
			@Override
			public void run() {
				// Pass an empty String so that no countdown is displayed
				updateUIListener.updateCountdownTimer("");
				aiTimer.stopTimer();	
			}
		});		
	}
	
	public void toggleSounds(){
		isSoundEnabled = !isSoundEnabled;
		updateUIListener.updateVolumeIcon(isSoundEnabled);
		soundsService.adjustVolume(isSoundEnabled);
	}

	public boolean isSoundEnabled() {
		return isSoundEnabled;
	}
}
