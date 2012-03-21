package com.clikclok.event;

import com.clikclok.domain.Level;
import com.clikclok.domain.OperationType;


/**
 * Implements all operations to update the application's views. These operations will be performed on the main activity and UI thread
 * @author David
 */
public interface UpdateUIService {
	
	/**
	 * Show's the dialog for the next level
	 * @param currentLevel
	 */
	public abstract void showNextLevelDialog(Level currentLevel);
	
	/**
	 * Show's the user winner dialog
	 */
	public abstract void showUserWinnerDialog();
	
	/**
	 * Show's the AI winner dialog
	 */
	public abstract void showAIWinnerDialog();

	/**
	 * Update the countdown timer's view
	 * @param secondsLeft
	 */
	public abstract void updateCountdownTimerView(String secondsLeft);
	
	/**
	 * Shows the volume on or off button
	 * @param volumeIsOn
	 */
	public abstract void updateVolumeIcon(boolean volumeIsOn);

	/**
	 * Shows the demo dialog
	 */
	public abstract void showDemoDialog();

	/**
	 * Updates the level number on the application screen
	 * @param number
	 */
	public abstract void updateLevelView(int number);

	/**
	 * Updates the grid view and kicks off the next task in the UI queue
	 * @param userScore
	 * @param aiScore
	 * @param operationType
	 */
	public abstract void updateGridView(int userScore, int aiScore, OperationType operationType);
}
