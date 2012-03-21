package com.clikclok.service.domain;

import com.clikclok.domain.OperationType;
import com.clikclok.service.GameLogicService;
import com.google.inject.Inject;

/**
 * Base class to perform the operations when the grid needs to be updated, for both user and AI
 * @author David
 */
public abstract class GridUpdateTask extends Task {
	@Inject
	private GameLogicService gameLogicService;
	private OperationType operationType;
	
	public GridUpdateTask(GameLogicService gameLogicService,
			OperationType operationType) {
		super();
		this.gameLogicService = gameLogicService;
		this.operationType = operationType;
	}

	/**
	 * This performs the update the grid on the main thread
	 * @param enemyTilesGained
	 */
	public void refreshGrid(boolean enemyTilesGained)
	{
		gameLogicService.updateGrid(operationType, enemyTilesGained);
	}

	public OperationType getOperationType() {
		return operationType;
	}
}
