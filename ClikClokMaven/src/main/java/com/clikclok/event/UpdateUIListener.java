package com.clikclok.event;

import com.clikclok.domain.Level;
import com.clikclok.service.GamePauseAndResumeService;


public interface UpdateUIListener {
	
	public abstract void updateGrid(final int userScore, final int aiScore, final GamePauseAndResumeService pauseAndResumeService);
	
	public abstract void showNextLevelDialog(Level currentLevel);
	
	public abstract void showUserWinnerDialog();
	
	public abstract void showAIWinnerDialog();

	public abstract void updateCountdownTimer(String secondsLeft);
	
	public abstract void updateVolumeIcon(boolean volumeIsOn);
}
