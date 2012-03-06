package com.clikclok.service.domain;

import com.clikclok.domain.OperationType;
import com.clikclok.service.UIOperationQueue;

public class GameResumeTask implements Runnable{
	private UIOperationQueue uiOperationQueue;
	private boolean userHasWon;
	private OperationType operationType;
	
	public GameResumeTask(UIOperationQueue uiOperationQueue, boolean userHasWon, OperationType operationType) {
		this.uiOperationQueue = uiOperationQueue;
		this.userHasWon = userHasWon;
		this.operationType = operationType;
	}
	
	@Override
	public void run() {
		if(uiOperationQueue != null && !userHasWon) {
			uiOperationQueue.startNextGridUpdateTask(operationType);
		}
	}

}
