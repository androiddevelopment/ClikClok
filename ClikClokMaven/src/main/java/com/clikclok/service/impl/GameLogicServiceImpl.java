package com.clikclok.service.impl;

import com.clikclok.domain.GameState;
import com.clikclok.domain.GameStateInitializer;
import com.clikclok.domain.Level;
import com.clikclok.domain.OperationType;
import com.clikclok.domain.TileColour;
import com.clikclok.event.UpdateUIService;
import com.clikclok.service.AICalculationQueue;
import com.clikclok.service.GameLogicService;
import com.clikclok.service.SoundsService;
import com.clikclok.service.TileOperationService;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.service.domain.Task;
import com.clikclok.util.AITimer;
import com.clikclok.util.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class is used to encapsulate the overall logic of the game including the {@link GameState}.
 * It is the link between all other services and the main activity. It effectively operates as the controller of the game.
 * @author David
 */
@Singleton
public class GameLogicServiceImpl implements GameLogicService {
	private Level currentLevel;
	private UpdateUIService updateUIService;
	private GameState gameState;
	@Inject
	private AITimer aiTimer;
	@Inject
	private TileOperationService tileOperationService;
	@Inject
	private SoundsService soundsService;
	@Inject
	private UIOperationQueue uiOperationQueue;
	@Inject 
	private GameStateInitializer gameStateInitializer;
	@Inject
	private AICalculationQueue aICalculationQueue;
	private boolean isSoundEnabled = true;
	private boolean isDemoVersion = false;
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#initialize()
	 */
	@Override
	public void initialize() {
		// Start these threads if not already started
		uiOperationQueue.startQueueThread();
		aICalculationQueue.startQueueThread();
		currentLevel = Level.ONE;
		// Timer does not need to be started here
		checkWillStartTimer(true);	
		// Cretae a new GameState
		gameState = gameStateInitializer.createNewGameState();
		// For the purposes of our UI task queue we need to specify that the next operation to be performed is a user update
		updateUIGrid(OperationType.AI_OPERATION);
		// Adjust the volume depending on whether the user wants it on or off
		soundsService.adjustVolume(isSoundEnabled);
		Level.setGridSize(gameState.getTileGridSize());
	}
	
	@Override
	public void destroyGame() {
		uiOperationQueue.clearQueue();
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#nextLevel()
	 */
	@Override
	public void nextLevel()
	{
		// Create a new GameState and move to the next level
		gameState = gameStateInitializer.createNewGameState();
		updateUIGrid(OperationType.AI_OPERATION);		
		currentLevel = Level.getNextLevel(currentLevel);
		updateUIService.updateLevelView(currentLevel.getLevelNum());
	}

	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#getGameState()
	 */
	@Override
	public GameState getGameState()
	{
		return gameState;
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#updateGrid(com.clikclok.domain.OperationType, boolean)
	 */
	@Override
	public void updateGrid(OperationType operationType, boolean enemyTilesGained)
	{
		// Update the grid on the UPI thread
		updateUIGrid(operationType);				
		
		boolean userHasWon = getNumberOfRedTiles() == 0;
		boolean aiHasWon = getNumberOfGreenTiles() == 0;
		// Play the appropriate sound depending on the operation type
		soundsService.playMoveSound(operationType, enemyTilesGained);
		
		if(userHasWon) 
		{
			// If the user has won then we do not want the AI's UI tasks to be performed
			uiOperationQueue.clearQueue();
			// If the next level is null then we there are no more levels and the game is over
			if(Level.getNextLevel(currentLevel) == null)
			{
				soundsService.playUserWinsSound();
				updateUIService.showUserWinnerDialog();
			}
			else if(isDemoVersion)
			{
				updateUIService.showDemoDialog();
			}
			else
			{ 
				
				updateUIService.showNextLevelDialog(currentLevel); 
			}
		}
		else if(aiHasWon)
		{
			soundsService.playAIWinsSound();
			updateUIService.showAIWinnerDialog();
		}
		else
		{
			checkWillStartTimer(operationType.isUserOperationNext());		
		}
	}

	/**
	 * Updates the GridView on the UI thread
	 * @param operationType
	 */
	private void updateUIGrid(OperationType operationType) {
		// Perform the update operation from non UI thread
		updateUIService.updateGridView(getNumberOfGreenTiles(), getNumberOfRedTiles(), operationType);
	}

	/**
	 * @return the current level
	 */
	@Override
	public Level getCurrentLevel() {
		return currentLevel;
	}
	
	/**
	 * Starts the timer at one second greater as the first countdown does not display
	 */
	private void startLastLevelTimer()
	{
		uiOperationQueue.addUITaskToQueue(new Task() {
			@Override
			public void run() {
				updateUIService.updateCountdownTimerView("" + Constants.NUMBER_COUNTDOWN_SECONDS + 1);
				aiTimer.startTimer();
				
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#updateTimerText(int)
	 */
	@Override
	public void updateTimerText(final int secondsLeft)
	{
		if(secondsLeft == 0 ){
			tileOperationService.performAIOperationAfterTimeOut();
		}
		uiOperationQueue.addUITaskToQueue(new Task() {
			@Override
			public void run() {
				updateUIService.updateCountdownTimerView("" + secondsLeft);				
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#stopTimer()
	 */
	@Override
	public void stopTimer() 
	{
		uiOperationQueue.addUITaskToQueue(new Task() {
			@Override
			public void run() {
				// Pass an empty String so that no countdown is displayed
				updateUIService.updateCountdownTimerView("");
				aiTimer.stopTimer();	
			}
		});		
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#toggleSounds()
	 */
	@Override
	public void toggleSounds(){
		isSoundEnabled = !isSoundEnabled;
		updateUIService.updateVolumeIcon(isSoundEnabled);
		soundsService.adjustVolume(isSoundEnabled);
	}

	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.GameLogicService#isSoundEnabled()
	 */
	@Override
	public boolean isSoundEnabled() {
		return isSoundEnabled;
	}
	
	/**
	 * @return number of red or red turning tiles
	 */
	private int getNumberOfRedTiles() {
		int numberOfRedTiles = gameState.getNumberOfTilesForColour(TileColour.RED) + gameState.getNumberOfTilesForColour(TileColour.RED_TURNING);
		return numberOfRedTiles;
	}

	/**
	 * @return number of green or green turning tiles
	 */
	private int getNumberOfGreenTiles() {
		int numberOfGreenTiles = gameState.getNumberOfTilesForColour(TileColour.GREEN) + gameState.getNumberOfTilesForColour(TileColour.GREEN_TURNING);
		return numberOfGreenTiles;
	}

	/**
	 * Checks if we should start the timer
	 * @param userOperationNext
	 */
	private void checkWillStartTimer(boolean userOperationNext)
	{
		if(currentLevel.equals(Level.FIVE) && userOperationNext)
		{
			startLastLevelTimer();
		}
	}

	/**
	 * Sets this service manually, as dependency injection does not work here
	 * @param updateUIService
	 */
	public void setUpdateUIService(UpdateUIService updateUIService) {
		this.updateUIService = updateUIService;		
	}	
}
