package com.clikclok.service.domain;

import com.clikclok.domain.OperationType;
import com.clikclok.service.GameLogicService;

public abstract class GridUpdateTask extends Task {
	private GameLogicService gameLogicService;
	private OperationType operationType;
	
	public GridUpdateTask(GameLogicService gameLogicService, OperationType operationType)
	{
		this.gameLogicService = gameLogicService;
		this.operationType = operationType;
	}
	
	public void refreshGrid(boolean enemyTilesGained)
	{
		gameLogicService.updateGrid(operationType, enemyTilesGained);
	}

	public OperationType getOperationType() {
		return operationType;
	}
}
