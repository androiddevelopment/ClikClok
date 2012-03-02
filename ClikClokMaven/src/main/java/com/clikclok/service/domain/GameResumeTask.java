package com.clikclok.service.domain;

import com.clikclok.service.GamePauseAndResumeService;

public class GameResumeTask implements Runnable{
	private GamePauseAndResumeService pauseAndResumeService;
	
	public GameResumeTask(GamePauseAndResumeService pauseAndResumeService) {
		this.pauseAndResumeService = pauseAndResumeService;
	}
	
	@Override
	public void run() {
		pauseAndResumeService.resumeQueuedTasks();		
	}

}
