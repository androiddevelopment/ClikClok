package com.clikclok.event;

import com.clikclok.domain.Level;


public interface UpdateUIListener {
	
	public abstract void updateGrid(int userScore, int aiScore);
	
	public abstract void showNextLevelDialog(Level currentLevel);
	
	public abstract void showUserWinnerDialog();
	
	public abstract void showAIWinnerDialog();

	public abstract void updateCountdownTimer(String secondsLeft);
	
	public abstract void updateVolumeIcon(boolean volumeIsOn);
}
