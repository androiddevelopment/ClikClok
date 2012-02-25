package com.clikclok.domain;

import com.clikclok.R;
import com.clikclok.util.Constants;

public enum OperationType {
	USER_OPERATION(Constants.USER_PAUSE_TIME, false, R.raw.user_move, R.raw.user_gains_made),
	AI_SELECTION_OPERATION(Constants.AI_SELECTION_PAUSE_TIME, false),
	AI_OPERATION(Constants.AI_PAUSE_TIME, true, R.raw.random_sound_2, R.raw.ai_gains_made);
	
	private int millisecondsToPauseFor;
	private boolean userOperationNext;
	private int moveSoundResource;
	private int enemyTilesGainedSoundResource;	
	
	private OperationType(int millisecondsToPauseFor, boolean userOperationNext, int moveSoundResource, int enemyTilesGainedSoundResource) {
		this.millisecondsToPauseFor = millisecondsToPauseFor;
		this.userOperationNext = userOperationNext;
		this.moveSoundResource = moveSoundResource;
		this.enemyTilesGainedSoundResource = enemyTilesGainedSoundResource;
	}

	private OperationType(int millisecondsToPauseFor, boolean userOperationNext)
	{
		this(millisecondsToPauseFor, userOperationNext, 0, 0);
	}
	
	public int getMillisecondsToPauseFor()
	{
		return millisecondsToPauseFor;
	}

	public boolean isUserOperationNext() {
		return userOperationNext;
	}

	public int getMoveSoundResource() {
		return moveSoundResource;
	}

	public int getEnemyTilesGainedSoundResource() {
		return enemyTilesGainedSoundResource;
	}
	
}
