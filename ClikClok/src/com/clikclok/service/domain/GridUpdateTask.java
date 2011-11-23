package com.clikclok.service.domain;

import com.clikclok.service.GameLogicService;
import com.google.inject.Inject;

public abstract class GridUpdateTask implements Runnable {
	@Inject
	private GameLogicService gameLogicService;
	
	public void refreshGrid()
	{
		gameLogicService.updateGrid();
	}
	
	public abstract void run();
	
	
}
