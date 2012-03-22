package com.clikclok.service.domain;

import com.clikclok.domain.OperationType;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.util.Constants;
import com.google.inject.Inject;

/**
 * A task that will be added to the GridView's handler to notify the {@link UIOperationQueue} that the grid has been updated
 * @author David
 */
public class ResumeNextUITask implements Runnable{
	@Inject
	private UIOperationQueue uiOperationQueue;
	private boolean haveWinner;
	private OperationType operationType;
	
	public ResumeNextUITask(boolean userHasWon, OperationType operationType) {
		this.haveWinner = userHasWon;
		this.operationType = operationType;
	}
	
	@Override
	public void run() {
		// If we have a winner then we will pause before the dialog is displayed
		if(haveWinner) {
			pauseThread(Constants.PAUSE_LENGTH_BEFORE_DIALOG);
		}
		else
		{
			pauseThread(operationType.getPauseLength());
		}
		
		// For some reason sometimes this UIOperationQueue has been null
		// If the user has won then we do not want to perform the next operation in the queue
		if(uiOperationQueue != null && !haveWinner) {
			uiOperationQueue.startNextGridUpdateTask(operationType);
		}
	}

	private void pauseThread(long seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			// This shouldn't happen
		}
	}

}
