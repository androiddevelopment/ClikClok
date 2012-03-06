package com.clikclok.service.domain;

import android.os.Handler;
import android.os.Message;

import com.clikclok.domain.OperationType;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.util.Constants;

public class UIOperationHandler extends Handler {
	private UIOperationQueue uiOperationQueue;
	private boolean aiCalculationComplete;
	private boolean gridViewUpdateComplete;
	
	public UIOperationHandler(UIOperationQueue uiOperationQueue) {
		this.uiOperationQueue = uiOperationQueue;
	}

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
