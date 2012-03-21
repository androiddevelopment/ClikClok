package com.clikclok.domain;

import com.clikclok.R;
import com.clikclok.service.UIOperationQueue;

/**
 * Enum to hold the various operation types allowed, as well as their associated sounds
 * @author David
 */
public enum OperationType {
	USER_SELECTION_OPERATION,
	USER_OPERATION(R.raw.user_move, R.raw.user_gains_made),
	AI_SELECTION_OPERATION,
	AI_OPERATION(R.raw.random_sound_2, R.raw.ai_gains_made);
	
	private int moveSoundResource;
	private int enemyTilesGainedSoundResource;	
	
	private OperationType() {
	}
	
	private OperationType(int moveSoundResource, int enemyTilesGainedSoundResource) {
		this.moveSoundResource = moveSoundResource;
		this.enemyTilesGainedSoundResource = enemyTilesGainedSoundResource;
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
	
}
