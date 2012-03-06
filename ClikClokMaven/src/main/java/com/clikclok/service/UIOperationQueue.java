package com.clikclok.service;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.clikclok.domain.OperationType;
import com.clikclok.service.domain.GridUpdateTask;
import com.clikclok.service.domain.UIOperationHandler;
import com.clikclok.util.Constants;
import com.google.inject.Singleton;

@Singleton
public class UIOperationQueue extends Thread {
	private Handler handler;
	private Map<OperationType, GridUpdateTask> gridUpdateTasks;
	
	@Override
	public void run() {
		Looper.prepare();
		
		handler = new UIOperationHandler(this);
		gridUpdateTasks = new HashMap<OperationType, GridUpdateTask>();
						
		Looper.loop();	
	}
	
	public void addGridUpdateTaskToQueue(final GridUpdateTask task)
	{
		GridUpdateTask taskReturned = gridUpdateTasks.put(task.getOperationType(), task);
		if(taskReturned != null) {
			throw new RuntimeException("Attempted to add a new task for an operation type" + task.getOperationType() + " that already has an unprocessed task");
		}
	}
	
	public void addUITaskToQueue(final Runnable task)
	{
		handler.post(task);		
	}
	
	public void clearQueue(){
		handler.removeCallbacksAndMessages(null);
		gridUpdateTasks.clear();
	}
	
	public void startNextGridUpdateTask(OperationType currentOperationType) {
		OperationType nextOperationType = currentOperationType.getNextOperationType();
		
		if(nextOperationType.equals(OperationType.AI_SELECTION_OPERATION)){
			Message message = handler.obtainMessage(Constants.GRIDVIEW_UPDATE_COMPLETE);
			handler.sendMessage(message);
		}
		else
		{
			GridUpdateTask task = gridUpdateTasks.remove(nextOperationType);
			if(task != null) {
				addUITaskToQueue(task);
			}
			
		}
	}
	
	public void notifyAICalculationComplete() {
		Message message = handler.obtainMessage(Constants.AI_CALCULATION_COMPLETE);
		handler.sendMessage(message);
	}
	
	public GridUpdateTask getTaskForOperationType(OperationType operationType) {
		GridUpdateTask task = gridUpdateTasks.remove(operationType);
		if(task == null) {
			throw new RuntimeException("No task exists for operation type " + operationType);
		}
		return task;
	}
}
