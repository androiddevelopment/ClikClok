package com.clikclok.service.domain;

import android.util.Log;

import com.clikclok.domain.OperationType;
import com.clikclok.service.GameLogicService;

public abstract class GridUpdateTask extends Task {
	private GameLogicService gameLogicService;
	
	public GridUpdateTask(GameLogicService gameLogicService)
	{
		this.gameLogicService = gameLogicService;
	}
	
	public void refreshGridForOperationType(OperationType operationType, boolean enemyTilesGained)
	{
		Log.d(this.getClass().toString(), "Current thread is ID " + Thread.currentThread().getId());
		gameLogicService.updateGrid(operationType, enemyTilesGained);
	}
	
}
