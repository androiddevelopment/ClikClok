package com.clikclok.event;

import com.clikclok.domain.Level;
import com.clikclok.domain.OperationType;


public interface UpdateUIListener {
	
	public abstract void updateGrid(final int userScore, final int aiScore, final boolean userHasWon, final OperationType operationType);
	
	public abstract void showNextLevelDialog(Level currentLevel);
	
	public abstract void showUserWinnerDialog();
	
	public abstract void showAIWinnerDialog();

	public abstract void updateCountdownTimer(String secondsLeft);
	
	public abstract void updateVolumeIcon(boolean volumeIsOn);

	void showDemoDialog();
}
