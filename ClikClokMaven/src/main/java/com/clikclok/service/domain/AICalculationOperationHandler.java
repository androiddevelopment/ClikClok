package com.clikclok.service.domain;

import android.os.Handler;
import android.os.Message;

import com.clikclok.domain.OperationType;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.util.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This handler receives messages to specify when the operation to update the AI's tile can take place. This can only take place when the grid has refreshed after the user's update
 * and when the task to calculate the optimum AI tile has completed
 * @author David
 */
@Singleton
public class AICalculationOperationHandler extends Handler {
	@Inject
	private UIOperationQueue uiOperationQueue;
	private boolean aiCalculationComplete;
	private boolean gridViewUpdateComplete;
	
	@Override
	public void handleMessage(Message msg) {
		
		if(msg.what == Constants.AI_CALCULATION_COMPLETE) {
			aiCalculationComplete = true;
		}
		else if(msg.what == Constants.GRIDVIEW_UPDATE_COMPLETE) {
			gridViewUpdateComplete = true;
		}
		
		if (aiCalculationComplete && gridViewUpdateComplete) {
			aiCalculationComplete = false;
			gridViewUpdateComplete = false;
			uiOperationQueue.addUITaskToQueue(uiOperationQueue.getTaskForOperationType(OperationType.AI_SELECTION_OPERATION));			
		}
	}
	
	
}
