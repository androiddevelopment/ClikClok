package com.clikclok.service.domain;

import com.clikclok.domain.OperationType;
import com.clikclok.service.GamePauseAndResumeService;

public class GameResumeTask implements Runnable{
	private GamePauseAndResumeService pauseAndResumeService;
	private boolean userHasWon;
	private OperationType operationType;
	
	public GameResumeTask(GamePauseAndResumeService pauseAndResumeService, boolean userHasWon, OperationType operationType) {
		this.pauseAndResumeService = pauseAndResumeService;
		this.userHasWon = userHasWon;
		this.operationType = operationType;
	}
	
	@Override
	public void run() {
		if(pauseAndResumeService != null && !userHasWon) {
			if(operationType.equals(OperationType.USER_OPERATION)) {
				pauseAndResumeService.usersGridViewUpdateComplete();
			}
			else
			{
				pauseAndResumeService.startNextTask();
			}
		}
	}

}
