package com.clikclok.domain;

import com.clikclok.R;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.util.Constants;

/**
 * Enum to hold the various operation types allowed, as well as their associated sounds
 * @author David
 */
public enum OperationType {
	USER_SELECTION_OPERATION(R.raw.user_move, R.raw.user_gains_made, Constants.PAUSE_LENGTH_AFTER_SELECTION_TASK),
	USER_OPERATION(0, 0, Constants.PAUSE_LENGTH_AFTER_UPDATE_TASK),
	AI_SELECTION_OPERATION(R.raw.random_sound_2, R.raw.ai_gains_made, Constants.PAUSE_LENGTH_AFTER_SELECTION_TASK),
	AI_OPERATION(0, 0, Constants.PAUSE_LENGTH_AFTER_UPDATE_TASK);
	
	private int moveSoundResource;
	private int enemyTilesGainedSoundResource;	
	private long pauseLength;
	
	private OperationType() {
	}
	
	private OperationType(int moveSoundResource, int enemyTilesGainedSoundResource, long pauseLength) {
		this.moveSoundResource = moveSoundResource;
		this.enemyTilesGainedSoundResource = enemyTilesGainedSoundResource;
		this.pauseLength = pauseLength;
	}

	public boolean isUserOperationNext() {
		return getNextOperationType().equals(OperationType.USER_SELECTION_OPERATION);
	}

	public int getMoveSoundResource() {
		return moveSoundResource;
	}

	public int getEnemyTilesGainedSoundResource() {
		return enemyTilesGainedSoundResource;
	}
	
	/**
	 * We need to know the next operation type in the sequence of tasks to be performed in the {@link UIOperationQueue}
	 * @return
	 */
	public OperationType getNextOperationType() {
		OperationType nextOperationType = null;
		switch(this) {
			case USER_SELECTION_OPERATION: nextOperationType = OperationType.USER_OPERATION;
				break;
			case USER_OPERATION: nextOperationType = OperationType.AI_SELECTION_OPERATION; 
				break;
			case AI_SELECTION_OPERATION: nextOperationType = OperationType.AI_OPERATION;
				break;
			case AI_OPERATION: nextOperationType = OperationType.USER_SELECTION_OPERATION;
				break;
		}
		return nextOperationType;
	}
	
	public long getPauseLength() {
		return pauseLength;
	}

	
}
