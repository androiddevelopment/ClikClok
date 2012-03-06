package com.clikclok.domain;

import com.clikclok.R;
import com.clikclok.util.Constants;

public enum OperationType {
	USER_OPERATION(Constants.USER_PAUSE_TIME, R.raw.user_move, R.raw.user_gains_made),
	AI_SELECTION_OPERATION(Constants.AI_SELECTION_PAUSE_TIME),
	AI_OPERATION(Constants.AI_PAUSE_TIME, R.raw.random_sound_2, R.raw.ai_gains_made);
	
	private int millisecondsToPauseFor;
	private int moveSoundResource;
	private int enemyTilesGainedSoundResource;	
	
	private OperationType(int millisecondsToPauseFor, int moveSoundResource, int enemyTilesGainedSoundResource) {
		this.millisecondsToPauseFor = millisecondsToPauseFor;
		this.moveSoundResource = moveSoundResource;
		this.enemyTilesGainedSoundResource = enemyTilesGainedSoundResource;
	}

	private OperationType(int millisecondsToPauseFor)
	{
		this(millisecondsToPauseFor, 0, 0);
	}
	
	public int getMillisecondsToPauseFor()
	{
		return millisecondsToPauseFor;
	}

	public boolean isUserOperationNext() {
		return getNextOperationType().equals(OperationType.USER_OPERATION);
	}

	public int getMoveSoundResource() {
		return moveSoundResource;
	}

	public int getEnemyTilesGainedSoundResource() {
		return enemyTilesGainedSoundResource;
	}
	
	public OperationType getNextOperationType() {
		OperationType nextOperationType = null;
		switch(this) {
			case USER_OPERATION: nextOperationType = OperationType.AI_SELECTION_OPERATION; 
				break;
			case AI_SELECTION_OPERATION: nextOperationType = OperationType.AI_OPERATION;
				break;
			case AI_OPERATION: nextOperationType = OperationType.USER_OPERATION;
				break;
		}
		return nextOperationType;
	}
	
}
