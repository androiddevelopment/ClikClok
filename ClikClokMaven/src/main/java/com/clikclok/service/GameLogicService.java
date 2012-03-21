package com.clikclok.service;

import com.clikclok.domain.GameState;
import com.clikclok.domain.Level;
import com.clikclok.domain.OperationType;
import com.clikclok.service.impl.TileOperationServiceImpl;

public interface GameLogicService {

	/**
	 * This logic is maintained in a method as opposed to the constructor as it will be used when the game is both created and resumed
	 */
	public abstract void initialize();

	/**
	 * Resets the UI queue and updates the level views
	 */
	public abstract void nextLevel();

	/**
	 * @return the GameState for the current level may be required by other classes 
	 */
	public abstract GameState getGameState();

	/**
	 * This logic is performed for each update operation on the grid. Logic such as determining whether the user or AI won, or whether we need to move to a 
	 * new level is located here
	 * @param operationType
	 * @param enemyTilesGained
	 */
	public abstract void updateGrid(OperationType operationType,
			boolean enemyTilesGained);

	/**
	 * If the countdown has completed then we need to notify the {@link TileOperationServiceImpl} to kick off the AI's turn
	 * @param secondsLeft
	 */
	public abstract void updateTimerText(final int secondsLeft);

	/**
	 * Stop's the timer and updates the view
	 */
	public abstract void stopTimer();

	/**
	 * Enables and disables the sounds
	 */
	public abstract void toggleSounds();

	/**
	 * @return whether the sound is enabled or disabled
	 */
	public abstract boolean isSoundEnabled();
	
	/**
	 * Clears the UI queue of any pending operations
	 */
	public void destroyGame();

	Level getCurrentLevel();

}