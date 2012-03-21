package com.clikclok.service.impl;

import java.util.HashMap;
import java.util.Map;

import android.os.Looper;
import android.os.Message;

import com.clikclok.domain.OperationType;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.service.domain.AICalculationOperationHandler;
import com.clikclok.service.domain.GridUpdateTask;
import com.clikclok.service.domain.Task;
import com.clikclok.util.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * See {@link UIOperationQueue}
 */
@Singleton
public class UIOperationQueueImpl extends Thread implements UIOperationQueue{
	@Inject
	private AICalculationOperationHandler handler;
	private Map<OperationType, GridUpdateTask> gridUpdateTasks;
	
	@Override
	public void run() {
		Looper.prepare();
		
		gridUpdateTasks = new HashMap<OperationType, GridUpdateTask>();
						
		Looper.loop();	
	}
	
	@Override
	public void addGridUpdateTaskToQueue(final GridUpdateTask task)
	{
		// If a task already exists for this OperationType then this will return a non-null object
		GridUpdateTask taskReturned = gridUpdateTasks.put(task.getOperationType(), task);
		if(taskReturned != null) {
			throw new RuntimeException("Attempted to add a new task for an operation type" + task.getOperationType() + " that already has an unprocessed task");
		}
	}
	@Override
	public void addUITaskToQueue(final Task task)
	{
		handler.post(task);		
	}
	@Override
	public void clearQueue(){
		handler.removeCallbacksAndMessages(null);
		gridUpdateTasks.clear();
	}
	@Override
	public void startNextGridUpdateTask(OperationType currentOperationType) {
		OperationType nextOperationType = currentOperationType.getNextOperationType();
		// If the next operation type to be performed is the AI selection, then we must wait until the AI calculation task has completed
		if(nextOperationType.equals(OperationType.AI_SELECTION_OPERATION)){
			Message message = handler.obtainMessage(Constants.GRIDVIEW_UPDATE_COMPLETE);
			handler.sendMessage(message);
		}
		else
		{
			// Otherwise add the relevant task to the queue
			GridUpdateTask task = gridUpdateTasks.remove(nextOperationType);
			if(task != null) {
				addUITaskToQueue(task);
			}
			
		}
	}
	@Override
	public void notifyAICalculationComplete() {
		Message message = handler.obtainMessage(Constants.AI_CALCULATION_COMPLETE);
		handler.sendMessage(message);
	}
	@Override
	public GridUpdateTask getTaskForOperationType(OperationType operationType) {
		GridUpdateTask task = gridUpdateTasks.remove(operationType);
		if(task == null) {
			throw new RuntimeException("No task exists for operation type " + operationType);
		}
		return task;
	}
	@Override
	public boolean hasUnprocessedTasks()
	{
		return !gridUpdateTasks.isEmpty();
	}

	@Override
	public void startQueueThread() {
		if(!isAlive())
		{
			start();
		}
		
	}

	
}
